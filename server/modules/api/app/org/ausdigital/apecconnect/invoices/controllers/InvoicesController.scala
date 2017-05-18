package org.ausdigital.apecconnect.invoices.controllers

import javax.inject.Inject

import io.kanaka.monadic.dsl._
import io.kanaka.monadic.dsl.compat.scalaz._
import org.ausdigital.apecconnect.nelAsResponse
import au.com.agiledigital.rest.controllers.transport.JsonApiResponse
import au.com.agiledigital.rest.security.BodyParsers
import org.ausdigital.apecconnect.admin.controllers.AuthorisingController
import org.ausdigital.apecconnect.auth.ApecConnectSilhouette
import org.ausdigital.apecconnect.invoice.model.Invoice
import org.ausdigital.apecconnect.invoice.model.Invoice.{Invoice, InvoiceId, PendingInvoice}
import org.ausdigital.apecconnect.invoice.services.InvoiceService
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantId
import org.ausdigital.apecconnect.participants.services.ParticipantService
import org.ausdigital.apecconnect.db.model.RecordOps._
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.{ExecutionContext, Future}
import scalaz._
import Scalaz._

/**
  * Provides public APIs related to Invoices of APEC Connect application.
  */
class InvoicesController @Inject()(invoiceService: InvoiceService, participantService: ParticipantService, val apecConnectSilhouette: ApecConnectSilhouette)(
    override implicit val executionContext: ExecutionContext)
    extends AuthorisingController
    with Controller {

  /**
    * Creates an [[Invoice]] for a Participant.
    * @return 200 OK with generated Invoice, otherwise return 400 bad request if failed.
    */
  def createInvoice(): Action[PendingInvoice] = participantAction.async(BodyParsers.whitelistingJson[PendingInvoice]) { implicit request =>
    for {
      generatedInvoice <- invoiceService.create(Invoice.pendingInvoiceToInvoiceData(request.identity.id, request.body)) ?| nelAsResponse
    } yield JsonApiResponse.buildResponse("Successfully generated invoice.", generatedInvoice)
  }

  /**
    * Marks an [[Invoice]] has been paid successfully.
    * FIXME: this should not be invoked simply by the Participant, it should be wired as callback after the real payment has been cleared with a
    * valid reference. For now, we simply mark the Invoice isPaid s True.
    * @param invoiceId id of the Invoice that is paid.
    * @return 200 OK with paid Invoice, otherwise 500 internal server error if failed.
    */
  def invoicePaid(invoiceId: InvoiceId): Action[AnyContent] = participantAction.async { implicit request =>
    for {
      paidInvoice <- invoiceService.update(invoiceId)(invoice => invoice.copy(data = invoice.data.copy(isPaid = true))) ?| JsonApiResponse.internalServerErrorResponse(
        "Failed to mark the Invoice to be paid.")
    } yield JsonApiResponse.buildResponse("Successfully marked Invoice as paid.", paidInvoice)
  }

  /**
    * Marks an [[Invoice]] payment has been accepted successfully. Only the Invoice issuer can accept the payment.
    * @param invoiceId id of the Invoice that will be accepted.
    * @return 200 OK with Invoice payment accepted, otherwise 404 not fonud if Invoice cannot be found, or
    *         500 internal server error if Invoice failed to mark as payment accepted.
    */
  def acceptInvoicePayment(invoiceId: InvoiceId): Action[AnyContent] = participantAction.async { implicit request =>
    for {
      invoice <- invoiceService.findById(invoiceId) ?| JsonApiResponse.notFoundResponse(s"Cannot find Invoice with id [${invoiceId.value}] to accept.")
      _       <- isInvoicePaymentAcceptable(request.identity.id, invoice)
      paidInvoice <- invoiceService.update(invoiceId)(invoice => invoice.copy(data = invoice.data.copy(isAccepted = true))) ?| JsonApiResponse.internalServerErrorResponse(
        "Failed to mark the Invoice payment to be accepted.")
    } yield JsonApiResponse.buildResponse("Successfully marked Invoice payment as accepted.", paidInvoice)
  }

  /**
    * An Invoice can only be accepted if it has been paid and the accept Participant is the Invoice issuer.
    * @param issuerId id of the Participant who issued the Invoice.
    * @param invoice that may be paid.
    * @return success step, or 400 bad request if failed.
    */
  private def isInvoicePaymentAcceptable(issuerId: ParticipantId, invoice: Invoice) =
    for {
      _                 <- Future.successful(invoice.issuerId === issuerId) ?| JsonApiResponse.badRequestResponse("You did not issue the Invoice, cannot accept payment.", Nil)
      invoiceAcceptable <- Future.successful(invoice.isPaid)                ?| JsonApiResponse.badRequestResponse("Invoice has not been paid, cannot accept payment", Nil)
    } yield invoiceAcceptable
}
