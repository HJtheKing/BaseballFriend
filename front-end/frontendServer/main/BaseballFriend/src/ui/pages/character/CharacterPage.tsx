import { Canvas } from '@react-three/fiber';
import NewBear15 from './NewBear15';
import CustomDropdown from './CustomDropdown';
import { useEffect } from 'react';
import { EventSourcePolyfill } from 'event-source-polyfill';
import useCharacterStore, { WearModel } from '../../store/useCharacterStore';
import { useState } from 'react';
import useIpcStore, { equipType } from '../../store/useIpcStore';
import TalkingComponet from './TalkingComponet';
import Alarm from '../../components/Alarm';
// import StateCheck from '../../components/StateCheck';

const CharacterPage = () => {
  const { setFullSize, setWear } = useCharacterStore();
  const { getValue, devModeCheck } = useIpcStore();
  useState(() => {
    setFullSize({ width: window.innerWidth, height: window.innerHeight });
  });

  const sseSub = async () => {
    const isDev: boolean = await devModeCheck();
    // @ts-ignore
    const token: string = await getValue('token');
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
        textLogs: object;
      };
      // @ts-ignore
      window.electron.setStoreValue('text', event.data);
      console.log(data);
    });

    eventSource.addEventListener('alert', (event: MessageEvent) => {
      const data = JSON.parse(event.data) as number | number[] | null;
      console.log('alert:', data);
      // @ts-ignore
      window.electron.setStoreValue('alert', event.data);
    });

    eventSource.addEventListener('brief', (event: MessageEvent) => {
      console.log('brief:', event.data);
      // @ts-ignore
      window.electron.setStoreValue('brief', event.data);
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
        console.log('text', newValue);
      });
      // @ts-ignore
      window.electron.onStoreChange3((newValue) => {
        console.log('alerts', newValue);
      });
      // @ts-ignore
      window.electron.onStoreChange4((newValue) => {
        console.log('brief', newValue);
      });
    }
    return data;
  };

  useEffect(() => {
    const fetchData = async () => {
      // @ts-ignore
      const result: equipType = await window.electron.getStoreValue('equips');

      console.log('equips : ', result);
      const preload: WearModel = {
        armItemSerialNumber: result.armItemSerialNumber,
        backgroundSerialNumber: result.backgroundSerialNumber,
        bodyItemSerialNumber: result.bodyItemSerialNumber,
        characterSerialNumber: result.characterSerialNumber,
        headItemSerialNumber: result.headItemSerialNumber,
      };
      setWear(preload);
    };
    fetchData();
    // @ts-ignore
    window.electron.onStoreChange5((newValue) => {
      fetchData();
    });
  }, []);

  useEffect(() => {
    sseCheck();
    // Store 변경 감지를 위한 리스너 설정 (한번 렌더링되어도 유지됨)
    // @ts-ignore
    window.electron.onStoreChange2(async (newValue) => {
      if (!newValue) {
        // @ts-ignore
        await window.electron.setStoreValue('sse-window', 'chr');
        // @ts-ignore
        const res = await window.electron.getStoreValue('sse-window');
        console.log(res);
        if (res === 'chr') {
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
  return (
    <>
      <div className="h-screen w-screen">
        {/* <StateCheck /> */}
        <Canvas
          orthographic
          camera={{ zoom: 80, position: [0, 10, 15] }}
        >
          <directionalLight
            position={[5, 5, 5]}
            intensity={1}
          />
          {/* <gridHelper /> */}
          <ambientLight intensity={2} />
          <NewBear15 />
        </Canvas>
        <Alarm />
        <CustomDropdown />
        <TalkingComponet />
      </div>
    </>
  );
};

export default CharacterPage;
