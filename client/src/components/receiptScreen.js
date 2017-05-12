import React, { Component } from 'react';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import Divider from 'material-ui/Divider';
import {List, ListItem} from 'material-ui/List';
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

    this.sellerName='Steve Jobs';
    this.buyerName='David\'s Hat Co.';

    this.sellerID='SJob55';
    this.buyerID='DHat72';

    this.invoiceID = '5a5df5case35f5case65f6as5df';
    this.invoiceWhat = '15 Beanie Hats';
    this. invoiceAmount = 99.99;
    this.invoiceSentDate = '11/05/2017';
    this.invoiceDueDate = '19/05/2017';

    this.invoicePaid = false;
    this.invoiceAccepted = false;

  };

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'View Receipt'}/>
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
          <List>
            <ListItem>Seller: {this.sellerName} ({this.sellerID})</ListItem>
            <ListItem>Buyer: {this.buyerName} ({this.buyerID}) </ListItem>
            <Divider />
            <ListItem>InvoiceID: {this.invoiceID} </ListItem>
            <ListItem>What: {this.invoiceWhat} </ListItem>
            <ListItem>Amount: ${this.invoiceAmount}</ListItem>
            <ListItem>Date sent: {this.invoiceSentDate}</ListItem>
            <ListItem>Date due: {this.invoiceDueDate}</ListItem>
            <Divider />
            <br />
            <ListItem>Paid and accepted</ListItem>
            <br />
          </List>

          </Paper>
        </Paper>
      </div>
    );
  }
}


