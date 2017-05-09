import { takeLatest } from 'redux-saga';
import { call, put } from 'redux-saga/effects';
import actions from 'state/actions';
import {
  CREDENTIALS_LOGIN,
  LOGOUT
} from 'state/actions/actionTypes';
import apis from 'apis';

/**
 * Handles the credentials based login request action.
 * The success authentication puts an auth cookie with valid browser session.
 * @param action the action contains the login credentials for payload.
 */
export function* credentialsLogin (action) {
  try {
    const authenticateResponse = yield call(apis.authentication.authenticateWithCredentials, action.payload.email, action.payload.password);
    yield put(actions.closeLoginModal());
    yield put(actions.authResponse({loggedIn: true, token: authenticateResponse.result.token}));
  }
  catch (error) {
    yield put(actions.authResponse(error));
  }
}

/**
 * Handles the logout request action.
 * The success logout removes the auth cookie from current session.
 */
export function* logout () {
  try {
    yield call(apis.authentication.signOut);
    yield put(actions.authResponse({loggedIn: false}));
  }
  catch (error) {
    yield put(actions.authResponse(error));
  }
}

/**
 * Sagas that watches client specific actions.
 */
export default function* clientSaga () {
  yield [
    takeLatest(CREDENTIALS_LOGIN, credentialsLogin),
    takeLatest(LOGOUT, logout)
  ];
}
