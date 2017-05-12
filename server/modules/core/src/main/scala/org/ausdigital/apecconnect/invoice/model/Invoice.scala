package org.ausdigital.apecconnect.invoice.model

import enumeratum.{EnumEntry, PlayEnum}
import org.ausdigital.apecconnect.common.model.ApecConnectEnumEntry
import org.ausdigital.apecconnect.db.model.{Record, RecordId}
import org.joda.money.Money
import org.joda.time.DateTime

import scalaz.Equal


sealed abstract class PaymentOption(val value: Int, val name: String) extends EnumEntry with ApecConnectEnumEntry

object PaymentOption extends PlayEnum[PaymentOption] {

  case object Cash extends PaymentOption(1, "CASH")

  case object VISA extends PaymentOption(2, "VISA")

  case object MasterCard extends PaymentOption(3, "MASTERCARD")

  case object PayPal extends PaymentOption(4, "PAYPAL")

  override val values: IndexedSeq[PaymentOption] = findValues

  implicit val equal: Equal[PaymentOption] = Equal.equalA
}


object Invoice {

  final case class InvoiceData(
                                dateIssued: DateTime,
                                dateDue: DateTime,
                                isPaid: Boolean,
                                isAccepted: Boolean,
                                amount: Money,
                                paymentReference: Option[String],
                                paymentOptions: Seq[PaymentOption],
                                paymentMethod: Option[PaymentOption])


  type Invoice = Record[InvoiceData]

  type InvoiceId = RecordId[Invoice]

}
