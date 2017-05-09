package org.ausdigital.apecconnect.users.services

import javax.inject.Inject

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.{ AuthInfo, LoginInfo }
import org.ausdigital.apecconnect.users.dao.{ AuthInfoDao, AuthInfoFormatter }

import scala.concurrent.{ ExecutionContext, Future }
import scala.reflect.ClassTag
import scalaz.OptionT._
import scalaz._
import Scalaz._

class ApecConnectAuthInfoRepository @Inject() (
    authInfoDao: AuthInfoDao,
    authInfoFormatter: AuthInfoFormatter
)(implicit val ec: ExecutionContext) extends AuthInfoRepository {
  override def find[T <: AuthInfo](loginInfo: LoginInfo)(implicit tag: ClassTag[T]): Future[Option[T]] = {
    val authType = tag.runtimeClass.getName

    optionT(authInfoDao.find(loginInfo, authType)).map {
      authInfoFormatter.read[T]
    }.run
  }

  override def update[T <: AuthInfo](loginInfo: LoginInfo, authInfo: T): Future[T] = {
    val authInfoT = authInfoFormatter.write(authInfo)(ClassTag(authInfo.getClass))
    val authType = authInfo.getClass.getName

    authInfoDao.update(loginInfo, authInfoT, authType) map {
      authInfoFormatter.read(_)(ClassTag[T](authInfo.getClass))
    }
  }

  override def remove[T <: AuthInfo](loginInfo: LoginInfo)(implicit tag: ClassTag[T]): Future[Unit] = {
    val authType = tag.runtimeClass.getName
    authInfoDao.remove(loginInfo, authType)
  }

  override def save[T <: AuthInfo](loginInfo: LoginInfo, authInfo: T): Future[T] = {
    val authInfoT = authInfoFormatter.write(authInfo)(ClassTag(authInfo.getClass))
    val authType = authInfo.getClass.getName

    authInfoDao.save(loginInfo, authInfoT, authType) map {
      authInfoFormatter.read(_)(ClassTag[T](authInfo.getClass))
    }
  }

  override def add[T <: AuthInfo](loginInfo: LoginInfo, authInfo: T): Future[T] = {
    val authInfoT = authInfoFormatter.write(authInfo)(ClassTag(authInfo.getClass))
    val authType = authInfo.getClass.getName

    authInfoDao.add(loginInfo, authInfoT, authType) map {
      authInfoFormatter.read(_)(ClassTag[T](authInfo.getClass))
    }
  }
}
