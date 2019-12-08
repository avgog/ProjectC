// NODEJS MODULES //
const express = require('express');
const fs = require('fs');
const bodyparser = require('body-parser');
const crypto = require('crypto');

// IMPORTING FILES //
const functions = require("./functions.js");
const app_settings = JSON.parse( fs.readFileSync(__dirname + "/settings/unsafe.json") );
const db = require(__dirname + "/db.js");

// SETTING UP EXPRESS SERVER //
var app = express();
  app.use(bodyparser.json());
  app.use(bodyparser.urlencoded({ extended: false }));

// INTERNAL USE ONLY //
app.post('/query', function(req, res) {
  let data = req.body;
  let query = data.query;
    
  console.log(Object.keys(data));
  console.log(query);
  
  db.exec(query,res);
});

// USED IN OVNOTIFIER SERVICE //
app.get('/routes', function(req, res) {
  let data = req.body;
  let route = data.route
  let query = "SELECT * FROM Routes WHERE route_id = '" + route + "';";
  db.exec(query,res);
});

app.get('/times', function(req, res) {
  let query = "SELECT * FROM Times;";
  db.exec(query,res);
});

app.get('/time', function(req, res) {
  let data = req.body;
  let time_id = data.id;
  let query = "SELECT * FROM Times WHERE id='" + time_id + "';";
  db.exec(query,res);
});

app.get('/stops', function(req, res) {
  let data = req.body;
  let stop = data.stop;
  let town = data.town;
  let query = "SELECT * FROM Stops WHERE name = '" + stop + "' AND town = '" + town + "';";
  db.exec(query,res);
});

app.post('/starttime', function(req, res) {
  let data = req.body;
  let id = data.id;
  let time = data.time;
  let query = "UPDATE Times SET timeofstart = '" + time + "' WHERE id = '" + id + "';";
  db.exec(query,res);
});


// PUBLIC ACCESSABLE //
app.get('/public/route', function(req, res) {
  let data = req.body;
  let from = data.from;
  let to = data.to;
  let date = data.date;
  let time = data.time;
  let type = data.type; /* normal / full / int */
});

app.post('/public/auth', function(req, res){
  let data = req.body;
  let username = data.username;
  let password = data.password;
  let string = username + "^63@431%32=21432*8421345fd2sSqla" + password;
  let auth_token = crypto.createHash('sha256').update(string).digest('hex');
  //let db_entry = db.execInternalResponse("SELECT user_id AS id, auth_token FROM Users WHERE username='" + username + "' AND password='" + password + "';");
  let queryDatabase = db.execInternalResponse("UPDATE Users set auth_token='" + auth_token + "' WHERE username='" + username + "' AND password='" + password + "';")
  let db_entry = db.execInternalResponse("SELECT user_id AS id, auth_token FROM Users WHERE username='" + username + "' AND password='" + password + "';");
  db_entry.then(function(output) {
    console.log("---");
    console.log("[]");
    console.log(typeof []);
    console.log("---");
    console.log(output);
    console.log(typeof output);
    console.log("---");
    
    if ( output.length > 0 ){
      if ( JSON.parse(JSON.stringify(output[0])) != JSON.parse("{}") ) {
        res.send(output);
      } else {
        res.send("{ \"ERROR\": \"Unauthorized\" }");
      }
    } else {
        res.send("{ \"ERROR\": \"Unauthorized\" }");
      }
  });
});


// AUDI //
//list of API url's and query indices
const requests = [
  {url:"/public/routes/add", queryIndex:functions.Query.ROUTE_ADD},
  {url:"/public/routes/get/from_id", queryIndex:functions.Query.ROUTE_GET_BY_ID},
  {url:"/public/routes/get/from_user", queryIndex:functions.Query.ROUTE_GET_BY_USER},
  {url:"/public/routes/change/start_point", queryIndex:functions.Query.ROUTE_CHANGE_START},
  {url:"/public/routes/change/end_point", queryIndex:functions.Query.ROUTE_CHANGE_END},
  {url:"/public/routes/change/route_name", queryIndex:functions.Query.ROUTE_CHANGE_NAME},
  {url:"/public/routes/remove", queryIndex:functions.Query.ROUTE_REMOVE},
  {url:"/public/times/get/from_id", queryIndex:functions.Query.TIME_GET_BY_ID},
  {url:"/public/times/get/from_route", queryIndex:functions.Query.TIME_GET_BY_ROUTE},
  {url:"/public/times/add", queryIndex:functions.Query.TIME_ADD},
  {url:"/public/times/change/time", queryIndex:functions.Query.TIME_CHANGE_TIME},
  {url:"/public/times/remove", queryIndex:functions.Query.TIME_REMOVE}
];

function defaultCallback(err, res,callbackData){
    if(!err){
        err=""
    }
    else{
      console.log("callback error: " + err)
    }
    if(!res){
        res=[]
    }
    callbackData.res.send({"error":err, "result":res})
}

function checkToken(token, res, user_id, callback){ //checks if the authentication token is valid
  console.log("checktoken/user_id = " + user_id);
  if(isNaN(parseInt(user_id))){
    user_id = -1;
  }
  let query = "SELECT auth_token FROM Users WHERE user_id=" + user_id + ";";
  let entry = db.execInternalResponse(query);
  entry.then(function(result){
    if(result.length > 0){
      let dbToken = result[0].auth_token;
      console.log("received token: " + token + ", stored token: " + dbToken);
      //Check if the token from the database isn't empty (which can happen if the user of that user_id hasn't logged in)
      //Then compare it to the token received from the request.
      if(dbToken != null && dbToken != "" && token == dbToken){
        console.log("valid token, executing callback...");
        callback();
      }
      else{
        console.log("token does not belong to the corresponding user_id, callback won't be executed.");
        res.send({"error":"invalid token", "result":""})
      }
    }
    else{
      console.log("this user ("+user_id+") has no token stored in the database, callback won't be executed.");
      res.send({"error":"invalid token", "result":""})
    }
  });
}

for(let i = 0; i < requests.length; i++){
  app.post(requests[i].url, (req,res) => {
    checkToken(req.body.token, res, req.body.user_id, () => {
      console.log("got post request [url: '" + req.url +"']")
      let callbackObject = {"callback":defaultCallback, "data":{res}}
      functions.executeAPIQuery(requests[i].queryIndex, callbackObject, req.body);
    });
  });
}

//use this to test the connection between the client and server
app.get("/", (req, res) => {
    res.send({"error":"", "result":"callback from server"})
});


// START NODEJS SERVER //
app.listen(666, function () {
  console.log('Ready...');
});

