// TeamStatusPage.tsx
import { useState, useEffect } from 'react';
import { instance } from '../../../api/axios';
import { getMemberFavoriteTeam } from '../news/api';

// 팀 랭킹 데이터 타입 정의
interface TeamRank {
  teamRank: number;
  teamName: string;
  winCount: number;
  lossCount: number;
  drawCount: number;
  odds: number;
  last10GamesResults: string;
}

const TeamStatusTable = () => {
  const [teamRanks, setTeamRanks] = useState<TeamRank[]>([]);
  const [loading, setLoading] = useState<boolean>(true); // 로딩 상태 추가
  const [favoriteTeamName, setFavoriteTeamName] = useState<string>('');

  useEffect(() => {
    const fetchTeamRanks = async () => {
      try {
        const favoriteTeamName = await getMemberFavoriteTeam();
        if (favoriteTeamName) setFavoriteTeamName(favoriteTeamName);

        const response = await instance.get('/match/rank');
        setTeamRanks(response.data);
      } catch (error) {
        console.error('Failed to fetch team ranks:', error);
      } finally {
        setLoading(false); // 데이터 로딩 완료
      }
    };
    fetchTeamRanks();
  }, []);

  if (loading) {
    return <div>Loading...</div>; // 데이터가 로딩 중일 때 표시
  }

  return (
    <div className="px-4 mt-6 mb-6">
      <div className="bg-white rounded-lg shadow-md Jua">
        <div className="grid grid-cols-7 p-3 text-sm font-medium text-gray-600">
          <div className="min-w-[60px]">순위</div>
          <div className="min-w-[120px]">팀명</div>
          <div className="min-w-[40px] text-center">승</div>
          <div className="min-w-[40px] text-center">패</div>
          <div className="min-w-[40px] text-center">무</div>
          <div className="min-w-[60px] text-center">승률</div>
          <div className="min-w-[70px] text-center">최근경기</div>
        </div>
        {teamRanks.map((team, index) => (
          <div
            key={team.teamRank}
            className={`grid grid-cols-7 p-3 items-center ${
              team.teamName === favoriteTeamName
                ? 'bg-[#F7B2B2]/70'
                : index % 2 === 1
                  ? 'bg-white'
                  : 'bg-blue-100'
            }`}
          >
            <div>{team.teamRank}</div>
            <div>{team.teamName}</div>
            <div className="text-center">{team.winCount}</div>
            <div className="text-center">{team.lossCount}</div>
            <div className="text-center">{team.drawCount}</div>
            <div className="text-center">{team.odds.toFixed(3)}</div>
            <div className="text-center">
              {team.last10GamesResults
                .replace('승', '-')
                .replace('무', '-')
                .replace('패', '')}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TeamStatusTable;
