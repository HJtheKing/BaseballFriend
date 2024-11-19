import Lottie from 'lottie-web';
import { FC, useEffect, useRef } from 'react';

interface LottieComponentProps {
  animationData: object;
  loop?: boolean;
  autoplay?: boolean;
  init?: number;
  width?: number | string; // 너비
  height?: number | string; //
}

const LottieComponent: FC<LottieComponentProps> = ({
  animationData,
  loop = true,
  autoplay = true,
  width = '100%',
  height = '100%',
}) => {
  const containerRef = useRef<HTMLDivElement | null>(null); // 애니메이션을 렌더링할 DOM 요소

  useEffect(() => {
    if (containerRef.current) {
      const animation = Lottie.loadAnimation({
        container: containerRef.current, // 애니메이션을 표시할 DOM 요소
        animationData, // 애니메이션 데이터
        loop, // 반복 여부
        autoplay, // 자동 재생 여부
      });
      return () => animation.destroy();
    }
  }, [animationData, loop, autoplay]);
  return (
    <div
      ref={containerRef}
      style={{ width, height }}
    />
  );
};
export default LottieComponent;
