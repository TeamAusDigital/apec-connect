import path from 'path';
import express from 'express';
import compression from 'compression';
import cookieParser from 'cookie-parser';

import React from 'react';

import routes from './src/routes';
import {
  setupReducers,
  renderHTMLString,
} from 'redux-router';

import reducers from './src/state/reducers';

const port = process.env.NODE_ENV === 'production' ? 8000 : 8005;

let app = express();
app.use(compression());
app.use(cookieParser());
app.use(express.static(path.join(process.cwd(), 'public')));
app.set('views', path.join(process.cwd(), 'views'));
app.set('view engine', 'pug');

const scriptDir = process.env.NODE_ENV === 'production' ? '/js' : 'http://localhost:8079/assets/js'

const scripts = ['plugins', 'app']
const scriptBlock = scripts.map(function(script) {
  return `<script src="${scriptDir}/${script}.js"></script>`
}).join('\n');

const styleBlock = process.env.NODE_ENV === 'production' ? '<link rel="stylesheet" href="/css/main.css" />' :
  '<script src="http://localhost:8079/assets/js/devServerClient.js"></script>\n<script src="http://localhost:8079/assets/js/main.js"></script>'

function renderHTML(req, res) {
  res.render('index', { app_scripts: scriptBlock, app_stylesheets: styleBlock });
}

app.use('*', function (req, res, next) {
  renderHTML(req, res);
})

app.listen(port, '0.0.0.0', () => {
  console.log(`Node.js app is running at http://0.0.0.0:${port}/`);
});
