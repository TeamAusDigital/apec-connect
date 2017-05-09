import fetchIt from 'fetch-it';

function signOut() {
  return fetchIt.fetch('/api/v1/current-user/sign-out', {
    method: 'POST'
  });
}

function authenticateWithCredentials(email, password) {
  var credentials = {
    identifier: email,
    password: password
  };

  return fetchIt.fetch('/api/v1/current-user/credentials/sign-in', {
    method: 'POST',
    body: JSON.stringify(credentials),
    headers: {
      'Content-Type': 'application/json'
    }
  });
}

module.exports = {
  signOut,
  authenticateWithCredentials
};
