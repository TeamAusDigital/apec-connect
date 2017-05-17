import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import ToPayItem from '../components/toPayItem';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import actions from 'state/actions';
import Immutable from 'immutable';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100%',
  minHeight: '40vh',
  position: 'relative'
};

const headerDivStyle = {
  position: 'relative',
  width: '30%',
};

const payStyle = {
  width: '50%',
};

const businessNameStyle ={
  fontSize: 48,
};

const userDetailsStyle = {
  fontSize: 20,
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

{/** User information that will be pulled from the database **/}
const CompanyName = 'David\'s Hat Co.';
const userName = 'DHat72';
const userStarRating = 1;

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui,
    participant: state.participant,
    messages: state.messages,
  };
})
export default class PayScreen extends React.Component {

  constructor(props) {
    super(props);
  };

  componentDidMount() {
    let { dispatch } = this.props;

    setTimeout(function () {
      dispatch(actions.getParticipantMessages());
    });
  }

  componentWillReceiveProps() {
    clearTimeout(this.inboxRefreshTimeout);
    this.refreshInbox();
  }

  refreshInbox = () => {
    let { dispatch } = this.props;
    this.inboxRefreshTimeout = setTimeout(function() {
      dispatch(actions.getParticipantMessages());
    }, 5 * 1000);
  }

  generateInboxItems = () => {
    /**Create a list of messages WHERE
     *  # Buyer === participant
     *  # Invoice part exists
     *  => If paid === true, disable PAY button
    **/
    const items = Immutable.List(this.props.messages.messages).map ((m, index) => {
      if (m.invoice && (m.sender.identifier != this.props.participant.identifier) ){
        return <ToPayItem key={index} message={m} keyID={index}/>;
      }
    });
    if (items.size > 0) {
      return items;
    } else {
      return <div> Nothing to pay! </div>;
    }

  }

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Pay'}/>
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
            {/** container for to pay elements **/}
            {this.generateInboxItems()}
          </Paper>
          <br />
        </Paper>
      </div>
    );
  }
}


