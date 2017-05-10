import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import RaisedButton from 'material-ui/RaisedButton';
import {grey,red,indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import actions from 'state/actions';

import Background from '../common/assets/bg-bottom.png';

const logoStyle ={
  width: '100%',
  position: 'relative'
};

const paperStyle = {
  padding: 5,
  textAlign: 'center',
  position: 'relative',
  height:'100vh',
  overflow: 'hidden',
};

const divStyle = {
   height: '100%',
   backgroundImage: `url(${Background})`,
   backgroundSize: 'cover',
   textColor: {white},
   position: 'relative',
   textAlign: 'center',
};

const bStyle ={
  width: '50%',
  textAlign: 'center',
  color: `${white}`,
  marginLeft: 'auto',
  marginRight: 'auto',
 position: 'fixed',
  bottom: 0,
};

const bStyleInner ={

};

export default class MainSplashScreen extends React.Component {
  render() {
    return (
    <Paper
      zDepth={1}
      style={paperStyle}
    >
      <img src={Logo} style={logoStyle} />
      <div style={divStyle}>

        <div style={bStyle}>
          <div>This app was developed during the APEC App Challenge</div>

          <RaisedButton label='Join' backgroundColor={red} labelColor={white} />
        </div>

      </div>

    </Paper>
    );
  }
}
