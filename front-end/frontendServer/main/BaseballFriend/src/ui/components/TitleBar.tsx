import closeImg from '../assets/imgs/Close.png';
import minusImg from '../assets/imgs/Minus.png';
import BFImage from '../assets/imgs/BF-logo.png';
import { useState } from 'react';

const TitleBar = () => {
  const closeWindow = () => {
    // @ts-ignore
    window.electron.closeWindow();
  };
  const minimizeWindow = () => {
    // @ts-ignore
    window.electron.minimizeWindow();
  };

  const toggleOnTopWindow = () => {
    // @ts-ignore
    window.electron.toggleOnTopWindow();
  };

  const [isLock, setIsLock] = useState<boolean>(false);
  const handleClick = () => {
    setIsLock(!isLock);
    toggleOnTopWindow();
  };
  return (
    <div className="titlebar">
      <div className="titlebar-left">
        <img
          className="titlebar-img"
          src={BFImage}
          alt="TitleLogo"
        />
      </div>

      <button
        onClick={handleClick}
        className="lock-button"
      >
        <div className={`lock-body ${isLock ? 'on' : 'off'}`}>
          <div className="lock-shackle"></div>
        </div>
      </button>
      <button
        onClick={minimizeWindow}
        className="minus-button"
      >
        <div>
          <img
            src={minusImg}
            alt="closeImg"
          />
        </div>
      </button>
      <button
        onClick={closeWindow}
        className="close-button"
      >
        <div>
          <img
            src={closeImg}
            alt="closeImg"
          />
        </div>
      </button>
    </div>
  );
};

export default TitleBar;
