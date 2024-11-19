import MainLogo from '../../../assets/imgs/MainLogo.png';
import LoginBackGround from '../../../assets/imgs/LoginBackground.png';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { loginRequest } from './api';
import Swal from 'sweetalert2';
import { EquipmentInfo } from '../equip/api';

const LoginPage = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');

  const fetchEquips = async () => {
    const result = await EquipmentInfo();
    // @ts-ignore
    await window.electron.setStoreValue('equips', result);
  };

  const handleLogin = () => {
    const fetchData = async () => {
      const formData = new FormData();
      formData.append('email', email);
      formData.append('password', password);
      try {
        const response = await loginRequest(formData);
        console.log(response);
        const tempToken = response.headers['authorization'];
        // 토큰 일렉트론 스토어에 저장
        // @ts-ignore
        await window.electron.setStoreValue('token', tempToken);
        // 유저 정보 획득
        await fetchEquips();
        // 전체 창 띄우기
        // @ts-ignore
        window.electron.createFullWindow('character');
        // 로그인 창 끄기
        // @ts-ignore
        window.electron.closeLoginWindow();
        // @ts-ignore
        window.electron.openMainWindow('window/temp');
      } catch (e) {
        Swal.fire({
          icon: 'error',
          title: '로그인 실패',
          text: '아이디 또는 비밀번호가 일치하지 않습니다.',
          width: '80%',
          timer: 1000,
          timerProgressBar: true,
        });
        console.log(e);
      }
    };
    fetchData();

    // 로그인하면 sse이벤트 구독여부 초기화
    // @ts-ignore
    window.electron.setStoreValue('on-sse-event', false);
  };

  const handleGoToSignup = () => {
    navigate('/smallWindow/signup');
  };

  return (
    <div className="MainLogo bg-sky-50">
      <div className="pt-16 px-7">
        <img
          src={MainLogo}
          alt="MainLogo"
          draggable="false"
        />
      </div>

      <div className="relative pt-4 px-0">
        <img
          src={LoginBackGround}
          alt="LoginBackGround"
          className="opacity-70"
          draggable="false"
        />
        <input
          type="text"
          className="Jua absolute top-[72%] left-1/2 transform -translate-x-1/2 -translate-y-1/2 shadow-xl h-5 w-40 text-black bg-white rounded-full text-center focus:outline-none text-sm"
          placeholder="이메일"
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          className="Jua absolute top-[81%] left-1/2 transform -translate-x-1/2 -translate-y-1/2 shadow-xl h-5 w-40 text-black bg-white rounded-full text-center focus:outline-none text-sm"
          placeholder="비밀번호"
          onChange={(e) => setPassword(e.target.value)}
        />
        <button
          onClick={handleLogin}
          className="Jua absolute top-[90%] left-1/2 transform -translate-x-1/2 -translate-y-1/2 shadow-xl h-5 w-40 bg-blue-600 text-white rounded-full hover:bg-blue-200 hover:text-black transition-colors duration-200 focus:outline-none text-sm"
        >
          로그인
        </button>
        <button
          onClick={handleGoToSignup}
          className="Jua absolute top-[97%] left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-transparent text-gray-700 text-[10px] underline "
        >
          회원가입
        </button>
      </div>
    </div>
  );
};

export default LoginPage;
