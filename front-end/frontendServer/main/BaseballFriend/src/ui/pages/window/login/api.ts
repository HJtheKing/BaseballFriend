import { instance } from '../../../api/axios';
import {
  AuthMessage,
  CodeCheck,
  UserPreferences,
  UserRegistration,
} from './authType';

export async function loginRequest(formData: FormData) {
  const response = await instance.post<AuthMessage>('/auth/login', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
  return response;
}

// 이메일 코드 요청
export async function emailVerification(email: string) {
  const response = await instance.post<AuthMessage>('/auth/email', { email });
  return response;
}

// 이메일 인증 확인
export async function emailVerificationCheck(preload: CodeCheck) {
  const response = await instance.patch<AuthMessage>('/auth/email', preload);
  return response;
}

// 회원가입
export async function signUp(preload: UserRegistration) {
  const response = await instance.post<AuthMessage>('/auth/join', preload);
  return response;
}

// 회원 정보 입력
export async function changeUserInfo(preload: UserPreferences) {
  const response = await instance.patch<AuthMessage>('/member', preload);
  return response;
}

// 자동 로그인 가능 여부 반환
export async function autoLogin() {
  const response = await instance.get<AuthMessage>('/member/check');
  return response;
}
