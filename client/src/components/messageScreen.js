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
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import AutoComplete from 'material-ui/AutoComplete';
import actions from 'state/actions';
import Immutable from 'immutable';

/***

This is the basic elements for a main view in the app.

***/
const invoiceViewStyle ={
  textAlign: 'left',
  position: 'relative',
  padding: '5px',
  height: '100%',
};

const feedbackStyle ={
  textAlign: 'center',
  width: '100%',
  position: 'relative',
  padding: '5px',
};

const paperStyle = {
  padding: '5px',
  textAlign: 'center',
  height: '100%',
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
export default class MessageScreen extends React.Component {

  constructor(props) {
    super(props);
    //this.onParticipantSelected(this.props.messages.selectedMessage.sender.businessName);
    this.state = {
      message: {
        content: '',
        derived: {
          receiverId: '',
        }
      },
      errors: {}
    };
  };
  /**
     * Updates Message information.
     */
  onMessageInfoChange = (nextMessageInfo) => {
    this.setState({
      message: Object.assign({}, this.state.message, nextMessageInfo)
    });
  }

  /**
   * Updates the receiver ID for this Message.
   */
  onParticipantSelected = (selected) => this.onMessageInfoChange({derived: Object.assign({}, this.state.message.derived, {receiverId: selected.identifier})});

  /**
   * Fires action to lookup Participant based on the provided query.
   */
  handleParticipantLookup = (query) => this.props.dispatch(actions.lookupParticipants(query));


  sendMessage = () => {
    let {dispatch} = this.props;
    let {message} = this.state;

    let nextErrors = {};

    if (message.derived.receiverId === '') {
      nextErrors = Object.assign({}, nextErrors, {participantError: 'Please select a valid Business.'});
    }
    else {
      nextErrors = Object.assign({}, nextErrors, {participantError: null});
    }
    if (!message.content) {
      nextErrors = Object.assign({}, nextErrors, {messageError: 'Please enter an message.'});
    }
    else {
      nextErrors = Object.assign({}, nextErrors, {messageError: null});
    }

    this.setState({
      errors: nextErrors
    });

    if (!Immutable.fromJS(nextErrors).find((error) => error)) {
      let messageToSend = {
        message: {
          receiverId: message.derived.receiverId,
          message: message.content
        }
      };
      //console.log(messageToSend);
      dispatch(actions.sendParticipantMessage(messageToSend));
    }
    //console.log(message);

  }

  getSelectedMessageText () {
    if (this.props.messages.selectedMessage) {
      let businessName = this.props.messages.selectedMessage.sender.businessName;
      this.props.dispatch(actions.selectedParticipantMessage({}));
      return businessName;
    } else {
      return '';
    }
  };

  render() {
    let {state, props} = this;

    let matchedParticipants = props.matchedParticipants.participants;

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
            <AutoComplete
              fullWidth={true}
              filter={AutoComplete.caseInsensitiveFilter}
              hintText='Message Recipient'
              floatingLabelText='Message Recipient'
              dataSourceConfig={{ text: 'businessName', value: 'identifier',}}
              dataSource={matchedParticipants}
              errorText={this.state.errors.participantError}
              onNewRequest={(selected) => this.onParticipantSelected(selected)}
              onUpdateInput={(value) => this.handleParticipantLookup(value)}
              searchText={this.getSelectedMessageText()}
            />

            <br/>
            <br/>

            <TextField
              hintText='User message'
              floatingLabelText='User message'
              multiLine={true}
              rows={1}
              fullWidth={true}
              onChange={(event) => this.onMessageInfoChange({content: event.target.value})}
              errorText={this.state.errors.messageError}
            />

            <br/>
            <br/>

            <RaisedButton
              label={'Send Message'}
              backgroundColor={red}
              labelColor={white}
              fullWidth={true}
              onTouchTap={() => this.sendMessage()}
            />

          </Paper>
        </Paper>
      </div>
    );
  }
}


