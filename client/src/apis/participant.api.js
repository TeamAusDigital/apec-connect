import fetchIt from 'fetch-it';

/**
 * Register a new participant over the remote.
 * This API will register an user account on APEC Connect Business Register.
 * @param  {object} params required to register a participant.
 * @param  {string} params.businessName of a participant that is required to register.
 * @param  {string} params.email of participant, this is optional.
 * @param  {string} params.phone of the participant, this is optional.
 * @return {promise} registered participant's auth token (JWT), or error message.
 */
function signUp(params) {
  let participant = {
    businessName: params.businessName
  };
  let payload;
  if (params.email && params.email !== '') {
    payload = Object.assign({}, participant, {email: params.email});
  }
  else if (params.phone && params.phone !== ''){
    payload = Object.assign({}, participant, {phone: params.phone});
  }
  else {
    payload = participant;
  }

  return fetchIt.fetch('/api/v1/participants/sign-up', {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

/**
 * Fetches current authenticated participant with saved auth token.
 * @return {promise} a participant.
 */
function fetchCurrentParticipant() {
  return fetchIt.fetch('/api/v1/participants/current', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

/**
 * Sending a message to another participant.
 * @param  {object}    payload            payload of the message to be sent.
 * @param  {string}    payload.receiverId the identifier of the receiver participant, this is the uuid from IBR system.
 * @param  {string}    payload.message    the text content of the message, this is optional field.
 * @param  {rating}    payload.rating     the rating for the receiver, this is optional field.
 * @param  {invoiceId} payload.invoiceId  the associated id of the invoice that maybe attached with this message, this is optional field.
 * @return {promise} a created message for the receiver.
 */
function sendMessage(payload) {
  return fetchIt.fetch('/api/v1/participants/current/messages/create', {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

/**
 * Fetches all the messages that are related to current participant, both as sender or receiver.
 * TODO: currently no filter retriction on the query, add the query filter, e.g. last 30 days.
 * @return {promise} of all the messages related to the current participant.
 */
function fetchMessages() {
  return fetchIt.fetch('/api/v1/participants/current/messages', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

/**
 * Queries the Participants that matches the supplied query.
 * Currently the query only try to match Business Name of a Participant.
 * @param  {string} query contains the business name to find Participants.
 * @return {promise} of matched Participants.
 */
function lookupParticipants(query) {
  return fetchIt.fetch(`/api/v1/participants/lookup?query=${query}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  }).then((response) => response.result);
}

module.exports = {
  signUp,
  fetchCurrentParticipant,
  lookupParticipants,
  fetchMessages,
  sendMessage
};
