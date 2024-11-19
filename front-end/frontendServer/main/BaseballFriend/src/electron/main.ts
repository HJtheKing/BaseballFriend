import { app } from 'electron';
import { createLandingWindow } from './windows.js';
import { initIpcHandlers } from './ipcHandlers.js';

app.on('ready', () => {
  createLandingWindow();
  initIpcHandlers();
});
