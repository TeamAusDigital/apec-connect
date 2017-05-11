import { all, fork } from 'redux-saga/effects';
import participantSaga from './participant.saga';

export default function* saga () {
  yield all([
    fork(participantSaga)
  ]);
};
