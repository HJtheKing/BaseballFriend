import { Tray, Menu, BrowserWindow, app } from 'electron';
import path from 'path';

let tray: Tray;

export function initTrayIconMenu(win: BrowserWindow) {
  // tray = new Tray('./BaseballFriend.png');
  tray = new Tray(
    path.join(app.getAppPath(), '/dist-react/BaseballFriend.png'),
  );
  const myMenu = Menu.buildFromTemplate([
    {
      label: '열기',
      type: 'normal',
      click: () => {
        win.show();
      },
    },
    { label: '닫기', type: 'normal', role: 'quit' },
  ]);

  tray.setToolTip('베이스볼 프렌즈');
  tray.setContextMenu(myMenu);
  tray.on('double-click', () => (win.isVisible() ? win.hide() : win.show()));
}
