import {
  AUTH_RESPONSE,
  NOT_AUTHENTICATED
} from '../actions/actionTypes';

import { handleActions } from 'redux-actions';

/**
 * Authentication property that is stored in the application state (store).
 */
const authentication = handleActions({
  AUTH_RESPONSE: (state, action) => {
    return Object.assign({}, state, {
      errorMessage: action.error?action.payload.message:'',
      loggedIn: action.payload.loggedIn,
      token: action.payload.token
    });
  },
  NOT_AUTHENTICATED: (state, action) => {
    return Object.assign({}, state, {
      loggedIn: false,
      token: null
    });
  }
}, {});

module.exports = {
  authentication
};
