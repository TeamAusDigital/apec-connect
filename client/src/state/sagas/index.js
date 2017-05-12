import { all, fork } from 'redux-saga/effects';
import participantSaga from './participant.saga';
import authSaga from './auth.saga';

export default function* saga () {
  yield all([
    fork(participantSaga),
    fork(authSaga)
  ]);
};
