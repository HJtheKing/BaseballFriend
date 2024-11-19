export interface NewsType {
  newsId: number;
  createdAt: string;
  title: string;
  content: string;
  newsImage: string;
}

export interface NewsResponse {
  newsList: NewsType[];
}
