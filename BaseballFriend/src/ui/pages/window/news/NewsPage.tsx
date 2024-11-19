import { useEffect, useState } from 'react';
import { ReactComponent as Vector } from '../../../assets/imgs/Vector.svg';
import { NewsType, NewsResponse } from './News';
import { getMemberFavoriteTeam, getNews } from './api';
import CardNews from './CardNews';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import '../../../css/datepicker.css';
import SelectBox from '../../../components/SelectBox';
import { ko } from 'date-fns/locale';
import cry from '../../../assets/imgs/cry.png';

const options = [
  '전체',
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

const NewsPage = () => {
  const [date, setDate] = useState<string>(getFormattedDate(new Date()));
  const [teamName, setTeamName] = useState<string>('전체');
  const [newsResponse, setNewsResponse] = useState<NewsResponse>({
    newsList: [],
  });

  useEffect(() => {
    const fetchData = async () => {
      const favoriteTeamName = await getMemberFavoriteTeam();
      if (favoriteTeamName) setTeamName(favoriteTeamName);
    };
    fetchData();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const currDate = parseStringToDate(date);
        const targetDate = formatDate(currDate);
        const response = await getNews(
          targetDate,
          teamName === '전체' ? '' : teamName,
        );
        if (response.status === 200) {
          setNewsResponse({ newsList: response.data });
        }
      } catch (error) {
        console.log(error);
        setNewsResponse({ newsList: [] });
      }
    };

    fetchData();
  }, [date, teamName]);

  function parseStringToDate(dateStr: string) {
    const [year, month, day] = dateStr.split('.').map(Number);
    return new Date(year, month - 1, day);
  }

  function getFormattedDate(date: Date) {
    // YYYY.MM.DD
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

  function formatDate(date: Date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const dayOfMonth = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${dayOfMonth}`;
  }

  const settings = {
    dots: false, // 페이지네이션 도트 표시
    infinite: true, // 무한 루프
    slidesToShow: 1, // 한 번에 보여줄 카드 수
    slidesToScroll: 1, // 스크롤 시 이동할 카드 수
    autoplay: false, // 자동 전환 비활성화
    arrows: false, // 이전/다음 화살표 표시
    centerMode: true,
  };

  function handleDatePicker(selectedDate: Date | null): void {
    if (selectedDate) {
      setDate(getFormattedDate(selectedDate));
    }
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

  function handleSelectChange(team: string): void {
    setTeamName(team);
  }

  return (
    <div className="flex flex-col items-center justify-center w-full h-screen">
      <h1 className="text-[40px] pt-[15px]">오늘의 브리핑</h1>
      <div className="flex justify-center h-[30px] align-middle">
        <div className="my-auto">
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
          <SelectBox
            options={options}
            selectedValue={teamName}
            onChange={(team) => handleSelectChange(team)}
          />
        </div>
        <div className="w-[7px] h-[12px] my-auto">
          <Vector
            onClick={handleNextDate}
            className="mx-auto my-auto cursor-pointer"
          />
        </div>
      </div>
      <div className="flex flex-col items-center justify-center w-full h-full">
        {newsResponse.newsList.length === 0 ? (
          // 뉴스 데이터 없을 때
          <div
            id="news-no-content"
            className="flex flex-col items-center justify-center text-2xl text-gray-500 opacity-35"
          >
            <div className="flex justify-center mb-5">
              <img
                src={cry}
                alt="cry"
                className="w-[100px] h-[100px]"
              />
            </div>
            <h1>뉴스가 없어요.</h1>
            <h1>다른 기사를 확인해볼까요?</h1>
          </div>
        ) : (
          // 뉴스 데이터 있을 때
          <div className="w-full overflow-hidden">
            <Slider
              {...settings}
              className="w-full overflow-hidden"
            >
              {newsResponse.newsList.map((News: NewsType) => (
                <CardNews
                  key={News.newsId}
                  News={News}
                />
              ))}
            </Slider>
          </div>
        )}
      </div>
    </div>
  );
};

export default NewsPage;
