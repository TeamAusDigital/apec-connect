import React, { Component } from 'react';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white,red} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';

/***

This is the basic elements for a main view in the app.

***/
const invoiceViewStyle ={
  textAlign: 'left',
  position: 'relative',
  padding: '10',
};

const feedbackStyle ={
  textAlign: 'center',
  width: '100%',
  position: 'relative',
  padding: '10',
};

const paperStyle = {
  padding: '10',
  textAlign: 'center',
  height: '100vh',
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

export default class MessageScreen extends React.Component {

  constructor(props) {
    super(props);
  };


  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'New Message'}/>
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
            style={invoiceViewStyle}
          >
            <TextField
              hintText='Message Recipient'
              floatingLabelText='Message Recipient'
              fullWidth={true}
            />

            <br/>
            <br/>

            <TextField
              hintText='User message'
              floatingLabelText='User message'
              multiLine={true}
              rows={1}
              fullWidth={true}
            />

            <br/>
            <br/>

            <RaisedButton label={'Send Message'} backgroundColor={red} labelColor={white} fullWidth={true}/>

          </Paper>
        </Paper>
      </div>
    );
  }
}


