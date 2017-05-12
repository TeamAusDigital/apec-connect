import {
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE,
  GET_PARTICIPANT_MESSAGES,
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
  }
  HANDLE_PARTICIPANT_MESSAGES: (state, action) => {
    return Object.assign({}, state, {messages: action.payload, isFetching: false});
  }
},
  /**
    Initial State
  **/
  {
    isFetching: false,
    messages: [],
  }
);


module.exports = {
  participant
};
