import { instance } from '../../../api/axios';
import { MemberInfoDTO } from './MemberInfoType';

// 회원정보 가져오기
export async function GetMemberInfo() {
  const response = await instance.get<MemberInfoDTO>(`/member`);
  return response.data;
}

// 회원정보 수정하기
export async function UpdateMemberInfo(memberInfoDTO: MemberInfoDTO) {
  const response = await instance.patch(`/member`, memberInfoDTO);
  console.log(memberInfoDTO);
  return response;
}
