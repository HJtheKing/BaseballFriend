import os
from dotenv import load_dotenv

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
import time

class GetTeamRank:
    def __init__(self):
        load_dotenv()
        self.MATCH_LIVE_URL_PREFIX = os.getenv("MATCH_LIVE_URL_PREFIX")

        # Chrome 옵션 설정
        chrome_options = Options()
        chrome_options.add_argument('--headless')  ###### 헤드리스 모드 - 서버에서 띄울 땐 주석 제거
        chrome_options.add_argument('--no-sandbox')  # 옵션 추가: 리소스 문제 방지
        chrome_options.add_argument('--disable-dev-shm-usage')  # 옵션 추가: 메모리 문제 방지
        chrome_options.set_capability("goog:loggingPrefs", {"browser": "ALL"})  # 브라우저 로그 수집 활성화

        # Chrome 드라이버 설정
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)

        # url 설정
        self.teamRankURL = self.MATCH_LIVE_URL_PREFIX + '/Record/TeamRank/TeamRankDaily.aspx'
    
    def get_team_rank(self):
        
        team_ranks = []
        
        # 팀 순위 페이지 열기
        self.driver.get(self.teamRankURL)
        
        # 팀 순위 저장
        team_rank_table = self.driver.find_element(By.CLASS_NAME, "tData").find_element(By.TAG_NAME, "tbody")
        team_rank_rows = team_rank_table.find_elements(By.TAG_NAME, "tr")
                
        # tr을 순회하며 순위 정보 저장
        for tr in team_rank_rows:
            team_rank = {}
            tds = tr.find_elements(By.TAG_NAME, "td")
            team_rank['teamRank'] = tds[0].text
            team_rank['teamName'] = tds[1].text
            team_rank['winCount'] = tds[3].text
            team_rank['lossCount'] = tds[4].text
            team_rank['drawCount'] = tds[5].text
            team_rank['odds'] = tds[6].text
            team_rank['last10GamesResults'] = tds[8].text
            
            team_ranks.append(team_rank)
            
        return team_ranks
        
        
    
if __name__ == "__main__":

    getter = GetTeamRank()
    team_ranks = getter.get_team_rank()