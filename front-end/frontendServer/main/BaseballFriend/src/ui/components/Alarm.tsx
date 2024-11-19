import { useEffect, useState } from 'react';
import useCharacterStore from '../store/useCharacterStore';
export const alarmSize = {
  width: 240,
  height: 100,
};

const Alarm = () => {
  const { newAlarm, alarmPos, setIgnoreMouseEvent, setNewAlarm } =
    useCharacterStore();
  const [text, setText] = useState<string>('');
  useEffect(() => {
    // @ts-ignore
    window.electron.onStoreChange((newValue) => {
      const temp = JSON.parse(newValue);
      if (temp.textLogs && temp.textLogs['log1']) {
        setText(temp.textLogs['log1']);
      } else {
        setText(
          `${temp.homeName}(${temp.homeScore}) : ${temp.awayName}(${temp.awayScore})`,
        );
      }
      console.log('파싱한 데이터:', temp);
    });
  }, []);
  if (!newAlarm) return;
  return (
    <>
      <div
        className="flex items-center justify-center balloon rounded-3xl"
        style={{
          width: `${alarmSize.width * 0.98}px`,
          height: `${alarmSize.height * 0.9}px`,
          left: alarmPos?.x,
          top: alarmPos?.y,
          zIndex: 1000,
          // backgroundColor: 'transparent', // 배경을 투명하게 설정
          // border: 'none',
        }}
        onMouseEnter={() => setIgnoreMouseEvent(false)}
        onMouseLeave={() => setIgnoreMouseEvent(true)}
        onClick={() => {
          setNewAlarm(false);
          setIgnoreMouseEvent(true);
        }}
      >
        <p className="absolute  my-auto font-semibold text-[20px] text-center Galmuri">
          {text ? text : '경기 시작!'}
        </p>
      </div>
    </>
  );
};

export default Alarm;
