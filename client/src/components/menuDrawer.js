import React , { Component} from 'react';
import PropTypes from 'prop-types';
import Drawer from 'material-ui/Drawer';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import actions from 'state/actions';
import MenuList from './menuList';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import Divider from 'material-ui/Divider';
import Background from '../common/assets/bg-bottom-alpha-60.png';
import StarRating from './starRating';

const textStyleBig = {
    fontSize: '28pt',
    padding: '2px',
};

const textStyleSmall = {
    fontSize: '16',
    padding: '2px',
};

const divStyle = {
  height: '200px',
  overflow: 'hidden',
  backgroundImage: `url(${Background})`,
  position: 'relative',
  backgroundSize: '300px',
};

const textStyle ={
  position: 'absolute',
  bottom: '0px',
};


@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui,
    participant: state.participant,
  };
})
export default class MenuDrawer extends React.Component {

  constructor(props) {
    super(props);
  }

  closerDrawer = () => this.props.dispatch(
    actions.closeMainMenu()
  );

  toggleDrawer = () => this.props.dispatch(
    this.props.ui.mainMenuOpen ?  actions.closeMainMenu() : actions.openMainMenu()
  );

  render() {
    return (
        <Drawer
          open={this.props.ui.mainMenuOpen}
          docked={false}
          onRequestChange={this.toggleDrawer}
          zDepth={3}
        >
          <div style={divStyle}>

            {/**<img src={Background} style={backgroundStyle}/>**/}
            <div style={textStyle}>
              <div style={textStyleBig}>{this.props.participant.businessName}</div>
              <div style={textStyleSmall}>{this.props.participant.username} <br/> <StarRating rating={1} /></div>
              <br />
            </div>

          </div>
          <Divider />
          <MenuList />
        </Drawer>
    );
  }
}
