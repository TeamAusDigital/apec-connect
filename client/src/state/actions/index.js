import auth from './auth.actions';
import ui from './ui.actions';
import participant from './participant.actions';

module.exports = {
  ...auth,
  ...ui,
  ...participant
};
