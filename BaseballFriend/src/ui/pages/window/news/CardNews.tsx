import { NewsType } from './News';
import NewsDefault from '../../../assets/imgs/NewsDefaultImage.jpg';

export const CardNews = ({ News }: { News: NewsType }) => {
  const { title, content, newsImage } = News;

  const onDefaultImg: React.ReactEventHandler<HTMLImageElement> = (e) => {
    const target = e.target as HTMLImageElement;
    target.src = NewsDefault;
  };

  const handleNaverNews = () => {
    const url = `https://search.naver.com/search.naver?where=news&ssc=tab.news.all&office_category=5&query=${title}`;
    window.open(url, '_blank'); // 새 창 또는 탭에서 URL 열기
  };

  return (
    <div className="mx-3">
      <div className="relative">
        <img
          src={newsImage}
          alt="thumbnail"
          className="w-full h-[400px] object-cover rounded-3xl"
          onError={onDefaultImg}
        />

        <div className="absolute inset-0 bg-gradient-to-b from-transparent to-black rounded-3xl" />
        <div className="absolute p-4 text-right text-white bg-transparent bg-opacity-50 bottom-4">
          <div className="text-[30px] py-[10px] Jua">{title}</div>
          <div className="flex justify-end">
            <hr className="w-10/12 my-2 border-white border-b-1" />
          </div>
          <div className="ml-6 text-sm Jua">{content}</div>
        </div>

        <button
          onClick={handleNaverNews}
          className="absolute z-20 p-2 text-lg text-white rounded-full right-5 top-5"
        >
          🔗
        </button>
      </div>
    </div>
  );
};

export default CardNews;
