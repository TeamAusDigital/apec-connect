import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white,red} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import {Link} from 'react-router';
import actions from 'state/actions';
import TextField from 'material-ui/TextField';
import Background from '../common/assets/bg-bottom.png';

/***`

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100vh',
  overflow: 'hidden',
};

const divStyle = {
   height: '100%',
   backgroundImage: `url(${Background})`,
   backgroundSize: 'cover',
   color: `${white}`,
   position: 'relative',
};

const textStyle = {
  color: `${white}`,
  width: '75%',
  margin: '0 auto',
  position: 'relative',
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

const btnStyle ={
  width: '100%',
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

          <div style={divStyle}>
            <div style={textStyle}>
              <TextField
                hintText="Business Name"
                errorText="This field is required"
                floatingLabelText='Business Name'
              />

              <div style={textStyle}> OR </div>
              <TextField
                hintText="Email Address"
                floatingLabelText="Email Address"
                type="email"
              />

              <div style={textStyle}> OR </div>
              <TextField
                hintText="Mobile Phone Number"
                floatingLabelText="Mobile Phone Number"
                type="number"
              />
              <div style={textStyle}> OR </div>
              <br />
              <RaisedButton style={btnStyle} label='Social Media' primary={true}/>
            </div>
            <br/>
            <RaisedButton style={btnStyle} label='Sign Up' onTouchTap={this.handleSignIn} backgroundColor={red} labelColor={white}/>
            <br />
          </div>
        </Paper>
      </div>
    );
  }
}


