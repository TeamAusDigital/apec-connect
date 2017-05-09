import { fork } from 'redux-saga/effects';
import authSaga from './auth.saga';

export default function* saga () {
  yield [
    fork(authSaga)
  ];
};
