import {
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE,
  GET_PARTICIPANT_MESSAGES,
  HANDLE_PARTICIPANT_MESSAGES,
  SEND_PARTICIPANT_MESSAGE,
  HANDLE_SEND_PARTICIPANT_MESSAGE,
  LOOKUP_PARTICIPANTS,
  HANDLE_PARTICIPANTS_RESPONSE,
} from '../actions/actionTypes';

import { handleActions } from 'redux-actions';

/**
 * Participant property that is stored in the application state (store).
 */
const participant = handleActions({
  GET_PARTICIPANT: (state, action) => {
    return Object.assign({}, state, {isFetching: true});
  },
  HANDLE_PARTICIPANT_RESPONSE: (state, action) => {
    return Object.assign({}, action.payload, {isFetching: false});
  }

},
  /**
    Initial State
  **/
  {
    isFetching: false,
  }
);


/**
 * Message history that is stored in the application state (store).
 */
const messages = handleActions({
  GET_PARTICIPANT_MESSAGES: (state, action) => {
    return Object.assign({}, state, {isFetching: true});
  },
  HANDLE_PARTICIPANT_MESSAGES: (state, action) => {
    return Object.assign({}, state, {messages: action.payload, isFetching: false});
  },
  SEND_PARTICIPANT_MESSAGE: (state, action) => {
    return Object.assign({}, state, {messageToSend: action.payload, isSending: true});
  },
  HANDLE_SEND_PARTICIPANT_MESSAGE: (state, action) => {
    return Object.assign({}, state, {isSending: false});
  },
  SELECTED_PARTICIPANT_MESSAGE: (state, action) => {
    return Object.assign({}, state, {selectedMessage: action.payload});
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
    return Object.assign({}, state, {participants: action.payload, isFetching: false});
  }
}, {isFetching: false, participants: []});

module.exports = {
  participant,
  messages,
  matchedParticipants
};
