// gameInfo.ts
export interface Game {
    teamHomeName: string;
    teamAwayName: string;
    homeScore: number | undefined;
    awayScore: number | undefined;
    matchTime: string;
    matchResult: number;
    location: string;
    favoriteTeam: string; // favoriteTeam 추가
  }
  