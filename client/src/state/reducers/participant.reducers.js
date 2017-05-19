import {
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE,
  GET_PARTICIPANT_MESSAGES,
  HANDLE_PARTICIPANT_MESSAGES,
  SEND_PARTICIPANT_MESSAGE,
  HANDLE_SEND_PARTICIPANT_MESSAGE,
  LOOKUP_PARTICIPANTS,
  HANDLE_PARTICIPANTS_RESPONSE,
  GET_ANNOUNCEMENTS,
  HANDLE_ANNOUNCEMENTS,
  SELECT_PARTICIPANT_MESSAGE,
  HANDLE_SELECT_PARTICIPANT_MESSAGE,
  FORGET_USER,
} from '../actions/actionTypes';

import { handleActions } from 'redux-actions';

/**
 * Participant property that is stored in the application state (store).
 */
const participant = handleActions({
  GET_PARTICIPANT: (state) => {
    return Object.assign({}, state, {
      isFetching: true
    });
  },
  HANDLE_PARTICIPANT_RESPONSE: (state, action) => {
    return Object.assign({}, action.payload);
  },
  FORGET_USER: () => {
    return {};
  }
}, {});


/**
 * Message history that is stored in the application state (store).
 */
const messages = handleActions({
  GET_PARTICIPANT_MESSAGES: (state, action) => {
    return Object.assign({}, state, {isFetching: true});
  },
  HANDLE_PARTICIPANT_MESSAGES: (state, action) => {
    return Object.assign({}, state, {messages: action.payload.messages, error: action.payload.error, isFetching: false});
  },
  SEND_PARTICIPANT_MESSAGE: (state, action) => {
    return Object.assign({}, state, {messageToSend: action.payload, isSending: true, sent: false});
  },
  HANDLE_SEND_PARTICIPANT_MESSAGE: (state, action) => {
    return Object.assign({}, state, {isSending: false, sent: true});
  },
  SELECT_PARTICIPANT_MESSAGE: (state, action) => {
    return Object.assign({}, state, {selectedMessage: action.payload, isFetching: true});
  },
  HANDLE_SELECT_PARTICIPANT_MESSAGE: (state, action) => {
    return Object.assign({}, state, {selectedMessage: action.payload, isFetching: false});
  },
},
  /**
    Initial State
  **/
  {
    isFetching: false,
    messages: [],
    messageToSend: {},
    isSending: false,
    sent: false,
  }
);

/**
 * Matched Participants after looking up the Participants.
 */
const matchedParticipants = handleActions({
  LOOKUP_PARTICIPANTS: (state, action) => {
    return Object.assign({}, state, {isFetching: true});
  },
  HANDLE_PARTICIPANTS_RESPONSE: (state, action) => {
    return Object.assign({}, state, {participants: action.payload.participants, error: action.payload.error, isFetching: false});
  }
},
  {
    isFetching: false,
    participants: [],
  });

/**
 * Officials object that will be stored in the store, it contains any official agencies inforamtion:
 * e.g. announcements.
 */
const officials = handleActions({
  GET_ANNOUNCEMENTS: (state, action) => {
    return Object.assign({}, state, {isFetching: true});
  },
  HANDLE_ANNOUNCEMENTS: (state, action) => {
    return Object.assign({}, state, {announcements: action.payload.announcements, error: action.payload.error, isFetching: false});
  }
},
  {
    isFetching: false,
    announcements: [],
  });

module.exports = {
  participant,
  messages,
  matchedParticipants,
  officials
};
