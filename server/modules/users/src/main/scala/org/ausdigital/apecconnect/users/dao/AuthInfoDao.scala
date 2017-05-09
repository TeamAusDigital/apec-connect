package org.ausdigital.apecconnect.users.dao

import java.time.LocalDateTime
import javax.inject.Inject

import org.ausdigital.apecconnect.db.dao.SlickDao
import org.ausdigital.apecconnect.db.model.{ MetaData, RecordStatus }
import com.mohiva.play.silhouette.api.LoginInfo
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsValue

import scala.concurrent.{ ExecutionContext, Future }

/**
  * The DAO to store the Auth Info information.
  */
class AuthInfoDao @Inject() (override val dbConfigProvider: DatabaseConfigProvider) extends SlickDao with UserDbTableDefinitions {

  import driver.api._

  protected def authInfoQuery(loginInfo: LoginInfo): Query[AuthInfos, DbAuthInfo, Seq] = for {
    dbLoginInfo <- loginInfoQuery(loginInfo)
    dbAuthInfo <- slickAuthInfos if dbAuthInfo.loginInfoId === dbLoginInfo.id
  } yield dbAuthInfo

  // Use subquery workaround instead of join to get authinfo because slick only supports selecting
  // from a single table for update/delete queries (https://github.com/slick/slick/issues/684).
  protected def authInfoSubQuery(loginInfo: LoginInfo): Query[AuthInfos, DbAuthInfo, Seq] =
    slickAuthInfos.filter(_.loginInfoId in loginInfoQuery(loginInfo).map(_.id))

  protected def addAction(loginInfo: LoginInfo, authInfo: JsValue, authType: String)(implicit ec: ExecutionContext) = {
    Logger.debug(s"Adding [$loginInfo] and [$authInfo] and [$authType].")
    loginInfoQuery(loginInfo).result.head.flatMap { dbLoginInfo =>
      slickAuthInfos += DbAuthInfo(-1, authType, authInfo, dbLoginInfo.id, MetaData(RecordStatus.Active, LocalDateTime.now(), LocalDateTime.now(), 0))
    }.transactionally
  }

  protected def updateAction(loginInfo: LoginInfo, authInfo: JsValue, authType: String)(implicit ec: ExecutionContext) = {
    Logger.debug(s"Updating [$loginInfo] and [$authInfo] and [$authType].")
    authInfoQuery(loginInfo).result.head.flatMap { dbAuthInfo =>
      slickAuthInfos.
        filter(a => a.id === dbAuthInfo.id && a.authType === authType).
        map(_.data).
        update(authInfo)
    }.transactionally
  }

  /**
    * Finds the auth info which is linked with the specified login info.
    *
    * @param loginInfo The linked login info.
    * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
    */
  def find(loginInfo: LoginInfo, authType: String)(implicit ec: ExecutionContext): Future[Option[JsValue]] = {
    Logger.debug(s"Finding auth info for [$loginInfo] [$authType].")
    val result = db.run(authInfoQuery(loginInfo).filter(_.authType === authType).result.headOption)
    result.map { maybeDbAuthInfo =>
      maybeDbAuthInfo.map {
        case dbAuthInfo =>
          Logger.debug(s"Found [$dbAuthInfo].")
          dbAuthInfo.data
      }
    }
  }

  /**
    * Adds new auth info for the given login info.
    *
    * @param loginInfo The login info for which the auth info should be added.
    * @param authInfo The auth info to add.
    * @return The added auth info.
    */
  def add(loginInfo: LoginInfo, authInfo: JsValue, authType: String)(implicit ec: ExecutionContext): Future[JsValue] =
    db.run(addAction(loginInfo, authInfo, authType)).map(_ => authInfo)

  /**
    * Updates the auth info for the given login info.
    *
    * @param loginInfo The login info for which the auth info should be updated.
    * @param authInfo The auth info to update.
    * @return The updated auth info.
    */
  def update(loginInfo: LoginInfo, authInfo: JsValue, authType: String)(implicit ec: ExecutionContext): Future[JsValue] =
    db.run(updateAction(loginInfo, authInfo, authType)).map(_ => authInfo)

  /**
    * Saves the auth info for the given login info.
    *
    * This method either adds the auth info if it doesn't exists or it updates the auth info
    * if it already exists.
    *
    * @param loginInfo The login info for which the auth info should be saved.
    * @param authInfo The auth info to save.
    * @return The saved auth info.
    */
  def save(loginInfo: LoginInfo, authInfo: JsValue, authType: String)(implicit ec: ExecutionContext): Future[JsValue] = {
    val query = loginInfoQuery(loginInfo).joinLeft(slickAuthInfos).on(_.id === _.loginInfoId)
    val action = query.result.head.flatMap {
      case (dbLoginInfo, Some(dbAuthInfo)) if dbAuthInfo.authType == authType => updateAction(loginInfo, authInfo, authType)
      case (dbLoginInfo, _) => addAction(loginInfo, authInfo, authType)
    }.transactionally
    db.run(action).map(_ => authInfo)
  }

  /**
    * Removes the auth info for the given login info.
    *
    * @param loginInfo The login info for which the auth info should be removed.
    * @return A future to wait for the process to be completed.
    */
  def remove(loginInfo: LoginInfo, authType: String)(implicit ec: ExecutionContext): Future[Unit] =
    db.run(authInfoSubQuery(loginInfo).filter(_.authType === authType).delete).map(_ => ())
}

