import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import actions from 'state/actions';
import { connect } from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import InboxItem from './inboxItem';
import Immutable from 'immutable';
import moment from 'moment';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import Message from 'material-ui/svg-icons/communication/message';

import {
  Table,
  TableBody,
  TableFooter,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100%'
};
const buttonStyle = {
  position: 'fixed',
  bottom: '10px',
  right: '25px',
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
    officials: state.officials
  };
})
export default class Inbox extends React.Component {

  constructor(props) {
    super(props);
  };

  /**
   * Generates the inbox items:
   *  # Participant messages, with or without Invoice.
   *  # Official announcements made by government agencies.
   * @return {nodes} of rednered InboxItem components.
   */
  generateInboxItems = () => {
    let {messages} = this.props.messages;

    // Wraps the announcements in ParticipantMessage manner. A ParticipantMessage contains:
    // {sender: {}, receiver: {}, invoice: {}, message: {}}
    let announcements = Immutable.List(this.props.officials.announcements).map((announce) => {
      return {
        message: {
          message: announce.message
        },
        isAnnoucement: true,
        metaData: announce.metaData
      };
    });

    let messagesAndAnnouncements = announcements.concat(Immutable.List(messages));

    return messagesAndAnnouncements.map((message, index) => <InboxItem key={index} message={message} keyID={index} />) ;
  }

  componentDidMount() {
    let { dispatch } = this.props;

    setTimeout(function () {
      dispatch(actions.getAnnouncements());
      dispatch(actions.getParticipantMessages());
    });
  }

  componentWillReceiveProps() {
    clearTimeout(this.inboxRefreshTimeout);
    this.refreshInbox();
  }

  /**
   * Periodically refresh the inbox to fetch updated messages and announcements.
   */
  refreshInbox = () => {
    let { dispatch } = this.props;
    this.inboxRefreshTimeout = setTimeout(function() {
      dispatch(actions.getAnnouncements());
      dispatch(actions.getParticipantMessages());
    }, 5 * 1000);
  }

  handleNewMessage = () => {
    this.props.router.push('/sendMessage');
  };

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Inbox'}/>
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
            <Table
              multiSelectable={false}
              showCheckBoxes={false}
            >
              <TableHeader
                displaySelectAll={false}
                adjustForCheckbox={false}
                enableSelectAll={false}
              >
                <TableRow>
                  <TableHeaderColumn>WHO</TableHeaderColumn>
                  <TableHeaderColumn>WHEN</TableHeaderColumn>
                  <TableHeaderColumn>WHAT</TableHeaderColumn>
                </TableRow>
              </TableHeader>
              <TableBody
                displayRowCheckbox={false}
                stripedRows={true}
              >
                {this.generateInboxItems()}

              </TableBody>
            </Table>
            <div style={buttonStyle}>
            <FloatingActionButton
              onTouchTap={() => this.handleNewMessage()}
            >
              <Message />
            </FloatingActionButton>
            </div>
          </Paper>
        </Paper>
      </div>
    );
  }
}
