package org.ausdigital.apecconnect.users.dao

import com.mohiva.play.silhouette.api.AuthInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.{ OAuth1Info, OAuth2Info }
import play.api.libs.json._

import scala.reflect.ClassTag
import scalaz.Scalaz._
import scalaz._

/**
  * Provides formatters between Silhouette [[com.mohiva.play.silhouette.api.AuthInfo]]  instances and their stored
  * representations in the users service.
  */
class AuthInfoFormatter {

  import AuthInfoFormatter._

  /**
    * Extracts a provider specific auth info (of type T) from the auth info transport.
    *
    * Like the DelegableAuthInfoRepository, throws an exception if the auth info can not be extracted.
    *
    * @param json the transport auth info container that the auth info should be extracted from.
    * @tparam T the target type of the auth info to extract.
    * @return the extracted auth info.
    */
  def read[T <: AuthInfo](json: JsValue)(implicit classTag: ClassTag[T]): T = {
    val errorOrT = for {
      formatter <- formatter toRightDisjunction s"No formatter for [$classTag]."
      authInfo <- Disjunction fromEither formatter.reads(json).asEither leftMap { errors => Json.stringify(JsError.toJson(errors)) }
    } yield authInfo

    errorOrT.fold(
      error => throw new IllegalArgumentException(error),
      identity
    )
  }

  /**
    * Creates an auth info transport container from the provider specific auth info.
    *
    * Like the DelegableAuthInfoRepository, throws an exception if the auth info can not be extracted.
    *
    * @param authInfo the auth info to wrap in the transport container.
    * @tparam T the T of auth info being wrapped.
    * @return the created transport container.
    */
  def write[T <: AuthInfo](authInfo: T)(implicit tag: ClassTag[T]): JsValue = {
    val errorOrT = for {
      formatter <- formatter toRightDisjunction s"No formatter for [$tag]."
    } yield {
      formatter.writes(authInfo)
    }

    errorOrT.fold(
      error => throw new IllegalArgumentException(error),
      identity
    )
  }

  @SuppressWarnings(Array("org.brianmckenna.wartremover.warts.AsInstanceOf"))
  private def formatter[T](implicit tag: ClassTag[T]): Option[Format[T]] =
    formatters.get(tag.runtimeClass.getName).map(_.asInstanceOf[Format[T]])
}

/**
  * Companion that provides the map of formatters.
  */
object AuthInfoFormatter {
  val formatters = Map[String, Format[_]](
    "com.mohiva.play.silhouette.api.util.PasswordInfo" -> Json.format[PasswordInfo],
    "com.mohiva.play.silhouette.impl.providers.OAuth1Info" -> Json.format[OAuth1Info],
    "com.mohiva.play.silhouette.impl.providers.OAuth2Info" -> Json.format[OAuth2Info]
  )

}
