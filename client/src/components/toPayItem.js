import React , { Component, PropTypes } from 'react';
import {List, ListItem} from 'material-ui/List';


const ToPayItem = (props) => (
  <ListItem
    primaryText={props.message}
    secondaryText={props.amount}
  />
);

ToPayItem.propTypes = {
  message: PropTypes.string,
  amount: PropTypes.number,
};

export default ToPayItem;

