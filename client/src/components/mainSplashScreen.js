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
import {Link} from 'react-router';

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
};

const bStyle ={
  width: '100%',
  textAlign: 'center',
  color: `${white}`,
  position: 'fixed',
  bottom: '50px',
  left: '0px',
  alignItems: 'center',
};

const textStyle ={
  width: '60%',
  textAlign: 'center',
  position: 'relative',
  margin: '0 auto',
};

const buttonStyle = {
  position: 'relative',
  width:'80%',
  margin: '0 auto',
};

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
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
          <div style={textStyle}>This app was developed during the APEC App Challenge</div>
          <br/>
          <div style={buttonStyle}>
            <RaisedButton
              label='Join'
              backgroundColor={red}
              labelColor={white}
              fullWidth={true}
              containerElement={<Link to='/signUp' />}
            />
          </div>
        </div>

      </div>

    </Paper>
    );
  }
}
