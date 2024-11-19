import { useState, useEffect, useRef } from 'react';
import { instance } from '../../../api/axios';
import GameCard from './GameCard';
import { Game } from './gameInfo';
import cry from '../../../assets/imgs/cry.png';

interface GameCardPageProps {
  date: string;
}

const GameCardPage = ({ date }: GameCardPageProps) => {
  const [games, setGames] = useState<Game[]>([]);
  const [favoriteTeam, setFavoriteTeam] = useState<string>(''); // 좋아하는 팀 상태 추가
  const scrollRef = useRef<HTMLDivElement>(null);
  const [scrollPosition, setScrollPosition] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 좋아하는 팀 정보 가져오기
        const memberResponse = await instance.get('/member');
        setFavoriteTeam(memberResponse.data.favoriteTeam || ''); // 좋아하는 팀 상태 설정

        // 경기 데이터 가져오기
        const formattedDate = date.replace(/\./g, '-');
        const response = await instance.get(`/match?date=${formattedDate}`);
        let gamesList = response.data.matchList || [];

        // 좋아하는 팀의 경기를 첫 번째로 정렬
        if (favoriteTeam) {
          gamesList = gamesList.sort((a: Game) => {
            if (a.teamHomeName === favoriteTeam) return -1; // 좋아하는 팀이 홈 팀이면 맨 앞으로
            if (a.teamAwayName === favoriteTeam) return -1; // 좋아하는 팀이 원정 팀이면 맨 앞으로
            return 1; // 그 외는 그대로
          });
        }

        setGames(gamesList);
      } catch (error) {
        console.error('Failed to fetch data:', error);
      }
    };

    fetchData();
  }, [date, favoriteTeam]); // date와 favoriteTeam이 변경될 때마다 데이터 재요청

  const handleScroll = (direction: 'left' | 'right') => {
    if (scrollRef.current) {
      const scrollAmount = 280;
      let newPosition = scrollPosition;

      if (direction === 'left') {
        newPosition = Math.max(0, scrollPosition - scrollAmount);
      } else if (direction === 'right') {
        newPosition = Math.min(
          scrollRef.current.scrollWidth - scrollRef.current.clientWidth,
          scrollPosition + scrollAmount,
        );
      }

      scrollRef.current.scrollTo({
        left: newPosition,
        behavior: 'smooth',
      });

      setScrollPosition(newPosition);
    }
  };

  return (
    <div className="relative flex items-center justify-center w-full">
      {games.length > 1 && (
        <button
          onClick={() => handleScroll('left')}
          className="absolute left-0 z-30 p-2 rounded-full"
        >
          ◀
        </button>
      )}

      <div
        ref={scrollRef}
        className="flex space-x-4 p-1 shadow-md overflow-x-hidden h-[270px] w-[430px] max-w-[430px] rounded-md"
        style={{ scrollSnapType: 'x mandatory' }}
      >
        {games.length === 0 ? (
          // 경기 데이터 없을 때
          <div
            id="news-no-content"
            className="flex flex-col items-center justify-center w-full h-full text-2xl text-gray-500 opacity-35"
          >
            <div className="flex justify-center mb-5 align-middle">
              <img
                src={cry}
                alt="cry"
                className="w-[100px] h-[100px]"
              />
            </div>
            <h1>경기가 없어요.</h1>
          </div>
        ) : (
          games.map((game, index) => (
            <GameCard
              key={index}
              {...game}
            />
          ))
        )}
      </div>

      {games.length > 1 && (
        <button
          onClick={() => handleScroll('right')}
          className="absolute right-0 z-30 p-2rounded-full"
        >
          ▶
        </button>
      )}
    </div>
  );
};

export default GameCardPage;
