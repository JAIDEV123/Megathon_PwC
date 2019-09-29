from flask import Flask, jsonify, json
from flask import render_template
from flask import request
import requests

app = Flask(__name__)

# lis = [[17.4486, 78.3908], [17.4399, 78.4983], [17.3616, 78.4747], [17.3833, 78.4011], [17.2403, 78.4294]]
lis_final = [
                {"place": "Madhapur", "cood": [17.4486, 78.3908]}, 
                {"place": "Secundarabad","cood": [17.4399, 78.4983]}, 
                {"place":"Chaar Minar", "cood": [17.3616, 78.4747]}, 
                {"place": "Golconda fort", "cood" :[17.3833, 78.4011]}, 
                {"place":"RGIA airport","cood": [17.2403, 78.4294]} 
            ]
# traffic = []'
rain  = []
i = 0

while i < 5:
    r = requests.get("https://api.tomtom.com/traffic/services/4/flowSegmentData/absolute/10/json?point="+str(lis_final[i]["cood"][0])+'%2C'+str(lis_final[i]["cood"][1])+"&key=JgWD08Svc7mh4qtvXAYBtwyrg5CHfRsv")
    x = r.json()
    x = x["flowSegmentData"]["currentSpeed"]
    lis_final[i]["traffic"] = x
    i += 1
        
@app.route("/")
def home():
    return render_template('home.html', lis_final = lis_final)

@app.route('/', methods = ['GET', 'POST'])
def new():
    if request.method == 'POST':
        i = 0
        ans = 0
        ans_max = 0
        while i < 5:
            rain.append(request.form.get('a'+ str(i+1)))
            print(rain[i])
            if i == 0:
                ans = i
                ans_max = int(rain[i])/2 - int(lis_final[i]["traffic"])/2

            if int(rain[i])/2 - int(lis_final[i]["traffic"])/2 > ans_max:
                ans = i
                ans_max = int(rain[i])/2 - int(lis_final[i]["traffic"])/2

            i += 1 

    print(ans, ans_max)
    #return render_template('home.html', lis_final = lis_final)
    oof = str(lis_final[ans]["cood"][0]) + ', ' + str(lis_final[ans]["cood"][1])
    return render_template('details.html', city = lis_final[ans]["place"], cood = lis_final[ans]["cood"], rain = rain[ans], traffic = int(lis_final[ans]["traffic"]), oof = oof)


if __name__ == '__main__':
	app.run(debug=True)