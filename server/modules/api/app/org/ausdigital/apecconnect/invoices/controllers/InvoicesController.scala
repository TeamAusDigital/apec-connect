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
import org.ausdigital.apecconnect.invoice.model.Invoice.PendingInvoice
import org.ausdigital.apecconnect.invoice.services.InvoiceService
import org.ausdigital.apecconnect.participants.services.ParticipantService
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

/**
  * Provides public APIs related to Invoices of APEC Connect application.
  */
class InvoicesController @Inject()(invoiceService: InvoiceService, participantService: ParticipantService, val apecConnectSilhouette: ApecConnectSilhouette)(
    override implicit val executionContext: ExecutionContext)
    extends AuthorisingController
    with Controller {

  /**
    * Creates a invoice for a participant.
    * @return 200 OK with generated Invoice, otherwise return 400 bad request if failed.
    */
  def createInvoice(): Action[PendingInvoice] = participantAction.async(BodyParsers.whitelistingJson[PendingInvoice]) { implicit request =>
    for {
      generatedInvoice <- invoiceService.create(Invoice.pendingInvoiceToInvoiceData(request.identity.id, request.body)) ?| nelAsResponse
    } yield JsonApiResponse.buildResponse("Successfully generated invoice.", generatedInvoice)
  }
}
