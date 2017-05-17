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
import EconomyFlag from '../components/EconomyFlag';

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

  /**
   * Renders the "Who" part of the inbox item.
   * @return {node} of rendered inbox item name table row.
   */
  displayName = () => {
    let currentParticipant = this.props.participant;
    let {sender, receiver, isAnnoucement} = this.props.message;

    if (isAnnoucement) {
      // FIXME: All announcements are hard coded from VN government for now.
      return <TableRowColumn style ={trStyle}>
               Vietnam Officials
               <br />
               <EconomyFlag economyCode='VN' />
             </TableRowColumn>;
    }
    else if (sender.identifier === currentParticipant.identifier) {
      return <TableRowColumn style ={trStyle}>
              To: {receiver.businessName}
              <br />
              <StarRating rating={receiver.rating} />
            </TableRowColumn>;
    }
    else {
      return <TableRowColumn style ={trStyle}>
              From: {sender.businessName}
              <br />
              <StarRating rating={sender.rating} />
            </TableRowColumn>;
    }
  }

  /**
   * Renders the dates properties of a message:
   *  # Message sent date;
   *  # Due Date of an Invoice if the message attached with an Invoice.
   * @return {node} of rendered table row with message dates.
   */
  dueDate = () => {
    let {invoice, metaData} = this.props.message;
    if (invoice) {
      return <TableRowColumn style ={trStyle}>
              {moment(invoice.dateIssued).format('YYYY-MM-DD')}
              <br/>
              {'Due: ' + moment(invoice.dateDue).format('YYYY-MM-DD') }
             </TableRowColumn>;
    }
    else {
      return <TableRowColumn style ={trStyle}>
              {moment(metaData.dateCreated).format('YYYY-MM-DD')}
            </TableRowColumn>;
    }

  }

  render() {
    const {message, messages,router, params, location, routes, dispatch, ui, participant, keyID,  ...otherProps} = this.props;

    return (
      <TableRow
        {...otherProps}
        style ={trStyle}
        onMouseDown={()=> this.props.router.push({pathname: '/viewInvoice', query:{key:this.props.keyID} })}
      >
        {this.displayName()}
        {this.dueDate()}
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
