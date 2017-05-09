package org.ausdigital.apecconnect.users.dao

import java.time.LocalDateTime
import javax.inject.Inject

import org.ausdigital.apecconnect.db.dao.SlickDao
import org.ausdigital.apecconnect.db.model.{ MetaData, RecordStatus }
import org.ausdigital.apecconnect.users.model.{ AccountStatus, ApecConnectUser, VerificationStatus }
import com.mohiva.play.silhouette.api.LoginInfo
import play.api.db.slick.DatabaseConfigProvider
import slick.dbio.Effect.{ Read, Write }

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.OptionT._
import scalaz.Scalaz._
import scalaz._

class UserDao @Inject() (override val dbConfigProvider: DatabaseConfigProvider) extends SlickDao with UserDbTableDefinitions {
  import driver.api._

  /**
    * Finds a user by its login info.
    *
    * @param loginInfo The login info of the user to find.
    * @return The found user or None if no user for the given login info could be found.
    */
  def find(loginInfo: LoginInfo)(implicit ec: ExecutionContext): Future[Option[ApecConnectUser]] = {
    val userQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbUserLoginInfo <- slickUserLoginInfos.filter(_.loginInfoId === dbLoginInfo.id)
      dbUser <- slickUsers.filter(_.id === dbUserLoginInfo.userId)
    } yield (dbLoginInfo, dbUser)

    optionT(db.run(userQuery.result.headOption)).map {
      case (dbLoginInfo, dbUser) => dbUser.toApecConnectUser(dbLoginInfo.toLoginInfo)
    }.run
  }

  /**
    * Finds a user by id.
    *
    * @param id the id of the user to find.
    * @return the found user (or None if no user with that id exists).
    */
  def findById(id: Long)(implicit ec: ExecutionContext): Future[Option[ApecConnectUser]] = findUser {
    _.id === id
  }

  /**
    * Fetches the details of all users in the system
    * @return a future of the users
    */
  def fetchAll(implicit ec: ExecutionContext): Future[Seq[ApecConnectUser]] = {
    val query = for {
      userAndLoginInfo <- slickUserLoginInfos
      dbUser <- activeSlickUsers.filter(_.id === userAndLoginInfo.userId)
      dbLoginInfo <- slickLoginInfos.filter(_.id === userAndLoginInfo.loginInfoId)
    } yield (dbUser, dbLoginInfo)

    db.run(query.result).map { dbTuples =>
      dbTuples.map {
        case (dbUser, dbLoginInfo) => dbUser.toApecConnectUser(dbLoginInfo.toLoginInfo)
      }
    }
  }

  /**
    * Saves a user.
    *
    * @param user The user to save.
    * @return The saved user.
    */
  def create(user: ApecConnectUser)(implicit ec: ExecutionContext): Future[ApecConnectUser] = {

    val now = LocalDateTime.now()

    val dbUser = DbUser(
      -1,
      user.firstName,
      user.lastName,
      user.fullName,
      VerificationStatus.Verified,
      AccountStatus.Enabled,
      MetaData(
        RecordStatus.Active,
        now,
        now,
        0
      )
    )

    val insertUserAction = slickUsers.returning(slickUsers.map(_.id)).into((user, id) => user.copy(id = id)) += dbUser

    // Check to see if UserLoginInfo exists.
    // If there is no UserLoginInfo yet for this user we create a new one.

    // combine database actions to be run sequentially
    val actions = (for {
      insertedUser <- insertUserAction
      // We don't have the LoginInfo id so we try to get it first.
      // If there is no LoginInfo yet for this user we retrieve the id on insertion.
      loginInfo <- findOrCreateLoginInfo(user.loginInfo, now)
      _ <- findOrCreateUserLoginInfo(insertedUser.id, loginInfo.id, now)
      found <- findUserQuery({
        _.id === insertedUser.id
      }).result.headOption
    } yield found).transactionally

    // run actions and return user afterwards
    db.run(actions) map {
      case None => throw new IllegalStateException(s"User [$user] not found after save.")
      case Some((createdDbUser, dbLoginInfo)) => createdDbUser.toApecConnectUser(dbLoginInfo.toLoginInfo)
    }
  }

  private def findOrCreateLoginInfo(
    loginInfo: LoginInfo,
    now: LocalDateTime
  )(implicit ec: ExecutionContext): DBIOAction[DbLoginInfo, NoStream, Read with Write] = {

    val dbLoginInfo = DbLoginInfo(-1, loginInfo.providerID, loginInfo.providerKey, MetaData(RecordStatus.Active, now, now, 0))

    val retrieveLoginInfo = activeSlickLoginInfos.filter(
      info => info.providerID === loginInfo.providerID &&
        info.providerKey === loginInfo.providerKey
    ).result.headOption

    val insertLoginInfo = slickLoginInfos.returning(slickLoginInfos.map(_.id)).
      into((info, id) => info.copy(id = id)) += dbLoginInfo

    for {
      loginInfoOption <- retrieveLoginInfo
      loginInfo <- loginInfoOption.map(DBIO.successful).getOrElse(insertLoginInfo)
    } yield loginInfo
  }

  private def findOrCreateUserLoginInfo(userId: Long, loginInfoId: Long, now: LocalDateTime)(implicit ec: ExecutionContext) = {

    val dbUserLoginInfo = DbUserLoginInfo(userId, loginInfoId, MetaData(RecordStatus.Active, now, now, 0))

    val retrieveUserLoginInfo = slickUserLoginInfos.filter(
      info => info.userId === dbUserLoginInfo.userId &&
        info.loginInfoId === dbUserLoginInfo.loginInfoId
    ).result.headOption
    val insertUserLoginInfo = slickUserLoginInfos += dbUserLoginInfo
    for {
      userLoginInfoOption <- retrieveUserLoginInfo
      userLoginInfo <- userLoginInfoOption.map(DBIO.successful).getOrElse(insertUserLoginInfo)
    } yield userLoginInfo
  }

  private def findUserQuery(criteria: Users => Rep[Boolean]) = for {
    dbUser <- activeSlickUsers filter criteria
    dbUserLoginInfo <- slickUserLoginInfos.filter(_.userId === dbUser.id)
    dbLoginInfo <- slickLoginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
  } yield (dbUser, dbLoginInfo)

  private def findUser(criteria: Users => Rep[Boolean])(implicit ec: ExecutionContext): Future[Option[ApecConnectUser]] = {
    optionT(db.run(findUserQuery(criteria).result.headOption)).map {
      case (dbUser, dbLoginInfo) => dbUser.toApecConnectUser(dbLoginInfo.toLoginInfo)
    }.run
  }

}
