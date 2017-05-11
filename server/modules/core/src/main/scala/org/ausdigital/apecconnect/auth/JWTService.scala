package org.ausdigital.apecconnect.auth

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.crypto.{ Crypter, CrypterAuthenticatorEncoder }
import com.mohiva.play.silhouette.impl.authenticators.{ JWTAuthenticator, JWTAuthenticatorService, JWTAuthenticatorSettings }
import play.api.mvc.RequestHeader

import scala.concurrent.Future

/**
 * Wraps Silhouette's JWTAuthenticatorService, exposing methods to create, serialize and unserialize JWT tokens.
 * @param service The authenticator service.
 * @param crypter The cypter used to create the authentication encoder.
 * @param settings The authenticator settings.
 * Created by danixon on 26/11/2015.
 */
class JWTService @Inject() (service: JWTAuthenticatorService, crypter: Crypter, settings: JWTAuthenticatorSettings) {

  lazy val crypterAuthenticatorEncoder = new CrypterAuthenticatorEncoder(crypter)

  /**
   * Creates a new authenticator for the specified login info.
   *
   * @param loginInfo The login info for which the authenticator should be created.
   * @param request The request header.
   * @return An authenticator.
   */
  def create(loginInfo: LoginInfo)(implicit request: RequestHeader): Future[JWTAuthenticator] = {
    service.create(loginInfo)
  }

  /**
   * Serializes the authenticator.
   *
   * @param authenticator The authenticator to serialize.
   * @return The serialized authenticator.
   */
  def serialize(authenticator: JWTAuthenticator): String = {
    JWTAuthenticator.serialize(authenticator, crypterAuthenticatorEncoder, settings)
  }

  /**
   * Unserializes the authenticator.
   *
   * @param str The string representation of the authenticator.
   * @return An valid authenticator on success, otherwise a None. If an authenticator is unserialized but is
   *         invalid, None is returned.
   */
  def unserialize(str: String): Option[JWTAuthenticator] = {
    JWTAuthenticator.unserialize(str, crypterAuthenticatorEncoder, settings).toOption.filter(_.isValid)
  }

}
