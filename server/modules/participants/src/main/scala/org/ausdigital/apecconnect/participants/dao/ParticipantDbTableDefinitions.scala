package org.ausdigital.apecconnect.participants.dao

import org.ausdigital.apecconnect.db.dao.BaseDbTableDefinitions
import org.ausdigital.apecconnect.participants.model.Participant.{Participant, ParticipantData}
import org.ausdigital.apecconnect.participants.model.AccountStatus
import slick.jdbc.JdbcType
import slick.lifted.ProvenShape

import scalaz._
import Scalaz._

/**
  * Table definitions and associated slick mappings for a Participant.
  */
trait ParticipantDbTableDefinitions extends BaseDbTableDefinitions {

  import profile.api._

  // TODO: use EnumerationMapping.enumeratumMapper instead, fix compile error first.
  implicit val accountStatusMapper: JdbcType[AccountStatus] = MappedColumnType.base[AccountStatus, Int](
    a => a.value,
    id =>
      AccountStatus.values.find(_.value === id) match {
        case None        => throw new NoSuchElementException(s"[$id] is not a member of AccountStatus - [${AccountStatus.values}].")
        case Some(found) => found
    }
  )

  class Participants(tag: Tag) extends RecordTable[ParticipantData](tag, "participant") {

    def identifier: Rep[String]           = column[String]("identifier")
    def businessName: Rep[String]         = column[String]("business_name")
    def email: Rep[Option[String]]        = column[Option[String]]("email")
    def phone: Rep[Option[String]]        = column[Option[String]]("phone")
    def economy: Rep[String]              = column[String]("economy")
    def username: Rep[String]             = column[String]("username")
    def authToken: Rep[String]            = column[String]("auth_token")
    def rating: Rep[Option[Int]]          = column[Option[Int]]("rating")
    def isVerified: Rep[Boolean]          = column[Boolean]("is_verified")
    def accountStatus: Rep[AccountStatus] = column[AccountStatus]("account_status")

    private[dao] def data = (identifier, businessName, email, phone, economy, username, authToken, isVerified, rating, accountStatus).mapTo[ParticipantData]

    override def * : ProvenShape[Participant] = record(data)
  }

}
