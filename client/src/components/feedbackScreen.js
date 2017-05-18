import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white,red} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import actions from 'state/actions';
import Immutable from 'immutable';
import CircularProgress from 'material-ui/CircularProgress';
import moment from 'moment';

/***

This is the basic elements for a main view in the app.

***/
const invoiceViewStyle ={
  textAlign: 'left',
  margin: '0 auto',
  position: 'relative',
  padding: '10px',
};

const starStyle = {
  width: '25%',
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

const loadingIconStyle = {
  textAlign: 'center'
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
export default class FeedbackScreen extends React.Component {

  constructor(props) {
    super(props);

    let {selectedMessage} = this.props.messages;

    this.invoiceID = selectedMessage.invoice.id;
    this.invoiceWhat = selectedMessage.message.message;
    this.invoiceAmount = selectedMessage.invoice.amount.amount;
    this.invoiceSentDate = moment(selectedMessage.invoice.issueDate).format('YYYY-MM-DD');
    this.invoiceDueDate = moment(selectedMessage.invoice.dueDate).format('YYYY-MM-DD');

    this.toName = this.props.participant.identifier === selectedMessage.sender.identifier ? selectedMessage.receiver.businessName : selectedMessage.sender.businessName;
    this.toID = this.props.participant.identifier === selectedMessage.sender.identifier ? selectedMessage.receiver.identifier : selectedMessage.sender.identifier;
    this.starRating = 0;

    this.state = {
      message: {
        rating: null,
        content: '',
        derived: {
          receiverId: this.toID,
        }
      },
      errors: {}
    };
  };

  componentWillReceiveProps(newProps) {
    if(newProps.messages.sent && this.props.messages.isSending && !newProps.messages.isSending) {
      this.props.dispatch(actions.showMessage('Your feedback has been sent.'));
      this.props.router.push('/viewInvoice');
    }
  }

  onMessageInfoChange = (nextMessageInfo) => {
    this.setState({
      message: Object.assign({}, this.state.message, nextMessageInfo)
    });
  }

  onRatingChange = (event, key, payload) => {

    this.setState({
      message: Object.assign({}, this.state.message,  {rating: payload})
    });

    this.forceUpdate();

  }


  handleLeaveFeedback = () => {
    console.log('Feedback!');
    let {dispatch} = this.props;
      let {message} = this.state;

      let nextErrors = {};

      if (!message.content) {
        nextErrors = Object.assign({}, nextErrors, {messageError: 'Please enter an message.'});
      }
      else {
        nextErrors = Object.assign({}, nextErrors, {messageError: null});
      }

      if (!message.rating) {
        nextErrors = Object.assign({}, nextErrors, {ratingError: 'Please select a rating.'});
      }
      else {
        nextErrors = Object.assign({}, nextErrors, {ratingError: null});
      }

      this.setState({
        errors: nextErrors
      });

      if (!Immutable.fromJS(nextErrors).find((error) => error)) {
        let messageToSend = {
          message: {
            receiverId: message.derived.receiverId,
            message: 'Feedback message (' + message.rating + '/3 stars): \n' + message.content,
            rating: message.rating,
          }
        };

        dispatch(actions.sendParticipantMessage(messageToSend));
      }
  };

  sendOrLoading = () => {
    if(this.props.messages.isSending) {
      return <div style={loadingIconStyle}> <CircularProgress /> </div>;
    } else {
      return <RaisedButton label={'Leave Feedback'} backgroundColor={red} labelColor={white} fullWidth={true} onTouchTap={() => this.handleLeaveFeedback()}/>;
    }
  }

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
              <div > {this.toName} </div>
              <div style={divStyle}><div style={divStyle}>${this.invoiceAmount}</div > issued on <div style={divStyle}>{this.invoiceSentDate}</div >  </div>
              <div> For: {this.invoiceWhat}</div>
            </Paper>
            <br/>
            <br/>
            <Paper
              zDepth={1}
              style={feedbackStyle}
            >
              <div> Give feedback to {this.toName} </div>
              <br />
              <div style={invoiceViewStyle}>
                <SelectField
                  floatingLabelText='Rating'
                  value={this.state.message.rating}
                  style={starStyle}
                  errorText={this.state.errors.ratingError}
                  onChange={this.onRatingChange}
                >
                  <MenuItem value={null} primaryText='' />
                  <MenuItem value={1} primaryText='1' />
                  <MenuItem value={2} primaryText='2' />
                  <MenuItem value={3} primaryText='3' />
                </SelectField>
                <StarRating rating={this.state.message.rating} />
              </div>
              <div style={invoiceViewStyle}>
                <TextField
                  hintText='Feedback message'
                  floatingLabelText='Feedback message'
                  multiLine={true}
                  rows={1}
                  fullWidth={true}
                  onChange={(event) => this.onMessageInfoChange({content: event.target.value})}
                  errorText={this.state.errors.messageError}
                />
              </div>

            </Paper>

            {this.sendOrLoading()}

          </Paper>
        </Paper>
      </div>
    );
  }
}


