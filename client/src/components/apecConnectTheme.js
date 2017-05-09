import {blue900,
        red500,
        lightBlue500,
        grey300,
        indigo900,
        amber500,
       } from 'material-ui/styles/colors';
import getMuiTheme from 'material-ui/styles/getMuiTheme';


export const blue = blue900;
export const red = red500;
export const grey = grey300;
export const indigo = indigo900;
export const lightBlue = lightBlue500;
export const amber = amber500;
export const white = '#ffffff';


const apecConnectTheme = getMuiTheme({
  palette: {
    primary1Color: blue900,
    primary2Color: indigo900,
    accent2Color: lightBlue500,
    accent1Color: indigo900,
  },
});

export default apecConnectTheme;
