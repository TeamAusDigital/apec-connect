import {
  AUTH_SUCCESS,
  AUTH_FAILURE,
  SIGN_UP,
  FORGET_USER,
} from '../actions/actionTypes';

import {
  REHYDRATE
} from 'redux-persist/constants';

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
  AUTH_SUCCESS: (state, action) => {
    return {
      loggedIn: true,
      token: action.payload,
      loading: false
    };
  },
  AUTH_FAILURE: (state, action) => {
    return {
      loggedIn: false,
      errorMessage: action.payload,
      loading: false
    };
  },
  FORGET_USER: () => {
    return { }
  },
  REHYDRATE: (state, action) => {
    // Only retain the token from the previous state, attempt re-auth
    return Object.assign({}, {
      token: action.payload.authentication.token,
    });
  }
}, {});

module.exports = {
  authentication
};
