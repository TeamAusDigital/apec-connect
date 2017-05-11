import React , { Component } from 'react';
import PropTypes from 'prop-types';
import {List, ListItem} from 'material-ui/List';
import ContentInbox from 'material-ui/svg-icons/content/inbox';
import ActionGrade from 'material-ui/svg-icons/action/grade';
import ContentSend from 'material-ui/svg-icons/content/send';
import ContentDrafts from 'material-ui/svg-icons/content/drafts';
import Divider from 'material-ui/Divider';
import ActionInfo from 'material-ui/svg-icons/action/info';
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
    this.props.router.push('/');
    this.props.dispatch(actions.forgetUser());
    this.props.dispatch(actions.closeMainMenu());
  };

  render() {
    return (
      <div>
        <Divider />
        <List>
          <ListItem primaryText="Home" leftIcon={<ContentInbox />} containerElement={<Link to='/home' />} onTouchTap={this.handleClick} />
          <ListItem primaryText="Get Paid" leftIcon={<ActionGrade />} containerElement={<Link to='/getPaid' />} onTouchTap={this.handleClick}/>
          <ListItem primaryText="Pay" leftIcon={<ContentSend />} containerElement={<Link to='/pay' />} onTouchTap={this.handleClick}/>
          <ListItem primaryText="Inbox" leftIcon={<ContentInbox />} containerElement={<Link to='/inbox' />} onTouchTap={this.handleClick}/>
        </List>
        <Divider />
        <List>
          <ListItem primaryText="All mail" rightIcon={<ActionInfo />} />
          <ListItem primaryText="Trash" rightIcon={<ActionInfo />} />
          <ListItem primaryText="Spam" rightIcon={<ActionInfo />} />
          <ListItem primaryText="Forget Me" rightIcon={<ActionInfo />} onTouchTap={this.handleForgetMe}/>
        </List>
      </div>
  );}
}
