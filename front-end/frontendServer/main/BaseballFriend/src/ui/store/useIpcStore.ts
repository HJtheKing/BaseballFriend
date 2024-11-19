import { create } from 'zustand';

type Character = {
  // 캐릭터의 속성들로 구성됩니다. 구체적인 속성을 명시해주시면 좋습니다.
  // 예시:
  characterSerialNumber: number;
  characterName: string;
  // 필요한 다른 속성들을 추가하세요.
};

export type equipType = {
  armItemSerialNumber: number;
  backgroundList: number[]; // 특정 타입이 있다면 any 대신 그 타입을 사용하세요.
  backgroundSerialNumber: number;
  bodyItemSerialNumber: number;
  characterList: Character[];
  characterSerialNumber: number;
  headItemSerialNumber: number;
};

type IpcType = {
  openMainWindow: (url: string) => void;
  createFullWindow: (url: string) => void;
  openLoginWindow: () => void;
  closeWindow: () => void;
  openNewRealtime: () => void;
  setIgnoreMouseEvents: (ignore: boolean) => void;
  minimizeWindow: () => void;
  toggleOnTopWindow: () => void;
  getAppPath: () => Promise<string>;
  closeLoginWindow: () => void;
  setStoreValue: (key: string, value: string | number | boolean | null) => void;
  getValue: (key: string) => Promise<string | undefined | equipType>;
  devModeCheck: () => Promise<boolean>;
  logout: () => void;
};

const useIpcStore = create<IpcType>()(() => ({
  openMainWindow: (url) => {
    // @ts-ignore
    window.electron.openMainWindow(url);
  },
  createFullWindow: (url) => {
    // @ts-ignore
    window.electron.createFullWindow(url);
  },
  openLoginWindow: () => {
    // @ts-ignore
    window.electron.openLoginWindow();
  },
  closeWindow: () => {
    // @ts-ignore
    window.electron.closeWindow();
  },
  openNewRealtime: () => {
    // @ts-ignore
    window.electron.openNewRealtime();
  },
  setIgnoreMouseEvents: (ignore) => {
    // @ts-ignore
    window.electron.setIgnoreMouseEvents(ignore);
  },
  minimizeWindow: () => {
    // @ts-ignore
    window.electron.minimizeWindow();
  },
  toggleOnTopWindow: () => {
    // @ts-ignore
    window.electron.toggleOnTopWindow();
  },
  getAppPath: () => {
    // @ts-ignore
    return window.electron.getAppPath();
  },
  closeLoginWindow: () => {
    // @ts-ignore
    window.electron.closeLoginWindow();
  },
  setStoreValue: (key, value) => {
    // @ts-ignore
    window.electron.setStoreValue(key, value);
  },
  getValue: (key) => {
    // @ts-ignore
    return window.electron.getStoreValue(key);
  },
  devModeCheck: () => {
    // @ts-ignore
    return window.electron.devModeCheck();
  },
  logout: () => {
    // @ts-ignore
    window.electron.logout();
  },
}));

export default useIpcStore;
