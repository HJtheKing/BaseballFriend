import { app, BrowserWindow, screen } from 'electron';
import { getPreloadPath } from './pathResolver.js';
import path from 'path';
import { isDev } from './util.js';
import { initTrayIconMenu } from './tray.js';

export let loginWindow: BrowserWindow | null = null; // 로그인 창
export let fullWindow: BrowserWindow | null = null; // 캐릭터 있는 창
export let landingWindow: BrowserWindow | null = null; // 임시 창
export let mainWindow: BrowserWindow | null = null; // 대부분의 페이지가 있는 창

export function createLandingWindow(): BrowserWindow {
  if (landingWindow === null) {
    landingWindow = new BrowserWindow({
      width: 240,
      height: 300,
      frame: false,
      fullscreenable: false,
      transparent: true,
      resizable: false,
      hasShadow: true,
      webPreferences: {
        contextIsolation: true,
        preload: getPreloadPath(),
      },
    });
    landingWindow.on('close', () => {
      landingWindow = null;
    });
    landingWindow.loadURL(
      isDev()
        ? 'http://localhost:5123'
        : `file://${path.join(app.getAppPath(), 'dist-react', 'index.html')}`,
    );
  }
  return landingWindow;
}

export function openLoginWindow(): BrowserWindow {
  if (loginWindow === null) {
    loginWindow = new BrowserWindow({
      width: 400,
      height: 500,
      // minWidth: 400,
      frame: false,
      transparent: true,
      fullscreenable: false,
      resizable: false,
      webPreferences: {
        contextIsolation: true,
        preload: getPreloadPath(),
      },
    });

    loginWindow.loadURL(
      isDev()
        ? 'http://localhost:5123/#/smallWindow/login'
        : `file://${path.join(
            app.getAppPath(),
            'dist-react',
            'index.html',
          )}#/smallWindow/login`,
    );

    loginWindow.on('closed', () => {
      loginWindow = null;
    });
  }
  return loginWindow;
}

export function openMainWindow(url: string): BrowserWindow {
  if (mainWindow === null) {
    mainWindow = new BrowserWindow({
      width: 520,
      height: 640,
      frame: false,
      transparent: true,
      resizable: false,
      show: true,
      webPreferences: {
        contextIsolation: true,
        preload: getPreloadPath(),
      },
    });

    mainWindow.loadURL(
      isDev()
        ? `http://localhost:5123/#/${url}`
        : `file://${path.join(
            app.getAppPath(),
            'dist-react',
            'index.html',
          )}#/${url}`,
    );
    mainWindow.on('closed', () => {
      mainWindow = null;
    });
  } else {
    // 기존 윈도우가 있는 경우
    mainWindow.loadURL(
      isDev()
        ? `http://localhost:5123/#/${url}`
        : `file://${path.join(
            app.getAppPath(),
            'dist-react',
            'index.html',
          )}#/${url}`,
    );
  }

  // 윈도우를 맨 앞으로 가져오고 포커스 주기
  if (!mainWindow.isDestroyed()) {
    mainWindow.show(); // 윈도우가 hidden 상태일 경우를 대비
    mainWindow.setAlwaysOnTop(true); // 임시로 항상 위에 표시
    mainWindow.focus(); // 포커스 주기
    mainWindow.setAlwaysOnTop(false); // 항상 위 설정 해제
  }

  return mainWindow;
}

export function createNewCharacterWindow(): BrowserWindow {
  const { width, height } = screen.getPrimaryDisplay().workAreaSize;

  const characterWindow = new BrowserWindow({
    width: width,
    height: height,
    center: true,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: true,
      preload: getPreloadPath(),
    },
    frame: false,
    transparent: true,
    alwaysOnTop: true,
    resizable: false,
  });

  characterWindow.loadURL(
    isDev()
      ? 'http://localhost:5123/#/cat'
      : `file://${path.join(
          app.getAppPath(),
          'dist-react',
          'index.html',
        )}#/cat`,
  );
  // characterWindow.setIgnoreMouseEvents(true, { forward: true });
  return characterWindow;
}

export function createNewRealtimeWindow(): BrowserWindow {
  const { width, height } = screen.getPrimaryDisplay().workAreaSize;
  const windowWidth = 300;
  const windowHeight = 500;
  const realtimeWindow = new BrowserWindow({
    width: windowWidth,
    height: windowHeight,
    x: width - windowWidth,
    y: height - windowHeight - 250,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: true,
      preload: getPreloadPath(),
    },
    frame: false,
    transparent: true,
    alwaysOnTop: false,
    resizable: false,
  });
  if (isDev()) {
    realtimeWindow.loadURL('http://localhost:5123/#/realtime');
  } else {
    realtimeWindow.loadFile(
      path.join(app.getAppPath(), '/dist-react/index.html'),
      { hash: '#/realtime' },
    );
  }

  return realtimeWindow;
}

export function createFullWindow(url: string): BrowserWindow {
  if (fullWindow === null) {
    fullWindow = new BrowserWindow({
      webPreferences: {
        preload: getPreloadPath(),
        nodeIntegration: false,
        contextIsolation: true,
      },
      frame: false,
      transparent: true,
      fullscreen: true,
      resizable: false,
      minimizable: false,
    });
    fullWindow.setAlwaysOnTop(true, 'normal');
    fullWindow.loadURL(
      isDev()
        ? `http://localhost:5123/#/${url}`
        : `file://${path.join(
            app.getAppPath(),
            'dist-react',
            'index.html',
          )}#/${url}`,
    );
    fullWindow.on('closed', () => {
      fullWindow = null;
    });
  }

  initTrayIconMenu(fullWindow);
  // fullWindow.webContents.openDevTools({ mode: 'detach' });
  // fullWindow.setIgnoreMouseEvents(true, { forward: true });
  return fullWindow;
}
