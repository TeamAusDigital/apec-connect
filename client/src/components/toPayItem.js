import React , { Component } from 'react';
import PropTypes from 'prop-types';
import {List, ListItem} from 'material-ui/List';
import NavigationChevronRight from 'material-ui/svg-icons/navigation/chevron-right';
import CommunicationEmail from 'material-ui/svg-icons/communication/email';
import RaisedButton from 'material-ui/RaisedButton';
import IconButton from 'material-ui/IconButton';
import {white,red} from './apecConnectTheme';
import {Link} from 'react-router';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';

const ItemStyle ={
  width: '75%',
  position: 'relative',
  display: 'table-cell',
  verticalAlign: 'middle',
};

const ListItemStyle={
  whiteSpace : 'normal',
};

const ButtonStyle ={
  position: 'relative',
  textAlign: 'right',
  verticalAlign: 'middle',
  width: '100%',
  minWidth: '10%',
};

const cellDivStyle={
  display: 'table-cell',
  position: 'relative',
  verticalAlign: 'middle',
  textAlign: 'center',
};

const DivStyle ={
  width: '100%',
  position: 'relative',
  display: 'table',
  tableLayout: 'fixed',
  textAlign: 'Left',
  verticalAlign: 'middle',
};

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class ToPayItem extends React.Component {

  constructor(props) {
    super(props);
  };

  rightIconButtonA = () => {
    return (
      <IconButton><CommunicationEmail/></IconButton>
    );
  };

  handleMsgButton = () => {
    this.props.router.push('/sendMessage');
  };

  render() {
    return (
      <div style={DivStyle}>
        <div style = {ItemStyle}>
          <ListItem
            innerDivStyle ={ListItemStyle}
            secondaryText ={this.props.message}
            primaryText ={'$' + this.props.amount.toFixed(2)}
            rightIconButton={<IconButton onTouchTap={this.handleMsgButton}><CommunicationEmail/></IconButton>}
            secondaryTextLines={1}
          />
        </div>
        <div style={cellDivStyle}>
          <RaisedButton style={ButtonStyle} label={'Pay'} backgroundColor={red} labelColor={white}/>
        </div>
      </div>
    );
  };
}

ToPayItem.propTypes = {
  message: PropTypes.string,
  amount: PropTypes.number,
};

