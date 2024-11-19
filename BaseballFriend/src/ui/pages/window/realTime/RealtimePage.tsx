import { Canvas } from '@react-three/fiber';
import { Environment, Html, OrbitControls } from '@react-three/drei';
import RealChr from './RealChr.tsx';
import { Stadium } from './Stadium.tsx';
import { Suspense, useEffect, useState } from 'react';
import envUrl from './day.hdr?url';
import '../../../css/color.css';
import { EventSourcePolyfill } from 'event-source-polyfill';
import * as THREE from 'three';

export default function RealTime() {
  const [cnt, setCnt] = useState(0);
  const [curBase, setcurBase] = useState<number[]>([0, 0, 0]);
  const [curOuts, setcurOuts] = useState(0);
  const [alerts, setAlerts] = useState<number[] | number | null>(null);
  const [isExpanded, setIsExpanded] = useState(false);
  const [isHovered, setIsHovered] = useState(false);
  const [outs, setOuts] = useState<number>(0);
  const [texts, setTexts] = useState<{
    awayName: string;
    homeName: string;
    awayScore: string;
    homeScore: string;
    inningNumber: number;
    balls: number;
    strikes: number;
    outs: number;
    bases: number[];
    textLogs: {
      log1?: string; // optional property
      [key: string]: string | undefined; // 다른 log 키들도 허용
    };
  } | null>(null);
  const [isEnd, setEnd] = useState(false);

  const sseSub = async () => {
    console.log(alerts, outs);
    // @ts-ignore
    const isDev: boolean = await window.electron.devModeCheck();
    // @ts-ignore
    const token: string = await window.electron.getStoreValue('token');
    console.log('구독');
    const eventSource = new EventSourcePolyfill(
      isDev ? '/bf/sse/sub/v2' : 'http://k11a505.p.ssafy.io/bf/sse/sub/v2',
      {
        headers: {
          Authorization: token,
        },
        heartbeatTimeout: 5000000,
        withCredentials: true,
      },
    ) as EventSource;
    eventSource.onopen = () => {
      console.log('연결 완료');
    };

    eventSource.addEventListener('text', (event: MessageEvent) => {
      // @ts-ignore
      window.electron.setStoreValue('text', event.data);
      const data = JSON.parse(event.data) as {
        awayName: string;
        homeName: string;
        awayScore: string;
        homeScore: string;
        inningNumber: number;
        balls: number;
        strikes: number;
        outs: number;
        bases: number[];
        textLogs: {
          log1?: string; // optional property
          [key: string]: string | undefined; // 다른 log 키들도 허용
        };
      };
      // console.log('본 페이지 text:', data);
      setOuts(data.outs);
      if (curBase !== data.bases) {
        setcurBase(data.bases);
      }
      if (curOuts !== data.outs) {
        setcurOuts(data.outs);
      }

      setTexts(data);
    });

    eventSource.addEventListener('alert', (event: MessageEvent) => {
      const data = JSON.parse(event.data) as number | number[] | null;
      setAlerts(data);
      // console.log('본 페이지 alert:', data);
      // @ts-ignore
      window.electron.setStoreValue('alert', event.data);
    });

    // // teaminfo + 모션

    eventSource.onerror = (e: Event) => {
      console.error('SSE 연결 오류 발생');
      console.log(e);
      eventSource.close();
    };
  };

  const sseCheck = async () => {
    // @ts-ignore
    const data = await window.electron.getStoreValue('on-sse-event');
    if (!data) {
      // @ts-ignore
      window.electron.setStoreValue('on-sse-event', true);
      sseSub();
    } else {
      // 이 페이지에서 sse 구독하지 않았으면, 스토어 변화를 감지하도록 설정
      // @ts-ignore
      window.electron.onStoreChange((newValue) => {
        const data = JSON.parse(newValue) as {
          awayName: string;
          homeName: string;
          awayScore: string;
          homeScore: string;
          inningNumber: number;
          balls: number;
          strikes: number;
          outs: number;
          bases: number[];
          textLogs: {
            log1?: string; // optional property
            [key: string]: string | undefined; // 다른 log 키들도 허용
          };
        };
        if (curBase !== data.bases) {
          setcurBase(data.bases);
        }
        // console.log('text', data);
        if (curOuts !== data.outs) {
          setcurOuts(data.outs);
        }
        setTexts(data);
      });
      // @ts-ignore
      window.electron.onStoreChange3((newValue) => {
        const data = JSON.parse(newValue) as number | number[] | null;
        setAlerts(data);
        // console.log('alerts', data);
      });
    }
    return data;
  };

  const getInfo = async () => {
    // @ts-ignore
    const text = await window.electron.getStoreValue('text');
    // @ts-ignore
    const alert = await window.electron.getStoreValue('alerts');

    if (text) {
      const data = JSON.parse(text) as {
        awayName: string;
        homeName: string;
        awayScore: string;
        homeScore: string;
        inningNumber: number;
        balls: number;
        strikes: number;
        outs: number;
        bases: number[];
        textLogs: {
          log1?: string; // optional property
          [key: string]: string | undefined; // 다른 log 키들도 허용
        };
      };
      setTexts(data);
    }
    if (alert) {
      const data2 = JSON.parse(alert) as number | number[] | null;
      setAlerts(data2);
    }
  };

  useEffect(() => {
    getInfo();
    sseCheck();
    // @ts-ignore
    window.electron.onStoreChange2(async (newValue) => {
      if (!newValue) {
        // @ts-ignore
        await window.electron.setStoreValue('sse-window', 'real');
        // @ts-ignore
        const res = await window.electron.getStoreValue('sse-window');
        if (res === 'real') {
          // @ts-ignore
          window.electron.setStoreValue('on-sse-event', true);
          // @ts-ignore
          window.electron.removeStoreListener();
          // @ts-ignore
          window.electron.removeStoreListener2();
          sseSub();
        }
      }
    });
  }, []);

  window.addEventListener('beforeunload', () => {
    // @ts-ignore
    window.electron.setStoreValue('on-sse-event', false);
  });

  const BasesDiamond = ({ bases = [0, 0, 0] }: { bases: any }) => {
    return (
      <div
        style={{
          position: 'relative',
          width: '40px', // 크기 조정
          height: '40px',
          margin: '0 auto',
        }}
      >
        {/* 1루 */}
        <div
          style={{
            position: 'absolute',
            right: '0',
            top: '50%',
            transform: 'translateY(-50%) rotate(45deg)',
            width: '15px', // 베이스 크기 조정
            height: '15px',
            backgroundColor: bases[0] === 1 ? '#ffd700' : '#ccc',
            border: '2px solid #333',
          }}
        />

        {/* 2루 */}
        <div
          style={{
            position: 'absolute',
            top: '0',
            left: '50%',
            transform: 'translate(-50%, 0) rotate(45deg)',
            width: '15px',
            height: '15px',
            backgroundColor: bases[1] === 1 ? '#ffd700' : '#ccc',
            border: '2px solid #333',
          }}
        />

        {/* 3루 */}
        <div
          style={{
            position: 'absolute',
            left: '0',
            top: '50%',
            transform: 'translateY(-50%) rotate(45deg)',
            width: '15px',
            height: '15px',
            backgroundColor: bases[2] === 1 ? '#ffd700' : '#ccc',
            border: '2px solid #333',
          }}
        />
      </div>
    );
  };

  const runnerRender = () => {
    const render = [];
    render.push(
      <RealChr
        key="batter"
        position={[53, 0, 3]}
        rotation={[
          THREE.MathUtils.radToDeg(0),
          THREE.MathUtils.radToDeg(90),
          0,
        ]}
        curBase={0} // 현재 자신의 베이스 위치 인덱스
        aniEvent={cnt} // 아웃 or 세이프 or 홈런
      />,
    );
    if (curBase[0]) {
      render.push(
        <RealChr
          key="first-batter"
          position={[35, 0, -17]}
          rotation={[
            THREE.MathUtils.radToDeg(0),
            THREE.MathUtils.radToDeg(-1),
            0,
          ]}
          curBase={1}
          aniEvent={cnt}
        />,
      );
    }
    if (curBase[1]) {
      render.push(
        <RealChr
          key="second-batter"
          position={[21, 0, 3]}
          rotation={[
            THREE.MathUtils.radToDeg(0),
            THREE.MathUtils.radToDeg(60),
            0,
          ]}
          curBase={2}
          aniEvent={cnt}
        />,
      );
    }
    if (curBase[2]) {
      render.push(
        <RealChr
          key="third-batter"
          position={[41, 0, 16]}
          rotation={[
            THREE.MathUtils.radToDeg(0),
            THREE.MathUtils.radToDeg(20),
            0,
          ]}
          curBase={3}
          aniEvent={cnt}
        />,
      );
    }
    return render;
  };

  const textRender = () => {
    const textList = [];
    if (!texts?.textLogs) {
      textList.push('경기 중...');
      return textList;
    }

    const logs = Object.values(texts.textLogs);

    if (logs.length <= 1) {
      return logs;
    }

    if (!isExpanded) {
      // 축소된 상태: 최신 로그 1개만 표시 + 더보기 버튼
      textList.push(logs[logs.length - 1]);
      textList.push(
        <div
          key="show-more"
          onClick={() => setIsExpanded(true)}
          style={{ cursor: 'pointer', color: '#898989' }}
        >
          더보기
        </div>,
      );
    } else {
      // 확장된 상태: 모든 로그 표시 + 접기 버튼
      textList.push(...logs);
      textList.push(
        <div
          key="show-less"
          onClick={() => setIsExpanded(false)}
          style={{ cursor: 'pointer', color: 'blue' }}
        >
          접기
        </div>,
      );
    }

    return textList;
  };

  useEffect(() => {
    if (!texts?.textLogs) {
      setCnt(0);
      return;
    }

    for (const d in texts.textLogs) {
      const msg = texts.textLogs[d];
      if (msg?.includes('종료')) {
        setEnd(true);
        break;
      } else {
        setEnd(false);
      }

      if (
        msg?.includes('아웃') ||
        msg?.includes('땅볼') ||
        msg?.includes('플라이')
      )
        setCnt(-1);
      else if (
        msg?.includes('진루') ||
        msg?.includes('안타') ||
        msg?.includes('홈인') ||
        msg?.includes('루타')
      )
        setCnt(1);
      else if (msg?.includes('홈런')) setCnt(2);
      else if (msg?.includes('삼진')) setCnt(-2);
      else setCnt(0);
    }
    runnerRender();
  }, [texts?.textLogs]);

  return (
    <div
      className="stadium"
      style={{
        width: '100%', // viewport width
        height: '100%', // viewport height
        position: 'fixed',
        top: 0,
        left: 0,
      }}
    >
      <div
        className="score-box"
        style={{
          background: '#FFFBFC',
          boxShadow: '4px 4px 4px rgba(0, 0, 0, 0.25)',
          borderRadius: '5px',
          width: '28.5%',
          height: '15%',
          position: 'absolute',
          top: '5%',
          left: '10%',
          listStyle: 'none',
          margin: 0,
          maxWidth: 200,
          zIndex: 1000,
          // WebkitAppRegion: 'drag',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
        }}
      >
        <div
          className={texts?.awayName}
          style={{
            height: '50%',
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'space-around',
          }}
        >
          <h2 style={{ color: '#ffffff' }}>{texts?.awayName}</h2>
          <h2 style={{ color: '#ffffff' }}>{texts?.awayScore}</h2>
        </div>
        <div
          className={texts?.homeName}
          style={{
            height: '50%',
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'space-around',
          }}
        >
          <h2 style={{ color: '#ffffff' }}>{texts?.homeName}</h2>
          <h2 style={{ color: '#ffffff' }}>{texts?.homeScore}</h2>
        </div>
      </div>
      <div
        className="alert-box"
        style={{
          background: '#FFFBFC',
          boxShadow: '4px 4px 4px rgba(0, 0, 0, 0.25)',
          borderRadius: '5px',
          width: '50%',
          height: isExpanded ? '30%' : '15%', // 확장 시 높이 증가
          position: 'absolute',
          top: '25%',
          left: '25%',
          listStyle: 'none',
          margin: 0,
          maxWidth: 200,
          zIndex: 1000,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          textAlign: 'center',
          transition: 'all 0.3s ease', // 부드러운 전환 효과
          transform: `scale(${isHovered ? 1.05 : 1})`, // 호버 시 크기 변경
          cursor: 'pointer',
        }}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <h1>{textRender()}</h1>
      </div>

      <div
        className="count-box"
        style={{
          background: '#000000',
          color: '#ffffff',
          boxShadow: '4px 4px 4px rgba(0, 0, 0, 0.25)',
          width: '35%',
          height: '15%', // 높이를 자동으로 조절
          position: 'absolute',
          top: '5%',
          right: '10%',
          listStyle: 'none',
          margin: 0,
          maxWidth: 200,
          zIndex: 1000,
          padding: '10px',
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-between',
        }}
      >
        <div style={{ flex: '1' }}>
          <h1 style={{ margin: '0', fontSize: '1rem' }}>
            {texts?.inningNumber}회
          </h1>
          <h1 style={{ margin: '0', fontSize: '0.75rem' }}>
            {texts?.outs}OUTS
          </h1>
        </div>
        <div style={{ flex: '1' }}>
          <BasesDiamond bases={texts?.bases} />
        </div>
      </div>
      <Canvas
        shadows
        camera={{
          position: [0, 95, 0], // 높이 조절
          fov: 60,
          near: 0.1,
          far: 135,
          up: [0, 0, -1], // 탑뷰를 위한 카메라 방향 설정
          rotation: [Math.PI / 2, 0, 0], // 카메라 회전
        }}
        style={{
          background: 'linear-gradient(to bottom, #87CEEB, #E0F6FF)',
        }}
      >
        <Suspense fallback={<Loading />}>
          {/* 조명 */}
          {/* <ambientLight intensity={0.5} /> */}
          <directionalLight
            position={[50, -50, 0]}
            intensity={0.4}
            castShadow
            shadow-mapSize-width={2048}
            shadow-mapSize-height={2048}
          />
          {/* <hemisphereLight intensity={0.7} /> */}
          <Environment
            files={envUrl}
            background
            backgroundIntensity={1}
          />

          {/* 야구장 */}
          <Stadium />

          {/* 카메라 컨트롤 */}
          <OrbitControls
            maxPolarAngle={15} // 카메라 각도 제한
            minPolarAngle={3} // 최소 각도
            minDistance={50}
            maxDistance={200}
            enablePan={false}
            enableZoom={false}
            enableRotate={false}
            enableDamping={false}
            target={[0, 7, 4]} // 카메라가 바라보는 중심점
          />

          {/* 안개 효과 */}
          <fog
            attach="fog"
            args={['#E0F6FF', 100, 200]}
          />
          <group rotation-y={-Math.PI / 2}>{runnerRender()}</group>
        </Suspense>
      </Canvas>
      {isEnd && (
        <div
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0, 0, 0, 0.5)', // 반투명한 검은색 배경
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 9999, // 다른 요소들 위에 표시
          }}
        >
          <div
            style={{
              backgroundColor: 'white',
              padding: '20px 40px',
              borderRadius: '10px',
              boxShadow: '0 0 10px rgba(0,0,0,0.2)',
            }}
          >
            <h2
              style={{
                margin: 0,
                color: '#333',
              }}
            >
              경기가 종료되었습니다.
            </h2>
          </div>
        </div>
      )}
    </div>
  );

  function Loading() {
    return (
      <Html center>
        <div
          style={{
            borderRadius: '5px',
            position: 'absolute',
            width: '50%',
            height: '50%',
            display: 'gird',
            placeItems: 'center',
          }}
        >
          <h1>공사중</h1>
        </div>
      </Html>
    );
  }
}
