import React, { Component } from 'react';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';

import { withRouter } from 'react-router';
import { connect } from 'react-redux';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100vh',
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

export default class ReceiptScreen extends React.Component {

  constructor(props) {
    super(props);
  };

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Home'}/>
        {/** Paper containing the logo **/}
        <Paper
          zDepth={1}
          style={paperStyle}
        >
          <img src={Logo} style={logoStyle} />
          <br />
          <br />

        </Paper>
      </div>
    );
  }
}


