import path from 'path';
import { app } from 'electron';
import { isDev } from './util.js';

// preload의 경로를 반환하는 함수
export function getPreloadPath() {
  return path.join(
    app.getAppPath(),
    isDev() ? '.' : '..',
    '/dist-electron/preload.cjs',
  );
}
