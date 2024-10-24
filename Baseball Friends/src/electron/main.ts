import { app, BrowserWindow, screen } from "electron";
import path from "path";
import { isDev } from "./util.js";

app.on("ready", () => {
  const { width, height } = screen.getPrimaryDisplay().workAreaSize; // Get primary display size

  const windowWidth = 150; // 조절 가능한 초기 너비
  const windowHeight = 150; // 조절 가능한 초기 높이
  const mainWindow = new BrowserWindow({
    width: windowWidth,
    height: windowHeight,
    x: width - windowWidth, // 오른쪽에서 20픽셀 띄움
    y: height - windowHeight, // 아래에서 20픽셀 띄움
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
    },
    frame: false,
    transparent: true,
    alwaysOnTop: true,
    resizable: false,
  });

  if (isDev()) {
    // 개발 모드일 경우 로컬 호스트 활용
    mainWindow.loadURL("http://localhost:5123");
  } else {
    // 개발 모드가 아닐 경우 파일 로드
    mainWindow.loadFile(path.join(app.getAppPath(), "/dist-react/index.html"));
  }
});
