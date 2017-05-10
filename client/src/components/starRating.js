import React , { Component } from 'react';
import PropTypes from 'prop-types';
import FullStarIcon from 'material-ui/svg-icons/toggle/star';
import EmptyStarIcon from 'material-ui/svg-icons/toggle/star-border';
import {amber} from './apecConnectTheme';

const starNumbers = [1,2,3];

const starRatingStyle ={
};

const FullOrEmpty = (number,rating) => (
    number <= rating ? <FullStarIcon key={number.toString()} style={starRatingStyle} color={amber}/> : <EmptyStarIcon key={number.toString()} style={starRatingStyle}  color={amber}/>
);

const createRating = (rating) => (
  starNumbers.map((number) =>
    FullOrEmpty(number,rating)
  )
);

const StarRating = (props) => (
  <span > {createRating(props.rating)} </span>
  );

StarRating.propTypes = {
  rating: PropTypes.number
};

export default StarRating;



