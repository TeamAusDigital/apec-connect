import { applyMiddleware, compose, createStore, combineReducers } from 'redux';
import reducers from './reducers';
import saga from './sagas';
import createSagaMiddleware from 'redux-saga';
/**
 * Main configuration of redux
 * Configures reducers and middleware.
 */

const sagaMiddleware = createSagaMiddleware();
setTimeout(function() { sagaMiddleware.run(saga); });

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
  combineReducers(reducers),
  {},
  composeEnhancers(
    applyMiddleware(sagaMiddleware)
  )
);

export default store;
