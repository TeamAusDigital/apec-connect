package org.ausdigital.apecconnect.invoice.dao

import org.ausdigital.apecconnect.db.dao.BaseDbTableDefinitions
import org.ausdigital.apecconnect.invoice.model.Invoice.{Invoice, InvoiceData}
import org.ausdigital.apecconnect.invoice.model.PaymentOption
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantId
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.DateTime
import slick.jdbc.JdbcType
import slick.lifted.{MappedProjection, ProvenShape}

import scalaz._
import Scalaz._

/**
  * Table definitions and associated slick mappings for an Invoice.
  */
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
        PaymentOption.namesToValuesMap.getOrElse(name, throw new NoSuchElementException(s"[$name] is not a member of PaymentOption - [${PaymentOption.values}]."))
    }
  )

  class Invoices(tag: Tag) extends RecordTable[InvoiceData](tag, "invoice") {

    def issuerId: Rep[ParticipantId]              = column[ParticipantId]("issuer_id")
    def dateIssued: Rep[DateTime]                 = column[DateTime]("date_issued")
    def dateDue: Rep[DateTime]                    = column[DateTime]("date_due")
    def isPaid: Rep[Boolean]                      = column[Boolean]("is_paid")
    def isAccepted: Rep[Boolean]                  = column[Boolean]("is_accepted")
    def currencyCode: Rep[String]                 = column[String]("currency_code")
    def amount: Rep[BigDecimal]                   = column[BigDecimal]("amount")
    def paymentReference: Rep[Option[String]]     = column[Option[String]]("payment_reference")
    def paymentOptions: Rep[Seq[PaymentOption]]   = column[Seq[PaymentOption]]("payment_options")
    def paymentMethod: Rep[Option[PaymentOption]] = column[Option[PaymentOption]]("payment_method")

    def money: MappedProjection[Money, (String, BigDecimal)] =
      (currencyCode, amount).<>[Money](
        {
          case (cu, am) => Money.of(CurrencyUnit.of(cu), am.bigDecimal)
        }, { money =>
          Some((money.getCurrencyUnit.getCode, money.getAmount))
        }
      )

    private[dao] def data = (issuerId, dateIssued, dateDue, isPaid, isAccepted, money.column, paymentReference, paymentOptions, paymentMethod).mapTo[InvoiceData]

    override def * : ProvenShape[Invoice] = record(data)

  }

}
