import React , { Component } from 'react';
import {List, ListItem} from 'material-ui/List';
import ContentInbox from 'material-ui/svg-icons/content/inbox';
import ActionGrade from 'material-ui/svg-icons/action/grade';
import ContentSend from 'material-ui/svg-icons/content/send';
import ContentDrafts from 'material-ui/svg-icons/content/drafts';
import Divider from 'material-ui/Divider';
import Home from 'material-ui/svg-icons/action/home';
import CreditCard from 'material-ui/svg-icons/action/credit-card';
import PowerSettingsNew from 'material-ui/svg-icons/action/power-settings-new';
import AccountBalanceWallet from 'material-ui/svg-icons/action/account-balance-wallet';
import Paper from 'material-ui/Paper';
import {Link} from 'react-router';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import actions from 'state/actions';

import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';

const paperStyle = {
  padding: 10,
  textAlign: 'left'
};


@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class MenuList extends React.Component {

  constructor(props) {
    super(props);
  }

  handleClick = () => this.props.dispatch(
    this.props.ui.mainMenuOpen ?  actions.closeMainMenu() : actions.openMainMenu()
  );

  handleForgetMe = () => {
    this.props.dispatch(actions.notAuthenticated());
    this.props.dispatch(actions.forgetUser());
    this.props.dispatch(actions.closeMainMenu());
  };

  render() {
    return (
      <div>
        <Divider />
        <List>
          <ListItem primaryText='Home' leftIcon={<Home />} containerElement={<Link to='/home' />} onTouchTap={this.handleClick} />
          <ListItem primaryText='Inbox' leftIcon={<ContentInbox />} containerElement={<Link to='/inbox' />} onTouchTap={this.handleClick}/>
          <ListItem primaryText='Get Paid' leftIcon={<AccountBalanceWallet />} containerElement={<Link to='/getPaid' />} onTouchTap={this.handleClick}/>
          <ListItem primaryText='Pay' leftIcon={<CreditCard />} containerElement={<Link to='/pay' />} onTouchTap={this.handleClick}/>
        </List>
        <Divider />
        <List>
          <ListItem primaryText='Forget Me' rightIcon={< PowerSettingsNew />} onTouchTap={() => this.handleForgetMe()}/>
        </List>
      </div>
  );}
}
