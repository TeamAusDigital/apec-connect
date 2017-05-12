import {
  SIGN_UP,
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE,
  GET_PARTICIPANT_MESSAGES,
  HANDLE_PARTICIPANT_MESSAGES,
} from './actionTypes';
import { createActions } from 'redux-actions';

/**
 * Participant related actions.
 */
const participantActions = createActions(
  SIGN_UP,
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE,
  GET_PARTICIPANT_MESSAGES,
  HANDLE_PARTICIPANT_MESSAGES,
);

module.exports = participantActions;
