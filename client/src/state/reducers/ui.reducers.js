import {
  OPEN_MAIN_MENU,
  CLOSE_MAIN_MENU,
  CLOSE_TERMS_MENU,
  OPEN_TERMS_MENU,
  SHOW_ERROR,
  HIDE_ERROR,
} from '../actions/actionTypes';

import { handleActions } from 'redux-actions';

const ui = handleActions({
  OPEN_MAIN_MENU: (state, action) => {
    return Object.assign({}, state, {
      mainMenuOpen: true
    });
  },
  CLOSE_MAIN_MENU: (state, action) => {
    return Object.assign({}, state, {
      mainMenuOpen: false
    });
  },
  TERMS_MENU_VALUE: (state, action) => {
    return Object.assign({}, state, {
      termsMenuValue: action.payload
    });
  },
  ACCEPTED_PAYMENTS: (state, action) => {
    return Object.assign({}, state, {
      /** Array of booleans  ***/
      acceptedPayments: action.payload
    });
  },
  REMEMBER_USER: (state, action) => {
    return Object.assign({}, state, {
      alreadyUser: true
    });
  },
  FORGET_USER: (state, action) => {
    return Object.assign({}, state, {
      alreadyUser: false
    });
  },
  SHOW_ERROR: (state, action) => {
    return Object.assign({}, state, {
      errorMessage: action.payload.message,
      showError: true
    });
  },
  HIDE_ERROR: (state, action) => {
    return Object.assign({}, state, {
      showError: false
    });
  },
},
  /*** initial state  ***/
  {
    mainMenuOpen: false,
    termsMenuValue: 1,
    acceptedPayments: [],
    alreadyUser: false,
    showError: false,
    errorMessage: '',
  }
);

module.exports = {
  ui
};
