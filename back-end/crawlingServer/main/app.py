import os
from flask import Flask, jsonify, request
from src.detect import crawl_game
from src.find_url import FindURL
from src.get_team_rank import GetTeamRank
from src.get_match_schedule import GetMatchSchedule
from dotenv import load_dotenv
from src.request_http import RequestToServer

app = Flask(__name__)

@app.route('/')
def home():
   return 'This is Home!'

@app.route('/flask/bf/match', methods=["POST"])
def startBroadcast():
   data = request.get_json()

   location = data.get('location')
   matchTime = data.get('matchTime')
   
   # 받은 location과 matchTime을 바탕으로 url추출 및 중계 시작
   # finder = FindURL(matchTime, location)
   # finder.login_kbo()
   # url = finder.get_match_url()
   load_dotenv()
   match_live_url = os.getenv("LIVE_URL")

   # print(f"{match_live_url}")

   # 중계 시작
   result = crawl_game(match_live_url)

   result["matchTime"] = matchTime
   result["location"] = location

   # 결과 반환
   RequestToServer().request_save_match_result(result)

   return jsonify({"status": "success", "location": location, "matchTime": matchTime})

@app.route('/flask/bf/match/rank', methods=["POST"])
def getTeamRank():
   getter = GetTeamRank()
   team_ranks = getter.get_team_rank()
   print(team_ranks)

   return team_ranks

@app.route('/flask/bf/match/schedule', methods=["POST"])
def getMathSchedule():
   data = request.get_json()
   month = data.get('month')
   
   getter = GetMatchSchedule()
   match_schedules = getter.get_match_schedule(month)
   print(match_schedules)

   return match_schedules
   
if __name__ == '__main__':  
   app.run('0.0.0.0',port=5000,debug=True)