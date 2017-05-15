import {
  OPEN_MAIN_MENU,
  CLOSE_MAIN_MENU,
  CLOSE_TERMS_MENU,
  OPEN_TERMS_MENU,
  SELECT_CURRENCY,
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
  SELECT_CURRENCY: (state, action) => {
     return Object.assign({}, state, {
       currencyType: action.payload
     });
   },
},
  /*** initial state  ***/
  {
    mainMenuOpen: false,
    termsMenuValue: 1,
    acceptedPayments: [],
    alreadyUser: false,
    currencyType: 'VND',
  }
);

module.exports = {
  ui
};
