package org.ausdigital.apecconnect.users.controllers

import javax.inject.Inject

import au.com.agiledigital.rest.controllers.transport.{ JsonApiResponse, Message, MessageLevel }
import au.com.agiledigital.rest.security.BodyParsers.whitelistingJson
import org.ausdigital.apecconnect.users.ApecConnectSilhouette
import org.ausdigital.apecconnect.users.model.{ AccountStatus, ApecConnectUser, VerificationStatus }
import com.mohiva.play.silhouette.api.{ LoginEvent, LoginInfo, LogoutEvent, SignUpEvent, Silhouette }
import com.mohiva.play.silhouette.api.util.{ Clock, Credentials, PasswordHasherRegistry }
import com.mohiva.play.silhouette.impl.providers.{ CommonSocialProfileBuilder, CredentialsProvider, SocialProvider, SocialProviderRegistry }
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.{ IdentityNotFoundException, InvalidPasswordException }
import com.mohiva.play.silhouette.api.Authenticator.Implicits._
import controllers.ActionDSL.{ MonadicActions, Step }
import org.ausdigital.apecconnect.users.controllers.transport.SignInApiResponse
import play.api.libs.json.{ JsNull, JsValue, Json, Reads }
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

/**
  * Supports sign in of users using OAuth or credentials.
  */
class AuthenticationController @Inject() (
    credentialsProvider: CredentialsProvider,
    socialProviderRegistry: SocialProviderRegistry,
    authEnvironment: ApecConnectSilhouette
)(implicit val ec: ExecutionContext) extends Controller with MonadicActions {

  private val env = authEnvironment.env

  /**
    * Result that is returned when the user provide invalid credentials. Note that Silhouette interprets invalidity very
    * broadly. Any failure from the authentication environment is assumed to be due to invalid credentials.
    */
  private val invalidCredentialsResult = Unauthorized(Json.toJson(JsonApiResponse[String](None, Message("invalid.credentials", MessageLevel.Alert))))

  /**
    * Result that is returned when OAuth2 authentication fails.
    */
  private val invalidOauthResult = Unauthorized(Json.toJson(JsonApiResponse[String](None, Message("invalid.oauth", MessageLevel.Alert))))

  /**
    * Checks whether the user's account is active and enabled.
    */
  private def accountActive(user: ApecConnectUser): Option[ApecConnectUser] = Some(user) filter { user => user.verificationStatus == VerificationStatus.Verified && user.accountStatus == AccountStatus.Enabled }

  implicit val credentialReads: Reads[Credentials] = Json.reads[Credentials]

  /**
    * Authenticates a user against the credentials provider.
    * Only those users with Active accounts will authenticate.
    *
    * @return the signin api response containing the authentication token.
    */
  def authenticateWithCredentials: Action[JsValue] = Action.async(whitelistingJson) { implicit request =>

    for {
      credentials <- request.body.validate[Credentials] ?| { _ =>
        invalidCredentialsResult
      }
      loginInfo <- credentialsProvider.authenticate(credentials) ?| { _ =>
        invalidCredentialsResult
      }
      token <- authenticate(loginInfo, invalidCredentialsResult)
    } yield JsonApiResponse.buildResponse("Signed in.", SignInApiResponse(token))
  }

  /**
    * Authenticates a user against a social auth provider.
    * Only those users with Active accounts will authenticate.
    *
    * @return the signin api response containing the authentication token.
    */
  def authenticateByOauth(providerName: String): Action[JsValue] = Action.async(whitelistingJson) { implicit request =>
    socialProviderRegistry.get[SocialProvider with CommonSocialProfileBuilder](providerName) match {
      case Some(provider) =>
        val result: Step[Result] = for {
          authInfo <- provider.authenticate ?| { _ => invalidOauthResult }
          profile <- provider.retrieveProfile(authInfo) ?| { _ => invalidOauthResult }
          token <- authenticate(profile.loginInfo, invalidOauthResult)
        } yield JsonApiResponse.buildResponse(s"Signed in with $provider.", SignInApiResponse(token))

        result
      case _ => Future.successful(invalidOauthResult)
    }
  }

  def authenticate(loginInfo: LoginInfo, invalidResult: Result)(implicit request: Request[JsValue]): Step[String] = {
    for {
      user <- env.identityService.retrieve(loginInfo) ?| { _ => invalidResult }
      _ <- accountActive(user) ?| { _ => invalidResult }
      authenticator <- env.authenticatorService.create(loginInfo) ?| { _ => invalidResult }
      _ = env.eventBus.publish(LoginEvent(user, request))
      token <- env.authenticatorService.init(authenticator) ?| { _ => invalidResult }
    } yield token
  }

  def test = authEnvironment.UserAwareAction { request =>
    request.identity match {
      case Some(identity) => Ok(s"Hello, ${identity.fullName.getOrElse("friendo")}")
      case None => Ok("Not logged in")
    }
  }

  def signOut: Action[AnyContent] = authEnvironment.SecuredAction.async { implicit request =>
    env.eventBus.publish(LogoutEvent(request.identity, request))
    env.authenticatorService.discard(request.authenticator, JsonApiResponse.buildResponse("Signed out", JsNull))
  }
}
