import { applyMiddleware, compose, createStore, combineReducers } from 'redux';
import reducers from './reducers';
import saga from './sagas';
import createSagaMiddleware from 'redux-saga';
import {persistStore, autoRehydrate} from 'redux-persist';
import FORGET_USER from './actions/actionTypes';

/**
 * Main configuration of redux
 * Configures reducers and middleware.
 */

const sagaMiddleware = createSagaMiddleware();
setTimeout(function() { sagaMiddleware.run(saga); });

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const combinedReducers = combineReducers(reducers);

const rootReducer = (state, action) {
  if (action.type === FORGET_USER) {
    state = undefined;
  }

  return combinedReducers(state, action);
}

const store = createStore(
  rootReducer,
  {},
  composeEnhancers(
    applyMiddleware(sagaMiddleware),
    autoRehydrate()
  )
);

persistStore(store);

export default store;
