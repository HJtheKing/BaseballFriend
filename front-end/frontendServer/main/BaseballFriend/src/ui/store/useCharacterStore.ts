import * as THREE from 'three';
import { create } from 'zustand';

type MenubarPosition = {
  x: number;
  y: number;
};

export type WearModel = {
  characterSerialNumber: number;
  headItemSerialNumber: number;
  bodyItemSerialNumber: number;
  armItemSerialNumber: number;
  backgroundSerialNumber: number;
};

type SizeType = {
  width: number;
  height: number;
};

interface CharacterState {
  nowObject: THREE.Object3D | null;
  nowAnimation: string;
  menubar: boolean;
  talking: boolean;
  menubarPos: MenubarPosition | null;
  talkingPos: MenubarPosition | null;
  fullSize: SizeType;
  isIgnored: boolean;
  isDragging: boolean;
  alwaysDisplay: boolean;
  wear: WearModel;
  newAlarm: boolean;
  alarmPos: MenubarPosition | null;
  alarmAnimationId: number;
  setNowObject: (state: THREE.Object3D) => void;
  setNowAnimation: (state: string) => void;
  setIsDragging: (state: boolean) => void;
  setMenumbar: (state: boolean) => void;
  setMenubarPos: (state: MenubarPosition | null) => void;
  setTalkingPos: (state: MenubarPosition | null) => void;
  setIgnoreMouseEvent: (state: boolean) => void;
  setFullSize: (state: SizeType) => void;
  setAlwaysDisplay: (state: boolean) => void;
  setWear: (state: WearModel) => void;
  setTalking: (state: boolean) => void;
  setNewAlarm: (state: boolean) => void;
  setAlarmPos: (state: MenubarPosition | null) => void;
  setAlarmAnimationId: (state: number) => void;
}

const useCharacterStore = create<CharacterState>((set) => ({
  wear: {
    characterSerialNumber: -1,
    headItemSerialNumber: -1,
    bodyItemSerialNumber: -1,
    armItemSerialNumber: -1,
    backgroundSerialNumber: -1,
  },
  nowObject: null,
  nowAnimation: '',
  menubar: false,
  menubarPos: null,
  fullSize: { width: 0, height: 0 },
  isIgnored: false,
  isDragging: false,
  alwaysDisplay: true,
  talking: false,
  talkingPos: null,
  newAlarm: false,
  alarmPos: { x: 400, y: 50 },
  alarmAnimationId: -1,
  setNowObject: (state) => set({ nowObject: state }),
  setNowAnimation: (state: string) => set({ nowAnimation: state }),
  setIsDragging: (state) => set({ isDragging: state }),
  setMenumbar: (state) => set({ menubar: state }),
  setTalking: (state) => set({ talking: state }),
  setMenubarPos: (state) => set({ menubarPos: state }),
  setTalkingPos: (state) => set({ talkingPos: state }),
  setFullSize: (state) => set({ fullSize: state }),
  setIgnoreMouseEvent: (state) => {
    // @ts-ignore
    window.electron.setIgnoreMouseEvents(state);
    set({ isIgnored: state });
  },
  setAlwaysDisplay: (state) => {
    // @ts-ignore
    window.electron.setAlwaysDisplay(state);
    set({ alwaysDisplay: state });
  },
  setWear: (state) => set({ wear: state }),
  setNewAlarm: (state) => set({ newAlarm: state }),
  setAlarmPos: (state) => set({ alarmPos: state }),
  setAlarmAnimationId: (state) => set({ alarmAnimationId: state }),
}));

export default useCharacterStore;
