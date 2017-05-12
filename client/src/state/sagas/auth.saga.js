import { all, call, put, takeLatest } from 'redux-saga/effects';
import actions from 'state/actions';
import { browserHistory } from 'react-router';
import {
  NOT_AUTHENTICATED
} from 'state/actions/actionTypes';

/**
 * Handles any side effects that the application will apply when participant authentication failed.
 * E.g. navigate to landing page.
 * @return {Generator} [description]
 */
export function* handleNotLoggedIn () {
  browserHistory.push('/');
}

export default function* authSaga () {
  yield all([
    takeLatest(NOT_AUTHENTICATED, handleNotLoggedIn)
  ]);
}
