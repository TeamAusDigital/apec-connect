import {
  SIGN_UP,
  GET_PARTICIPANT,
  LOOKUP_PARTICIPANTS,
  HANDLE_PARTICIPANTS_RESPONSE,
  HANDLE_PARTICIPANT_RESPONSE,
  GET_PARTICIPANT_MESSAGES,
  HANDLE_PARTICIPANT_MESSAGES,
  SEND_PARTICIPANT_MESSAGE,
  HANDLE_SEND_PARTICIPANT_MESSAGE,
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
  SEND_PARTICIPANT_MESSAGE,
  HANDLE_SEND_PARTICIPANT_MESSAGE,
  LOOKUP_PARTICIPANTS,
  HANDLE_PARTICIPANTS_RESPONSE,
);

module.exports = participantActions;
