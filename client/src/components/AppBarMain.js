import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import IconMenu from 'material-ui/IconMenu';
import MenuItem from 'material-ui/MenuItem';
import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';
import NavigationMenu from 'material-ui/svg-icons/navigation/menu';
import SearchIcon from 'material-ui/svg-icons/action/search';
import Paper from 'material-ui/Paper';
import RaisedButton from 'material-ui/RaisedButton';
import MenuDrawer from '../components/menuDrawer';

import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import actions from 'state/actions';

import {white} from './apecConnectTheme';

const ContextHelp = (props) => (
  <div>
  <IconButton> <SearchIcon color={white}/> </IconButton>
  <IconMenu
    {...props}
    iconButtonElement={
      <IconButton><MoreVertIcon /></IconButton>
    }
    targetOrigin={{horizontal: 'right', vertical: 'top'}}
    anchorOrigin={{horizontal: 'right', vertical: 'top'}}
  >
    <MenuItem primaryText="Help" />
  </IconMenu>
  </div>
);
ContextHelp.muiName = 'IconMenu';


@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class AppBarMain extends React.Component {

  constructor(props) {
    super(props);
  }

  handleToggle = (props) => this.props.dispatch(
      this.props.ui.mainMenuOpen ?  actions.closeMainMenu() : actions.openMainMenu()
  );

  render() {
    return (
      <div>
        <AppBar
          title={this.props.title}
          onLeftIconButtonTouchTap={this.handleToggle}
          iconElementRight={<ContextHelp />}
        />
        <MenuDrawer />
      </div>
    );
  }
}


AppBar.propTypes = {
  title: PropTypes.string.isRequired
};

/***
rating: PropTypes.number
export default AppBar;
***/
