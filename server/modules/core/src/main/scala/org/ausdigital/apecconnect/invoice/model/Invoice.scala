package org.ausdigital.apecconnect.invoice.model

import enumeratum.{EnumEntry, PlayEnum}
import org.ausdigital.apecconnect.common.model.ApecConnectEnumEntry
import org.ausdigital.apecconnect.db.model.{Record, RecordId}
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantId
import org.joda.money.{CurrencyUnit, Money}
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

object Invoice {

  import com.github.nscala_money.money.json.PlayImports._

  final case class InvoiceData(issuerId: ParticipantId,
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

  implicit val messageFormatter: OFormat[InvoiceData] = Json.format[InvoiceData]
}
