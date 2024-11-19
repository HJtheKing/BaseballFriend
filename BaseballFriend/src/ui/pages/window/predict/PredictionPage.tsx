import { useEffect, useState } from 'react';
import { ReactComponent as Vector } from '../../../assets/imgs/Vector.svg';
import PredictonButton from './PredictionButton';
import '../../../css/scrollbar.css';
// import { dummyMatches, MatchInfo } from './predictionType';
import ball from '../../../assets/imgs/Ball.png';
import { GetMoney, GetParticipationDay, predictionInfo } from './api';
import Swal from 'sweetalert2';
import { MatchInfo } from './predictionType';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import '../../../css/datepicker.css';
import { ko } from 'date-fns/locale';

const PredictionPage = () => {
  const [date, setDate] = useState<string>(getFormattedDate(new Date()));
  const [consecutiveDay, setConsecutiveDay] = useState<number>(0);
  const [totalDays, setTotalDays] = useState<number>(0);
  const [gameMoney, setGameMoney] = useState<number>(0);
  const [matches, setMatches] = useState<MatchInfo[] | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      const result = await GetParticipationDay();
      setConsecutiveDay(result.consecutiveDays);
      setTotalDays(result.totalDays);
      const money = await GetMoney();
      setGameMoney(money.gameMoney);
    };
    fetchData();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      const currDate = parseStringToDate(date);
      const tempDate = formatDate(currDate);
      try {
        const res = await predictionInfo(tempDate);
        if (res.status === 200) {
          setMatches(res.data.matchInfos);
          console.log(matches);
        } else {
          Swal.fire();
          console.log('200 말고 다른거:', res.data);
        }
      } catch (error) {
        Swal.fire({
          icon: 'error',
          title: '오류가 발생했습니다.',
          text: '데이터를 불러오지 못했습니다. 다시 시도해주세요.',
          width: '60%',
        });
        console.log(error);
      }
    };

    fetchData();
    // 날짜 바뀔 때마다 api 요청 보냄
  }, [date]);

  function parseStringToDate(dateStr: string) {
    const [year, month, day] = dateStr.split('.').map(Number);
    return new Date(year, month - 1, day);
  }

  // 날짜를 원하는 형태로 변경 YYYY.MM.DD
  function getFormattedDate(date: Date) {
    return date
      .toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
      })
      .replace(/\s/g, '') // 공백 제거
      .replace(/-/g, '.') // 구분자를 '.'로 변경
      .slice(0, -1); // 마지막에 붙어있는 '.'제거
  }

  function formatDate(date: Date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더하고
    // , 한 자리 수일 경우 앞에 0을 추가
    const dayOfMonth = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${dayOfMonth}`;
  }

  function handlePreviousDate(): void {
    const currDate = parseStringToDate(date);
    currDate.setDate(currDate.getDate() - 1);
    setDate(getFormattedDate(currDate));
  }

  function handleNextDate(): void {
    const currDate = parseStringToDate(date);
    currDate.setDate(currDate.getDate() + 1);
    setDate(getFormattedDate(currDate));
  }

  function handleDatePicker(selectedDate: Date | null): void {
    if (selectedDate) {
      setDate(getFormattedDate(selectedDate));
    }
  }

  return (
    <div className="px-[20px]">
      <h1 className="text-[40px] pt-[15px]">승부 예측</h1>
      <div className="flex justify-center w-full h-[30px] align-middle">
        <div className="w-[7px] h-[12px] my-auto">
          <Vector
            onClick={handlePreviousDate}
            className="mx-auto my-auto rotate-180 cursor-pointer"
          />
        </div>
        <div className="flex px-5">
          {/* <h2 className="text-[20px] ">{date}</h2> */}
          <DatePicker
            // className="text-[20px] z-50 text-[#545454] Jua pr-0 flex items-center"
            className="datePicker"
            dateFormat="yyyy.MM.dd"
            locale={ko}
            shouldCloseOnSelect
            minDate={new Date('2010-01-01')}
            maxDate={new Date()}
            selected={parseStringToDate(date)}
            onChange={(selectedDate) => handleDatePicker(selectedDate)}
          />
        </div>
        <div className="w-[7px] h-[12px] my-auto">
          <Vector
            onClick={handleNextDate}
            className="mx-auto my-auto cursor-pointer"
          />
        </div>
      </div>
      <div className="flex justify-between">
        <div>
          <h2>총 참여일 : {totalDays}일</h2>
          <h2>현재 {consecutiveDay}일 째 연속 참여 중입니다!</h2>
        </div>

        <div className="flex justify-between h-[22px]">
          <div>
            <h2>{gameMoney}</h2>
          </div>
          <div className="h-full">
            <img
              className="h-full"
              src={ball}
              alt=""
            />
          </div>
        </div>
      </div>

      {matches && matches?.length > 0 ? (
        matches.map((match) => (
          <PredictonButton
            key={match.matchInfoId}
            props={match}
          />
        ))
      ) : (
        <div className="my-container h-3/4">
          <p className="text-center Inter my-[50px]">
            해당일은 경기가 없습니다.
          </p>
        </div>
      )}
    </div>
  );
};

export default PredictionPage;
