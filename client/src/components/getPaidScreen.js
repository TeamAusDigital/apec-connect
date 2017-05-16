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
import AutoComplete from 'material-ui/AutoComplete';
import actions from 'state/actions';
import moment from 'moment';
import Immutable from 'immutable';

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

const lStyle = {
  fontSize: '20px'
};

const moneyDivStyle = {
  position: 'relative',
  display: 'block',
};

/**
 * Currently supported payment options (methods).
 * @type {Array}
 */
const paymentOptions = [
  {value: 'Cash', text: 'Cash'},
  {value: 'VISA', text: 'VISA'},
  {value: 'MasterCard', text: 'MasterCard'},
  {value: 'PayPal', text: 'PayPal'}
];

/**
 * The payment terms for an Invoice to be paid.
 * i.e. how far away from due date.
 * @type {Array}
 */
const paymentTerms = [
  {value: 0, unit: 'days', text: 'Today'},
  {value: 1, unit: 'weeks', text: '1 Week'},
  {value: 2, unit: 'weeks', text: '2 Weeks'},
  {value: 1, unit: 'months', text: '1 Month'}
];

/**
 * Currently supported currency types.
 * TODO: more currency types quried from remote.
 * @type {Array}
 */
const currencyTypes = [
  {value: 'VND', text: 'VND'},
  {value: 'AUD', text: 'AUD'},
  {value: 'USD', text: 'USD'}
];

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui,
    participant: state.participant,
    messages: state.messages,
    matchedParticipants: state.matchedParticipants,
  };
})

/**
 * Renders Get Paid screen that contains Invoice form.
 */
export default class GetPaidScreen extends React.Component {

  /**
   * Set init state of the component.
   * @see https://facebook.github.io/react/docs/react-component.html#constructor
   */
  constructor(props) {
    super(props);

    // Default value for an Invoice.
    this.state = {
      invoice: {
        dateIssued: moment(),
        dateDue: moment(),
        isPaid: false,
        isAccepted: false,
        amount: {
          currency: 'VND'
        },
        paymentOptions: [],
        derived: {
          paymentTerm: paymentTerms[0],
          receiverId: '',
          message: ''
        }
      },
      errors: {}
    };
  }

  /**
   * Renders select options for payment options.
   */
  paymentOptions = () => Immutable.List(paymentOptions).map((method) => (<MenuItem key={method.value} insetChildren={true} checked={this.props.ui.acceptedPayments.includes(method.value)} value={method.value} primaryText={method.text} />));

  /**
   * Renders select options for currencies.
   */
  currencies = () => Immutable.List(currencyTypes).map((currency) => <MenuItem key={currency.value} value={currency.value} primaryText={currency.text} />);

  /**
   * Renders selection options for payment terms.
   */
  paymentTermOptions = () => Immutable.List(paymentTerms).map((term) => <MenuItem key={term.text} value={term} primaryText={term.text} />);

  /**
   * Updates the selected payment terms.
   */
  handlePaymentTermChange = (event, index, selected) => {
    let nextDerived = Object.assign({}, this.state.invoice.derived, {
      paymentTerm: paymentTerms[index]
    });
    let nextInvoice = Object.assign({}, this.state.invoice, {
      dateDue: moment().add(selected.value, selected.unit),
      derived: nextDerived
    });
    this.setState({
      invoice: nextInvoice
    });
  };

  /**
   * Updates the selected payment options.
   */
  handlePaymentOptionsChange = (event, index, values) => {
    let nextInvoice = Object.assign({}, this.state.invoice, {paymentOptions: values});
    this.setState({
      invoice: nextInvoice
    });
  };

  /**
   * Updates the selected currency to accept.
   */
  handleCurrencyChange  = (event, index, value) => {
    let nextAmount = Object.assign({}, this.state.invoice.amount, {currency: value});
    this.setState({
      invoice: Object.assign({}, this.state.invoice, {amount: nextAmount})
    });
  };

  /**
   * Updates Invoice information.
   */
  onInvoiceInfoChange = (nextInvoiceInfo) => {
    this.setState({
      invoice: Object.assign({}, this.state.invoice, nextInvoiceInfo)
    });
  }

  /**
   * Renders the payment options selections.
   * @see http://www.material-ui.com/#/components/select-field
   */
  selectionRenderer = (values) => {
    switch (values.length) {
      case 0:
        return '';
      case 1:
        return values[0];
      default:
        return `${values.length} methods selected`;
    }
  }

  /**
   * Updates the receiver ID for this Invoice.
   */
  onParticipantSelected = (selected) => this.onInvoiceInfoChange({derived: Object.assign({}, this.state.invoice.derived, {receiverId: selected.identifier})});

  /**
   * Fires action to lookup Participant based on the provided query.
   */
  handleParticipantLookup = (query) => this.props.dispatch(actions.lookupParticipants(query));


  /**
   * Issuing the Invoice by sending a message.
   * Fires the action to send a Participant message after the validation passes.
   */
  sendMessage = () => {
    let {dispatch} = this.props;
    let {invoice} = this.state;

    let nextErrors = {};

    if (invoice.derived.receiverId === '') {
      nextErrors = Object.assign({}, nextErrors, {participantError: 'Please select a valid Business.'});
    }
    else {
      nextErrors = Object.assign({}, nextErrors, {participantError: null});
    }

    if (invoice.paymentOptions.length === 0) {
      nextErrors = Object.assign({}, nextErrors, {paymentOptionsError: 'Please select at least one payment option.'});
    }
    else {
     nextErrors= Object.assign({}, nextErrors, {paymentOptionsError: null});
    }

    if (!invoice.amount.amount) {
      nextErrors = Object.assign({}, nextErrors, {amountError: 'Please enter an amount for the Invoice.'});
    }
    else {
      nextErrors = Object.assign({}, nextErrors, {amountError: null});
    }

    this.setState({
      errors: nextErrors
    });

    if (!Immutable.fromJS(nextErrors).find((error) => error)) {
      let messageToSend = {
        invoice: invoice,
        message: {
          receiverId: invoice.derived.receiverId,
          message: invoice.derived.message
        }
      };

      dispatch(actions.sendParticipantMessage(messageToSend));
    }
  }

  render() {
    let {state, props} = this;

    let matchedParticipants = props.matchedParticipants.participants;

    return (
      <div>
        <AppBarMain title={'Get Paid'}/>
        <Paper
          zDepth={1}
          style={paperStyle}>
          <img src={Logo} style={logoStyle} />
          <br />
          <br />
          <Paper
            zDepth={1}
            style={getPaidPaperStyle}>
              <AutoComplete
                fullWidth={true}
                filter={AutoComplete.caseInsensitiveFilter}
                style={textFieldStyle}
                hintText='Person you are paying'
                floatingLabelText='Who'
                dataSourceConfig={{ text: 'businessName', value: 'identifier',}}
                dataSource={matchedParticipants}
                errorText={this.state.errors.participantError}
                onNewRequest={(selected) => this.onParticipantSelected(selected)}
                onUpdateInput={(value) => this.handleParticipantLookup(value)} />
              <br />
            <div style={moneyDivStyle}>
              <SelectField
                floatingLabelText='Currency'
                value={state.invoice.amount.currency}
                onChange={this.handleCurrencyChange}>
                {this.currencies()}
              </SelectField>
              <TextField
                type='number'
                style = {textFieldStyle}
                fullWidth={true}
                errorText={this.state.errors.amountError}
                hintText='Amount to pay'
                floatingLabelText='How much'
                onChange={(event) => this.onInvoiceInfoChange({amount: Object.assign({}, this.state.invoice.amount, {amount: event.target.value})})} />
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
              onChange={(event) => this.onInvoiceInfoChange({derived: Object.assign({}, this.state.invoice.derived, {message: event.target.value})})} />
            <br />
            <SelectField
              floatingLabelText='Payment Terms'
              value={state.invoice.derived.paymentTerm}
              onChange={this.handlePaymentTermChange}
              fullWidth={true}
              style={textFieldStyle}>
              {this.paymentTermOptions()}
            </SelectField>
            <br />
            <SelectField
              multiple={true}
              style={textFieldStyle}
              floatingLabelText='Payment methods'
              value={state.invoice.paymentOptions}
              onChange={this.handlePaymentOptionsChange}
              fullWidth={true}
              errorText={this.state.errors.paymentOptionsError}
              selectionRenderer={this.selectionPaymentOptionsRenderer}>
              {this.paymentOptions()}
            </SelectField>

            <br />

            <RaisedButton labelStyle={lStyle} label='Send Invoice' fullWidth={true} backgroundColor={red} labelColor={white} onTouchTap={() => this.sendMessage()} />
          </Paper>
        </Paper>
      </div>
    );
  }
}
