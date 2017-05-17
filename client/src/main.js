require('es6-promise').polyfill();
require('babel-polyfill');
require('flag-icon-css/css/flag-icon.min.css');

import injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin();

import React from 'react';
import ReactDOM from 'react-dom';
import routes from './routes';
import { Router, Route, browserHistory } from 'react-router';
import { Provider } from 'react-redux';
import Store from './state';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';

import apecConnectTheme from './components/apecConnectTheme';

const MOUNT_NODE = document.getElementById('app-container');


let render = (routes) => {
  ReactDOM.render(
    <MuiThemeProvider muiTheme={apecConnectTheme}>
      <Provider store={Store}>
        <Router history={browserHistory}>
          {routes}
        </Router>
      </Provider>
    </MuiThemeProvider>,
    MOUNT_NODE
  );
};


if (module.hot) {
  module.hot.accept('./routes', () => {
    // reload routes again
    let routes = require('./routes').default;
    render(routes);
  });

  module.hot.accept('./state/reducers', () => {
    // reload reducers again
    let newReducers = require('./state/reducers');
    replaceReducers(newReducers);
  });

  module.hot.accept('./state/sagas', () => {
    // reload sagas again
    let newSagas = require('./state/sagas');
    replaceReducers(newSagas);
  });
}

render(routes);
