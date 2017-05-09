import React, { Component, PropTypes } from 'react';
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
/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 5,
  textAlign: 'center',
  position: 'relative',
  height:'100vh',
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
    ui: state.ui
  };
})
export default class GetPaidScreen extends React.Component {

  constructor(props) {
    super(props);
  }

  state = {
      values: [],
  };

  handleChange = (event, index, value) => this.props.dispatch(
    actions.termsMenuValue(value)
  );

  handleChangePayment = (event, index, values) => this.props.dispatch(
    actions.acceptedPayments(values)
  );

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
            <TextField
              fullWidth={true}
              style = {textFieldStyle}
              hintText='Amount to pay'
              floatingLabelText='How much'
            />
            <br />
            <TextField
              fullWidth={true}
              style = {textFieldStyle}
              hintText='What for'
              floatingLabelText='What did you purchase'
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

            <RaisedButton label='Send' fullWidth={true} backgroundColor={red} labelColor={white} />

          </Paper>
        </Paper>
      </div>
    );
  }
}


