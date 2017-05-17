import fetchIt from 'fetch-it';
import store from 'state';
import participantApi from './participant.api';
import invoiceApi from './invoice.api';
import actions from '../state/actions';
import Immutable from 'immutable';

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
      store.dispatch(actions.notAuthenticated());
    }
    else if (res.status === 400) {

      res.json().then((e) => {
        if(e.messages.length) {
          let error = e.messages[0];
          if (!Immutable.fromJS(error.context).isEmpty()) {
            store.dispatch(actions.showError({message: 'Please fill in required field.'}));
          }
          else {
            store.dispatch(actions.showError(error));
          }
        }
      });
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
  ...participantApi,
  ...invoiceApi
};
