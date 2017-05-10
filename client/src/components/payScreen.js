import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import ToPayItem from '../components/toPayItem';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100vh',
  position: 'relative'
};

const headerDivStyle = {
  position: 'relative',
  width: '30%',
};

const payStyle = {
  width: '50%',
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

export default class PayScreen extends React.Component {

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Pay'}/>
        {/** Paper containing the logo **/}
        <Paper
          zDepth={1}
          style={paperStyle}
        >
          <img src={Logo} style={logoStyle} />
          <br />
          <br />
          <Paper
            zDepth={1}
            style={paperStyle}
          >
            {/** container for to pay elements **/}
            <ToPayItem message={'15 Beanie Hats'} amount={100.0}/>
            <ToPayItem message={'3 Beret'} amount={91.15}/>
            <ToPayItem message={'A Really long item name that may or may not wrap lets test it okay cool lets go'} amount={200.0}/>
          </Paper>
          <br />
        </Paper>
      </div>
    );
  }
}


