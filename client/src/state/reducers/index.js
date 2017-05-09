import auth from './auth.reducers';
import ui from './ui.reducers';

module.exports = {
  ...auth,
  ...ui
};
