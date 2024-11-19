import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';

interface PersistState {
  bears: number;
  increase: (by: number) => void;
}

const usePersistStore = create<PersistState>()(
  devtools(
    persist(
      (set) => ({
        bears: 0,
        increase: (by) => set((state) => ({ bears: state.bears + by })),
      }),
      { name: 'bearStore' },
    ),
  ),
);

export default usePersistStore;
