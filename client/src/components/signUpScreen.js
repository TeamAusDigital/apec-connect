import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {withRouter} from 'react-router';
import {connect} from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo, white, red} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import {Link} from 'react-router';
import actions from 'state/actions';
import TextField from 'material-ui/TextField';
import Background from '../common/assets/bg-bottom.png';

/***`

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100vh',
  overflow: 'hidden'
};

const divStyle = {
  height: '100%',
  backgroundImage: `url(${Background})`,
  backgroundSize: 'cover',
  color: `${white}`,
  position: 'relative'
};

const textStyle = {
  color: `${white}`,
  width: '75%',
  margin: '0 auto',
  position: 'relative'
};

const logoStyle = {
  width: '100%',
  maxHeight: '150px'
};

const btnStyle = {
  width: '100%'
};

@withRouter
@connect((state) => {
  return {dispatch: state.dispatch, ui: state.ui, participant: state.participant};
})

/**
 * Responsible for the rendering and actions on Sign Up screen.
 */
export default class SignUpScreen extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      participant: {
        businessName: '',
        email: '',
        phone: ''
      }
    };
  }

  /**
   * On next props update, check the log in status.
   * TODO: handle sign up error message here and also determine participant sign-in status.
   */
  componentWillReceiveProps(nextProps) {
    if (!nextProps.participant.isFetching && nextProps.participant.accountStatus === 'Enabled') {
      nextProps.router.push('/home');
    } else if (!nextProps.participant.isFetching && !nextProps.participant.accountStatus !== 'Enabled') {
      this.setState({message: 'Please sign up later.'});
    }
  }

  onParticipantInfoChange = (participantInfo) => {
    let nextParticipant = Object.assign({}, this.state.participant, participantInfo);
    this.setState(Object.assign({}, this.state, {participant: nextParticipant}));
  }

  handleSignUp = () => {
    if (this.state.participant && this.state.participant.businessName !== '') {
      this.props.dispatch(actions.signUp(this.state.participant));
    }
  }

  render() {
    let {participant} = this.state;

    return (
      <div>
        <Paper zDepth={1} style={paperStyle}>
          <img src={Logo} style={logoStyle}/>
          <br/>
          <br/>

          <div style={divStyle}>
            <div style={textStyle}>
              <TextField hintText='Business Name' errorText='This field is required' floatingLabelText='Business Name' onChange={(event) => this.onParticipantInfoChange({businessName: event.target.value})} value={participant.businessName}/>

              <div style={textStyle}>
                AND
              </div>
              <TextField hintText='Email Address' floatingLabelText='Email Address' type='email' onChange={(event) => this.onParticipantInfoChange({email: event.target.value})} value={participant.email}/>

              <div style={textStyle}>
                OR
              </div>
              <TextField hintText='Mobile Phone Number' floatingLabelText='Mobile Phone Number' type='tel' onChange={(event) => this.onParticipantInfoChange({phone: event.target.value})} value={participant.phone}/>
              <div style={textStyle}>
                OR
              </div>
              <br/>
              <RaisedButton style={btnStyle} label='Social Media' primary={true}/>
            </div>
            <br/>
            <RaisedButton style={btnStyle} label='Sign Up' onTouchTap={() => this.handleSignUp()} backgroundColor={red} labelColor={white}/>
            <br/>
          </div>
        </Paper>
      </div>
    );
  }
}
