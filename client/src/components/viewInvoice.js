import React, { Component } from 'react';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white,red} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import Divider from 'material-ui/Divider';
import {List, ListItem} from 'material-ui/List';


/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100%'
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

export default class ViewInvoice extends React.Component {

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

      this.invoicePaid = false;
      this.invoiceAccepted = false;


      this.sellerText = this.sellerID;
      if (this.yourID === this.sellerID) {
        this.sellerText = 'You';
      }
      this.buyerText = this.sellerID;
      if (this.yourID === this.buyerID) {
        this.buyerText = 'You';
      }


  };

  handlePay = () => {
    this.invoicePaid=true;
    this.forceUpdate();
    setTimeout(()=>{
      this.invoiceAccepted=true;
      this.forceUpdate();
      },3000);

  };

  handleAcceptPayment = () => {
    this.invoiceAccepted=true;
    this.forceUpdate();
  };

  handleViewReceipt = () => {
    this.props.router.push('/viewReceipt');
  };

  handleFeedback = () => {
    this.props.router.push('/feedback');
  };

  payButton = () => {
    if (this.buyerID===this.yourID && this.invoicePaid === false) {
      return(<RaisedButton label='Pay' backgroundColor={red} labelColor={white} fullWidth={true} onTouchTap={this.handlePay}/>);
    } else if (this.invoicePaid === true){
      return(<RaisedButton label='Invoice Paid' disabled={true} fullWidth={true}/>);
    } else {
      return(<RaisedButton label='Awaiting Payment' disabled={true} fullWidth={true}/>);
    }
  };

  acceptButton = () => {
    if (this.sellerID===this.yourID && this.invoiceAccepted === false && this.invoicePaid===true) {
      return(<RaisedButton label='Accept Payment' primary={true} fullWidth={true} onTouchTap={this.handleAcceptPayment}/>);
    } else if (this.invoiceAccepted === true){
      return(<RaisedButton label='Payment Accepted' disabled={true} fullWidth={true}/>);
    } else if (this.invoicePaid === true){
      return(<RaisedButton label='Awaiting Acceptance' disabled={true} fullWidth={true}/>);
    } else {
      return(<RaisedButton label='Awaiting Payment' disabled={true} fullWidth={true}/>);
    }

  };

  feedbackButton = () => {
    if (this.invoiceAccepted && this.invoicePaid) {
      return(<RaisedButton label='Send Feedback' disabled={false} fullWidth={true} onTouchTap={this.handleFeedback}/>);
    } else {
      return(<RaisedButton label='Send Feedback' disabled={true} fullWidth={true}/>);
    }

  };

  receiptButton = () => {
    if (this.invoiceAccepted && this.invoicePaid) {
      return(<RaisedButton label='View Receipt' disabled={false} fullWidth={true} onTouchTap={this.handleViewReceipt}/>);
    } else {
      return(<RaisedButton label='View Receipt' disabled={true} fullWidth={true}/>);
    }
  };

  render() {

    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'View Invoice'}/>
        {/** Paper containing the logo **/}
        <Paper
          zDepth={1}
          style={paperStyle}
        >
          <img src={Logo} style={logoStyle} />
          <br />
          <Paper
            zDepth={1}
            style={paperStyle}
          >
          <List>
            <ListItem>Seller: {this.sellerText} <StarRating rating={this.sellerStarRating}/></ListItem>
            <ListItem>Buyer: {this.buyerText} <StarRating rating={this.buyerStarRating}/></ListItem>
            <Divider />
            <ListItem>InvoiceID: {this.invoiceID} </ListItem>
            <ListItem>What: {this.invoiceWhat} </ListItem>
            <ListItem>Amount: ${this.invoiceAmount}</ListItem>
            <ListItem>Date sent: {this.invoiceSentDate}</ListItem>
            <ListItem>Date due: {this.invoiceDueDate}</ListItem>
            <Divider />
            <br />
            {this.payButton()}
            {this.acceptButton()}

            <br />

            <Divider />
            <br />
            {this.feedbackButton()}
            {this.receiptButton()}
          </List>

          </Paper>
        </Paper>
      </div>
    );
  }
}


