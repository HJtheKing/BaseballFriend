import { EventSourcePolyfill } from 'event-source-polyfill';

export const SseEvent = async () => {
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
  });

  eventSource.addEventListener('alert', (event: MessageEvent) => {
    // @ts-ignore
    window.electron.setStoreValue('alert', event.data);
  });

  eventSource.addEventListener('brief', (event: MessageEvent) => {
    // const data = JSON.parse(event.data)

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
