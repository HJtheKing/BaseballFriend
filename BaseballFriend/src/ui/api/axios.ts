import axios, { AxiosInstance } from 'axios';

let instance: AxiosInstance;

// instance 생성 함수 초기에 단 한번만 불림
async function createAxiosInstance(): Promise<AxiosInstance> {
  // @ts-ignore
  const isDev: boolean = await window.electron.devModeCheck();

  const axiosInstance = axios.create({
    baseURL: isDev ? '/bf' : 'http://k11a505.p.ssafy.io:8080/bf',
    headers: {
      'Content-Type': 'application/json',
    },
  });
  // instance가 사용될때마다 헤더의 Authorization에 token을 할당
  axiosInstance.interceptors.request.use(
    async (config) => {
      // @ts-ignore
      const token = await window.electron.getStoreValue('token');
      if (token) {
        config.headers['Authorization'] = token;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    },
  );

  return axiosInstance;
}

// 최상위 레벨에서 초기화
// instance가 최초로 불릴때만 isDev를 확인하여 instance를 만듦
(async () => {
  instance = await createAxiosInstance();
  console.log('Axios instance initialized');
})();

export { instance };
