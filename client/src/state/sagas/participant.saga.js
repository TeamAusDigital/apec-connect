import { all, call, put, takeLatest} from 'redux-saga/effects';
import actions from 'state/actions';
import {
  SIGN_UP,
  GET_PARTICIPANT,
  GET_PARTICIPANT_MESSAGES,
  HANDLE_PARTICIPANT_MESSAGES,
  HANDLE_SEND_PARTICIPANT_MESSAGE,
  SEND_PARTICIPANT_MESSAGE,
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
export function* getParticipantMessages () {
  try {
    const messages = [
       {
         sender: {
           identifier: '0858bb02-ace6-45fb-9684-ebe7e277f1bb',
           businessName: 'Test Business',
           username: 'test-dusty-elemental-9597',
           authToken: 'ee77690a4bf241439c7d9ddf3fb62ff5',
           isVerified: false,
           accountStatus: 'Enabled',
           rating:3,
           id: 1,
         },
         receiver: {
           identifier: '0e5cb22a-1e64-4a55-966e-7eac1e58fd69',
           businessName: 'Test Business',
           username: 'test-bitter-moon-7244',
           authToken: 'b6a9d1feab29457eb734f20e7b2caba2',
           isVerified: false,
           accountStatus: 'Enabled',
           rating:2,
           id: 3,

         },
         message: {
           senderId: '0858bb02-ace6-45fb-9684-ebe7e277f1bb',
           receiverId: '0e5cb22a-1e64-4a55-966e-7eac1e58fd69',
           message: 'Hello, going to generate invoice for you.',
           id: 3,
         }
       },
       {
         sender: {
           identifier: 'd0a8250e-f891-46c3-8ff9-8a27cdcf5c68',
           businessName: 'Jacob',
           username: 'test-celestial-sea-736',
           authToken: 'c1afb139ef2442bdb15ba096476a979a',
           isVerified: false,
           accountStatus: 'Enabled',
           id: 2,
           rating:2,
         },
         receiver: {
           identifier: '0e5cb22a-1e64-4a55-966e-7eac1e58fd69',
           businessName: 'Test Business',
           username: 'test-bitter-moon-7244',
           authToken: 'b6a9d1feab29457eb734f20e7b2caba2',
           isVerified: false,
           accountStatus: 'Enabled',
           id: 3,
           rating:2,
         },
         message: {
           senderId: 'd0a8250e-f891-46c3-8ff9-8a27cdcf5c68',
           receiverId: '0e5cb22a-1e64-4a55-966e-7eac1e58fd69',
           message: 'Hello, going to generate invoice for you.',
           invoiceId: 2,
           id: 4,
           metaData: {
            'recordStatus': 0,
            'dateCreated': 1494823315589,
            'lastUpdated': 1494823315589,
            'version': 0
           }
         },
         invoice: {
           issuerId: 'd0a8250e-f891-46c3-8ff9-8a27cdcf5c68',
           dateIssued: 1494511200000,
           dateDue: 1494770400000,
           isPaid: false,
           isAccepted: false,
           amount: {
             currency: 'AUD',
             amount: 43.5
           },
           currencyCode: 'AUD',
           paymentOptions: [
             'Cash'
           ],
           id: 2,
         }
       }
     ];
    yield put(actions.handleParticipantMessages(messages));
  }
  catch (error) {
    yield put(actions.handleParticipantMessages({error: error}));
  }
}

export function* sendParticipantMessage(action) {
  try {
    /** Here is the api call to post the message**/
    yield put(actions.handleSendParticipantMessage);
  }
  catch (error) {
    yield put(actions.handleSendParticipantMessage({error: error}));
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
  ]);
}
