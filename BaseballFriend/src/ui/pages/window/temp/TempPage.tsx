// import ToggleButton from '../../../components/ToggleButton';
// import { EventSourcePolyfill } from 'event-source-polyfill';
// import { ipcRenderer } from 'electron';

const TempPage = () => {
  const openNewRealtime = async () => {
    // @ts-ignore
    const check = await window.electron.getStoreValue('texts');
    if (check) {
      // @ts-ignore
      window.electron.openNewRealtime();
    } else {
      alert('시합중이지 않습니다.');
    }
  };
  const openLoginWindow = () => {
    // @ts-ignore
    window.electron.openLoginWindow(); // 새 창 열기 요청
  };
  const handleOnClick2 = () => {
    // @ts-ignore
    window.electron.openNewCharacter();
  };
  const createFullWindow = () => {
    // @ts-ignore
    window.electron.createFullWindow('character');
  };
  const openMainWindow = (url: string) => {
    // @ts-ignore
    window.electron.openMainWindow(url);
  };

  return (
    <div>
      <h1>그냥 임시 페이지 입니다</h1>
      <button
        className="basic-button"
        onClick={openLoginWindow}
      >
        로그인 창
      </button>
      <button
        className="basic-button"
        onClick={handleOnClick2}
      >
        캐릭터
      </button>
      <button
        className="basic-button"
        onClick={createFullWindow}
      >
        전체 창
      </button>
      <button
        className="basic-button"
        onClick={() => openMainWindow('window/predictions')}
      >
        승부 예측
      </button>
      <button
        className="basic-button"
        onClick={() => openMainWindow('window/equipments')}
      >
        장비/상점
      </button>
      <button
        className="basic-button"
        onClick={() => openNewRealtime()}
      >
        실시간 중계
      </button>
      {/* <ToggleButton /> */}
    </div>
  );
};

export default TempPage;
