import os
from dotenv import load_dotenv

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
import time

class FindURL:
    def __init__(self, matchTime, location):
        load_dotenv()
        self.KBO_ID = os.getenv("KBOID")
        self.KBO_PW = os.getenv("KBOPW")
        self.MATCH_LIVE_URL_PREFIX = os.getenv("MATCH_LIVE_URL_PREFIX")

        # Chrome 옵션 설정
        chrome_options = Options()
        # chrome_options.add_argument('--headless')  ###### 헤드리스 모드 - 서버에서 띄울 땐 주석 제거
        chrome_options.add_argument('--no-sandbox')  # 옵션 추가: 리소스 문제 방지
        chrome_options.add_argument('--disable-dev-shm-usage')  # 옵션 추가: 메모리 문제 방지
        chrome_options.set_capability("goog:loggingPrefs", {"browser": "ALL"})  # 브라우저 로그 수집 활성화

        # Chrome 드라이버 설정
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)

        # url 설정
        self.loginURL = self.MATCH_LIVE_URL_PREFIX + '/Member/Login.aspx'
        self.matchURL = self.MATCH_LIVE_URL_PREFIX + '/Schedule/ScoreBoard.aspx'
        
        # 경기 시간, 위치 초기화
        self.matchTime = matchTime
        self.location = location

    def login_kbo(self):

        # 로그인 페이지 열기
        self.driver.get(self.loginURL)

        # 아이디 입력
        username_input = self.driver.find_element(By.ID, "cphContents_cphContents_cphContents_txtUserId")
        username_input.send_keys(self.KBO_ID)

        # 비밀번호 입력
        password_input = self.driver.find_element(By.ID, "cphContents_cphContents_cphContents_txtPassWord")
        password_input.send_keys(self.KBO_PW)

        # 확인 버튼 클릭
        login_button = self.driver.find_element(By.ID, "cphContents_cphContents_cphContents_btnLogin")
        login_button.click()

        # 잠시 대기 (로그인 후 페이지 로드 시간)
        time.sleep(3)

    def selct_match_date(self, month, day):
        # 달력 버튼 클릭
        calendar_button = self.driver.find_element(By.CLASS_NAME, "ui-datepicker-trigger")
        calendar_button.click()

        # ui-datepicker-calendar 테이블 찾기
        calendar_table = self.driver.find_element(By.CLASS_NAME, "ui-datepicker-calendar")

        # data-month가 month인 td 태그 찾기
        td_tags = calendar_table.find_elements(By.XPATH, f".//td[@data-month='{month-1}']")

        # 조건에 맞는 td 태그 찾기
        for td in td_tags:
            a_tag = td.find_element(By.TAG_NAME, "a")
            if a_tag and a_tag.text == str(day):  # a 태그의 값이 day인지 확인
                td.click()  # 조건에 맞는 td 태그 클릭
                print("클릭")
                return  # 클릭 후 메소드 종료

        print("조건에 맞는 td 태그를 찾을 수 없습니다.")  # 조건에 맞는 td가 없으면 메시지 출력

    def get_match_url(self):

        # 경기 정보 페이지 열기
        self.driver.get(self.matchURL)

        ## 오늘 경기가 없어서.. 임의로 날짜 지정
        self.selct_match_date(10, 28)
        time.sleep(2)
        
        # url 저장
        # cphContents_cphContents_cphContents_udpRecord ID를 가진 태그 찾기
        record_div = self.driver.find_element(
            By.ID, "cphContents_cphContents_cphContents_udpRecord"
        )
        print("record 찾음")

        # smsScore 클래스를 가진 모든 태그 찾기
        sms_score_tags = record_div.find_elements(By.CLASS_NAME, "smsScore")
        print(f"smsScore 태그 개수: {len(sms_score_tags)}")

        # 각 smsScore 태그를 순회
        if len(sms_score_tags) > 0 :
            for sms_score in sms_score_tags:
                
                place_tag = sms_score.find_element(By.CLASS_NAME, "place")
                place_text = place_tag.text.split(" ")[0]
                
                match_time = place_tag.find_element(By.TAG_NAME, 'span').text.strip()
                
                if( place_text == self.location and match_time == self.matchTime) :
                    btn_sms_tag = sms_score.find_element(By.CLASS_NAME, "btnSms")
                    a_tag = btn_sms_tag.find_element(By.TAG_NAME, "a")
                    if (
                       a_tag 
                       and a_tag.find_element(By.TAG_NAME, "img").get_attribute("alt")
                        == "문자중계보기"):
                        # window.open() 괄호 안의 URL 추출
                        onclick_value = a_tag.get_attribute("onclick")
                        url = onclick_value.split("'")[1]  # URL 추출
                        return self.MATCH_LIVE_URL_PREFIX + url
        return None
    
    
if __name__ == "__main__":

    # FindURL 클래스 인스턴스 생성
    finder = FindURL("18:30", "광주")

    # KBO 로그인
    finder.login_kbo()

    # 경기 URL 가져오기
    url = finder.get_match_url()
    print(f"{url}")