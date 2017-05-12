import { all, call, put, takeLatest} from 'redux-saga/effects';
import actions from 'state/actions';
import {
  SIGN_UP,
  GET_PARTICIPANT
} from 'state/actions/actionTypes';
import apis from 'apis';

/**
 * Handles sign up action's side effects:
 *  # lodge the successfully signed in authentication token (JWT).
 *  # retrieve current participant using the lodeged token.
 */
export function* signUp (action) {
  try {
    const token = yield call(apis.signUp, {
      businessName: action.payload.businessName,
      email: action.payload.email,
      phone: action.payload.phone
    });
    yield put(actions.authResponse({token: token}));
    yield put(actions.getParticipant());
  }
  catch (error) {
    yield put(actions.authResponse(error));
  }
}

/**
 * Handles side effects of get participant action:
 *  # stores updated participant into the application store, or.
 *  # error message for participant.
 */
export function* getParticipant () {
  try {
    const participant = yield call(apis.fetchCurrentParticipant, {});
    yield put(actions.handleParticipantResponse(participant));
  }
  catch (error) {
    yield put(actions.handleParticipantResponse({error: error}));
  }
}

/**
 * Sagas that watches client specific actions.
 */
export default function* participantSaga () {
  yield all([
    takeLatest(SIGN_UP, signUp),
    takeLatest(GET_PARTICIPANT, getParticipant)
  ]);
}
