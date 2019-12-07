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
  {url:"/public/routes/add", query:functions.Query.ROUTE_ADD},
  {url:"/public/routes/get/from_id", query:functions.Query.ROUTE_GET_BY_ID},
  {url:"/public/routes/get/from_user", query:functions.Query.ROUTE_GET_BY_USER},
  {url:"/public/routes/change/start_point", query:functions.Query.ROUTE_CHANGE_START},
  {url:"/public/routes/change/end_point", query:functions.Query.ROUTE_CHANGE_END},
  {url:"/public/routes/change/route_name", query:functions.Query.ROUTE_CHANGE_NAME},
  {url:"/public/routes/remove", query:functions.Query.ROUTE_REMOVE},
  {url:"/public/times/get/from_id", query:functions.Query.TIME_GET_BY_ID},
  {url:"/public/times/get/from_route", query:functions.Query.TIME_GET_BY_ROUTE},
  {url:"/public/times/add", query:functions.Query.TIME_ADD},
  {url:"/public/times/change/time", query:functions.Query.TIME_CHANGE_TIME},
  {url:"/public/times/remove", query:functions.Query.TIME_REMOVE}
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

function checkToken(token, user_id, callback){ //checks if the authentication token is valid
  console.log("checktoken/user_id = " + user_id);
  if(isNaN(parseInt(user_id))){
    user_id = -1;
  }
  let query = "SELECT auth_token FROM Users WHERE user_id=" + user_id + ";";
  let entry = db.execInternalResponse(query);
  entry.then(function(result){
    console.log("result:");
    console.log(result);
    console.log("-------");
  });
}

for(let i = 0; i < requests.length; i++){
  app.post(requests[i].url, (req,res) => {
    checkToken(req.body.token, req.body.user_id, () => {
      let callbackObject = {"callback":defaultCallback, "data":{res}}
      functions.executeAPIQuery(requests[i].query, callbackObject, req.body);
    });
  });
}

//use this to test the connection between the client and server
app.get("/", (req, res) => {
    res.send({"error":"", "result":"callback from server"})
});


/*app.post("/public/routes/add", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    checkToken(req.body.token, req.body.user_id, () => {
      functions.add_route(req.body.user_id, req.body.start_point, req.body.end_point, req.body.route_name, callbackObject)
    });
});

app.post("/public/routes/get/from_id", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.get_route(req.body.route_id, callbackObject)
});

app.post("/public/routes/get/from_user", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.get_user_routes(req.body.user_id, callbackObject)
});

app.post("/public/routes/change/start_point", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.change_start_point(req.body.route_id, req.body.start_point, callbackObject)
});

app.post("/public/routes/change/end_point", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.change_end_point(req.body.route_id, req.body.end_point, callbackObject)
});

app.post("/public/routes/change/route_name", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.change_route_name(req.body.route_id, req.body.route_name, callbackObject)
});

app.post("/public/routes/remove", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.remove_route(req.body.route_id, callbackObject)
});



app.post("/public/times/get/from_id", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.get_time(req.body.time_id, callbackObject)
});

app.post("/public/times/get/from_route", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.get_route_times(req.body.route_id, callbackObject)
});

app.post("/public/times/add", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.add_time(req.body.route_id, req.body.date, req.body.end_time, callbackObject)
});

app.post("/public/times/change/time", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.change_time(req.body.time_id, req.body.date, req.body.end_time, callbackObject)
});

//app.post("/public/times/change/time", (req, res) => {
//    console.log("got post request [url: '" + req.url +"']")
//    let callbackObject = {"callback":defaultCallback, "data":{res}}
//    functions.change_time(req.body.time_id, req.body.date, req.body.end_time, callbackObject)
//});

app.post("/public/times/remove", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    functions.remove_time(req.body.time_id, callbackObject)
});*/


// START NODEJS SERVER //
app.listen(666, function () {
  console.log('Ready...');
});

