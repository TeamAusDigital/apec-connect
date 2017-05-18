import { all, call, put, takeLatest} from 'redux-saga/effects';
import actions from 'state/actions';
import { browserHistory } from 'react-router';
import {
  NOT_AUTHENTICATED,
  SIGN_IN,
  AUTH_SUCCESS,
} from 'state/actions/actionTypes';
import apis from 'apis';

/**
 * Handles any side effects that the application will apply when participant authentication failed.
 * E.g. navigate to landing page.
 */
export function* notAuthenticated (action) {
  yield browserHistory.push('/join');
  yield put(actions.forgetUser());
}

export function* signIn (action) {
  const result = yield call(apis.fetchCurrentParticipant, {});
  if(result) {
    yield put(actions.authSuccess({
      token: action.payload,
      loggedIn: true
    }));
  }
}

export function* authSuccess() {
  yield put(actions.getParticipant());
}

export default function* authSaga () {
  yield all([
    takeLatest(NOT_AUTHENTICATED, notAuthenticated),
    takeLatest(SIGN_IN, signIn),
    takeLatest(AUTH_SUCCESS, authSuccess)
  ]);
}
