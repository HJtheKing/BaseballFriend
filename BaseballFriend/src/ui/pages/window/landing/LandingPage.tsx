import { useEffect, useState } from 'react';
import { autoLogin } from '../login/api';
import MainLongLogo from '../../../assets/imgs/BF-logo-long.png';
import BallPark from '../../../assets/imgs/BallPark.png';
import Loading from '../../../assets/lottie/loading.json';
import LottieComponent from '../../../components/LottieComponent';
import { EquipmentInfo } from '../equip/api';

const LandingPage = () => {
  const [statusMessage, setStatusMessage] = useState('로딩 중');

  const openLoginWindow = () => {
    // @ts-ignore
    window.electron.openLoginWindow(); // 새 창 열기 요청
  };
  // const openTempPage = () => {
  //   // @ts-ignore
  //   window.electron.openMainWindow('window/temp');
  // };

  const openCharacterWindow = () => {
    // @ts-ignore
    window.electron.createFullWindow('character');
  };

  const getAppPath = async (): Promise<string> => {
    // @ts-ignore
    return await window.electron.getAppPath();
  };

  const closeWindow = () => {
    // @ts-ignore
    window.electron.closeWindow();
  };

  // 0. app의 위치 가져오기 ()
  // 1. 랜딩페이지에서 electron storage조회해서 토큰 가져오기, zustand에 저장
  // 2. 토큰을 이용해 인증하기
  // 2-1. 유효하면 바로 로그인 -> 회원정보 조회하기
  // 2-2. 유효하지 않으면 로그인 창 띄우기

  // 유저 정보 획득 메서드
  const fetchEquips = async () => {
    const result = await EquipmentInfo();
    // @ts-ignore
    await window.electron.setStoreValue('equips', result);
  };

  useEffect(() => {
    const fetchData = async () => {
      await getAppPath();
      // Wait for 3 seconds (3000 milliseconds)
      await new Promise((resolve) => setTimeout(resolve, 3000));

      try {
        const response = await autoLogin();
        if (response.status === 200) {
          setStatusMessage('로그인 중');
          setStatusMessage('로그인 중');
          await new Promise((resolve) => setTimeout(resolve, 1000));
          console.log(response.data);
          // @ts-ignore
          await window.electron.setStoreValue('on-sse-event', false);
          await fetchEquips();
          // openTempPage();
          openCharacterWindow();
        }
      } catch (e) {
        console.log(e);
        openLoginWindow();
      } finally {
        closeWindow();
      }
    };

    fetchData();
  }, []);

  return (
    <div className="bg-black h-[300px] w-full flex justify-center items-center drag rounded-xl ">
      <img
        className="absolute inset-0 object-cover w-full h-[300px] opacity-60 rounded-xl border-[2px] border-black"
        src={BallPark}
        alt="BallPark"
      />
      <div className="relative z-10 flex flex-col items-center gap-10 rounded-xl">
        <img
          className="w-4/5"
          src={MainLongLogo}
          alt="MainLogo"
          draggable={false}
        ></img>
        <div className="flex items-center justify-items-center">
          <p className="ms-[30px] me-[-20px] text-xl text-white Jua w-fit">
            {statusMessage}
          </p>
          <LottieComponent
            animationData={Loading}
            loop={true}
            autoplay={true}
            width={70}
            height={70}
          />
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
