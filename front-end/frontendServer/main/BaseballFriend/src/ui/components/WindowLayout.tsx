import { Route, Routes } from 'react-router-dom';
import TitleBar from './TitleBar';
import TempPage from '../pages/window/temp/TempPage';
import PredictionPage from '../pages/window/predict/PredictionPage';
import EquipPage from '../pages/window/equip/EquipPage';
import SideBar from './SideBar';
import NewsPage from '../pages/window/news/NewsPage';
import InfoPage from '../pages/window/info/InfoPage';
import SettingPage from '../pages/window/setting/SettingPage';
import { SseEvent } from './SseEvent';
import { useEffect } from 'react';

const sseCheck = async () => {
  // @ts-ignore
  const data = await window.electron.getStoreValue('on-sse-event');
  if (!data) {
    // @ts-ignore
    window.electron.setStoreValue('on-sse-event', true);
    SseEvent();
  }
  return data;
};

const WindowLayout = () => {
  useEffect(() => {
    sseCheck();
    // @ts-ignore
    window.electron.onStoreChange2(async (newValue) => {
      if (!newValue) {
        // @ts-ignore
        await window.electron.setStoreValue('sse-window', 'layout');
        // @ts-ignore
        const res = await window.electron.getStoreValue('sse-window');
        if (res === 'layout') {
          // @ts-ignore
          window.electron.setStoreValue('on-sse-event', true);
          SseEvent();
        }
      }
    });
  }, []);

  window.addEventListener('beforeunload', () => {
    // @ts-ignore
    window.electron.setStoreValue('on-sse-event', false);
  });

  return (
    <div className="window-layout">
      <TitleBar />
      <div className="window-container">
        <SideBar />
        <div className="content scrollbar">
          <Routes>
            <Route
              path="temp"
              element={<TempPage />}
            />
            <Route
              path="predictions"
              element={<PredictionPage />}
            />
            <Route
              path="equipments"
              element={<EquipPage />}
            />
            <Route
              path="news"
              element={<NewsPage />}
            />
            <Route
              path="info"
              element={<InfoPage />}
            />
            <Route
              path="setting"
              element={<SettingPage />}
            />
          </Routes>
        </div>
      </div>
    </div>
  );
};

export default WindowLayout;
