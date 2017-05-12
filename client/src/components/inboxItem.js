import React , { Component } from 'react';
import {List, ListItem} from 'material-ui/List';
import NavigationChevronRight from 'material-ui/svg-icons/navigation/chevron-right';
import CommunicationEmail from 'material-ui/svg-icons/communication/email';
import RaisedButton from 'material-ui/RaisedButton';
import IconButton from 'material-ui/IconButton';
import StarRating from '../components/starRating';
import {Link} from 'react-router';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import { PropTypes } from 'prop-types';

import {
  Table,
  TableBody,
  TableFooter,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';


@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class InboxItem extends React.Component {

  constructor(props) {
    super(props);
  };

  render() {
    const {who,rating,issueDate,dueDate,what,router, params, location, routes, dispatch, ui,...otherProps} = this.props;
    return (
      <TableRow
        {...otherProps}
        onMouseDown={()=> this.props.router.push('/viewInvoice')}>
      >
        <TableRowColumn>{this.props.who}<br /><StarRating rating={this.props.rating}/></TableRowColumn>
        <TableRowColumn>{this.props.issueDate}<br/>Due: {this.props.dueDate}</TableRowColumn>
        <TableRowColumn>{this.props.what}</TableRowColumn>
      </TableRow>
    );
  };
}

InboxItem.propTypes = {
  id: PropTypes.string.isRequired,
  who: PropTypes.string.isRequired,
  rating: PropTypes.number,
  issueDate:  PropTypes.string.isRequired,
  dueDate:  PropTypes.string,
  what: PropTypes.string.isRequired,
};
InboxItem.muiName = 'TableRow';
