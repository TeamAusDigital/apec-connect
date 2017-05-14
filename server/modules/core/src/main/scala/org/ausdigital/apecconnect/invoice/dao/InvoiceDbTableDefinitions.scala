package org.ausdigital.apecconnect.invoice.dao

import org.ausdigital.apecconnect.db.dao.BaseDbTableDefinitions
import org.ausdigital.apecconnect.invoice.model.Invoice.{Invoice, InvoiceData}
import org.ausdigital.apecconnect.invoice.model.PaymentOption
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantId
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.DateTime
import play.api.Logger
import slick.jdbc.JdbcType
import slick.lifted.ProvenShape

import scalaz._
import Scalaz._

trait InvoiceDbTableDefinitions extends BaseDbTableDefinitions {

  import profile.api._

  // TODO: use EnumerationMapping.enumeratumMapper instead, fix compile error first.
  implicit val paymentOptionMapper: JdbcType[PaymentOption] = MappedColumnType.base[PaymentOption, String](
    a => a.name,
    name =>
      PaymentOption.values.find(_.name === name) match {
        case None        => throw new NoSuchElementException(s"[$name] is not a member of PaymentOption - [${PaymentOption.values}].")
        case Some(found) => found
    }
  )

  implicit val paymentOptionsMapper: JdbcType[Seq[PaymentOption]] = MappedColumnType.base[Seq[PaymentOption], String](
    paymentOptions => paymentOptions.map(_.name.toString).mkString(","),
    names =>
      names.split(",").map { name =>
        Logger.warn(s"${PaymentOption.namesToValuesMap}, $name")
        PaymentOption.namesToValuesMap.getOrElse(name, throw new NoSuchElementException(s"[$name] is not a member of PaymentOption - [${PaymentOption.values}]."))
    }
  )

  class Invoices(tag: Tag) extends RecordTable[InvoiceData](tag, "invoice") {

    def issuerId: Rep[ParticipantId]              = column[ParticipantId]("issuer_id")
    def dateIssued: Rep[DateTime]                 = column[DateTime]("date_issued")
    def dateDue: Rep[DateTime]                    = column[DateTime]("date_due")
    def isPaid: Rep[Boolean]                      = column[Boolean]("is_paid")
    def isAccepted: Rep[Boolean]                  = column[Boolean]("is_accepted")
    def amount: Rep[Money]                        = column[Money]("amount")
    def paymentReference: Rep[Option[String]]     = column[Option[String]]("payment_reference")
    def paymentOptions: Rep[Seq[PaymentOption]]   = column[Seq[PaymentOption]]("payment_options")
    def paymentMethod: Rep[Option[PaymentOption]] = column[Option[PaymentOption]]("payment_method")

    private[dao] def data = (issuerId, dateIssued, dateDue, isPaid, isAccepted, amount, paymentReference, paymentOptions, paymentMethod).mapTo[InvoiceData]

    override def * : ProvenShape[Invoice] = record(data)

  }

}
