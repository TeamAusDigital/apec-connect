import {
  OPEN_MAIN_MENU,
  CLOSE_MAIN_MENU,
  TERMS_MENU_VALUE,
  ACCEPTED_PAYMENTS,
} from './actionTypes';
import { createActions } from 'redux-actions';

/**
 * UI related actions.
 */
const uiActions = createActions(
  OPEN_MAIN_MENU,
  CLOSE_MAIN_MENU,
  TERMS_MENU_VALUE,
  ACCEPTED_PAYMENTS
);

module.exports = uiActions;
