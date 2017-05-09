import React, { Component, PropTypes } from 'react';
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



export default class Home extends React.Component {
  render() {
    return ( <HomeScreen />);
  }
}
