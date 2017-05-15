import React , { Component } from 'react';
import PropTypes from 'prop-types';
import {List, ListItem} from 'material-ui/List';
import NavigationChevronRight from 'material-ui/svg-icons/navigation/chevron-right';
import CommunicationEmail from 'material-ui/svg-icons/communication/email';
import RaisedButton from 'material-ui/RaisedButton';
import IconButton from 'material-ui/IconButton';
import StarRating from '../components/starRating';
import {Link} from 'react-router';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import moment from 'moment';

import {
  Table,
  TableBody,
  TableFooter,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

const trStyle = {
  padding: '2px',
  height: '100%',
  width: '100%',
};

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui,
    participant: state.participant,
    messages: state.messages,
  };
})
export default class InboxItem extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      displayName: '',
      starRating: 0,
    };
  };

  getDisplayName = () => {
    if (this.props.participant.identifier === this.props.message.sender.identifier) {
      this.state.displayName = this.props.participant.businessName;
      this.state.starRating = this.props.participant.starRating;
    } else {
      this.state.displayName = this.props.message.receiver.businessName;
      this.state.starRating = this.props.message.receiver.rating;
    }
  }

  hasDueDate = () => {
    if (this.props.message.invoice) {
      return <TableRowColumn style ={trStyle}>{moment(this.props.message.invoice.dateIssued).format('YYYY-MM-DD')}<br/>{'Due: ' + moment(this.props.message.invoice.dateDue).format('YYYY-MM-DD') }</TableRowColumn>;
    } else {
      return <TableRowColumn style ={trStyle} />;
    }

  }

  render() {
    const {message, messages,router, params, location, routes, dispatch, ui, participant, keyID,  ...otherProps} = this.props;

    return (
      <TableRow
        {...otherProps}
        style ={trStyle}
        onMouseDown={()=> this.props.router.push({pathname: '/viewInvoice', query:{key:this.props.keyID} })}>
      >
        {this.getDisplayName()}
        <TableRowColumn style ={trStyle} >{this.state.displayName}<br /><StarRating rating={this.state.starRating}/></TableRowColumn>
        {this.hasDueDate()}
        <TableRowColumn style ={trStyle} >{this.props.message.message.message}</TableRowColumn>
      </TableRow>
    );
  };
}

InboxItem.propTypes = {
  message: PropTypes.any,
  keyID: PropTypes.number,
};
InboxItem.muiName = 'TableRow';
