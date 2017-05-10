import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import {Link} from 'react-router';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height:'100vh',
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

export default class HomeScreen extends React.Component {

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

          {/** Username and rating **/}

          <div style={businessNameStyle}>{CompanyName}</div>
          <div style={userDetailsStyle}> {userName} <StarRating rating={userStarRating} /> </div>
          <br />

          {/** Action buttons  **/}
          <RaisedButton label='Get Paid' style={getPaidStyle} secondary={true} containerElement={<Link to='/getPaid' />}/>
          <RaisedButton label='Pay' style={payStyle} primary={true} containerElement={<Link to='/pay' />} />
          <RaisedButton label='Inbox' style={inboxStyle} fullWidth={true} backgroundColor={indigo} labelColor={white} />
          <br />
        </Paper>
      </div>
    );
  }
}


