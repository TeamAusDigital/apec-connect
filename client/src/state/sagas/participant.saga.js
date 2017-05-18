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
  GET_ANNOUNCEMENTS,
  SELECT_PARTICIPANT_MESSAGE,
  HANDLE_SELECT_PARTICIPANT_MESSAGE,
  NOTIFY_INVOICE_PAID,
  ACCEPT_INVOICE_PAYMENT,
} from 'state/actions/actionTypes';
import Immutable from 'immutable';
import apis from 'apis';

/**
 * Handles sign up action's side effects:
 *  # lodge the successfully signed in authentication token (JWT).
 *  # retrieve current participant using the lodeged token.
 */
export function* signUp (action) {
  try {
    const token = yield call(apis.signUp, action.payload);
    if(token) {
      yield put(actions.authSuccess(token));
    }
    else {
      yield put(actions.authResponse({loading: false}));
    }
  }
  catch (error) {
    yield put(actions.authFailure(error));
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
    let sortedMessages = Immutable.List(messages).sortBy((m) => -m.message.metaData.dateCreated).toArray();
    yield put(actions.handleParticipantMessages(sortedMessages));
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
    } else {
      messageToSend = message;
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
 * Handles action for a Participant Message has been selected.
 */
export function* selectParticipantMessage(action) {
  try {
    let message = action.payload;
    yield put(actions.handleSelectParticipantMessage(message));
  }
  catch (error) {
    yield put(actions.handleSelectParticipantMessage({error: error}));
  }

};

/**
 * Handles action to get official annoucements. Officials store will be updated with
 * fetched announcements from API call and the announcements are sorted by date created.
 */
export function* getAnnouncements() {
  try {
    const announcements = yield call(apis.fetchAnnouncements);
    let sortedAnnouncements = Immutable.List(announcements).sortBy((annouce) => -annouce.metaData.dateCreated).toArray();
    yield put(actions.handleAnnouncements(sortedAnnouncements));
  }
  catch (error) {
    yield put(actions.handleAnnouncements({error: error}));
  }
}

export function* handleInvoiceState(action) {
  try {
    let {invoiceId, selectedMessageId} = action.payload;
    let messages;
    switch(action.type) {
      case NOTIFY_INVOICE_PAID:
        yield call(apis.invoicePaid, invoiceId);

        let messages = yield call(apis.fetchMessages);

        updatedMessage = Immutable.List(messages).find((m) => m.message.id === selectedMessageId);

        yield put(actions.selectParticipantMessage(updatedMessage));

        break;
      case ACCEPT_INVOICE_PAYMENT:
        yield call(apis.acceptInvoicePayment, invoiceId);

        messages = yield call(apis.fetchMessages);

        let updatedMessage = Immutable.List(messages).find((m) => m.message.id === selectedMessageId);

        yield put(actions.selectParticipantMessage(updatedMessage));
        break;
      default:
        updatedVendor = new Error(`Unknown action type [${action.type}]`);
    }
  }
  catch (error) {
    throw error;
  }
  finally {
    yield put(actions.handleInvoicePaymentNotified());
    yield put(actions.handleInvoicePaymentAccepted());
  }
}

/**
 * Sagas that watch Participant specific actions.
 */
export default function* participantSaga () {
  yield all([
    takeLatest(SIGN_UP, signUp),
    takeLatest(GET_PARTICIPANT, getParticipant),
    takeLatest(GET_PARTICIPANT_MESSAGES, getParticipantMessages),
    takeLatest(SEND_PARTICIPANT_MESSAGE, sendParticipantMessage),
    takeLatest(LOOKUP_PARTICIPANTS, lookupParticipants),
    takeLatest(GET_ANNOUNCEMENTS, getAnnouncements),
    takeLatest(SELECT_PARTICIPANT_MESSAGE, selectParticipantMessage),
    takeLatest([NOTIFY_INVOICE_PAID, ACCEPT_INVOICE_PAYMENT], handleInvoiceState)
  ]);
}
