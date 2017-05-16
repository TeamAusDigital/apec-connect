package org.ausdigital.apecconnect.invoice.model

import enumeratum.{EnumEntry, PlayEnum}
import org.ausdigital.apecconnect.common.model.ApecConnectEnumEntry
import org.ausdigital.apecconnect.db.model.{Record, RecordId}
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantId
import org.joda.money.Money
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

import scalaz.Equal
import scala.collection.immutable.IndexedSeq

sealed abstract class PaymentOption(val value: Int, val name: String) extends EnumEntry with ApecConnectEnumEntry

object PaymentOption extends PlayEnum[PaymentOption] {

  case object Cash extends PaymentOption(1, "Cash")

  case object VISA extends PaymentOption(2, "VISA")

  case object MasterCard extends PaymentOption(3, "MasterCard")

  case object PayPal extends PaymentOption(4, "PayPal")

  override val values: IndexedSeq[PaymentOption] = findValues

  implicit val equal: Equal[PaymentOption] = Equal.equalA
}

/**
  * Invoice generated by Participant to indicate a payment is requested.
  */
object Invoice {

  import com.github.nscala_money.money.json.PlayImports._
  import org.ausdigital.apecconnect.common.model.JsonFormatters._

  final case class InvoiceData(issuerId: ParticipantId,
                               dateIssued: DateTime,
                               dateDue: DateTime,
                               isPaid: Boolean,
                               isAccepted: Boolean,
                               amount: Money,
                               currencyCode: String,
                               paymentReference: Option[String],
                               paymentOptions: Seq[PaymentOption],
                               paymentMethod: Option[PaymentOption])

  final case class PendingInvoice(dateIssued: DateTime,
                                  dateDue: DateTime,
                                  isPaid: Boolean,
                                  isAccepted: Boolean,
                                  amount: Money,
                                  paymentReference: Option[String],
                                  paymentOptions: Seq[PaymentOption],
                                  paymentMethod: Option[PaymentOption])

  type Invoice = Record[InvoiceData]

  type InvoiceId = RecordId[Invoice]

  def pendingInvoiceToInvoiceData(issuerId: ParticipantId, pending: PendingInvoice) = InvoiceData(
    issuerId = issuerId,
    dateIssued = pending.dateIssued,
    dateDue = pending.dateDue,
    isPaid = pending.isPaid,
    isAccepted = pending.isAccepted,
    amount = pending.amount,
    currencyCode = pending.amount.getCurrencyUnit.getCode,
    paymentReference = pending.paymentReference,
    paymentOptions = pending.paymentOptions,
    paymentMethod = pending.paymentMethod
  )

  implicit val invoiceFormatter: OFormat[InvoiceData] = Json.format[InvoiceData]

  implicit val pendingInvoiceFormatter: OFormat[PendingInvoice] = Json.format[PendingInvoice]
}
