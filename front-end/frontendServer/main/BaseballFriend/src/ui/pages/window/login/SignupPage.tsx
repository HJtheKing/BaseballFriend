import Dog from '../../../assets/imgs/GgaggungDog.png';
import { ReactComponent as Vector } from '../../../assets/imgs/Vector.svg';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { emailVerification, emailVerificationCheck, signUp } from './api';
import Swal from 'sweetalert2';
import { UserRegistration } from './authType';
import { AxiosError } from 'axios';

const SignupPage = () => {
  const [focusedField, setFocusedField] = useState('');
  const [email, setEmail] = useState(''); // 이메일 상태 추가
  const [loading, setLoading] = useState<boolean>(false);
  const [code, setCode] = useState<string>('');
  const [checkLoading, setCheckLoading] = useState<boolean>(false);
  const [pw, setPw] = useState<string>('');
  const [checkPw, setCheckPw] = useState<string>('');
  const [userName, setUserName] = useState<string>('');
  const [isValidEmail, setIsValidEmail] = useState<boolean>(false);
  const [favoriteTeamName, setFavoriteTeamName] = useState('');
  const [showVerificationInput, setShowVerificationInput] = useState(false);
  const [pwError, setPwError] = useState<string>(''); // 비밀번호 에러 메시지 상태 추가
  const [checkPwError, setCheckPwError] = useState<string>('');

  const navigate = useNavigate();

  const inputStyle = (field: string) => {
    return `Jua shadow-xl mt-6 bg-transparent border-b-2 transition-colors duration-200 focus:outline-none text-xl ${
      focusedField === field ? 'border-blue-900' : 'border-gray-400'
    }`;
  };

  // input필드 순서
  const inputFields = [
    { id: 'email', type: 'email', placeholder: '이메일' },
    { id: 'password', type: 'password', placeholder: '비밀번호' },
    { id: 'passwordCheck', type: 'password', placeholder: '비밀번호 재확인' },
    { id: 'name', type: 'text', placeholder: '이름' },
  ];

  // 로그인 페이지로 이동
  const handleGoToLogin = () => {
    navigate('/smallWindow/login');
  };

  const handleFocus = (field: string) => {
    setFocusedField(field);
  };

  const handleBlur = () => {
    setFocusedField('');
  };

  // 비밀번호 적을 때
  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newPw = e.target.value;
    setPw(newPw);

    // 비밀번호 조건 검사
    if (newPw.length === 0) {
      setPwError(''); // 입력이 없을 때 에러 메시지 제거
    } else if (newPw.length < 8) {
      setPwError('비밀번호는 8자 이상 이어야 합니다');
    } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(newPw)) {
      setPwError('특수 문자가 포함되어야 합니다');
    } else {
      setPwError(''); // 모든 조건을 만족할 때 에러 메시지 제거
    }
  };

  // 이메일 변경될 때
  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newEmail = e.target.value;
    setEmail(newEmail);
    setIsValidEmail(false);
  };

  // 코드 변경할 때
  const handleCodeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newCode = e.target.value;
    setCode(newCode ? newCode : '0');
  };

  // 비밀번호 체크 칸 변경시
  const handleCheckPasswordChange = (
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const newCheckPw = e.target.value;
    setCheckPw(newCheckPw);

    // 비밀번호와 일치하는지 확인
    if (newCheckPw !== pw) {
      setCheckPwError('비밀번호가 일치하지 않습니다');
      console.log(pw, newCheckPw);
    } else {
      setCheckPwError(''); // 일치할 경우 에러 메시지 제거
    }
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    // Form 제출 메서드
    event.preventDefault();
    if (pwError !== '') {
      Swal.fire({
        icon: 'error',
        title: '인증 실패',
        text: '비밀번호가 유효하지 않습니다',
        width: '80%',
        timer: 1000,
        timerProgressBar: true,
      });
      return;
    }
    if (checkPwError !== '') {
      Swal.fire({
        icon: 'error',
        title: '가입 실패',
        text: '비밀번호가 일치하지 않습니다',
        width: '80%',
        timer: 1000,
        timerProgressBar: true,
      });
      return;
    }
    if (isValidEmail === false) {
      Swal.fire({
        icon: 'error',
        title: '가입 실패',
        text: '이메일 인증이 필요합니다',
        width: '80%',
        timer: 1000,
        timerProgressBar: true,
      });
      return;
    }
    if (userName === '') {
      Swal.fire({
        icon: 'error',
        title: '가입 실패',
        text: '닉네임을 입력해주세요',
        width: '80%',
        timer: 1000,
        timerProgressBar: true,
      });
      return;
    }
    const fetchData = async () => {
      const preload: UserRegistration = {
        email: email,
        pw: pw,
        checkPw: checkPw,
        isValidEmail: isValidEmail,
        favoriteTeamName: favoriteTeamName,
        name: userName,
      };
      try {
        const response = await signUp(preload);
        if (response.status === 200) {
          Swal.fire({
            icon: 'success',
            title: '회원가입 성공',
            text: `${userName} 님 환영합니다!`,
            width: '80%',
            timer: 1000,
            timerProgressBar: true,
          }).then(() => {
            navigate('/smallWindow/login');
          });
        }
      } catch (error) {
        if (error instanceof AxiosError && error.response) {
          const status = error.response.status;
          if (status === 400) {
            Swal.fire({
              icon: 'error',
              title: '회원가입 실패',
              text: '입력값이 올바르지 않습니다.',
              width: '80%',
              timer: 1000,
              timerProgressBar: true,
            });
          } else if (status === 409) {
            Swal.fire({
              icon: 'error',
              title: '회원가입 실패',
              text: '비밀번호가 일치하지 않습니다.',
              width: '80%',
              timer: 1000,
              timerProgressBar: true,
            });
          }
        } else {
          Swal.fire({
            icon: 'error',
            title: '회원가입 실패',
            text: '회원가입에 실패했습니다.',
            width: '80%',
            timer: 1000,
            timerProgressBar: true,
          });
          console.log(error);
        }
      }
    };

    fetchData();
  };

  //  이메일 인증 코드 요청하기
  const handleVerificationCode = (event: React.MouseEvent) => {
    event.preventDefault();
    const fetchData = async () => {
      if (email.includes('@')) {
        setLoading(true);
        try {
          const res = await emailVerification(email);
          if (res.status === 200) {
            console.log('정상요청 완료');
            Swal.fire({
              icon: 'success',
              title: '이메일 발송 완료',
              text: `${email} 에서 인증 번호를 확인해주세요`,
              width: '80%',
              timer: 1000,
              timerProgressBar: true,
            });
            setShowVerificationInput(true);
          } else if (res.status === 400) {
            Swal.fire({
              icon: 'error',
              title: '오류가 발생했습니다.',
              text: '정상적인 이메일 형태인지 확인해주세요.',
              width: '80%',
              timer: 1000,
              timerProgressBar: true,
            });
          } else if (res.status === 409) {
            Swal.fire({
              icon: 'error',
              title: '오류가 발생했습니다.',
              text: '이미 존재하는 이메일입니다.',
              width: '80%',
              timer: 1000,
              timerProgressBar: true,
            });
          }
          setLoading(false);
        } catch (error) {
          Swal.fire({
            icon: 'error',
            title: '오류가 발생했습니다.',
            text: '데이터를 불러오지 못했습니다. 다시 시도해주세요.',
            width: '80%',
            timer: 1000,
            timerProgressBar: true,
          });
          console.log(error);
          setLoading(false);
        }
      } else {
        Swal.fire({
          icon: 'error',
          title: '오류가 발생했습니다.',
          text: '정상적인 이메일 형태인지 확인해주세요.',
          width: '80%',
          timer: 1000,
          timerProgressBar: true,
        });
        setLoading(false);
      }
      console.log(email);
      // 인증 코드 요청
    };
    fetchData();
  };

  // 이메일 인증 코드 확인
  const handleEmailVerification = (event: React.MouseEvent) => {
    event.preventDefault();
    const fetchData = async () => {
      try {
        const response = await emailVerificationCheck({
          email: email,
          code: code,
        });
        setCheckLoading(true);
        if (response.status === 200) {
          Swal.fire({
            icon: 'success',
            title: '인증 성공',
            text: `이메일 인증에 성공했습니다.`,
            width: '80%',
            timer: 1000,
            timerProgressBar: true,
          });
          setIsValidEmail(true);
          setShowVerificationInput(false);
        }
      } catch (error) {
        Swal.fire({
          icon: 'error',
          title: '오류가 발생했습니다.',
          text: '인증 코드가 올바르지 않습니다.',
          width: '80%',
          timer: 1000,
          timerProgressBar: true,
        });
        console.log(error);
      }
      setCheckLoading(false);
      // 이메일 인증 확인
    };
    fetchData();
  };

  return (
    <div className="bg-slate-50">
      <h1 className="pt-5 text-2xl">회원가입</h1>
      <div className="absolute top-10 left-3">
        <Vector
          onClick={handleGoToLogin}
          className="mx-auto my-auto rotate-180 cursor-pointer"
        />
      </div>
      <form
        onSubmit={handleSubmit}
        className="flex flex-col items-center"
      >
        {inputFields.map((field) => (
          <div key={field.id}>
            <input
              id={field.id}
              type={field.type}
              className={inputStyle(field.id)}
              placeholder={field.placeholder}
              onFocus={() => handleFocus(field.id)}
              onBlur={handleBlur}
              onChange={(e) => {
                if (field.id === 'email') handleEmailChange(e);
                else if (field.id === 'password') handlePasswordChange(e);
                else if (field.id === 'passwordCheck')
                  handleCheckPasswordChange(e);
                else if (field.id === 'name') setUserName(e.target.value);
              }} // 상태 업데이트
            />
            {field.id === 'password' &&
              pwError && ( // 에러 메시지 출력
                <div className="absolute mt-1 text-sm text-red-500">
                  {pwError}
                </div>
              )}
            {field.id === 'passwordCheck' &&
              checkPwError && ( // 비밀번호 확인 에러 메시지 출력
                <div className="absolute mt-1 text-sm text-red-500">
                  {checkPwError}
                </div>
              )}
            {field.id === 'email' && (
              <div>
                <button
                  onClick={handleVerificationCode}
                  className={`absolute right-10 top-[100px] Jua w-12 h-6 text-sm rounded-full transition-colors duration-200 flex items-center justify-center ${
                    loading || isValidEmail
                      ? 'bg-gray-400 text-white text-[13px]'
                      : 'bg-blue-100 text-blue-900 hover:bg-blue-200 hover:text-black'
                  }`}
                  disabled={loading || isValidEmail}
                >
                  {isValidEmail ? (
                    '인증완료'
                  ) : loading ? (
                    <LoadingSpinner />
                  ) : (
                    '인증'
                  )}
                </button>
                {showVerificationInput && (
                  <div>
                    <input
                      id="verification"
                      type="number"
                      className={inputStyle('verification')}
                      placeholder="인증번호"
                      onFocus={() => handleFocus('verification')}
                      onBlur={handleBlur}
                      onChange={(e) => handleCodeChange(e)} // 이메일 코드 상태 업데이트
                    />
                    <button
                      onClick={handleEmailVerification}
                      className="absolute right-10 top-[155px] Jua w-12 h-6 text-sm bg-blue-100 text-blue-900 rounded-full hover:bg-blue-200 hover:text-black transition-colors duration-200"
                    >
                      {checkLoading ? <LoadingSpinner /> : '확인'}
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>
        ))}
        <div>
          <select
            id="team"
            className={inputStyle('team')}
            onFocus={() => handleFocus('team')}
            onBlur={handleBlur}
            value={favoriteTeamName}
            onChange={(e) => setFavoriteTeamName(e.target.value)}
          >
            <option
              value=""
              disabled
            >
              응원 팀 선택
            </option>
            <option value="KIA">KIA</option>
            <option value="삼성">삼성</option>
            <option value="LG">LG</option>
            <option value="두산">두산</option>
            <option value="KT">KT</option>
            <option value="SSG">SSG</option>
            <option value="롯데">롯데</option>
            <option value="한화">한화</option>
            <option value="NC">NC</option>
            <option value="키움">키움</option>
          </select>
        </div>
        <button
          type="submit"
          className="w-48 h-8 mt-6 text-white transition-colors duration-200 bg-blue-800 rounded-full Jua hover:bg-blue-200 hover:text-black"
        >
          가입
        </button>
      </form>
      <div className="fixed right-1 -bottom-3">
        <img
          src={Dog}
          alt="Dog"
          className="w-28 h-28"
          draggable="false"
        />
      </div>
    </div>
  );
};

function LoadingSpinner() {
  return (
    <svg
      className="w-4 h-4 text-white animate-spin"
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
    >
      <circle
        className="opacity-25"
        cx="12"
        cy="12"
        r="10"
        stroke="currentColor"
        strokeWidth="4"
      ></circle>
      <path
        className="opacity-75"
        fill="currentColor"
        d="M4 12a8 8 0 018-8v8H4z"
      ></path>
    </svg>
  );
}

export default SignupPage;
