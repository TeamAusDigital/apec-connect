import { all, call, put, takeLatest} from 'redux-saga/effects';
import actions from 'state/actions';
import {
  SIGN_UP,
  GET_PARTICIPANT,
  GET_PARTICIPANT_MESSAGES,
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
 * Handles side effects of get participant messages action:
 *  # stores messages into the application store, or.
 *  # error message for participant.
 */
export function* getParticipantMessages (action) {
  try {
    const messages = [
      {
        id: 54654654,
        senderId: '21a48212548', // uuid from IBR
        receiverId: '21a2d212e2126225'', // uuid from IBR
        message: 'Hello There',
        rating: 0,
        invoice: {
          id: 21313213213,
          dateIssued: '2017-05-12T18:00:00.000+10:00',
          dateDue: '2017-05-12T18:00:00.000+10:00',
          isPaid: true,
          isAccepted: false,
          amount: {amount: 113.7,
                   currencyCode:"AUD",
                   currencySymbol:"$",
                   minorAmount:11370,}
          paymentReference: '1211321321',
          paymentOptions: ['VISA','CASH'],
          paymentMethod: 'VISA'
        }
      }
    ];
    yield put(actions.handleParticipantMessage(messages));
  }
  catch (error) {
    yield put(actions.handleParticipantMessage({error: error}));
  }
}

/**
 * Sagas that watches client specific actions.
 */
export default function* participantSaga () {
  yield all([
    takeLatest(SIGN_UP, signUp),
    takeLatest(GET_PARTICIPANT, getParticipant),
    takeLatest(GET_PARTICIPANT_MESSAGES, getParticipantMessages),
  ]);
}
