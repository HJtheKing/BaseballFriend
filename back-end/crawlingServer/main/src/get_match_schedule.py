import os
from dotenv import load_dotenv

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.support.ui import Select
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
import time

class GetMatchSchedule:
    def __init__(self):
        load_dotenv()
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
        self.matchScheduleURL = self.MATCH_LIVE_URL_PREFIX + '/Schedule/Schedule.aspx'

    def get_match_schedule(self, month):
        
        match_schedules = []
        
        # 경기 일정 페이지 열기
        self.driver.get(self.matchScheduleURL)
        
        # 정규 시즌 선택
        regular_series_tag = self.driver.find_element(By.XPATH, '//*[@id="ddlSeries"]/option[2]')
        regular_series_tag.click()
        
        # month 선택
        month_select_tag = Select(self.driver.find_element(By.ID, "ddlMonth"))
        month_select_tag.select_by_visible_text(str(month).zfill(2))
        
        # 경기 정보 받아옴
        schedule_table = self.driver.find_element(By.ID, "tblScheduleList").find_element(By.TAG_NAME, "tbody")
        match_schedule_rows = schedule_table.find_elements(By.TAG_NAME, "tr")
        
        match_date = None
        for tr in match_schedule_rows:
            match_schedule = {}
            tds = tr.find_elements(By.TAG_NAME, "td")

            match_schedule['location'] = tds[-2].text
            match_result = None
            for td in tds:
                if "day" in td.get_attribute("class"):
                    match_date = td.text
                    
                if "time" in td.get_attribute("class"):
                    match_schedule['matchTime'] = td.text
                if "play" in td.get_attribute("class"):
                    match_play_span_tag = td.find_elements(By.TAG_NAME, "span")
                    match_schedule['teamAwayName'] = match_play_span_tag[0].text
                    match_schedule['teamHomeName'] = match_play_span_tag[-1].text
                    match_play_span_em_tag = td.find_element(By.TAG_NAME, "em")
                    if len(match_play_span_em_tag.find_elements(By.TAG_NAME, "span")) >= 3:
                        away_score = int(match_play_span_em_tag.find_elements(By.TAG_NAME, "span")[0].text)
                        home_score = int(match_play_span_em_tag.find_elements(By.TAG_NAME, "span")[2].text)
                        
                        match_schedule['awayScore'] = away_score
                        match_schedule['homeScore'] = home_score

                        if away_score < home_score:
                            match_result = 1 # 홈팀 승
                        elif away_score > home_score:

                            match_result = 3 # 어웨이팀 승
                        elif away_score == home_score:
                            match_result = 2 # 무승부
                    else:
                        match_schedule['awayScore'] = 0
                        match_schedule['homeScore'] = 0
                        if tds[-1].text == "-" : match_result = 0
                        elif "취소" in tds[-1].text: match_result = 4
                    match_schedule['matchResult'] = match_result
            match_schedule['matchDate'] = match_date          
            match_schedules.append(match_schedule)
        return match_schedules
    
if __name__ == "__main__":
    
    start = time.time()
    match_schedules = getter.get_match_schedule2(9)
    end = time.time()
    
    for index, match_schedule in enumerate(match_schedules):
        print(f"{index + 1}: {match_schedule}")
    print(f"{end-start:.5f} sec")
