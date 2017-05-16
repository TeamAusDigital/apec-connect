import { all, call, put, takeLatest} from 'redux-saga/effects';
import actions from 'state/actions';
import {
  SIGN_UP,
  GET_PARTICIPANT,
  GET_PARTICIPANT_MESSAGES,
  HANDLE_PARTICIPANT_MESSAGES,
  HANDLE_SEND_PARTICIPANT_MESSAGE,
  SEND_PARTICIPANT_MESSAGE,
  CREATE_INVOICE,
  LOOKUP_PARTICIPANTS,
  HANDLE_PARTICIPANTS_RESPONSE,
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
    if(token) {
      yield put(actions.authResponse({token: token}));
      yield put(actions.getParticipant());
    }
  }
  catch (error) {
    yield put(actions.authResponse({error: error}));
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
export function* getParticipantMessages () {
  try {
    const messages = yield call(apis.fetchMessages);
    yield put(actions.handleParticipantMessages(messages));
  }
  catch (error) {
    yield put(actions.handleParticipantMessages({error: error}));
  }
}

/**
 * Handles the action to send a message.
 */
export function* sendParticipantMessage(action) {
  try {
    let {message, invoice} = action.payload;
    let generatedInvoice, messageToSend;

    // If the payload contains an invoice, generate the invoice first.
    if (invoice) {
      generatedInvoice = yield call(apis.createInvoice, action.payload.invoice);
      messageToSend = Object.assign({}, message, {invoiceId: generatedInvoice.id});
    }

    yield call(apis.sendMessage, messageToSend);

    yield put(actions.handleSendParticipantMessage());
  }
  catch (error) {
    yield put(actions.handleSendParticipantMessage({error: error}));
  }
}

/**
 * Handles the action to lookup Participants.
 */
export function* lookupParticipants(action) {
  try {
    let query = action.payload;

    const matchedParticipants = yield call(apis.lookupParticipants, query);

    yield put(actions.handleParticipantsResponse(matchedParticipants));
  }
  catch (error) {
    yield put(actions.handleParticipantsResponse({error: error}));
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
    takeLatest(SEND_PARTICIPANT_MESSAGE, sendParticipantMessage),
    takeLatest(LOOKUP_PARTICIPANTS, lookupParticipants),
  ]);
}
