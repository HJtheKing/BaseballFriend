import { contextBridge, ipcRenderer } from 'electron';

interface EquipInfo {
  characterList: [
    {
      characterSerialNumber: number;
      characterName: string;
      itemList:
        | [
            {
              itemSerialNumber: number;
              itemCategory: number;
              teamName: string;
            },
          ]
        | null;
    },
  ];
  backgroundList:
    | [
        {
          backgroundSerialNumber: number;
        },
      ]
    | null;
  characterSerialNumber: number;
  headItemSerialNumber: number;
  bodyItemSerialNumber: number;
  armItemSerialNumber: number;
  backgroundSerialNumber: number;
}

contextBridge.exposeInMainWorld('electron', {
  // 콜백 함수를 받아 호출하는 메서드
  subscribeStatistics: (callback: (statistics: object) => void) => {
    callback({});
  },
  getStaticData: () => console.log('static'),
  openNewCharacter: () => ipcRenderer.send('open-new-character'),
  openMainWindow: (url: string) => ipcRenderer.send('open-new-window', url),
  closeWindow: () => ipcRenderer.send('close-window'),
  openNewRealtime: () =>
    ipcRenderer.send('open-new-realtime'),
  //특정 컴포넌트 마우스 이벤트 막기 또는 안막기
  setIgnoreMouseEvents: (ignore: boolean) =>
    ipcRenderer.send('set-ignore-mouse-events', ignore),
  minimizeWindow: () => ipcRenderer.send('minimize-window'),
  getDatas: () => ipcRenderer.invoke('get-datas'),
  toggleOnTopWindow: () => ipcRenderer.send('toggle-on-top-window'),
  getAppPath: () => ipcRenderer.invoke('get-app-path'),
  createFullWindow: (url: string) =>
    ipcRenderer.send('open-new-full-window', url),
  openLoginWindow: () => ipcRenderer.send('open-login-page'),
  closeLoginWindow: () => ipcRenderer.send('close-login-page'),
  setStoreValue: (
    key: string,
    value: string | number | boolean | null | EquipInfo | MessageEvent,
  ) => ipcRenderer.invoke('setStoreValue', key, value),
  getStoreValue: (key: string) => ipcRenderer.invoke('getStoreValue', key),
  devModeCheck: () => ipcRenderer.invoke('dev-mode-check'),
  logout: () => ipcRenderer.send('logout'),
  onStoreChange: (callback: (newValue: any) => void) =>
    ipcRenderer.on('store-change-text', (event, newValue) =>
      callback(newValue),
    ),
  onStoreAlertChange: (callback: (newValue: number) => void) =>
    ipcRenderer.on('store-change-alert', (event, newValue) =>
      callback(newValue),
    ),
  onStoreChange2: (callback: (newValue: any) => void) =>
    ipcRenderer.on('store-change-on-sse-Event', (event, newValue) =>
      callback(newValue),
    ),
  onStoreChange3: (callback: (newValue: any) => void) =>
    ipcRenderer.on('store-change-alert', (event, newValue) =>
      callback(newValue),
    ),
  onStoreChange4: (callback: (newValue: any) => void) =>
    ipcRenderer.on('store-change-brief', (event, newValue) =>
      callback(newValue),
    ),

  onStoreChange5: (callback: (newValue: any) => void) =>
    ipcRenderer.on('store-change-equips', (event, newValue) =>
      callback(newValue),
    ),
  
  setAlwaysDisplay: (state: boolean) =>
    ipcRenderer.send('set-always-display', state),
  removeStoreListener: () =>
    ipcRenderer.removeAllListeners('store-change-alert'),
  removeStoreListener2: () =>
    ipcRenderer.removeAllListeners('store-change-text'),
  removeStoreListener3: () =>
    ipcRenderer.removeAllListeners('store-change-on-sse-Event'),
  removeStoreListener4: () =>
    ipcRenderer.removeAllListeners('store-change-brief'),
});
