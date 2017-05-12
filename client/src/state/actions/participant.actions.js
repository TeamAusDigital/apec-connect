import {
  SIGN_UP,
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE
} from './actionTypes';
import { createActions } from 'redux-actions';

/**
 * Participant related actions.
 */
const participantActions = createActions(
  SIGN_UP,
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE
);

module.exports = participantActions;
