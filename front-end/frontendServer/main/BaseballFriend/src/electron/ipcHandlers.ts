import { ipcMain, BrowserWindow, app } from 'electron';
import {
  createNewCharacterWindow,
  openMainWindow,
  createNewRealtimeWindow,
  createFullWindow,
  openLoginWindow,
  fullWindow,
  loginWindow,
  mainWindow,
  landingWindow,
} from './windows.js';
import { isDev } from './util.js';
import Store from 'electron-store';
// import { EquipmentInfo } from '../ui/pages/window/equip/api.ts'

const store = new Store();
let realtimeWindow: BrowserWindow;
let datas: string;

export function initIpcHandlers() {
  ipcMain.on('open-login-page', () => {
    openLoginWindow();
  });

  ipcMain.on('close-login-page', () => {
    if (loginWindow !== null) loginWindow.close();
  });

  ipcMain.on('open-new-window', (event, url) => {
    openMainWindow(url);
  });

  ipcMain.on('close-window', (event) => {
    const focusedWindow = BrowserWindow.fromWebContents(event.sender);
    if (focusedWindow) {
      focusedWindow.close();
    }
  });

  ipcMain.on('minimize-window', () => {
    const focusedWindow = BrowserWindow.getFocusedWindow();
    if (focusedWindow) {
      focusedWindow.minimize();
    }
  });

  ipcMain.on('toggle-on-top-window', () => {
    const focusedWindow = BrowserWindow.getFocusedWindow();
    if (focusedWindow) {
      if (focusedWindow.isAlwaysOnTop()) {
        focusedWindow.setAlwaysOnTop(false);
      } else {
        focusedWindow.setAlwaysOnTop(true);
      }
    }
  });

  ipcMain.on('open-new-character', () => {
    createNewCharacterWindow();
  });

  ipcMain.on('open-new-realtime', (event) => {
    realtimeWindow = createNewRealtimeWindow();
  });

  ipcMain.on('open-new-full-window', (event, url) => {
    if (fullWindow === null) createFullWindow(url);
  });

  ipcMain.on('set-ignore-mouse-events', (event, ignore) => {
    if (fullWindow !== null) {
      if (ignore) {
        fullWindow.setIgnoreMouseEvents(true, { forward: true });
      } else {
        fullWindow.setIgnoreMouseEvents(false);
      }
    }

    // const newnewwindow = BrowserWindow.fromWebContents(event.sender);
    // if (newnewwindow !== null) {
    //   if (ignore) {
    //     newnewwindow.setIgnoreMouseEvents(true, { forward: true });
    //     // newnewwindow.setAlwaysOnTop(true, 'screen-saver');
    //   } else {
    //     newnewwindow.setIgnoreMouseEvents(false);
    //   }
    // }
  });

  ipcMain.on('logout', () => {
    // 1. token 초기화
    store.set('token', '');
    // 모든 창 꺼버리기
    // const thisWindow = BrowserWindow.fromWebContents(event.sender);
    mainWindow?.close();
    landingWindow?.close();
    fullWindow?.close();
    // 로그인 창 띄우기
    openLoginWindow();
  });

  ipcMain.handle('get-datas', () => {
    return datas;
  });

  ipcMain.handle('get-app-path', () => {
    if (isDev()) {
      store.set('appPath', '');
      return '';
    } else {
      store.set('appPath', `${app.getAppPath()}`);
      return `${app.getAppPath()}`;
    }
  });

  // 저장소에서 key로 value 가져오기
  ipcMain.handle('getStoreValue', (event, key) => {
    return store.get(key);
  });

  // 저장소에 해당 key에 맞는 value 저장하기
  ipcMain.handle('setStoreValue', (event, key, value) => {
    store.set(key, value);
  });

  ipcMain.handle('dev-mode-check', () => {
    return isDev();
  });

  // store의 특정 이벤트 값 감지 (상태 관리)
  store.onDidChange('text', (newValue) => {
    BrowserWindow.getAllWindows().forEach((window) => {
      window.webContents.send('store-change-text', newValue);
    });
  });

  store.onDidChange('brief', (newValue) => {
    BrowserWindow.getAllWindows().forEach((window) => {
      window.webContents.send('store-change-brief', newValue);
    });
  });

  store.onDidChange('on-sse-event', (newValue) => {
    BrowserWindow.getAllWindows().forEach((window) => {
      window.webContents.send('store-change-on-sse-Event', newValue);
    });
  });

  // 스토어에서 alert값 변경될 때, 구독한 곳에서 모두 callback함수를 실행할 수 있게함
  store.onDidChange('alert', (newValue) => {
    BrowserWindow.getAllWindows().forEach((window) => {
      window.webContents.send('store-change-alert', newValue);
    });
  });

  store.onDidChange('brief', (newValue) => {
    BrowserWindow.getAllWindows().forEach((window) => {
      window.webContents.send('store-change-brief', newValue);
    });
  });

  store.onDidChange('equips', (newValue) => {
    BrowserWindow.getAllWindows().forEach((window) => {
      window.webContents.send('store-change-equips', newValue);
    });
  });

  ipcMain.on('set-always-display', (event, state) => {
    if (fullWindow !== null) {
      if (state) {
        fullWindow.setAlwaysOnTop(true, 'screen-saver');
      } else {
        fullWindow.setAlwaysOnTop(false);
      }
    }
  });
}
