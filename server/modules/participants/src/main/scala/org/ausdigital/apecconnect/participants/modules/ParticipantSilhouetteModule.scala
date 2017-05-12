package org.ausdigital.apecconnect.participants.modules

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.crypto.{Crypter, CrypterAuthenticatorEncoder}
import com.mohiva.play.silhouette.api.{Environment, EventBus}
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticator, JWTAuthenticatorService, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.util.{DefaultFingerprintGenerator, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import org.ausdigital.apecconnect.participants.model.ParticipantAuthEnv
import org.ausdigital.apecconnect.participants.services.ParticipantService
import play.api.Configuration
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.concurrent.duration.DurationLong

class ParticipantSilhouetteModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[IDGenerator]).toInstance(new SecureRandomIDGenerator())
    bind(classOf[PasswordHasher]).toInstance(new BCryptPasswordHasher)
    bind(classOf[FingerprintGenerator]).toInstance(new DefaultFingerprintGenerator(false))
    bind(classOf[EventBus]).toInstance(EventBus())
  }

  /**
    * Provides the Silhouette environment that can be used to handle consumer authentication.
    *
    * @param identityService The user service implementation.
    * @param authenticatorService The authentication service implementation.
    * @param eventBus The event bus instance.
    * @return The Silhouette environment.
    */
  @Provides
  def provideEnvironment(
      identityService: ParticipantService,
      authenticatorService: AuthenticatorService[JWTAuthenticator],
      eventBus: EventBus
  ): Environment[ParticipantAuthEnv] =
    Environment[ParticipantAuthEnv](
      identityService,
      authenticatorService,
      Nil,
      eventBus
    )

  /**
    * Provides the authenticator service.
    *
    * @param idGenerator The ID generator used to create the authenticator ID.
    * @return The authenticator service.
    */
  @Provides
  def provideJWTAuthenticatorService(
      crypter: Crypter,
      settings: JWTAuthenticatorSettings,
      idGenerator: IDGenerator,
      configuration: Configuration
  ): JWTAuthenticatorService = {

    val encoder = new CrypterAuthenticatorEncoder(crypter)
    new JWTAuthenticatorService(settings, None, encoder, idGenerator, Clock())
  }

  /**
    * Provides the authenticator settings.
    * @return The authenticator settings.
    */
  @Provides
  def provideAuthenticatorSettings(configuration: Configuration): JWTAuthenticatorSettings =
    JWTAuthenticatorSettings(
      fieldName =
        configuration.getString("silhouette.authenticator.headerName").getOrElse(throw configuration.reportError("silhouette.authenticator.headerName", "Authenticator header name must be set.")),
      issuerClaim =
        configuration.getString("silhouette.authenticator.issuerClaim").getOrElse(throw configuration.reportError("silhouette.authenticator.issuerClaim", "Authenticator issuer claim must be set.")),
      authenticatorExpiry = configuration
        .getLong("silhouette.authenticator.authenticatorExpiry")
        .getOrElse(throw configuration.reportError("silhouette.authenticator.authenticatorExpiry", "Authenticator token expiry must be set.")) seconds,
      sharedSecret = configuration.getString("play.crypto.secret").getOrElse(throw configuration.reportError("play.crypto.secret", "Secret must be set."))
    )

  /**
    * Provides the authenticator service.
    *
    * @param idGenerator The ID generator used to create the authenticator ID.
    * @return The authenticator service.
    */
  @Provides
  def provideAuthenticatorService(
      crypter: Crypter,
      settings: JWTAuthenticatorSettings,
      idGenerator: IDGenerator,
      configuration: Configuration
  ): AuthenticatorService[JWTAuthenticator] = {

    val encoder = new CrypterAuthenticatorEncoder(crypter)
    new JWTAuthenticatorService(settings, None, encoder, idGenerator, Clock())
  }

  /**
    * Provides the password hasher registry.
    *
    * @param passwordHasher The default password hasher implementation.
    * @return The password hasher registry.
    */
  @Provides
  def providePasswordHasherRegistry(passwordHasher: PasswordHasher): PasswordHasherRegistry =
    PasswordHasherRegistry(passwordHasher)

  /**
    * Provides the crypter for the authenticator.
    *
    * @param configuration The Play configuration.
    * @return The crypter for the authenticator.
    */
  @Provides
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    val config = configuration.underlying.as[JcaCrypterSettings]("silhouette.authenticator.crypter")

    new JcaCrypter(config)
  }
}
