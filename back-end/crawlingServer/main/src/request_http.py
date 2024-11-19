import os
from dotenv import load_dotenv
import requests

class RequestToServer:
    def __init__(self):
        load_dotenv()
        self.match_result_url = os.getenv("RESULT_URL")
        
    def request_save_match_result(self, result):
        if result["type"] == 0:
            del result['type']
            result['matchResult'] = util.calc_match_result(result['homeScore'], result['awayScore'])
            requests.patch(self.match_result_url, json=result)

        elif result["type"] == 1:
            del result['type']
            result['matchResult'] = 5
            requests.post(self.match_result_url, json=result)

class util:
    def calc_match_result(homeScore, awayScore):
        if homeScore > awayScore: return 1
        if homeScore == awayScore: return 2
        if homeScore < awayScore: return 3