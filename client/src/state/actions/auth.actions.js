import {
  NOT_AUTHENTICATED,
  SIGN_IN,
  AUTH_SUCCESS,
  AUTH_FAILURE,
} from './actionTypes';
import { createActions } from 'redux-actions';

/**
 * Authentication related actions.
 */
const authActions = createActions(
  NOT_AUTHENTICATED,
  SIGN_IN,
  AUTH_SUCCESS,
  AUTH_FAILURE,
);

module.exports = authActions;
