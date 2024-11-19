// InfoPage.tsx
import { useState } from 'react';
import GameCardPage from './GameCardPage';
import TeamStatusTable from './TeamStatusPage';
import { ko } from 'date-fns/locale';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import '../../../css/datepicker.css';
import { ReactComponent as Vector } from '../../../assets/imgs/Vector.svg';

const InfoPage = () => {
  const [date, setDate] = useState<string>(getFormattedDate(new Date()));

  function getFormattedDate(date: Date) {
    return date
      .toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
      })
      .replace(/\s/g, '')
      .replace(/-/g, '.')
      .slice(0, -1);
  }

  const handlePrevDate = () => {
    const currentDate = new Date(date.replace(/\./g, '-'));
    currentDate.setDate(currentDate.getDate() - 1);
    setDate(currentDate.toISOString().split('T')[0].replace(/-/g, '.'));
  };

  const handleNextDate = () => {
    const currentDate = new Date(date.replace(/\./g, '-'));
    currentDate.setDate(currentDate.getDate() + 1);
    setDate(currentDate.toISOString().split('T')[0].replace(/-/g, '.'));
  };

  function parseStringToDate(dateStr: string) {
    const [year, month, day] = dateStr.split('.').map(Number);
    return new Date(year, month - 1, day);
  }

  function handleDatePicker(selectedDate: Date | null): void {
    if (selectedDate) {
      setDate(getFormattedDate(selectedDate));
    }
  }

  return (
    <div className="flex flex-col items-center justify-center w-full">
      <h1 className="text-[40px] pt-[15px]">경기 정보</h1>

      <div className="flex justify-center h-[30px] align-middle mb-5">
        <div className="mx-auto my-auto cursor-pointer">
          <Vector
            onClick={handlePrevDate}
            className="mx-auto my-auto rotate-180 cursor-pointer"
          />
        </div>
        <div className="flex px-5">
          <DatePicker
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

      <GameCardPage date={date} />

      <TeamStatusTable />
    </div>
  );
};

export default InfoPage;
