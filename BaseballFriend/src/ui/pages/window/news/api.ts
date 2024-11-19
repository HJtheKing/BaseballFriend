import { instance } from '../../../api/axios';
import { NewsType } from './News';

// 특정 일 뉴스 조회
export async function getNews(date: string, team: string) {
  const response = await instance.get<NewsType[]>('/news', {
    params: {
      date: date,
      team: team,
    },
  });
  return response;
}

export async function getMemberFavoriteTeam() {
  const response = await instance.get('/member/team');
  return response.data;
}
