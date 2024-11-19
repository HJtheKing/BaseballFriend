import { create } from 'zustand';

interface MyState {
  token: string;
  setToken: (state: string) => void;
}

const myStateStore = create<MyState>((set) => ({
  token: '',
  setToken: (state: string) => {
    set(() => ({
      token: state,
    }));
  },
}));

export default myStateStore;
