import fetchIt from 'fetch-it';
import store from 'state';
import participantApi from './participant.api';
import actions from '../state/actions';

let apiMiddleware = {
  // TODO: central place to handle remote server error here: i.e. 4XX, 5XX.
  response (res) {
    let processedResponse = res;

    if (res.status === 200) {
      // Try to return a JSON representation.
      processedResponse = res.json().catch((e) => {
        return e;
      });
    }
    else if (res.status === 401) {
      Store.dispatch(actions.notAuthenticated());
    }

    return processedResponse;
  },

  request (req) {
    var state = store.getState();
    if(state.authentication.token) {
      req.headers.append('X-Auth-Token', state.authentication.token);
    }
    return req;
  }
};

fetchIt.addMiddlewares([apiMiddleware]);

module.exports = {
  ...participantApi
};
