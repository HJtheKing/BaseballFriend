<div align="center">
<a href="https://drive.google.com/file/d/1aZ2Ey50I8RBGo6aRQkasdOjDq1BCRQN9/view">
<img src="https://i.postimg.cc/7ZMHkxHp/BF-logo-long-1.png" alt="exe" width="500"/>
</a>
</div>

> **서비스 명** : BaseballFriend(베프)
야구팬들을 위한 데스크탑 앱 ⚾😊 <br>
**개발 기간** : 2024.10.14 ~ 2024.11.19 (6주)
>
<br>

## ❓기획 배경
- ***직장인 야구 팬의 증가***
    - 올해 9월, 프로야구 출범 이후 처음으로 1000만 관중을 넘는 기록을 세우며, 최근 야구에 대한 관심이 급증하고 있습니다. 특히, 2030세대의 젊은 팬층이 주요한 흥행 요인으로 작용하고 있습니다. 티켓링크에서 제공한 프로야구 입장권 연령대 비율을 보면, 20대와 30대가 전체의 65%를 차지하고 있어, 이들이 야구 관람의 주 소비층임을알 수 있습니다.
- ***KBO리그와 캐릭터 콜라보***
    - KBO리그는 다양한 인기 캐릭터와의 콜라보를 진행하며 젊은 팬들에게 큰 인기를 끌었습니다. 팬들은 자신이 좋아하는 팀의 귀여운 캐릭터 굿즈를 통해 더욱 친밀하게 야구를 즐길 수 있습니다.
- ***KBO리그의 긴 경기 시간***
    - KBO에 따르면 2024시즌 정규 이닝의 평균 경기 시간은 약 3시간 10분입니다. 이는 바쁜 직장인들이 경기를 매일 보기에 어려움을 느끼게 합니다.
<br>

## 💡프로젝트 소개

BaseballFriend은 야구 팬들이 캐릭터와 함께 야구를 즐길 수 있는 데스크탑 앱입니다. 

저희는 BaseballFriend을 통해 최근 증가하는 직장인 야구 팬들을 겨냥하여, 그들의 야구 관람 경험을 한층 더 향상시키고자 합니다.

BaseballFriend는 실시간으로 최소한의 필수 야구 정보를 제공하여, 접근성이 뛰어납니다. 이를 통해 사용자들은 야구에 대한 사랑과 열정을 더욱 깊게 느낄 수 있으며, 야구 경기를 관람하지 않아도 언제 어디서나 쉽게 정보를 확인하고 즐길 수 있습니다! ⚾
<br>

## 📽️UCC 영상
<div align="center">
<a href="https://youtu.be/TfduJoKIBqk">
<img src="https://i.postimg.cc/RCkJCZmF/A505-PJT-1.png" alt="exe" width="500">
</a>
</div>
<br>

## 💌주요 기능

### 1️⃣***야구 정보 제공***

**(FE)**

- ***경기일정***
    - datepicker 라이브러리를 활용하여 캘린더를 통해 날짜를 선택할 수 있고, 해당 날짜의 경기 결과를 조회할 수 있으며, 예정 경기 또한 조회 가능합니다.
    - 응원하는 팀의 순위가 순위표에 하이라이트 되어 출력됩니다.
- ***뉴스 브리핑***
    - slick 라이브러리를 통해 각 뉴스를 카드 형태로 만들어 caroussel로 표현할 수 있도록 설정했습니다.
    - 팀을 선택하거나 날짜를 선택하여 필터링이 된 뉴스를 조회할 수 있습니다.

**(BE)**

- ***경기 일정***
    - 주기적으로 KBO 홈페이지를 Crawling하여(@Scheduling) 경기 일정 및 결과를 제공합니다.
    - 오늘의 경기를 쉽게 조회 가능합니다.
- ***뉴스 브리핑***
    - 전날에 올라온 각 팀 별 야구 관련 기사들을 요약하고, 내용에 맞는 썸네일을 생성하여 사용자에게 보여줍니다.
    - OpenAI API Prompting 작성에 있어, CoT(Chain-of-Thought) Prompting 기법을 적용하여 뉴스 브리핑 및 썸네일에 대한 결과 정확도를 향상 시켰습니다.

### 2️⃣ ***데이터 크롤링***

- KBO 홈페이지를 크롤링하여 경기 일정 및 결과, 문자 중계 데이터를 크롤링합니다.
- Mutation API를 활용한 동적 크롤링으로 서버 부하를 줄이고, 호스트의 IP 밴을 예방합니다.
- Kafka로 데이터를 Produce하여 Server-to-Server 통신을 가능하게 합니다.

### 3️⃣ ***Comsumer + SSE***

**(BE)**

- 클라이언트와의 SSE 연결을 위한 `SseEmitter`를 관리합니다.
- @kafkaListener를 통해 `Kafka`의 Topic을 구독, 각 상황에 맞는 데이터를 클라이언트에게 전송합니다.

### 4️⃣ ***승부예측게임***

**(FE)**

- 게임머니(볼)을 통해 각 야구 경기의 승무패 예측을 클릭으로 할 수 있습니다.
- 경기 결과에 따라 승리 팀의 테두리가 강조되며 옆에 볼 획득 여부를 조회할 수 있습니다.

**(BE)**

- 당일의 경기들의 결과를 예측하여 그에 따른 리워드를 획득합니다.
- 경기 중단, 취소 등의 비정상적인 경기 종료에 대한 예외 처리를 통해 서비스의 완성도를 높였습니다.

### 5️⃣ ***캐릭터*** 🐱🐻🐼🐥

**(FE)**

- 로그인 시, 일렉트론에서 설정한 윈도우를 토대로 투명한 창 안에 캐릭터가 출력되어 다른 창 위에서도 상호작용 할 수 있도록 하였습니다.
- 캐릭터는 애니메이션을 반복하거나 클릭 이벤트, 위치에 의해 특정 애니메이션을 수행합니다.
- SSE 환경에서 야구 중계와 뉴스 브리핑 등의 알람 이벤트를 실시간으로 받아, 알람 내용을 말풍선에 출력하고, 캐릭터가 특정 모션을 수행하도록 설정했습니다.

### 6️⃣ ***중계***

**(FE)**

- three.js를 이용하여 야구장 디자인을 하였고, 캐릭터를 불러와 애니메이션과 함께 출력했습니다.
- SSE 환경에서 실시간으로 중계 알람을 받아, 데이터를 파싱하여 본 중계의 주자 움직임과 캐릭터 움직임이 유사하도록 설정했습니다.

### 7️⃣ ***상점***

**(FE)**

- 일렉트론 스토어와 API를 활용하여 액세서리 구매 시 바로 옷장에 반영이 되며, 반영에서 착용 후 확정 버튼을 누를 시 메인 캐릭터 (투명 창의 캐릭터)에 실시간 반영되도록 설정했습니다.

**(BE)**

- 존재 여부, 보유 여부, 금액의 유무를 확인하여 구매 가능 여부를 판단하고 구매를 진행합니다.
<br>

## 📺 화면 소개

### (1) 회원 관련 페이지

| 로그인 | 회원가입 | 설정 |
| --- | --- | --- |
| <img src="https://i.postimg.cc/t4Hg6SZ5/image.png" alt="로그인" width="490"> | <img src="https://i.postimg.cc/1zm5C8KV/image.png" alt="회원가입" width="512"> | <img src="https://i.postimg.cc/SR0dqbR8/image.png" alt="설정" width="553">|
| - `[로그인]` 버튼으로 로그인을 진행합니다.<br> - `[회원가입]` 버튼으로 회원가입을 진행합니다. | - `[인증]` 버튼을 통해 이메일 인증을 진행합니다.<br> - 응원 팀 정보를 수집합니다. | -알림 설정(모닝 브리핑, 애니메이션 중계)을 수정합니다.<br> -응원 팀 정보를 수정합니다. |

### (2) 경기 관련 페이지

| 경기 정보 | 승부 예측| 브리핑 |
| --- | --- | --- |
| <img src="https://i.postimg.cc/7Z4R00x7/image.png" alt="경기정보" width="550"> | <img src="https://i.postimg.cc/ZRWs4Lck/image.png" alt="승부예측" width="550"> | <img src="https://i.postimg.cc/brb62NW3/image.png" alt="뉴스" width="550"> |
| - 경기 정보를 확인합니다.<br> - `[📅]` 를 통해 날짜를 변경합니다.<br> - 전체 팀 랭킹을 확인합니다.<br> - 응원 팀 랭킹은 붉게 표시합니다. | - 당일 경기의 승부 예측을 참여합니다.<br> - 승리 할 팀과 게임 머니를 입력 후 `[⚾]`버튼을 눌러 참여합니다.<br> - 예측 성공 시 2배, 무승부 5배를 얻습니다.<br> - 예측 실패 시 예측 게임 머니를 잃습니다. | - 오늘의 브리핑 뉴스를 확인합니다. <br> - `[📅]` 를 통해 날짜를, `팀`버튼을 통해 팀을 변경합니다. <br> - 전 일 기준, 각 팀 별 핵심 이슈를 요약해 5개씩 제공합니다. <br> - 각 뉴스를 슬라이드해 이동할 수 있습니다. <br> 뉴스의 `[📎]` 버튼을 통해 네이버 뉴스로 이동합니다. |

### (3) 캐릭터 관련 페이지

| 옷장 | 상점 |
| --- | --- |
| <img src="https://i.postimg.cc/ZRBQmhPF/image.png" alt="옷장" width="300"> | <img src="https://i.postimg.cc/bY2V53rk/image.png" alt="상점" width="300"> |
| - `[머리], [몸통], [팔]` 버튼을 눌러 각 부위에 따른 보유 굿즈를 확인합니다. <br> - 굿즈 목록 중 원하는 굿즈를 눌러 착용해볼 수 있습니다. <br> - `[확정]` 버튼을 눌러 실제 캐릭터에 적용합니다. | - `[머리], [몸통], [팔]` 버튼을 눌러 각 부위에 따른 판매 굿즈를 확인합니다. <br> - `[구매]` 버튼을 눌러 구매합니다. |

### (4) 캐릭터 상호작용

| 기본 | 메뉴 |
| --- | --- |
| <img src="https://i.postimg.cc/L6pDbSrF/image.png" alt="기본" width="550"> | <img src="https://i.postimg.cc/8chHzXfY/image.png" alt="메뉴" width="550"> |
| - 데스크탑에서 캐릭터를 확인합니다. | - 캐릭터 위로 `[마우스 오른쪽 버튼]` 을 클릭해 메뉴바를 실행합니다. |

| 채팅 | 애니메이션 중계 |
| --- | --- |
| <img src="https://i.postimg.cc/pLVZPZW0/image.png" alt="채팅" width="550"> | <img src="https://i.postimg.cc/q7zx3vCy/image.png" alt="중계" width="550"> |
|  - 캐릭터 위로 `[마우스 왼쪽 버튼]` 을 더블 클릭해 채팅 창을 실행합니다. | - 메뉴 바의 `[실시간 중계]` 버튼을 눌러 애니메이션 중계를 실행합니다. <br> - 중계 화면으로 애니메이션 중계를 확인합니다. <br> - 중계 화면 없이 캐릭터 말풍선을 통해 경기 상황을 전달받을 수 있습니다. |

| 애니메이션 중계 모션 |
| --- |
| <img src="https://i.postimg.cc/C18Ccr7K/image.gif" alt="중계GIF" width="300"> |
| - 애니메이션 중계 시작 모션입니다. <br> - `[안타], [홈런], [역전]` 등의 상황에 맞는 모션과 알림을 실시간으로 전달받을 수 있습니다. |
<br>

## ⚙️개발 환경

### 🖥️Frontend

- Nodejs 20.18.0
- Nginx 1.21.4-alpine
- IDE : VSCode 1.95.1

### 🔭Backend

- ***Main, SSE Server***
    - OpenJDK 17.0.13
    - gradle 7.6.1
    - Python 3.10.11
    - IDE : IntelliJ

- ***Crawling Server***
    - Python 3.10.11
    - IDE : PyCham 2024.2

### 💪Infra

- AWS EC2 Ubuntu 20.06.6 LTS
- Docker 27.3.1
- Docker Compose v2.29.1
- nginx 1.18.0 (Ubuntu)
- Jenkins 2.482

### 🔐Database

- MySQL 8.0
- Redis
<br>

## 🔧기술 스택

### 🖥️Frontend
<img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=white">
<img src="https://img.shields.io/badge/Electron-47848F?style=flat-square&logo=Electron&logoColor=white">
<img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=TypeScript&logoColor=white">
<img src="https://img.shields.io/badge/Zustand-433e38?style=flat-square&logoColor=white">
<img src="https://img.shields.io/badge/Blender-E87D0D?style=flat-square&logo=Blender&logoColor=white">
<img src="https://img.shields.io/badge/Three.js-000000?style=flat-square&logo=Three.js&logoColor=white">

### 🔭Backend
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/Spring Validation-6DB33F?style=flat-square&logoColor=white">
<img src="https://img.shields.io/badge/Spring Batch-6DB33F?style=flat-square&logoColor=white">
<img src="https://img.shields.io/badge/JPA-6DB33F?style=flat-square&logoColor=white">
<img src="https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=JMeter&logoColor=white">

### 💪Infra
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/Docker Compose-2496ED?style=flat-square&logoColor=white">

### 🔐Database
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/Redis-FF4438?style=flat-square&logo=Redis&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=Amazon S3&logoColor=white">

### 🗨️Communication
<img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira&logoColor=white">
<img src="https://img.shields.io/badge/Mattermost-0058CC?style=flat-square&logo=Mattermost&logoColor=white">
<img src="https://img.shields.io/badge/Gitlab-FC6D26?style=flat-square&logo=Gitlab&logoColor=white">
<img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white">
<img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=Figma&logoColor=white">
<br>

## 🦮이용 가이드
```
// Main Server 실행
1. 프로젝트 클론
$ git clone [프로젝트 레포지토리 URL]
$ cd [프로젝트 디렉토리 이름]/back-end/mainServer/main

2. Docker Compose를 사용하여 kafka, mysql, redis, zookeeper 실행
$ docker compose up -d

// SSE Server 실행
1. 프로젝트 클론
$ git clone [프로젝트 레포지토리 URL]
$ cd [프로젝트 디렉토리 이름]/back-end/sseServer/main


// Crawling Server 실행
1. 프로젝트 클론
$ git clone [프로젝트 레포지토리 URL]
$ cd [프로젝트 디렉토리 이름]/back-end/crawlingServer/main

// 어플리케이션 실행
1. 프로젝트 클론
$ git clone [프로젝트 레포지토리 URL]
$ cd [프로젝트 디렉토리 이름]/front-end/frontServere/main
$ npm install
$ npm run dev
```
<br>

## 🏠서비스 아키텍처
<div align="center">
<img src="https://i.postimg.cc/qqvx58Tb/BF.png" alt="서비스 아키텍처" width="500">
</div>

- 서버 :  `Main`, `Crawling` , `SSE`
    - 부하 방지를 위해 알림과 실시간 중계 용 SSE 서버를 분리합니다.
    - 데이터 크롤링을 위해 크롤링 서버를 두고, SSE (알림) 서버와 통신하기 위해 `Kafka` 를 이용했습니다.
<br>

## 🌿ERD
<div align="center">
<img src="https://i.postimg.cc/7P9SxT9N/image.png" alt="erd" width="500">
</div>
<br>

## ⏳시퀀스 다이어그램
<div align="center">
<img src="https://i.postimg.cc/0QfK2LYN/image-1.png" alt="시퀀스 다이어그램" width="500">
</div>
<br>

## 💣트러블 슈팅

### 야구 경기 실시간 중계 데이터 통신 문제

▶️**문제 상황**

- 크롤링 서버에서 얻은 데이터를 실시간으로 SSE 서버로 전달 필요
- 문자 중계 사이트의 구조상, 타자 이름을 페어링하는 로직으로 데이터를 처리하기 때문에 데이터 손실이 없어야 합니다.

▶️**해결** 

- 이벤트 브로커인 `Kafka`는 생산한 이벤트를 처리 후 삭제하지 않고 저장합니다.
- `consumer`인 SSE 서버는 장애가 발생하더라도 데이터 손실 없이 다시 메세지를 수신 가능할 수 있습니다.

### 야구 경기 중계 동시성 문제

▶️**문제 상황**

- 여러 경기가 동시에 진행될 경우, 서비스 지연문제가 발생

▶️**원인**

- 파이썬 코드는 python.exe 프로세스 내에서 동작하며, 멀티 쓰레드 상황에서 GIL(Global Interpreter Lock) 발생
- 따라서 쓰레드가 스위칭되며 수행되고, 병렬 처리가 보장되지 않음. 이로 인해 오히려 병목 현상이 발생될 수 있음.

▶️**해결**

- `멀티 프로세싱`으로 처리하여 여러 건의 요청이 병렬적으로 동시에 처리되도록 보장함.
<br>

## 🎄프로젝트 결과 및 성과

✅ **3D 캐릭터 구현**

- `Blender`와 `Three.js` 를 활용한 3D 캐릭터와 캐릭터 굿즈 개발

✅ **실시간 중계와 캐릭터 모션 알림 구현**

- `Kafka` 와 `SSE` 를 활용한 관심팀에 대한 실시간 경기 문자 중계 전달
- 형상관리 시스템인  `Electron` 을 이용해 실시간 중계창 구현
- `Blender` 를 활용한 다양한 캐릭터 애니메이션 모션 개발. `Three.js(Fiber)` 로 캐릭터를 로드한 후, `React`에서 실행

✅ **야구 관련 알림 구현**

- AI를 활용해 매일 새벽 팀별 뉴스 요약 및 관련 이미지 생성
- 매일 아침마다 브리핑 알람 전송을 통해 사용자가 뉴스를 읽고 당일 경기 승부예측 게임에 참여하도록 유도
<br>

## 🎡추후 계획

🚩 **캐릭터 다양화**

- 다양한 캐릭터 구매 및 장착 기능 구현
- 캐릭터와 함께하는 다양한 미니게임 구현

🚩 **AI 모델 도입**

- 텍스트 요약 AI 모델 도입

🚩 **성능 최적화**

- MySQL과 Redis의 사용을 효율적으로 관리하기 위한 방법 모색

