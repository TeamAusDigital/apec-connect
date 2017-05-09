package org.ausdigital.apecconnect.users.dao

import org.ausdigital.apecconnect.db.dao.{ BaseDbTableDefinitions, EnumerationMapper }
import org.ausdigital.apecconnect.db.model.{ HasMetaData, Identifiable, MetaData }
import org.ausdigital.apecconnect.users.model.AccountStatus.AccountStatus
import org.ausdigital.apecconnect.users.model.{ AccountStatus, ApecConnectUser, VerificationStatus }
import org.ausdigital.apecconnect.users.model.VerificationStatus.VerificationStatus
import com.mohiva.play.silhouette.api.LoginInfo
import play.api.libs.json.JsValue
import slick.jdbc.JdbcType
import slick.lifted
import slick.lifted.ProvenShape

/**
  * Created by rory on 24/04/17.
  */
trait UserDbTableDefinitions extends BaseDbTableDefinitions {
  import driver.api._

  implicit val verificationStatusMapper: JdbcType[VerificationStatus] = EnumerationMapper.mapper(VerificationStatus)

  implicit val accountStatusMapper: JdbcType[AccountStatus] = EnumerationMapper.mapper(AccountStatus)

  class Users(tag: Tag) extends Table[DbUser](tag, "user") with TableWithMetaData[DbUser] with TableWithId[DbUser] {

    def firstName: Rep[Option[String]] = column[Option[String]]("first_name")

    def lastName: Rep[Option[String]] = column[Option[String]]("last_name")

    def fullName: Rep[Option[String]] = column[Option[String]]("last_name")

    def verificationStatus: Rep[VerificationStatus] = column[VerificationStatus]("verification_status")

    def accountStatus: Rep[AccountStatus] = column[AccountStatus]("account_status")

    override def * : ProvenShape[DbUser] = (id, firstName, lastName, fullName, verificationStatus, accountStatus, meta) <> (DbUser.tupled, DbUser.unapply)
  }

  class UserTokens(tag: Tag) extends Table[DbUserToken](tag, "user_token") with TableWithMetaData[DbUserToken] with TableWithId[DbUserToken] {

    def userId: Rep[Long] = column[Long]("user_id")

    def token: Rep[String] = column[String]("token")

    def tokenType: Rep[String] = column[String]("token_type")

    def * : ProvenShape[DbUserToken] = (id, userId, token, tokenType, meta) <> (DbUserToken.tupled, DbUserToken.unapply)
  }

  class LoginInfos(tag: Tag) extends Table[DbLoginInfo](tag, "login_info") with TableWithMetaData[DbLoginInfo] with TableWithId[DbLoginInfo] {

    def providerID: Rep[String] = column[String]("provider_id")

    def providerKey: Rep[String] = column[String]("provider_key")

    def * : ProvenShape[DbLoginInfo] = (id, providerID, providerKey, meta) <> (DbLoginInfo.tupled, DbLoginInfo.unapply)
  }

  class UserLoginInfos(tag: Tag) extends Table[DbUserLoginInfo](tag, "user_login_info") with TableWithMetaData[DbUserLoginInfo] {
    def userId: Rep[Long] = column[Long]("user_id")

    def loginInfoId: Rep[Long] = column[Long]("login_info_id")

    def * : ProvenShape[DbUserLoginInfo] = (userId, loginInfoId, meta) <> (DbUserLoginInfo.tupled, DbUserLoginInfo.unapply)
  }

  class AuthInfos(tag: Tag) extends Table[DbAuthInfo](tag, "auth_info") with TableWithMetaData[DbAuthInfo] with TableWithId[DbAuthInfo] {

    def authType: Rep[String] = column[String]("auth_type")

    def data: Rep[JsValue] = column[JsValue]("data")

    def loginInfoId: Rep[Long] = column[Long]("login_info_id")

    def * : ProvenShape[DbAuthInfo] = (id, authType, data, loginInfoId, meta) <> (DbAuthInfo.tupled, DbAuthInfo.unapply)
  }

  //   queries used in multiple places
  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfos, DbLoginInfo, Seq] =
    activeSlickLoginInfos.filter(dbLoginInfo =>
      dbLoginInfo.providerID === loginInfo.providerID &&
        dbLoginInfo.providerKey.toLowerCase === loginInfo.providerKey.toLowerCase)

  // table query definitions
  val slickUsers: lifted.TableQuery[Users] = TableQuery[Users]
  val activeSlickUsers: Query[Users, DbUser, Seq] = slickUsers.filter(isActive)

  val slickUserTokens: lifted.TableQuery[UserTokens] = TableQuery[UserTokens]
  val activeSlickUserTokens: Query[UserTokens, DbUserToken, Seq] = slickUserTokens.filter(isActive)

  val slickLoginInfos: lifted.TableQuery[LoginInfos] = TableQuery[LoginInfos]
  val activeSlickLoginInfos: Query[LoginInfos, DbLoginInfo, Seq] = slickLoginInfos.filter(isActive)

  val slickUserLoginInfos: lifted.TableQuery[UserLoginInfos] = TableQuery[UserLoginInfos]
  val activeSlickUserLoginInfos: Query[UserLoginInfos, DbUserLoginInfo, Seq] = slickUserLoginInfos.filter(isActive)

  val slickAuthInfos: lifted.TableQuery[AuthInfos] = TableQuery[AuthInfos]
  val activeSlickAuthInfos: Query[AuthInfos, DbAuthInfo, Seq] = slickAuthInfos.filter(isActive)
}

final case class DbUser(
    id: Long,
    firstName: Option[String],
    lastName: Option[String],
    fullName: Option[String],
    verificationStatus: VerificationStatus,
    accountStatus: AccountStatus,
    metaData: MetaData
) extends HasMetaData[DbUser] with Identifiable[DbUser] {
  def toApecConnectUser(loginInfo: LoginInfo): ApecConnectUser = ApecConnectUser(
    id = id,
    loginInfo = loginInfo,
    firstName = firstName,
    lastName = lastName,
    fullName = fullName,
    verificationStatus = verificationStatus,
    accountStatus = accountStatus
  )

  override def updateId(_id: Long): DbUser = copy(id = _id)
  override def updateMetaData(_metaData: MetaData): DbUser = copy(metaData = _metaData)
}

final case class DbLoginInfo(
    id: Long,
    providerID: String,
    providerKey: String,
    metaData: MetaData
) extends HasMetaData[DbLoginInfo] with Identifiable[DbLoginInfo] {
  def toLoginInfo: LoginInfo = LoginInfo(
    providerID = providerID,
    providerKey = providerKey
  )

  override def updateId(_id: Long): DbLoginInfo = copy(id = _id)
  override def updateMetaData(_metaData: MetaData): DbLoginInfo = copy(metaData = _metaData)
}

final case class DbUserLoginInfo(
    userId: Long,
    loginInfoId: Long,
    metaData: MetaData
) extends HasMetaData[DbUserLoginInfo] {
  override def updateMetaData(metaDataM: MetaData = metaData): DbUserLoginInfo =
    copy(metaData = metaDataM)
}

final case class DbAuthInfo(
    id: Long,
    authType: String,
    data: JsValue,
    loginInfoId: Long,
    metaData: MetaData
) extends HasMetaData[DbAuthInfo] with Identifiable[DbAuthInfo] {
  override def updateMetaData(metaDataM: MetaData = metaData): DbAuthInfo =
    copy(metaData = metaDataM)

  override def updateId(id: Long = id): DbAuthInfo = copy(id = id)
}

final case class DbUserToken(
    id: Long,
    userId: Long,
    token: String,
    tokenType: String,
    metaData: MetaData
) extends HasMetaData[DbUserToken] with Identifiable[DbUserToken] {
  override def updateMetaData(metaDataM: MetaData = metaData): DbUserToken =
    copy(metaData = metaDataM)

  override def updateId(id: Long = id): DbUserToken = copy(id = id)
}