import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {withRouter} from 'react-router';
import {connect} from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo, white, red} from './apecConnectTheme';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import {Link} from 'react-router';
import actions from 'state/actions';
import TextField from 'material-ui/TextField';
import Background from '../common/assets/bg-bottom-alpha-60.png';
import FontIcon from 'material-ui/FontIcon';
import Immutable from 'immutable';
import EconomyFlag, {ApecEconomyCodes} from '../components/EconomyFlag';
import CircularProgress from 'material-ui/CircularProgress';

/***`

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: '5px',
  textAlign: 'center',
  height: '100vh',
  overflow: 'hidden',
};

const divStyle = {
  height: '100%',
  backgroundSize: 'cover',
  position: 'relative',
};

const textStyle = {
  width: '75%',
  margin: '0 auto',
  position: 'relative',
  fontWeight: 'bold',
  display: 'block',
};

const logoStyle = {
  width: '100%',
  maxHeight: '150px'
};

const btnStyle = {
  width: '100%',
  fontSize: '20px',
  verticalAlign: 'middle',
};

const signUpBtnStyle = {
  width: '100%',
  fontSize: '40px',
  height: '60px',
};

const signUpLblStyle = {
  verticalAlign: 'middle',
  textAlign: 'center',
  lineHeight: '50px',
  fontSize: '35px',
};

const textFieldStyle = {
  position: 'relative',
};

const textOrStyle = {
  marginBottom: '-22px',
};

const spinnerContainerStyle = {
  textAlign: 'center'
};

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    authentication: state.authentication,
    participant: state.participant
  };
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
        phone: '',
        economy: 'VN'
      },
      validationErrors: {}
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

  onEconomyChange = (event, key, payload) => {
    let nextParticipant = Object.assign({}, this.state.participant, {economy: payload});
    this.setState({
      participant: nextParticipant
    });
  }

  economies = () => Immutable.List(ApecEconomyCodes).map((code) =>
    <MenuItem key={code} value={code} primaryText={<span><EconomyFlag economyCode={code} /> {code}</span>} />
  );


  handleSignUp = () => {
    let {participant, validationErrors} = this.state;
    let nextValidationErrors = {};

    if (participant.businessName === '') {
      nextValidationErrors.businessName = 'Please enter your business name.';
    }
    else {
      nextValidationErrors.businessName = null;
    }

    this.setState({
      validationErrors: nextValidationErrors
    });

    if (!Immutable.fromJS(nextValidationErrors).find((error) => error)) {
      this.props.dispatch(actions.signUp(this.state.participant));
    }
  }

  buttonOrSpinner = () => {
    if(this.props.authentication.loading) {
      return <div style={spinnerContainerStyle}> <CircularProgress /> </div>;
    } else {
      return <RaisedButton labelStyle={signUpLblStyle} style={signUpBtnStyle} label='Sign Up Now' onTouchTap={() => this.handleSignUp()} backgroundColor={red} labelColor={white}/>;
    }
  }

  render() {
    let {participant, validationErrors} = this.state;

    return (
      <div>
        <Paper zDepth={1} style={paperStyle}>
          <img src={Logo} style={logoStyle}/>
          <br/>
          <br/>

          <div style={divStyle}>
            <div style={textStyle}>

              <div style={{display: 'flex', marginBottom: '30px', alignItems: 'flex-end'}}>
                <TextField
                  hintText='Business Name'
                  errorText={validationErrors.businessName}
                  floatingLabelText='Business Name'
                  onChange={(event) => this.onParticipantInfoChange({businessName: event.target.value})}
                  value={participant.businessName}
                  style={{flex: '1'}}
                />
                <SelectField
                  value={participant.economy}
                  onChange={this.onEconomyChange}
                  style={{width: '90px'}}
                >
                  {this.economies()}
                </SelectField>
              </div>

              <div style={textStyle}>
                AND
              </div>

              <TextField style={textFieldStyle} hintText="Email Address" floatingLabelText="Email Address" type="email" onChange={(event) => this.onParticipantInfoChange({email: event.target.value})} value={participant.email}/>

              <div style={textOrStyle}>
                OR
              </div>

              <TextField style={textFieldStyle} hintText="Mobile Phone Number" floatingLabelText="Mobile Phone Number" type="tel" onChange={(event) => this.onParticipantInfoChange({phone: event.target.value})} value={participant.phone}/>

              <div style={textStyle}>
                OR
              </div>
              <br/>
              <RaisedButton
                style={btnStyle}

                primary={true}
                icon={
                  <span>
                  <FontIcon className='fa fa-google-plus fa-fw' color={white}/>
                  <FontIcon className='fa fa-facebook-official fa-fw' color={white}/>
                  <FontIcon className='fa fa-twitter fa-fw' color={white}/>
                  </span>
                  }
                />
            </div>
            <br/>
            {this.buttonOrSpinner()}
            <br/>
          </div>
        </Paper>
      </div>
    );
  }
}
