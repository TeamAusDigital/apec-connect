import {
  OPEN_MAIN_MENU,
  CLOSE_MAIN_MENU,
  CLOSE_TERMS_MENU,
  OPEN_TERMS_MENU,
  SELECT_CURRENCY,
  SHOW_ERROR,
  HIDE_ERROR,
  SHOW_MESSAGE,
  NOTIFY_INVOICE_PAID,
  ACCEPT_INVOICE_PAYMENT,
  HANDLE_INVOICE_PAYMENT_NOTIFIED,
  HANDLE_INVOICE_PAYMENT_ACCEPTED,
} from '../actions/actionTypes';

import {
  REHYDRATE
} from 'redux-persist/constants';

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
  SELECT_CURRENCY: (state, action) => {
    return Object.assign({}, state, {
      currencyType: action.payload
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
  SHOW_MESSAGE: (state, action) => {
    return Object.assign({}, state, {
      errorMessage: action.payload,
      showError: true
    });
  },
  NOTIFY_INVOICE_PAID: (state, action) => {
    return Object.assign({}, state, {
      isNotifyingPayment: true
    });
  },
  ACCEPT_INVOICE_PAYMENT: (state, action) => {
    return Object.assign({}, state, {
      isAcceptingPayment: true
    });
  },
  HANDLE_INVOICE_PAYMENT_NOTIFIED: (state, action) => {
    return Object.assign({}, state, {
      isNotifyingPayment: false
    });
  },
  HANDLE_INVOICE_PAYMENT_ACCEPTED: (state, action) => {
    return Object.assign({}, state, {
      isAcceptingPayment: false
    });
  },
  REHYDRATE: (state, action) => {
    // Don't restore 'fetching' state, as callback will never come
    return Object.assign({}, action.payload.officials, {
      isNotifyingPayment: false,
      isAcceptingPayment: false,
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
    isNotifyingPayment: false,
    isAcceptingPayment: false
  }
);

module.exports = {
  ui
};
