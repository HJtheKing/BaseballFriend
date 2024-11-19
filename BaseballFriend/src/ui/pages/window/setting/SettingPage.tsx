import SelectBox from '../../../components/SelectBox';
import ToggleButton from '../../../components/ToggleButton';
import { MemberInfoDTO } from './MemberInfoType';
import { useEffect, useState } from 'react';
import { GetMemberInfo, UpdateMemberInfo } from './api';
import Swal from 'sweetalert2';

const options = [
  'KIA',
  '삼성',
  'LG',
  'KT',
  '두산',
  'SSG',
  '롯데',
  '한화',
  'NC',
  '키움',
];

const SettingPage = () => {
  const [registerData, setRegisterData] = useState<MemberInfoDTO>({
    name: '',
    isBriefingAllowed: true,
    isBroadcastAllowed: true,
    favoriteTeam: '',
  });
  useEffect(() => {
    const fetchData = async () => {
      const result = await GetMemberInfo();
      if (result) {
        const transformedData: MemberInfoDTO = {
          name: result.name,
          isBriefingAllowed: result.isBriefingAllowed,
          isBroadcastAllowed: result.isBroadcastAllowed,
          favoriteTeam: result.favoriteTeam,
        };
        setRegisterData(transformedData);
      }
    };
    fetchData();
  }, []);

  const handleToggleChange = (
    key: 'isBriefingAllowed' | 'isBroadcastAllowed',
    value: boolean,
  ) => {
    setRegisterData((prev) => ({ ...prev, [key]: value }));
  };

  const handleSelectChange = (value: string) => {
    console.log(value);
    setRegisterData((prev) => ({ ...prev, favoriteTeam: value }));
  };

  const handleLogout = () => {
    //@ts-ignore
    window.electron.logout();
  };

  const handleUpdateMemberInfoBtn = () => {
    const fetchData = async () => {
      try {
        const response = await UpdateMemberInfo({
          favoriteTeam: registerData.favoriteTeam,
          isBriefingAllowed: registerData.isBriefingAllowed,
          isBroadcastAllowed: registerData.isBroadcastAllowed,
          name: registerData.name,
        });
        if (response.status === 200) {
          console.log(response.data);
          Swal.fire({
            icon: 'success',
            title: '변경 완료!',
            text: '설정이 변경되었습니다.',
            width: '80%',
            timer: 1000,
            timerProgressBar: true,
          });
        }
      } catch (error) {
        Swal.fire({
          icon: 'error',
          title: '회원 정보 수정 중 오류가 발생했습니다.',
          text: '데이터를 불러오지 못했습니다. 다시 시도해주세요.',
          width: '80%',
          timer: 1000,
          timerProgressBar: true,
        });
        console.log(error);
      }
    };
    fetchData();
  };
  return (
    <>
      <h1 className="text-[40px] py-[15px]">설정</h1>
      <div className="w-[340px] mx-auto text-[18px] outline outline-2 outline-gray-300 rounded-2xl px-10 py-5">
        <section className="flex justify-between my-[10px] h-[60px]">
          <h2 className="my-auto">아침 야구 브리핑</h2>
          <ToggleButton
            value={registerData.isBriefingAllowed}
            onChange={(value) => handleToggleChange('isBriefingAllowed', value)}
          />
        </section>
        <section className="flex justify-between my-[10px] h-[60px]">
          <h2 className="my-auto">경기 중요 내용 알림</h2>
          <ToggleButton
            value={registerData.isBroadcastAllowed}
            onChange={(value) =>
              handleToggleChange('isBroadcastAllowed', value)
            }
          />
        </section>
        <section className="flex justify-between my-[10px] h-[60px]">
          <h2 className="my-auto">선호하는 팀</h2>
          <SelectBox
            options={options}
            selectedValue={registerData.favoriteTeam}
            onChange={(selectedValue) => handleSelectChange(selectedValue)}
          />
        </section>

        <div className="flex justify-center gap-5 mt-5">
          <button
            className="px-5 py-1 transition-colors duration-200 bg-[#5383E8] rounded-2xl Jua hover:bg-[#5383E8]/50"
            onClick={handleUpdateMemberInfoBtn}
          >
            <h2 className="my-auto text-white">변경저장</h2>
          </button>
        </div>
      </div>
      <div className="flex justify-center mt-[40px]">
        <button
          className="px-5 py-1 bg-black/10 rounded-2xl hover:bg-black/20 hover:outline-none group"
          onClick={handleLogout}
        >
          <h2 className="my-auto group-hover:text-[#545454]">로그아웃</h2>
        </button>
      </div>
    </>
  );
};

export default SettingPage;
