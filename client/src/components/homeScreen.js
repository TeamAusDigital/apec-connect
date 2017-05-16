import React, {Component} from 'react';
import PropTypes from 'prop-types';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo, white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import {Link} from 'react-router';
import actions from 'state/actions';
import {connect} from 'react-redux';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: '5px',
  textAlign: 'center',
  height: '100%'
};

const getPaidStyle = {
  width: '50%',
  height: '20vh',
};

const payStyle = {
  width: '50%',
  height: '20vh',
};

const inboxStyle = {
  height: '20vh',
};

const businessNameStyle = {
  fontSize: 48,
  position: 'relative',
};

const userDetailsStyle = {
  fontSize: 20
};

const logoStyle = {
  width: '100%',
  maxHeight: '150px'
};

const lStyle = {
  width: '100%',
  fontSize: '28px',
  verticalAlign: 'middle',
  textAlign: 'center',
  lineHeight: '20vh',
};

// TODO: fake rating.
const userStarRating = 2;

@connect((state) => {
  return {dispatch: state.dispatch, ui: state.ui, participant: state.participant};
})
export default class HomeScreen extends React.Component {

  componentDidMount() {
    let {dispatch} = this.props;
    dispatch(actions.getParticipant());
  }

  render() {
    let {businessName, username} = this.props.participant;

    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Home'}/> {/** Paper containing the logo **/}
        <Paper zDepth={1} style={paperStyle}>
          <img src={Logo} style={logoStyle}/>
          <br/>
          <br/> {/** Username and rating **/}

          <div style={businessNameStyle}>{businessName}</div>
          <div style={userDetailsStyle}>
            {username}
            <StarRating rating={userStarRating}/>
          </div>
          <br/> {/** Action buttons  **/}
          <RaisedButton labelStyle={lStyle} label='Get Paid' style={getPaidStyle} secondary={true} containerElement={< Link to = '/getPaid' />}/>
          <RaisedButton labelStyle={lStyle} label='Pay' style={payStyle} primary={true} containerElement={< Link to = '/pay' />}/>
          <RaisedButton labelStyle={lStyle} label='Inbox' style={inboxStyle} fullWidth={true} backgroundColor={indigo} labelColor={white} containerElement={< Link to = '/inbox' />}/>
          <br/>
        </Paper>
      </div>
    );
  }
}
