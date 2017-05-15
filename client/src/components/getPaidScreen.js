import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {grey,red,indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import actions from 'state/actions';
import moment from 'moment';
/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 5,
  textAlign: 'center',
  position: 'relative',
  height:'100%',
};

const getPaidPaperStyle = {
  padding: 10,
  margin: 5,
  textAlign: 'left',
  position: 'relative',
};

const userDetailsStyle = {
  fontSize: 20,
};

const logoStyle ={
  width: '75%',
  maxHeight: '150px',
};

const textFieldStyle = {
  padding: 0,
  margin: 0,
};

const currencyFieldStyle = {

};

const moneyDivStyle = {
  position: 'relative',
  display: 'block',
};



const paymentMethods = [
  {value: 0, name: 'Cash'},
  {value: 1, name: 'VISA'},
  {value: 2, name: 'PayPal'},
  {value: 3, name: 'BitCoin'}
];


@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui,
    participant: state.participant,
    messages: state.messages,
  };
})
export default class GetPaidScreen extends React.Component {

  constructor(props) {
    super(props);

    //Hold the prototype message to be sent as an invoice
    this.state = {
      invoiceMessage: {
         sender: {
           identifier: '0858bb02-ace6-45fb-9684-ebe7e277f1bb',
           businessName: 'Test Business',
           username: 'test-dusty-elemental-9597',
           authToken: 'ee77690a4bf241439c7d9ddf3fb62ff5',
           isVerified: false,
           accountStatus: 'Enabled',
           rating:3,
           id: 1,
         },
         receiver: {
           identifier: '0e5cb22a-1e64-4a55-966e-7eac1e58fd69',
           businessName: 'Test Business',
           username: 'test-bitter-moon-7244',
           authToken: 'b6a9d1feab29457eb734f20e7b2caba2',
           isVerified: false,
           accountStatus: 'Enabled',
           rating:2,
           id: 3,
         },
         message: {
           senderId: '0858bb02-ace6-45fb-9684-ebe7e277f1bb',
           receiverId: '0e5cb22a-1e64-4a55-966e-7eac1e58fd69',
           message: 'Hello, going to generate invoice for you.',
           id: 3,
         },
         invoice: {
            issuerId: '0858bb02-ace6-45fb-9684-ebe7e277f1bb',
            dateIssued: 1494511200000,
            dateDue: 1494770400000,
            isPaid: false,
            isAccepted: false,
            amount: {
              currency: 'AUD',
              amount: 0.0
            },
            currencyCode: '',
            paymentOptions: [
              'Cash'
            ],
            id: 2,
         }
      }
    };
  }

  //Handle updating the payment terms menu
  handleChange = (event, index, value) => this.props.dispatch(
    actions.termsMenuValue(value)
  );

  //handle change payment optiosn menu
  handleChangePayment = (event, index, values) => {
    this.props.dispatch( actions.acceptedPayments(values) );
  };

  //Handle change the currency options
  handleChangeCurrency  = (event, index, value) => {
    this.props.dispatch( actions.selectCurrency(value));
  };

  //Change payment option text depending on amount selected
  selectionRenderer = (values) => {
    switch (values.length) {
      case 0:
        return '';
      case 1:
        return paymentMethods[values[0]].name;
      default:
        return `${values.length} methods selected`;
    }
  }

  //Menu item for the payment options
  menuItems(paymentMethods) {
    return paymentMethods.map((paymentMethods) => (
      <MenuItem
        key={paymentMethods.value}
        insetChildren={true}
        checked={this.props.ui.acceptedPayments.includes(paymentMethods.value)}
        value={paymentMethods.value}
        primaryText={paymentMethods.name}
      />
    ));
  }

  //Calculate the due date
  calculateDueDate = () => {
    switch(this.props.ui.termsMenuValue) {
      case 1:
        return moment().endOf('day').fromNow().format('x');
      case 2:
        return moment().add(7, 'days').format('x');
      case 3:
        return moment().add(14, 'days').format('x');
      case 4:
        return moment().add(28, 'days').format('x');
      default:
        return moment().add(1, 'days').format('x');
    }
  }


  /** Collect remaining information and send a message
    * TODO Add randomised ids for each invoice and message
    * TODO add in lookup for receiver information
  **/
  handleSendMessage = () => {
    //Date Stuff
    this.state.invoiceMessage.invoice.dateIssued = moment().format('x');
    this.state.invoiceMessage.invoice.dateDue = this.calculateDueDate();

    //Currency Value
    this.state.invoiceMessage.invoice.amount.currency = this.props.ui.currencyType;

    //payment options
    this.state.invoiceMessage.invoice.paymentOptions = this.props.ui.acceptedPayments.map( (value) => paymentMethods[value].name);

    //My ID
    this.state.invoiceMessage.invoice.issuerId = this.props.participant.identifier;
    this.state.invoiceMessage.sender.identifier = this.props.participant.identifier;

    console.log(this.state.invoiceMessage);
    actions.sendParticipantMessage(this.state.invoiceMessage);

  }

  onMessageChange = (messageInfo) => {
    this.state.invoiceMessage.message.message = messageInfo;
  }

  onAmountChange = (amount) => {
    //let nextMessage = Object.assign({}, this.state.invoiceMessage.invoice.amount, amount);
    //this.setState(Object.assign({}, this.state, {invoiceMessage: nextMessage}));
    this.state.invoiceMessage.invoice.amount.amount = amount;
  }

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Get Paid'}/>
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
            style={getPaidPaperStyle}
          >
            <TextField
              fullWidth={true}
              style={textFieldStyle}
              hintText='Person you are paying'
              floatingLabelText='Who'
            />
            <br />
            <div style={moneyDivStyle}>
              <SelectField
                floatingLabelText='Currency'
                value={this.props.ui.currencyType}
                onChange={this.handleChangeCurrency}
                style={currencyFieldStyle}
              >
                <MenuItem value={'VND'} primaryText='VND' />
                <MenuItem value={'USD'} primaryText='USD' />
                <MenuItem value={'AUD'} primaryText='AUD' />
              </SelectField>
              <TextField
                type='number'
                style = {textFieldStyle}
                fullWidth={true}
                hintText='Amount to pay'
                floatingLabelText='How much'
                onChange={(event) => this.onAmountChange(event.target.value)}
              />
            </div>
            <br />
            <TextField
              fullWidth={true}
              style = {textFieldStyle}
              hintText='What for'
              floatingLabelText='What did you purchase'
              multiLine={true}
              rows={1}
              rowsMax={4}
              onChange={(event) => this.onMessageChange(event.target.value)}
            />
            <br />
            <SelectField
              floatingLabelText='Payment Terms'
              value={this.props.ui.termsMenuValue}
              onChange={this.handleChange}
              fullWidth={true}
              style={textFieldStyle}
            >
              <MenuItem value={1} primaryText='Today' />
              <MenuItem value={2} primaryText='1 Week' />
              <MenuItem value={3} primaryText='2 Weeks' />
              <MenuItem value={4} primaryText='1 Month' />
            </SelectField>
            <br />
            <SelectField
              multiple={true}
              style={textFieldStyle}
              floatingLabelText='Payment methods'
              value={this.props.ui.acceptedPayments}
              onChange={this.handleChangePayment}
              fullWidth={true}
              selectionRenderer={this.selectionRenderer}
            >
              {this.menuItems(paymentMethods)}
            </SelectField>

            <br />

            <RaisedButton label='Send' fullWidth={true} backgroundColor={red} labelColor={white} onTouchTap={() => this.handleSendMessage()}/>

          </Paper>
        </Paper>
      </div>
    );
  }
}


