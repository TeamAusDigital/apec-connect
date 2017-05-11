import auth from './auth.actiontype';
import ui from './ui.actiontype';
import participant from './participant.actiontype';

module.exports = {
  ...auth,
  ...ui,
  ...participant
};
