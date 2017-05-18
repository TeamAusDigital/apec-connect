import fetchIt from 'fetch-it';

/**
 * Creates an invoice remotely that will be issued to a participant.
 * @param  {object}       invoice payload that will be used to create the invoice.
 * @param  {string}       invoice.dateIssued invoice issue date.
 * @param  {string}       invoice.dateDue date of the invoice that must be paid.
 * @param  {boolean}      invoice.isPaid indicates if the invoice has been paid or not.
 * @param  {boolean}      invoice.isAccepted indicates if the payment of this invoice has been accepted or not.
 * @param  {object}       invoice.amount indicates how much the iovice worth, it must contain a currency and amount field.
 * @param  {array.string} invoice.paymentOptions that this invoice accepts.
 * @return {promise} of the created invoice.
 */
function createInvoice(invoice) {
  return fetchIt.fetch('/api/v1/participants/current/invoices/create', {
    method: 'POST',
    body: JSON.stringify(invoice),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

function invoicePaid(invoiceId) {
  return fetchIt.fetch(`/api/v1/invoices/${invoiceId}/paid`, {
    method: 'POST',
    body: JSON.stringify({}),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

function acceptInvoicePayment(invoiceId) {
  return fetchIt.fetch(`/api/v1/invoices/${invoiceId}/accept`, {
    method: 'POST',
    body: JSON.stringify({}),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

module.exports = {
  createInvoice,
  invoicePaid,
  acceptInvoicePayment,
};
