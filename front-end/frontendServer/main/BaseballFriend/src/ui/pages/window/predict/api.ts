import { instance } from '../../../api/axios';
import {
  MatchResponse,
  MoneyType,
  ParticipationDay,
  PredictResult,
  PredictType,
} from './predictionType';

// 해당일의 경기정보 가져오기
export async function predictionInfo(date: string) {
  const response = await instance.get<MatchResponse>(`/game/prediction`, {
    params: {
      date: date,
    },
  });
  console.log(response);
  return response;
}

// 예측 버튼 눌렀을 때 post 요청
export async function PostPrediction(predict: PredictType) {
  const response = await instance.post<PredictResult>(
    '/game/prediction',
    predict,
  );
  return response;
}

// 참여일 수 확인 api
export async function GetParticipationDay() {
  const response = await instance.get<ParticipationDay>(
    '/game/participation-days',
  );
  return response.data;
}

// 보유 머니 쳌
export async function GetMoney() {
  const response = await instance.get<MoneyType>('/game/money');
  return response.data;
}
