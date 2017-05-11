import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import IconMenu from 'material-ui/IconMenu';
import MenuItem from 'material-ui/MenuItem';
import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';
import NavigationMore from 'material-ui/svg-icons/navigation/expand-more';
import SearchIcon from 'material-ui/svg-icons/action/search';
import Paper from 'material-ui/Paper';
import RaisedButton from 'material-ui/RaisedButton';
import StarRating from '../components/starRating';

import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import BasicScreenTemplate from '../components/basicScreenTemplate';
import HomeScreen from '../components/homeScreen';
import GetPaidScreen from '../components/getPaidScreen';
import MainSplashScreen from '../components/mainSplashScreen';


@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class Home extends React.Component {

  constructor(props) {
    super(props);
  };

  componentWillReceiveProps(props) {
    {/** Check token is valid **/}
    if (props.ui.alreadyUser) {

      {/** If token is valid proceed to home **/}
      props.router.push('/home');
    } else {
      {/** proceed to join **/}
      props.router.push('/join');
    };
    console.log(props.ui);
  };

  render() {
    console.log(this.props.ui);
    return (<MainSplashScreen /> );
  };

}
