import Doosan from '../../../assets/imgs/prediction/Doosan Bears.png';
import Hanwha from '../../../assets/imgs/prediction/Hanwha Eagles.png';
import Samsung from '../../../assets/imgs/prediction/Samsung Lions.png';
import Kia from '../../../assets/imgs/prediction/Kia Tigers.png';
import Kiwoom from '../../../assets/imgs/prediction/Kiwoom Heroes.png';
import KT from '../../../assets/imgs/prediction/Kt wiz.png';
import LG from '../../../assets/imgs/prediction/LG Twins.png';
import Lotte from '../../../assets/imgs/prediction/Lotte Giants.png';
import NC from '../../../assets/imgs/prediction/NC Dinos.png';
import SSG from '../../../assets/imgs/prediction/SSG Landers.png';

export interface MatchInfo {
  matchInfoId: number; //경기 id
  matchTime: string; // 걸린 시간
  homeTeamName: string;
  awayTeamName: string;
  matchResult: number;
  homeScore: number;
  awayScore: number;
  isBeforeMatch: boolean;
  amount: number | null;
  isSuccessed: boolean | null;
  memberPrediction: number | null;
}

// 연속 참여일수, 총 참여일 수
export interface ParticipationDay {
  consecutiveDays: number;
  totalDays: number;
}

export interface PredictType {
  matchInfoId: number;
  memberPrediction: number;
  amount: number;
}

export interface PredictResult {
  matchPredictionId: number;
  matchInfoId: number;
  amount: number;
  memberPredict: number;
}

export interface MatchResponse {
  date: string;
  matchInfos: MatchInfo[];
}

export interface MoneyType {
  gameMoney: number;
}

interface TeamInfo {
  color: string;
  name: string;
  logo: string; // TypeScript가 이미지 import를 인식할 수 있게 타입을 string으로 설정합니다.
}

export const dictionary: Record<string, TeamInfo> = {
  KIA: {
    color: '#C30452', // KIA 타이거즈 색상 (빨강)
    name: 'KIA',
    logo: Kia,
  },
  삼성: {
    color: '#004B91', // 삼성 라이온즈 색상 (파랑)
    name: '삼성',
    logo: Samsung,
  },
  LG: {
    color: '#C60C30', // LG 트윈스 색상 (빨강)
    name: 'LG',
    logo: LG,
  },
  두산: {
    color: '#13274F', // 두산 베어스 색상 (남색)
    name: '두산',
    logo: Doosan,
  },
  KT: {
    color: '#000000', // KT 위즈 색상 (검정)
    name: 'KT',
    logo: KT,
  },
  SSG: {
    color: '#C60C30', // SSG 랜더스 색상 (빨강)
    name: 'SSG',
    logo: SSG,
  },
  롯데: {
    color: '#005BAC', // 롯데 자이언츠 색상 (파랑)
    name: '롯데',
    logo: Lotte,
  },
  한화: {
    color: '#FF6600', // 한화 이글스 색상 (주황)
    name: '한화',
    logo: Hanwha,
  },
  NC: {
    color: '#14274E', // NC 다이노스 색상 (파랑)
    name: 'NC',
    logo: NC,
  },
  키움: {
    color: '#872434', // 키움 히어로즈 색상 (와인색)
    name: '키움',
    logo: Kiwoom,
  },
};

export const dummyMatches: MatchInfo[] = [
  {
    matchResult: 0,
    matchInfoId: 3,
    matchTime: '18:30',
    homeTeamName: '삼성',
    awayTeamName: 'KIA',
    homeScore: 1,
    awayScore: 2,
    amount: 0,
    isSuccessed: null,
    memberPrediction: null,
    isBeforeMatch: true,
  },
  {
    matchResult: 4,
    matchInfoId: 3,
    matchTime: '18:30',
    homeTeamName: '롯데',
    awayTeamName: 'NC',
    homeScore: 1,
    awayScore: 2,
    amount: 0,
    isSuccessed: null,
    memberPrediction: 1,
    isBeforeMatch: false,
  },
  {
    matchResult: 3,
    matchInfoId: 3,
    matchTime: '18:30',
    homeTeamName: '롯데',
    awayTeamName: 'KT',
    homeScore: 1,
    awayScore: 2,
    amount: 100,
    isSuccessed: true,
    memberPrediction: 3,
    isBeforeMatch: false,
  },
  {
    matchResult: 1,
    matchInfoId: 3,
    matchTime: '18:30',
    homeTeamName: '롯데',
    awayTeamName: 'KT',
    homeScore: 3,
    awayScore: 1,
    amount: 30,
    isSuccessed: false,
    memberPrediction: 2,
    isBeforeMatch: false,
  },
  {
    homeTeamName: '한화',
    awayTeamName: 'NC',
    homeScore: 2,
    awayScore: 7,
    isBeforeMatch: false,
    isSuccessed: null,
    matchInfoId: 107,
    matchResult: 3,
    matchTime: '17:00',
    memberPrediction: null,
    amount: null,
  },
  {
    homeTeamName: '롯데',
    awayTeamName: '키움',
    homeScore: 6,
    awayScore: 3,
    isBeforeMatch: false,
    isSuccessed: null,
    matchInfoId: 107,
    matchResult: 3,
    matchTime: '17:00',
    memberPrediction: null,
    amount: null,
  },
];
