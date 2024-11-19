import asyncio
import json
import os
import threading
from cgitb import strong
from contextlib import nullcontext
from datetime import datetime
from time import sleep

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
import time
import re
from kafka import KafkaProducer
import logging

from static.teams import teams

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')

# 환경 변수로 Kafka 서버 주소를 설정 (기본값은 localhost:9092)
kafka_server = os.getenv('KAFKA_SERVER', 'localhost:9092')

# KafkaProducer 초기화
producer = KafkaProducer(
        bootstrap_servers=[kafka_server],
    value_serializer=lambda v: json.dumps(v, ensure_ascii=True).encode('utf-8')
)

def send_start_alert(away_id, home_id):
    logging.info("sending start alert")
    # await asyncio.sleep(5)
    publish_data(13, 'alert.' + away_id)
    publish_data(13, 'alert.' + home_id)

def publish_data(data, team_id):
    producer.send(team_id, data)
    producer.flush()
    logging.info(f"data published : {data}")

def crawl_game(url):
    ## 더미데이터 생생용
    # dummy_data = []

    ### 경기 결과 전송용 ###
    result = {}

    # Chrome 옵션 설정
    chrome_options = Options()
    chrome_options.add_argument('--headless')  ###### 헤드리스 모드 - 서버에서 띄울 땐 주석 제거
    chrome_options.add_argument('--no-sandbox')  # 옵션 추가: 리소스 문제 방지
    chrome_options.add_argument('--disable-dev-shm-usage')  # 옵션 추가: 메모리 문제 방지
    chrome_options.set_capability("goog:loggingPrefs", {"browser": "ALL"})  # 브라우저 로그 수집 활성화

    # Chrome 드라이버 설정
    driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)

    # 웹 페이지 열기
    driver.get(url)

    mutation_script = """
    // smsRight 요소를 선택합니다.
    const smsRightElement = document.getElementById('smsRight');

    // MutationObserver 설정
    const observer = new MutationObserver((mutationsList) => {
      if (mutationsList.length > 0) {
        console.log("change detected");
      }
    });

    // observer에 감지할 변경사항 설정
    const config = {
      childList: true,        // 자식 노드 추가/삭제를 감지
      subtree: true          // 하위 트리의 모든 요소도 감지
    };

    // smsRight 요소에 MutationObserver 적용
    observer.observe(smsRightElement, config);

    // 경기 시작 전 홈, 어웨이 정보 획득 위해 인위적 변경
    const tempDiv = document.createElement('div');
    tempDiv.style.display = 'none';

    // 5초 후에 투명 div를 추가했다가 1초 후에 제거
    setTimeout(() => {
        smsRightElement.appendChild(tempDiv);
        setTimeout(() => {
            smsRightElement.removeChild(tempDiv);
        }, 1000);  // 1초 후 제거
    }, 3000);  // 3초 후 추가
    """

    # Selenium으로 JavaScript 실행
    driver.execute_script(mutation_script)

    wait = WebDriverWait(driver, 20)
    wait.until(EC.presence_of_element_located((By.ID, "tblScoreBoard1")))

    # 팀 명 가져오기
    first_html = driver.page_source
    soup = BeautifulSoup(first_html, 'html.parser')

    away_name_tag = soup.find("th", {"id": "rptScoreBoard1_thTeamName_0"})
    away_name = away_name_tag.text.strip() if away_name_tag else "어웨이 팀 정보 없음"  ###### 어웨이 팀 명

    home_name_tag = soup.find("th", {"id": "rptScoreBoard1_thTeamName_1"})
    home_name = home_name_tag.text.strip() if home_name_tag else "홈 팀 정보 없음"  ###### 홈 팀 명

    # loop = asyncio.new_event_loop()  # 새로운 이벤트 루프 생성
    # asyncio.set_event_loop(loop)  # 스레드에 해당 루프를 설정
    # # `send_start_alert`를 백그라운드에서 비동기 실행
    # task = loop.create_task(send_start_alert(teams.get(away_name), teams.get(home_name)))
    # # `crawl_game`의 나머지 코드가 동작할 수 있도록 이벤트 루프를 백그라운드에서 실행
    # loop.run_until_complete(asyncio.sleep(0))  # 이벤트 루프를 잠시 실행하여 비동기 작업 시작

    timer = threading.Timer(0, send_start_alert, args=(teams.get(away_name), teams.get(home_name)))
    timer.start()

    prev_inning_cnt = 0
    prev_span_cnt = 0
    hitter = ""  ###### 타자 정보

    prev_home_score = 0
    prev_away_score = 7
    is_away_attack = False

    try:
        # 무한 루프에서 콘솔 로그 확인 및 데이터 가져오기
        while True:
            # 브라우저 로그 가져오기
            logs = driver.get_log("browser")
            for log in logs:
                if "change detected" in log["message"]:  # 데이터 변경이 감지 되었을 때만 서버에 데이터를 요청
                    for i in range(3):
                        page_html = driver.page_source
                        soup = BeautifulSoup(page_html, 'html.parser')

                        ## 더미데이터 생성 로직
                        # html_data = {
                        #     "time" : datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                        #     "html" : soup.prettify(),
                        # }
                        # dummy_data.append(html_data)

                        home_alert_data = []
                        away_alert_data = []

                        # 스코어 가져오기
                        score_table = soup.find("table", {"id": "tblScoreBoard3"})
                        away_score = home_score = -1
                        if score_table:
                            tbody = score_table.find("tbody")
                            if tbody:
                                rows = tbody.find_all("tr")

                                points = []
                                for row in rows:
                                    point = row.find("td", {"class": "point"})
                                    if point:
                                        points.append(point.text.strip())
                                if len(points) > 1:
                                    away_score = points[0]  ###### 어웨이 팀 점수
                                    home_score = points[1]  ###### 홈 팀 점수

                                    if int(away_score) > prev_away_score:
                                        away_alert_data.append(11)
                                        home_alert_data.append(12)
                                        prev_away_score = int(away_score)
                                    if int(home_score) > prev_home_score:
                                        home_alert_data.append(11)
                                        away_alert_data.append(12)
                                        prev_home_score = int(home_score)

                        inning_number = balls = strikes = outs = -1
                        span_base = soup.find("span", {"class": "base"})
                        if span_base:
                            strong_tags = span_base.find_all("strong")
                            if len(strong_tags) >= 2:
                                bs = strong_tags[1].text.strip().split(" ")[0].strip().split("-")
                                balls = int(bs[0])
                                strikes = int(bs[1])
                                outs_tmp = strong_tags[1].text.strip().split(" ")[1]
                                outs = int(re.search(r"\d+", outs_tmp).group())

                                inning_number = re.search(r"\d+", strong_tags[0].text)
                                if inning_number:
                                    inning_number = min(int(inning_number.group()), 10)  ###### 이닝 숫자
                                    if inning_number != prev_inning_cnt:
                                        prev_span_cnt = 0
                                        prev_inning_cnt = inning_number
                            else:
                                print("#1 이닝 정보를 찾을 수 없습니다.")
                        else:
                            print("#2 이닝 정보를 찾을 수 없습니다.")

                        bases = [0, 0, 0]
                        # 'ul' 태그의 'playerName' 클래스를 가진 요소를 찾음
                        player_name_ul = soup.find("div", {"class": "playerName"})
                        if player_name_ul:
                            # 'li' 태그 중에서 class가 'typing'으로 시작하는 태그들을 모두 찾음
                            typing_lis = player_name_ul.find_all("li", class_=re.compile("^typing"))
                            # 클래스 이름에서 숫자를 추출
                            for li in typing_lis:
                                base = int(re.search(r"\d+", li["class"][0]).group()) - 1
                                if 0 <= base < 3:
                                    bases[base] = 1

                        resume_date = None

                        text_logs = {}
                        if (inning_number > 0):
                            div_id = f"numCont{inning_number}"
                            num_count_div = soup.find("div", {"id": div_id})
                            if num_count_div:
                                span_elements = num_count_div.find_all("span")
                                span_cnt = len(span_elements)

                                if span_cnt > prev_span_cnt:
                                    new_span_cnt = span_cnt - prev_span_cnt
                                    new_span_elements = span_elements[:new_span_cnt]

                                    # 데이터 가공에 사용하기 위한 배열
                                    text_list = []

                                    for span in new_span_elements:
                                        span_text = span.get_text(strip=True)
                                        if span_text and not span_text.startswith('-'):
                                            text_list.append(span.text.strip())

                                    ##### 로직 수행 #####
                                    cnt = 1
                                    for text in reversed(text_list):
                                        ### 공격 팀
                                        if away_name + " 공격" in text:
                                            is_away_attack = True
                                        elif home_name + " 공격" in text:
                                            is_away_attack = False

                                        ### 타자 갱신 ###
                                        elif "번타자" in text:
                                            match = re.search(r"대타\s+([가-힣]+)", text)
                                            hitter = match.group(1) if match else text.split(" ")[1]

                                        ### 타자 수행 동작 ###
                                        elif hitter == text.split(":")[0].strip():
                                            hitter_status = text.split(":", 1)[1] if "홈런" in text else text.split(":")[1].strip()

                                            if "루타" in hitter_status:  ### 안타 알람
                                                if is_away_attack:
                                                    away_alert_data.append(8)
                                                else:
                                                    home_alert_data.append(8)
                                            elif "홈런" in hitter_status:  ### 홈런 알람
                                                if is_away_attack:
                                                    away_alert_data.append(9)
                                                else:
                                                    home_alert_data.append(9)
                                            elif "삼진" in hitter_status:  ### 삼진 알람
                                                if is_away_attack:
                                                    home_alert_data.append(10)
                                                else:
                                                    away_alert_data.append(10)

                                            text_logs["log" + str(cnt)] = hitter_status
                                            cnt += 1

                                        ### 주자 수행 동작 ###
                                        elif "루주자" in text and "대주자" not in text:  # 대주자 스킵
                                            # 'n루주자' 가져옴
                                            base_info = text.split(":")[0].strip().split(" ")[0]
                                            action = text.split(":", 1)[1].strip()
                                            runner_status = f"{base_info} {action}"
                                            text_logs["log" + str(cnt)] = runner_status
                                            cnt += 1

                                        elif "경기종료" in text:
                                            logging.info("경기 종료 발견")
                                            text_logs["log" + str(cnt)] = "경기종료"
                                            break
                                        elif "서스펜디드" in text:
                                            logging.info("경기 중단 발견")
                                            text_logs["log" + str(cnt)] = "경기중단"
                                            match = re.search(r'(\d{2}/\d{2})\(.+?\) (\d{2}:\d{2})', text)
                                            if match:
                                                current_year = datetime.now().year
                                                date_str = match.group(1)
                                                time_str = match.group(2)

                                                datetime_str = f"{current_year}/{date_str} {time_str}"
                                                ### 경기 재개 날짜 ###
                                                resume_date = datetime.strptime(datetime_str, "%Y/%m/%d %H:%M")
                                                logging.info(f"match_datetime : {resume_date}")
                                            break

                                prev_span_cnt = span_cnt

                        if len(away_alert_data) > 0:
                            publish_data(away_alert_data, 'alert.' + str(teams.get(away_name)))
                        if len(home_alert_data) > 0:
                            publish_data(home_alert_data, 'alert.' + str(teams.get(home_name)))

                        ### 문자 중계 데이터 전송
                        data = {
                            "awayName": away_name,
                            "homeName": home_name,
                            "awayScore": away_score,
                            "homeScore": home_score,
                            "inningNumber": inning_number,
                            "balls": balls,
                            "strikes": strikes,
                            "outs": outs,
                            "bases": bases,
                            "textLogs": text_logs
                        }
                        if away_score == -1 or home_score == -1 or inning_number == -1 or balls == -1 or strikes == -1 or outs == -1:
                            ### 데이터 전송 실패
                            logging.warning(f"Invalid data : {data}")
                            time.sleep(1)
                        else:
                            ### 데이터 전송 실행
                            publish_data(data, teams.get(away_name))
                            publish_data(data, teams.get(home_name))

                            last_log = data["textLogs"].get("log" + str(len(text_logs)))

                            if last_log == "경기종료":

                                result["type"] = 0
                                result["homeScore"] = home_score
                                result["awayScore"] = away_score
                                logging.info(f'result : {result}')

                                # loop.run_until_complete(task)
                                # loop.close()
                                timer.join()
                                return result

                            elif last_log == "경기중단":

                                result["type"] = 1
                                result["resumeDate"] = resume_date
                                logging.info(f'result : {result}')

                                # loop.run_until_complete(task)
                                # loop.close()
                                timer.join()
                                return result
                            break
                    break
            time.sleep(1)  # 매 초마다 콘솔 로그 체크
    except KeyboardInterrupt:
        print("감시 종료")
    finally:
        ### 더미데이터 저장 로직
        # file_path = "dummy_data.json"
        # with open(file_path, "w", encoding="utf-8") as file:
        #     json.dump(dummy_data, file, ensure_ascii=False, indent=4)
        driver.quit()


if __name__ == "__main__":

    # FindURL 클래스 인스턴스 생성
    # finder = FindURL()

    # KBO 로그인
    # finder.login_kbo()

    # 경기 URL 가져오기
    # urls = finder.get_match_url()

    # URLs 출력
    # for index, url in enumerate(urls):
    #     print(f"{index + 1}: {url}")

    urls = [
        'https://www.koreabaseball.com/Game/LiveText.aspx?leagueId=1&seriesId=7&gameId=20241021SSHT0&gyear=2024'
        # 'https://www.koreabaseball.com/Game/LiveText.aspx?leagueId=1&seriesId=7&gameId=20241026HTSS0&gyear=2024',
        # "http://localhost:3000",
        # 'http://k11a505.p.ssafy.io:3000'
    ]

    threads = []

    for url in urls:
        thread = threading.Thread(target=crawl_game, args=(url,))
        thread.start()
        threads.append(thread)

    for thread in threads:
        thread.join()
