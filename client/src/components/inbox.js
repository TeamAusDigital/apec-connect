import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router';
import actions from 'state/actions';
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
    ui: state.ui,
    participant: state.participant,
    messages: state.messages,
  };
})
export default class Inbox extends React.Component {

  constructor(props) {
    super(props);
    this.props.dispatch(actions.getParticipantMessages());
  };

  generateInboxItems = () => {
    return this.props.messages.messages.map( (m,index) => <InboxItem key={index} message={m} keyID={index}/> ) ;
  }

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
                {this.generateInboxItems()}

              </TableBody>
            </Table>
          </Paper>
        </Paper>
      </div>
    );
  }
}


