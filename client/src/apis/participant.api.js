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

module.exports = {
  signUp,
  fetchCurrentParticipant
};
