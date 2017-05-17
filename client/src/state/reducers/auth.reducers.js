import {
  AUTH_RESPONSE,
  NOT_AUTHENTICATED,
  SIGN_UP,
} from '../actions/actionTypes';

import { handleActions } from 'redux-actions';

/**
 * Authentication property that is stored in the application state (store).
 */
const authentication = handleActions({
  SIGN_UP: (state, action) => {
    return Object.assign({}, state, {
      loading: true
    });
  },
  AUTH_RESPONSE: (state, action) => {
    return Object.assign({}, state, {
      errorMessage: action.error?action.payload.message:'',
      loggedIn: action.payload.loggedIn,
      token: action.payload.token,
      loading: false
    });
  },
  NOT_AUTHENTICATED: (state, action) => {
    return Object.assign({}, state, {
      loggedIn: false,
      token: null,
      loading: false
    });
  }
}, {});

module.exports = {
  authentication
};
