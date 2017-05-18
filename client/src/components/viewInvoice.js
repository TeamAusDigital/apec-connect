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
import EconomyFlag from '../components/EconomyFlag';
import CircularProgress from 'material-ui/CircularProgress';

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
  paddingBottom: '20px'
};

const loadingIconStyle = {
  textAlign: 'center'
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
  };

  handlePay = (invoice, message) => {
    let {dispatch} = this.props;
    dispatch(actions.notifyInvoicePaid({invoiceId: invoice.id, selectedMessageId: message.id}));
  };

  handleAcceptPayment = (invoice, message) => {
    let {dispatch} = this.props;
    dispatch(actions.acceptInvoicePayment({invoiceId: invoice.id, selectedMessageId: message.id}));
  };

  listItemContent = (label, content) => {
    return (<ListItem>
              <div style={{float: 'left'}}>{label}:</div>
              <div style={{float: 'right'}}>
                {content}
              </div>
            </ListItem>);
  }

  payButton = () => {
    let {sender, invoice, message} = this.props.messages.selectedMessage;
    let {participant} = this.props;
    let {isNotifyingPayment} = this.props.ui;

    if (!isNotifyingPayment) {
      if (sender && invoice && sender.identifier !== participant.identifier) {
        if (invoice.isPaid) {
          return(<RaisedButton label='Invoice Paid' disabled={true} fullWidth={true}/>);
        }
        else {
          return(<RaisedButton label='Pay' backgroundColor={red} labelColor={white} fullWidth={true} onTouchTap={() => this.handlePay(invoice, message)}/>);
        }
      }
    }
    else {
      return <div style={loadingIconStyle}> <CircularProgress /> </div>;
    }
  };

  acceptButton = () => {
    let {sender, invoice, message} = this.props.messages.selectedMessage;
    let {participant} = this.props;
    let {isAcceptingPayment} = this.props.ui;

    if (!isAcceptingPayment) {
      if (sender && invoice && sender.identifier === participant.identifier) {
        if (!invoice.isAccepted && !invoice.isPaid) {
          return(<RaisedButton label='Awaiting Payment' disabled={true} fullWidth={true}/>);
        }
        else if (!invoice.isAccepted && invoice.isPaid) {
          return(<RaisedButton label='Accept Payment' primary={true} fullWidth={true} onTouchTap={() => this.handleAcceptPayment(invoice, message)}/>);
        }
      }
    }
    else {
      return <div style={loadingIconStyle}> <CircularProgress /> </div>;
    }
  };

  renderReceipt = () => {
    let {sender, invoice, message} = this.props.messages.selectedMessage;
    let {participant} = this.props;

    return (
      <div>
        {
          invoice ? this.listItemContent('Invoice ID', invoice.id) : ''
        }
        {
          invoice ? this.listItemContent('Amount', invoice.amount.currency + ' ' + invoice.amount.amount) : ''
        }
        {
          invoice ? this.listItemContent('Date Issued', moment(invoice.dateIssued).format('YYYY-MM-DD'))
                    :
                    this.listItemContent('Date Sent', moment(message.metaData.dateCreated).format('YYYY-MM-DD'))
        }
        {
          invoice ? this.listItemContent('Date Due', moment(invoice.dateDue).format('YYYY-MM-DD')) : ''
        }
        <ListItem>
          <div style={{textAlign: 'left', width: '100%'}}>Message:</div>
          <div style={{textAlign: 'left', paddingTop: '10px'}}>
            {message ? message.message : ''}
          </div>
        </ListItem>

        <br />

        <Divider />

        <div style={{padding: '10px 0'}}>
          {this.payButton()}
        </div>

        <div style={{padding: '10px 0'}}>
          {this.acceptButton()}
        </div>

        <div style={{padding: '10px 0'}}>
          {
            invoice ? <RaisedButton label='Send Feedback' disabled={!invoice.isAccepted || !invoice.isPaid} fullWidth={true}/> : ''
          }
        </div>
      </div>
    );
  };

  render() {
    let {sender, receiver, invoice, message} = this.props.messages.selectedMessage;
    let {participant} = this.props;

    return (
      <div>
        <AppBarMain title={'View Invoice'}/>
        <Paper
          zDepth={1}
          style={paperStyle}
        >
          <img src={Logo} style={logoStyle} />
          <Paper
            zDepth={1}
            style={paperStyle}
          >
            <List>
              <ListItem>
                {
                  invoice ?
                    <div>
                      <div>Seller: {sender.identifier === participant.identifier ? 'You' : sender.businessName}</div>
                      <StarRating rating={sender.rating}/>
                    </div>
                    :
                    <div>
                      <span>Official Announcement</span>
                      &nbsp;&nbsp;
                      {/* FIXME: This is hard coded official economy code */}
                      <EconomyFlag economyCode='VN' />
                    </div>
                }
              </ListItem>
              <ListItem>
                {
                  invoice ?
                    <div>
                      <div>Buyer: {receiver.identifier === participant.identifier ? 'You' : receiver.businessName}</div>
                      <StarRating rating={receiver.rating}/>
                    </div> : ''
                }
              </ListItem>
              <Divider />
              {this.renderReceipt()}
            </List>
          </Paper>
        </Paper>
      </div>
    );
  }
}
