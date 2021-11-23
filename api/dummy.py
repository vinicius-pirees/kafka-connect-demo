from flask import Flask
from flask import request
from flask import json
from random_word import RandomWords
import random



app = Flask(__name__)



class User:
    def __init__(self):
        self.current_user_id=0
        self.r = RandomWords()
        self.names = ['Alexandre','Eduardo','Henrique','Murilo',
                        'Theo','André','Enrico','Henry',
                        'Nathan','Thiago','Antônio','Enzo',
                        'Ian','Otávio','Thomas','Agatha','Camila','Esther','Isis',
                        'Maitê','Natália','Alícia','Carolina',
                        'Fernanda','Joana','Malu','Nicole']

    def set_next(self, id):
        self.current_user_id=int(id)+1

    def get_current_user_id(self):
        return self.current_user_id






@app.route("/")
def hello_world():
    return "<p>Dummy API</p>"



@app.route("/users")
def user():
    last_user = request.args.get('from_id')
    user.set_next(last_user)
    current_id = user.get_current_user_id()

    users = []
    for i in range(0,5):
        name_position = random.randint(0,len(user.names)-1)
        user_data = {"name": user.names[name_position], "id": int(current_id)}
        users.append(user_data)
        current_id+=1


    response = app.response_class(
        response=json.dumps({"users": users}),
        status=200,
        mimetype='application/json'
    )
    return response




if __name__ == '__main__':
    user = User()
    app.run(host='0.0.0.0', port=8080)   