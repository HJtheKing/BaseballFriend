import draw from '../../../assets/imgs/prediction/draw.png';
import ball from '../../../assets/imgs/Ball.png';
import { useEffect, useState } from 'react';
import { dictionary, MatchInfo } from './predictionType';
import { PostPrediction } from './api';
import Swal from 'sweetalert2';
const PredictonButton = ({ props }: { props: MatchInfo }) => {
  const {
    isBeforeMatch,
    matchResult,
    matchInfoId,
    matchTime,
    homeTeamName,
    awayTeamName,
    homeScore,
    awayScore,
    amount,
    isSuccessed,
    memberPrediction,
  } = props;

  const [prediction, setPrediction] = useState<number | null>(null);
  const [prAmount, setPrAmount] = useState<number>(0);
  const [gameState, setGameState] = useState<string>('');
  const [bgColor, setBgColor] = useState<string>('#fffeef');
  const [color, setColor] = useState<string>('#ffffff');
  const [displayAmount, setDisplayAmount] = useState<number>(0);
  const [sign, setSign] = useState<string>(''); // '+', '-', ''
  const [timeLeft, setTimeLeft] = useState<string>('');

  useEffect(() => {
    if (memberPrediction !== null) {
      setPrediction(memberPrediction);
    }
  }, [memberPrediction]);

  // 1. 성공
  // 버튼x, 파란 글씨, 하늘색 배경
  // 2. 실패
  // 버튼x, 빨간 글씨, 연한 빨강 배경
  // 3. 배팅 가능
  // 버튼o, 검정 글씨, 노랑 배경
  // 4. 배팅 완료(아직결과모름(진행중 or 진행전))
  // 버튼x, 검정 글씨, 노랑 배경
  // 5. 베팅 안하고 경기 완료
  // 버튼x, 검정 글씨, 회색 배경
  // 6. 경기 취소, 연기
  // 버튼x, 검정 글씨, 회색 배경
  useEffect(() => {
    if (isSuccessed === true) {
      // 예측 성공
      setBgColor('#ECF2FF');
      setGameState(`경기 종료`);
      setColor('#5383E8');
      setSign('+');
      setDisplayAmount(amount ? 2 * amount : 0);
    } else if (isSuccessed === false) {
      // 예측 실패
      setBgColor('#FFF1F3');
      setGameState(`경기 종료`);
      setColor('#E84057');
      setSign('-');
      setDisplayAmount(amount ? amount : 0);
    } else if (isBeforeMatch && memberPrediction) {
      // 시작전에 예측 끝낸 경우
      setBgColor('#EDFFE3');
      setGameState(`${matchTime} 경기 예정`);
      setColor('#000000');
      setSign('');
      setDisplayAmount(amount ? amount : 0);
    } else if (isBeforeMatch) {
      // 시작전에 예측 안한 경우
      setBgColor('#EDFFE3');
      setGameState(`${matchTime} 경기 예정`);
      setColor('#000000');
      setSign('');
      setDisplayAmount(amount ? amount : 0);
    } else if (matchResult === 0) {
      // 경기 진행 중
      setBgColor('#EDFFE3');
      setGameState(`경기 중...`);
      setColor('#000000');
      setSign('');
      setDisplayAmount(amount ? amount : 0);
    } else if (matchResult === 1) {
      setBgColor('#FFFEEF');
      setColor('#000000');
      setSign('+');
      setGameState(`${homeTeamName} 승리`);
      setDisplayAmount(amount ? amount : 0);
    } else if (matchResult === 2) {
      setBgColor('#FFFEEF');
      setColor('#000000');
      setSign('+');
      setGameState('무승부');
      setDisplayAmount(amount ? amount : 0);
    } else if (matchResult === 3) {
      setBgColor('#FFFEEF');
      setColor('#000000');
      setSign('+');
      setGameState(`${awayTeamName} 승리`);
      setDisplayAmount(amount ? amount : 0);
    } else if (matchResult === 4) {
      // 경기 취소
      setBgColor('#D9D9D9');
      setGameState(`경기 취소`);
      setColor('#000000');
      setSign('');
      setDisplayAmount(amount ? amount : 0);
    } else if (matchResult === 5) {
      // 경기 중단
      setBgColor('#D9D9D9');
      setGameState(`경기 중단`);
      setColor('#000000');
      setSign('');
      setDisplayAmount(amount ? amount : 0);
    }
  }, [
    amount,
    awayTeamName,
    homeTeamName,
    isBeforeMatch,
    isSuccessed,
    matchResult,
    matchTime,
    memberPrediction,
  ]);

  const clickPrediction = async () => {
    if (prediction === null) {
      Swal.fire({
        icon: 'warning',
        title: '미기입 항목이 있습니다.',
        text: '승부를 예측해주세요',
        width: '80%',
      });
      return;
    } else if (prAmount === 0) {
      Swal.fire({
        icon: 'warning',
        title: '미기입 항목이 있습니다.',
        text: '예측 금액을 넣어주세요',
        width: '80%',
      });
      return;
    }

    try {
      const res = await PostPrediction({
        matchInfoId: matchInfoId,
        memberPrediction: prediction,
        amount: prAmount,
      });

      if (res.status === 201) {
        console.log(201); // 성공적으로 처리된 경우
        console.log(res.data);
        Swal.fire({
          icon: 'success',
          title: '성공',
          text: '성공적으로 처리되었습니다.',
          width: '80%',
        }).then((result) => {
          if (result.isConfirmed) {
            location.reload(); // 페이지 리로딩
          }
        });
      } else {
        // 상태 코드가 201이 아닌 경우
        Swal.fire({
          icon: 'error',
          title: '오류가 발생했습니다.',
          text: '데이터를 불러오지 못했습니다. 다시 시도해주세요.',
          width: '80%',
        });
        console.log(res.data);
      }
    } catch (error) {
      // 예외 처리
      Swal.fire({
        icon: 'error',
        title: '알 수 없는 오류',
        text: '예기치 못한 오류가 발생했습니다.',
        width: '80%',
      });
      console.log(error);
    }
  };

  // 예측 팀 변경할 때 쓰일 함수
  const changePrediction = (num: number) => {
    if (!isBeforeMatch || memberPrediction) return;
    if (prediction === num) {
      setPrediction(null);
    } else {
      setPrediction(num);
    }
  };

  // 'pr-container c1', 'pr-container', 'pr-container c3' 등 클릭에 따라 뒤에 c1,c2,c3가 붙게 함. 기본은 안붙음
  const getClass = (baseClass: string, condition: number) => {
    if (condition === 0) return baseClass;
    return `${baseClass} c${condition}`;
  };

  // 경기 까지 남은 시간 계산
  useEffect(() => {
    function calculateTimeLeft() {
      const [matchHour, matchMinute] = matchTime.split(':').map(Number); // matchTime을 "시:분"으로 분리
      const now = new Date();
      const matchDate = new Date(
        now.getFullYear(),
        now.getMonth(),
        now.getDate(),
        matchHour,
        matchMinute,
      );

      const diff: number = Number(matchDate) - Number(now); // 경기 시각과 현재 시각의 차이를 밀리초로 계산

      if (diff > 0) {
        const hours = Math.floor(diff / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        setTimeLeft(`남은 시간 : ${hours}시간 ${minutes}분`);
      } else {
        setTimeLeft('경기 시작됨'); // 시간이 지났다면 시작 메시지 표시
      }
    }

    calculateTimeLeft(); // 컴포넌트 마운트 시 한 번 계산
    const timer = setInterval(calculateTimeLeft, 60000); // 1분마다 업데이트

    return () => clearInterval(timer); // 컴포넌트 언마운트 시 타이머 정리
  }, [matchTime]);

  const PrResult = () => {
    return (
      <div className="absolute right-2 top-0 z-10">
        <p
          className="text-sm rounded-b-full px-3"
          style={{ backgroundColor: color, color: 'white' }}
        >
          {isSuccessed ? '예측 성공' : '예측 실패'}
        </p>
      </div>
    );
  };

  return (
    <>
      <div
        className="pr-item"
        style={{ backgroundColor: bgColor }}
      >
        {matchResult >= 1 && <div className="pr-cover" />}

        {/* 경기예정 시간 또는 경기 종료 들어갈 부분 */}
        <div className="absolute top-[5px] text-[10px]">
          <p>{gameState}</p>
        </div>

        {/* 승부 예측 결과 컴포넌트 */}
        {isSuccessed !== null && !isBeforeMatch && <PrResult />}

        <div className={getClass('pr-container', prediction ? prediction : 0)}>
          <div className="absolute top-[-18px] text-[10px] right-[0px]">
            {isBeforeMatch ? <p>{timeLeft}</p> : ''}
          </div>
          <div
            className={`pr-left ${matchResult === 1 ? 'win' : ''}`}
            onClick={() => changePrediction(1)}
            style={{
              backgroundColor:
                prediction === 1 ? dictionary[homeTeamName].color : '#ffffff',
            }}
          >
            <img
              src={dictionary[homeTeamName].logo}
              alt={dictionary[homeTeamName].name}
            />
            <div className="absolute top-[38px] text-[14px] left-1">
              {dictionary[homeTeamName].name}
            </div>
            {/* 왼쪽 팀 스코어 혹은 빈칸 */}
            <div
              className={`score ${
                [1, 2, 3].includes(matchResult) ? 'isEnd' : ''
              }`}
            >
              {`${[1, 2, 3].includes(matchResult) ? homeScore : ''}`}
            </div>
          </div>
          <div
            className={`pr-middle ${matchResult === 2 ? 'win' : ''}`}
            onClick={() => changePrediction(2)}
          >
            <img
              src={draw}
              alt="draw"
            />
          </div>
          <div
            className={`pr-right ${matchResult === 3 ? 'win' : ''}`}
            onClick={() => changePrediction(3)}
            style={{
              backgroundColor:
                prediction === 3 ? dictionary[awayTeamName].color : '#ffffff',
            }}
          >
            <div
              className={`score ${
                [1, 2, 3].includes(matchResult) ? 'isEnd' : ''
              }`}
            >
              {`${[1, 2, 3].includes(matchResult) ? awayScore : ''}`}
            </div>

            <img
              src={dictionary[awayTeamName].logo}
              alt={dictionary[awayTeamName].name}
            />
            <div className="absolute top-[38px] text-[14px] right-1">
              {dictionary[awayTeamName].name}
            </div>
          </div>
        </div>
        <div className="betting-container z-10">
          {isBeforeMatch === false || memberPrediction !== null ? (
            <div
              className={`before:contents`}
              style={{ color: color }}
            >{`${sign}${displayAmount}`}</div>
          ) : (
            <>
              <input
                type="number"
                className="border-b border-black"
                style={{ color: color }}
                onChange={(e) => setPrAmount(Number(e.target.value))}
                defaultValue={`${sign}${displayAmount}`}
              ></input>
            </>
          )}
          {isBeforeMatch && !memberPrediction ? (
            <button onClick={clickPrediction}>
              <img
                src={ball}
                alt="ball"
              />
            </button>
          ) : (
            <div>
              <img
                src={ball}
                alt="ball"
              />
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default PredictonButton;
