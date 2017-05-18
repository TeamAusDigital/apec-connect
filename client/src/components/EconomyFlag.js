import React from 'react';
import {PropTypes} from 'prop-types';

export const ApecEconomyCodes = [
  'AU',
  'BN',
  'CA',
  'CL',
  'CN',
  'HK',
  'ID',
  'JP',
  'KR',
  'MY',
  'MX',
  'NZ',
  'PG',
  'PE',
  'PH',
  'RU',
  'SG',
  'TW',
  'TH',
  'US',
  'VN'
];


export default class EconomyFlag extends React.Component {
  render() {
    return (
     this.props.economyCode ?
      <span style={this.props.style} className={'flag-icon ' + 'flag-icon-' + this.props.economyCode.toLowerCase()}></span> : <span></span>
    );
  }
}


EconomyFlag.propTypes = {
  economyCode: PropTypes.string.isRequired,
  style: PropTypes.object
};
