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
import EconomyFlag from '../components/EconomyFlag';

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
  fontSize: 20,

};

const logoStyle = {
  width: '100%',
  maxHeight: '150px',
};

const labelStyle = {
  width: '100%',
  textAlign: 'center',
  color: white,
  position: 'relative',
  top: 'calc(50% - 29px)'
};

const translationStyle = {
  display: 'block',
  fontSize: '22px',
};

const labelTextStyle = {
  textTransform: 'uppercase',
  fontSize: '28px',
};

const flagStyle = {
  marginRight: '3px',
};

@connect((state) => {
  return {dispatch: state.dispatch, ui: state.ui, participant: state.participant};
})
export default class HomeScreen extends React.Component {

  componentDidMount() {
    let {dispatch} = this.props;
    dispatch(actions.getParticipant());
  }

  render() {
    let {businessName, username, rating} = this.props.participant;

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
            {this.props.participant.economy ? <EconomyFlag style={flagStyle} economyCode={this.props.participant.economy} /> : ''}
            {username}
            <StarRating rating={rating}/>
          </div>
          <br/> {/** Action buttons  **/}
          <RaisedButton style={getPaidStyle} secondary={true} containerElement={< Link to = '/getPaid' />} icon={
            <div style={labelStyle}>
              <div style={labelTextStyle}>Get Paid</div>
              <div style={translationStyle}>Được trả tiền</div>
            </div>
          }/>
          <RaisedButton style={payStyle} primary={true} containerElement={< Link to = '/pay' />} icon={
            <div style={labelStyle}>
              <div style={labelTextStyle}>Pay</div>
              <div style={translationStyle}>Trả tiền</div>
            </div>
          }/>
          <RaisedButton style={inboxStyle} fullWidth={true} backgroundColor={indigo} labelColor={white} containerElement={< Link to = '/inbox' />} icon={
            <div style={labelStyle}>
              <div style={labelTextStyle}>Inbox</div>
              <div style={translationStyle}>Hộp thư đến</div>
            </div>
          }/>
          <br/>
        </Paper>
      </div>
    );
  }
}
