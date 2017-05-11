import {
  NOT_AUTHENTICATED,
  AUTH_RESPONSE
} from './actionTypes';
import { createActions } from 'redux-actions';

/**
 * Authentication related actions.
 */
const authActions = createActions(
  NOT_AUTHENTICATED,
  AUTH_RESPONSE
);

module.exports = authActions;
