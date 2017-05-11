import fetchIt from 'fetch-it';

/**
 * [signUp description]
 * @param  {[type]} params [description]
 * @return {[type]}        [description]
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
