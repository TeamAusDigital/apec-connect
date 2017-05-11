import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import AppBarMain from '../components/AppBarMain';
import Paper from 'material-ui/Paper';
import StarRating from '../components/starRating';
import RaisedButton from 'material-ui/RaisedButton';
import {indigo,white} from './apecConnectTheme';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import InboxItem from './inboxItem';


import {
  Table,
  TableBody,
  TableFooter,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

/***

This is the basic elements for a main view in the app.

***/

const paperStyle = {
  padding: 10,
  textAlign: 'center',
  height: '100%'
};

const getPaidStyle = {
  width: '50%',
};

const payStyle = {
  width: '50%',
};

const inboxStyle ={
};

const businessNameStyle ={
  fontSize: 48,
};

const userDetailsStyle = {
  fontSize: 20,
};

const logoStyle ={
  width: '100%',
  maxHeight: '150px',
};

@withRouter
@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class Inbox extends React.Component {

  render() {
    return (
      <div>
        {/** AppBarMain contains the app bar and menu drawer **/}
        <AppBarMain title={'Inbox'}/>
        {/** Paper containing the logo **/}
        <Paper
          zDepth={1}
          style={paperStyle}
        >
          <img src={Logo} style={logoStyle} />
          <br />
          <br />
          <Paper
            zDepth={1}
            style={paperStyle}
          >
            <Table
              multiSelectable={false}
              showCheckBoxes={false}
            >
              <TableHeader
                displaySelectAll={false}
                adjustForCheckbox={false}
                enableSelectAll={false}
              >
                <TableRow>
                  <TableHeaderColumn>WHO</TableHeaderColumn>
                  <TableHeaderColumn>WHEN</TableHeaderColumn>
                  <TableHeaderColumn>WHAT</TableHeaderColumn>
                </TableRow>
              </TableHeader>
              <TableBody
                displayRowCheckbox={false}
                stripedRows={true}
              >
                <InboxItem id={'1'} who={'S. Jobs'} rating={2} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'M. Pfeiffer'} rating={1} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>
                <InboxItem id={'1'} who={'S. Jobs'} rating={3} issueDate={'11/01/2017'} dueDate={'11/01/2017'} what={'-$11.00'}/>

              </TableBody>
            </Table>
          </Paper>
        </Paper>
      </div>
    );
  }
}


