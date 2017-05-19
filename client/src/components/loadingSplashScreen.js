import React from 'react';
import Paper from 'material-ui/Paper';
import Logo from '../common/assets/APEC-CONNECT-LOGO.svg';
import CircularProgress from 'material-ui/CircularProgress';

import Background from '../common/assets/bg-bottom-alpha-60.png';

const logoStyle = {
  width: '100%',
  maxHeight: '150px'
};

const paperStyle = {
  padding: '5px',
  textAlign: 'center',
  position: 'relative',
  height:'100vh',
  overflow: 'hidden',
};

const divStyle = {
   height: '100%',
   backgroundImage: `url(${Background})`,
   backgroundSize: 'cover',
   position: 'relative',
};

const bStyle ={
  width: '100%',
  textAlign: 'center',
  position: 'fixed',
  bottom: '50px',
  left: '0px',
  alignItems: 'center',
};

const textStyle ={
  width: '60%',
  textAlign: 'center',
  position: 'relative',
  margin: '0 auto',
  fontWeight: 'bold',
};


/**
 * Responsible for the rendering of loading splash screen.
 */
export default class LoadingSplashScreen extends React.Component {
  render() {
    return (
    <Paper
      zDepth={1}
      style={paperStyle}
    >
      <img src={Logo} style={logoStyle} />
      <div style={divStyle}>

        <div style={bStyle}>
          <div style={textStyle}>This app was developed during the 2017 APEC App Challenge</div>
          <br/>
          <CircularProgress size={60} thickness={5}  />
        </div>

      </div>

    </Paper>
    );
  }
}
