import React, { Component } from 'react';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white,red} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import Divider from 'material-ui/Divider';
import {List, ListItem} from 'material-ui/List';
import PropTypes from 'prop-types';
import actions from 'state/actions';
import moment from 'moment';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';


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

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui,
    participant: state.participant,
    messages: state.messages,
  };
})
export default class ViewInvoice extends React.Component {

  constructor(props) {
      super(props);
      this.messageInvoice = this.props.messages.selectedMessage;

      this.isInvoice = false;
      if (this.messageInvoice.invoice) {
        this.isInvoice = true;
      }
      console.log(this.isInvoice);

      this.sellerText = this.messageInvoice.sender.businessName;
      this.sellerStarRating = this.messageInvoice.sender.rating;
      if (this.props.participant.identifier === this.messageInvoice.sender.identifier) {
        this.sellerText = 'You';
        this.sellerStarRating = this.props.participant.rating;
      }
      this.buyerText = this.messageInvoice.receiver.businessName;
      this.buyerStarRating = this.messageInvoice.receiver.rating;
      if (this.props.participant.identifier === this.messageInvoice.receiver.identifier) {
        this.buyerText = 'You';
        this.buyerStarRating = this.props.participant.rating;
      }

  };

  handlePay = () => {
    /** Handle the payment action **/
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
    if (this.messageInvoice.invoice.issuerId === this.props.participant.identifier &&
        this.messageInvoice.invoice.isPaid === false) {
      return(<RaisedButton label='Pay' backgroundColor={red} labelColor={white} fullWidth={true} onTouchTap={this.handlePay}/>);
    } else if (this.messageInvoice.invoice.isPaid === true){
      return(<RaisedButton label='Invoice Paid' disabled={true} fullWidth={true}/>);
    } else {
      return(<RaisedButton label='Awaiting Payment' disabled={true} fullWidth={true}/>);
    }
  };

  acceptButton = () => {
    if (this.messageInvoice.invoice.issuerId != this.props.participant.identifier && this.messageInvoice.invoice.isAccepted === false && this.messageInvoice.invoice.isPaid===true) {
      return(<RaisedButton label='Accept Payment' primary={true} fullWidth={true} onTouchTap={this.handleAcceptPayment}/>);
    } else if (this.messageInvoice.invoice.isAccepted === true){
      return(<RaisedButton label='Payment Accepted' disabled={true} fullWidth={true}/>);
    } else if (this.messageInvoice.invoice.isPaid === true){
      return(<RaisedButton label='Awaiting Acceptance' disabled={true} fullWidth={true}/>);
    } else {
      return(<RaisedButton label='Awaiting Payment' disabled={true} fullWidth={true}/>);
    }

  };

  feedbackButton = () => {
    if (this.messageInvoice.invoice.isAccepted && this.messageInvoice.invoice.isPaid) {
      return(<RaisedButton label='Send Feedback' disabled={false} fullWidth={true} onTouchTap={this.handleFeedback}/>);
    } else {
      return(<RaisedButton label='Send Feedback' disabled={true} fullWidth={true}/>);
    }

  };

  receiptButton = () => {
    if (this.messageInvoice.invoice.isAccepted && this.messageInvoice.invoice.isPaid) {
      return(<RaisedButton label='View Receipt' disabled={false} fullWidth={true} onTouchTap={this.handleViewReceipt}/>);
    } else {
      return(<RaisedButton label='View Receipt' disabled={true} fullWidth={true}/>);
    }
  };

  renderReceipt = () => {
    if (this.isInvoice) {
      return (
        [<ListItem>InvoiceID: {this.messageInvoice.invoice.id} </ListItem>,

        <ListItem>What: {this.messageInvoice.message.message} </ListItem>,
        <ListItem>Amount: {this.messageInvoice.invoice.currencyCode}  {this.messageInvoice.invoice.amount.amount}</ListItem>,
        <ListItem>Date sent: {moment(this.messageInvoice.invoice.dateIssued).format('YYYY-MM-DD')}</ListItem>,
        <ListItem>Date due: {moment(this.messageInvoice.invoice.dateDue).format('YYYY-MM-DD')}</ListItem>,
        <Divider />,
        <br />,
        this.payButton(),
        this.acceptButton(),

        <br />,

        <Divider />,
        <br />,
        this.feedbackButton(),
        this.receiptButton(),
        ]
      );
    } else {
      return (
        <ListItem>{this.messageInvoice.message.message} </ListItem>
      );
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
            {this.renderReceipt()}

          </List>

          </Paper>
        </Paper>
      </div>
    );
  }
}
