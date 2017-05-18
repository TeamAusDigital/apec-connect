import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import actions from 'state/actions';
import LoadingSplashScreen from '../components/loadingSplashScreen';


/**
 * Handles routing of user depending on authentication status
 */
@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    authentication: state.authentication
  };
})
export default class Home extends React.Component {
  routeUser(props) {
    const { loggedIn, token } = props.authentication;

    if(!token) {
      // No token, first time visitor, must join
      props.router.push('/join');
    }
    else if(loggedIn) {
      // Successfully logged in, go to home screen
      props.router.push('/home');
    }
    else {
      // Remembered token, not yet logged in, trigger sign-in event
      props.dispatch(actions.signIn(token));
    }
  }


  constructor(props) {
    super(props);
  };

  componentWillMount() {
    this.routeUser(this.props);
  }

  componentWillReceiveProps = this.routeUser;

  render() {
    return <LoadingSplashScreen />;
  };

}
