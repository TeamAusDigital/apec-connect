import { all, put, takeEvery } from 'redux-saga/effects';
import actions from 'state/actions';
import { browserHistory } from 'react-router';
import {
  NOT_AUTHENTICATED
} from 'state/actions/actionTypes';

/**
 * Handles any side effects that the application will apply when participant authentication failed.
 * E.g. navigate to landing page.
 */
export function* notAuthenticated (action) {
  yield browserHistory.push('/');
}

export default function* authSaga () {
  yield all([
    takeEvery(NOT_AUTHENTICATED, notAuthenticated)
  ]);
}
