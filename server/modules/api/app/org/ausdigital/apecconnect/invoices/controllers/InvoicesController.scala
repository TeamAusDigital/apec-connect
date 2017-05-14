package org.ausdigital.apecconnect.invoices.controllers

import javax.inject.Inject

import io.kanaka.monadic.dsl._
import io.kanaka.monadic.dsl.compat.scalaz._
import org.ausdigital.apecconnect.nelAsResponse
import au.com.agiledigital.rest.controllers.transport.JsonApiResponse
import au.com.agiledigital.rest.security.BodyParsers
import org.ausdigital.apecconnect.admin.controllers.AuthorisingController
import org.ausdigital.apecconnect.auth.ApecConnectSilhouette
import org.ausdigital.apecconnect.invoice.model.Invoice.InvoiceData
import org.ausdigital.apecconnect.invoice.services.InvoiceService
import org.ausdigital.apecconnect.participants.services.ParticipantService
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext


class InvoicesController @Inject()(invoiceService: InvoiceService, participantService: ParticipantService, val apecConnectSilhouette: ApecConnectSilhouette)(
    override implicit val executionContext: ExecutionContext)
    extends AuthorisingController
    with Controller {

  def createInvoice(): Action[InvoiceData] = participantAction.async(BodyParsers.whitelistingJson[InvoiceData]) { implicit request =>
    for {
      generatedInvoice <- invoiceService.create(request.body.copy(issuerId = request.identity.id)) ?| nelAsResponse
    } yield JsonApiResponse.buildResponse("Successfully generated invoice.", generatedInvoice)
  }
}
