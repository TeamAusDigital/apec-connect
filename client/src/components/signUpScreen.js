import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import {Link} from 'react-router';
import actions from 'state/actions';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100vh'
};

const getPaidStyle = {
  width: '50%',
};

const payStyle = {
  width: '50%',
};

const inboxStyle ={
};

const businessNameStyle ={
  fontSize: 48,
};

const userDetailsStyle = {
  fontSize: 20,
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

{/** User information that will be pulled from the database **/}
const CompanyName = 'David\'s Hat Co.';
const userName = 'DHat72';
const userStarRating = 1;

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class SignUpScreen extends React.Component {

  constructor(props) {
    super(props);
  }

  handleSignIn = () => {
    this.props.dispatch( actions.rememberUser() );
    this.props.router.push('/home');
  }

  render() {
    return (
      <div>
        {/** Paper containing the logo **/}
        <Paper
          zDepth={1}
          style={paperStyle}
        >
          <img src={Logo} style={logoStyle} />
          <br />
          <br />

          <div> Hello please sign up </div>
          <RaisedButton label='Sign Up' onTouchTap={this.handleSignIn}/>
          <br />
        </Paper>
      </div>
    );
  }
}


