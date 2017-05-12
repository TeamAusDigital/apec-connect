import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white,red} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import TextField from 'material-ui/TextField';

/***

This is the basic elements for a main view in the app.

***/
const invoiceViewStyle ={
  textAlign: 'left',
  margin: '0 auto',
  position: 'relative',
  padding: '10px',
};

const feedbackStyle ={
  textAlign: 'center',
  width: '100%',
  position: 'relative',
  padding: '10px',
};


const divStyle = {
  display: 'inline-block',
  textAlign: 'center',
};

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100vh',
  padding: '10px',
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

export default class FeedbackScreen extends React.Component {

  constructor(props) {
    super(props);
    this.yourID='David';

    this.sellerID='S. Jobs';
    this.buyerID='David';
    this.sellerStarRating=2;
    this.buyerStarRating=3;
    this.invoiceID = '5a5df5case35f5case65f6as5df';
    this.invoiceWhat = '15 Beanie Hats';
    this. invoiceAmount = 99.99;
    this.invoiceSentDate = '11/05/2017';
    this.invoiceDueDate = '19/05/2017';

    this.toName = this.yourID === this.sellerID ? this.buyerID : this.sellerID;
  };

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Feedback'}/>
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
            <Paper
              zDepth={1}
              style={invoiceViewStyle}
            >
              <div style={divStyle}> <div style={divStyle}>{this.toName}</div> <div style={divStyle}>{this.invoiceSentDate}</div > <div style={divStyle}>{this.invoiceAmount}</div > </div>
              <div> {this.invoiceWhat}</div>
            </Paper>
            <br/>
            <br/>
            <Paper
              zDepth={1}
              style={feedbackStyle}
            >
              <div> Give feedback to {this.toName} </div>
              <br />
              <div>
                STAR RATING SYSTEM GOES IN HERE
              </div>
              <div style={invoiceViewStyle}>
                <TextField
                  hintText="Feedback message"
                  floatingLabelText="Feedback message"
                  multiLine={true}
                  rows={1}
                  fullWidth={true}
                />
              </div>

            </Paper>

            <RaisedButton label={'Leave Feedback'} backgroundColor={red} labelColor={white} fullWidth={true}/>

          </Paper>
        </Paper>
      </div>
    );
  }
}


