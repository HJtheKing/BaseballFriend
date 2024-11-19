// GameCard.tsx
import { Game } from './gameInfo';
import { dictionary } from '../predict/predictionType';
import VSLogo from '../../../assets/imgs/vs_logo.png';

const GameCard = ({
  teamHomeName,
  teamAwayName,
  homeScore,
  awayScore,
  matchTime,
  location,
  matchResult,
}: Game) => (
  <div className="p-4 min-w-[280px] relative">
    <div className="relative flex mb-4 h-[180px]">
      <div
        className="flex flex-col items-center justify-center w-1/2 p-4"
        style={{
          backgroundColor: `${dictionary[teamAwayName].color}`,
        }}
      >
        <div className="w-[70px] h-[70px] bg-white rounded-full flex items-center justify-center overflow-hidden">
          <img
            src={dictionary[teamAwayName].logo}
            alt={dictionary[teamAwayName].name}
            className="object-contain p-4"
          />
        </div>
        <div className="mt-2 text-center text-gray-400 Jua">{teamAwayName}</div>
        <div className="Jua text-white text-xl font-extrabold mt-1 min-h-[20px]">
          {matchResult === 3 && 'WIN'}
          {matchResult === 1 && 'LOSE'}
        </div>
        {matchResult !== 0 && matchResult !== 3 && (
          <div className="absolute inset-0 w-1/2 bg-black bg-opacity-70" />
        )}
      </div>

      <div
        className="flex flex-col items-center justify-center w-1/2 p-4 "
        style={{
          backgroundColor: `${dictionary[teamHomeName].color}`,
        }}
      >
        <div className="w-[70px] h-[70px] bg-white rounded-full flex items-center justify-center overflow-hidden">
          <img
            src={dictionary[teamHomeName].logo}
            alt={dictionary[teamHomeName].name}
            className="object-contain p-4"
          />
        </div>
        <div className="mt-2 text-center text-gray-400 Jua">{teamHomeName}</div>
        <div className="Jua text-white text-xl font-extrabold mt-1 min-h-[20px]">
          {matchResult === 1 && 'WIN'}
          {matchResult === 3 && 'LOSE'}
        </div>
        {matchResult !== 0 && matchResult !== 1 && (
          <div className="absolute top-0 right-0 w-1/2 h-full bg-black bg-opacity-50" />
        )}
      </div>
    </div>

    {(matchResult === 1 ||
      matchResult === 2 ||
      matchResult === 3 ||
      matchResult === 5) && (
      <div className="absolute items-center -translate-x-1/2 translate-y-2/3 left-1/2 bottom-1/2">
        <div className="px-2 py-1 bg-gray-500 rounded-full shadow-md">
          <div className="text-xl font-semibold text-white Jua">
            {`${awayScore}`} : {`${homeScore}`}
          </div>
        </div>
      </div>
    )}

    <div className="absolute -translate-x-1/2 left-1/2 top-1/4 w-[70px] h-[70px]">
      <img
        src={VSLogo}
        alt="VSLogo"
        draggable="false"
      />
    </div>

    {(matchResult === 4 || matchResult === 5) && (
      <div className="absolute -translate-x-1/2 translate-y-1/2 left-1/2 top-1/2">
        <div className="px-2 py-1 bg-white border border-gray-200 rounded-full shadow-md">
          {matchResult === 4 && (
            <div className="text-xl font-semibold text-red-500 Jua">취소</div>
          )}
          {matchResult === 5 && (
            <div className="text-xl font-semibold text-yellow-500 Jua">
              중단
            </div>
          )}
        </div>
      </div>
    )}
    <p className="text-sm text-center text-gray-600 Jua">{matchTime}</p>
    <p className="text-sm text-center text-gray-600 Jua">{location}</p>
  </div>
);

export default GameCard;
