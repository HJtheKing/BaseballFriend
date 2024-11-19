import { ReactComponent as Info } from '../assets/imgs/baseball.svg';
import { ReactComponent as News } from '../assets/imgs/news.svg';
import { ReactComponent as MiniGame } from '../assets/imgs/minigame.svg';
import { ReactComponent as Setting } from '../assets/imgs/setting.svg';
import { ReactComponent as Prediction } from '../assets/imgs/prediction.svg';
import { ReactComponent as Bear } from '../assets/imgs/bear.svg';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
const SideBar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const routing = (url: string) => {
    navigate(url);
  };
  const [nowLocation, setLocation] = useState('/window');
  useEffect(() => {
    setLocation(location.pathname);
  }, [location.pathname]);

  return (
    <div className="sticky sidebar">
      <div className="relative group">
        <Info
          className={`my-svg ${nowLocation === '/window/info' ? 'active' : ''}`}
          onClick={() => routing('/window/info')}
        />
        <span className="tooltip Jua">경기</span>
      </div>
      <div className="relative group">
        <News
          className={`my-svg ${nowLocation === '/window/news' ? 'active' : ''}`}
          onClick={() => routing('/window/news')}
        />
        <span className="tooltip Jua">브리핑</span>
      </div>
      <div className="relative group">
        <Prediction
          className={`my-svg ${
            nowLocation === '/window/predictions' ? 'active' : ''
          }`}
          onClick={() => routing('/window/predictions')}
        />
        <span className="tooltip Jua">승부 예측</span>
      </div>
      {/* <div className="relative group">
        <MiniGame className="my-svg" />
        <span className="tooltip Jua">미니 게임</span>
      </div> */}

      <div className="relative group">
        <Bear
          className={`my-svg ${
            nowLocation === '/window/equipments' ? 'active' : ''
          }`}
          onClick={() => routing('/window/equipments')}
        />
        <span className="tooltip Jua">캐릭터</span>
      </div>
      <div className="relative group">
        <Setting
          className={`my-svg ${
            nowLocation === '/window/setting' ? 'active' : ''
          }`}
          onClick={() => routing('/window/setting')}
        />
        <span className="tooltip Jua">설정</span>
      </div>
    </div>
  );
};

export default SideBar;
