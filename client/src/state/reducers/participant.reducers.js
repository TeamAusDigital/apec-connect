import {
  GET_PARTICIPANT,
  HANDLE_PARTICIPANT_RESPONSE
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
    let nextState = Object.assign({}, action.payload, {isFetching: false});
    return Object.assign({}, state, nextState);
  }
}, {isFetching: false});

module.exports = {
  participant
};
