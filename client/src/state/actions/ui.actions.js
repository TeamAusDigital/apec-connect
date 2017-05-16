import {
  OPEN_MAIN_MENU,
  CLOSE_MAIN_MENU,
  TERMS_MENU_VALUE,
  ACCEPTED_PAYMENTS,
  REMEMBER_USER,
  FORGET_USER,
  SHOW_ERROR,
  HIDE_ERROR,
  SELECT_CURRENCY,
} from './actionTypes';
import { createActions } from 'redux-actions';

/**
 * UI related actions.
 */
const uiActions = createActions(
  OPEN_MAIN_MENU,
  CLOSE_MAIN_MENU,
  TERMS_MENU_VALUE,
  ACCEPTED_PAYMENTS,
  REMEMBER_USER,
  FORGET_USER,
  SELECT_CURRENCY,
  SHOW_ERROR,
  HIDE_ERROR,

);

module.exports = uiActions;
