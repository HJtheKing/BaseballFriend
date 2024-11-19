import { create } from 'zustand';

interface gpt {
  messages: string[];
  setMessages: (message: string) => void;
}

const useGptStore = create<gpt>((set) => ({
  messages: [],
  setMessages: (message) =>
    set((state) => ({ messages: [...state.messages, message] })),
}));

export default useGptStore;
