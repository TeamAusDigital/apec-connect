package org.ausdigital.apecconnect.participants.dao

import org.ausdigital.apecconnect.db.dao.BaseDbTableDefinitions
import org.ausdigital.apecconnect.participants.model.Participant.{ Participant, ParticipantData }
import org.ausdigital.apecconnect.participants.model.{ AccountStatus, VerificationStatus }
import slick.jdbc.JdbcType
import slick.lifted.ProvenShape

import scalaz._
import Scalaz._

trait ParticipantDbTableDefinitions extends BaseDbTableDefinitions {

  import profile.api._

  implicit val verificationStatusMapper: JdbcType[VerificationStatus] = MappedColumnType.base[VerificationStatus, Int](
    a => a.value,
    id =>
      VerificationStatus.values.find(_.value === id) match {
        case None => throw new NoSuchElementException(s"[$id] is not a member of VerificationStatus - [${VerificationStatus.values}].")
        case Some(found) => found
      }
  )

  implicit val accountStatusMapper: JdbcType[AccountStatus] = MappedColumnType.base[AccountStatus, Int](
    a => a.value,
    id =>
      AccountStatus.values.find(_.value === id) match {
        case None => throw new NoSuchElementException(s"[$id] is not a member of AccountStatus - [${AccountStatus.values}].")
        case Some(found) => found
      }
  )

  class Participants(tag: Tag) extends RecordTable[ParticipantData](tag, "participant") {

    def identifier: Rep[String] = column[String]("identifier")
    def businessName: Rep[String] = column[String]("business_name")
    def email: Rep[Option[String]] = column[Option[String]]("email")
    def phone: Rep[Option[String]] = column[Option[String]]("phone")
    def authToken: Rep[String] = column[String]("auth_token")
    def verificationStatus: Rep[VerificationStatus] = column[VerificationStatus]("verification_status")
    def accountStatus: Rep[AccountStatus] = column[AccountStatus]("account_status")

    private[dao] def data = (identifier, businessName, email, phone, authToken, verificationStatus, accountStatus).mapTo[ParticipantData]

    override def * : ProvenShape[Participant] = record(data)
  }

}
