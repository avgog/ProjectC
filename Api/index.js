// NODEJS MODULES //
const express = require('express');
const fs = require('fs');
const bodyparser = require('body-parser');
const crypto = require('crypto');

// IMPORTING FILES //
const functions = require(__dirname + "/functions.js");
const app_settings = JSON.parse( fs.readFileSync(__dirname + "/settings/unsafe.json") );
const db = require(__dirname + "/db.js");

// SETTING UP EXPRESS SERVER //
var app = express();
  app.use(bodyparser.json());
  app.use(bodyparser.urlencoded({ extended: false }));

/**
 * @api {post} /query/ Execute query and return output in json format.
 * @apiName Query
 * @apiGroup Internal
 * @apiParam {String} query De query die moet worden uitgevoerd.
*/

app.post('/query', function(req, res) {
  let data = req.body;
  let query = data.query;
    
  console.log(Object.keys(data));
  console.log(query);
  
  db.exec(query,res);
});

/**
 * @api {get} /routes/ Return a specific route in json format.
 * @apiName Routes
 * @apiGroup Internal
 * @apiParam {String} route The id of the route

 * @apiSuccess {Integer} route_id The id of the route.
 * @apiSuccess {String} start The location of department.
 * @apiSuccess {String} end The location of arrival.
 * @apiSuccess {Integer} user_id The id of the user that owns the route.
 * @apiSuccess {String} route_name The name of the route.
 * @apiSuccessExample Success-Response:
 * [
 *  {
 *    "route_id": 1,
 *    "start": "Sportcentrum de driesprong, Zoetermeer",
 *    "end": "Nesselande, Rotterdam",
 *    "user_id": 1,
 *    "route_name": "Mijn route test"
 *  }
 * ]
*/

app.get('/routes', function(req, res) {
  let data = req.body;
  let route = data.route
  let query = "SELECT * FROM Routes WHERE route_id = '" + route + "';";
  db.exec(query,res);
});

/**
 * @api {get} /times/ Return all timeschemes
 * @apiName Times
 * @apiGroup Internal

 * @apiSuccess {Integer} id The id of the timescheme
 * @apiSuccess {Integer} route_id The id of the route of this timescheme.
 * @apiSuccess {String} timeofarrival The time of the arrival.
 * @apiSuccess {String} timeofstart The time of the department.
 * @apiSuccess {String} date Day or date the timescheme is active.
 * @apiSuccess {Integer/null} last_checked The last time (epoch format) when the time you have to leave is calculated.
 * @apiSuccess {Integer/null} notified The time (epoch format) you have been notified. (could be null)
 * @apiSuccessExample Success-Response:
 * [
 *   {
 *    "id": 68,
 *    "route_id": 34,
 *    "timeofarrival": "04:42",
 *    "timeofstart": "03:45",
 *    "date": "Wednesday",
 *    "last_checked": "1577657898",
 *    "notified": null
 *  }
 *]
*/

app.get('/times', function(req, res) {
  let query = "SELECT * FROM Times;";
  db.exec(query,res);
});

/**
 * @api {get} /time/ Return a specific timescheme
 * @apiName Time
 * @apiGroup Internal
 * @apiParam {String} id The uniq id of the timescheme.

 * @apiSuccess {Integer} id The id of the timescheme
 * @apiSuccess {Integer} route_id The id of the route of this timescheme.
 * @apiSuccess {String} timeofarrival The time of the arrival.
 * @apiSuccess {String} timeofstart The time of the department.
 * @apiSuccess {String} date Day or date the timescheme is active.
 * @apiSuccess {Integer/null} last_checked The last time (epoch format) when the time you have to leave is calculated.
 * @apiSuccess {Integer/null} notified The time (epoch format) you have been notified. (could be null)
 * @apiSuccessExample Success-Response:
 * [
 *   {
 *    "id": 68,
 *    "route_id": 34,
 *    "timeofarrival": "04:42",
 *    "timeofstart": "03:45",
 *    "date": "Wednesday",
 *    "last_checked": "1577657898",
 *    "notified": null
 *  }
 *]
*/

app.get('/time', function(req, res) {
  let data = req.body;
  let time_id = data.id;
  let query = "SELECT * FROM Times WHERE id='" + time_id + "';";
  db.exec(query,res);
});

/**
 * @api {get} /stops/ Return the known data of a stop.
 * @apiName Stops
 * @apiGroup Internal
 * @apiParam {String} stop The name of the stop.
 * @apiParam {String} town The town the stop is.

 * @apiSuccess {Integer} stop_id The unique id of the stop.
 * @apiSuccess {String} town The town where the stop is.
 * @apiSuccess {String} name The name of the stop.
 * @apiSuccess {String} stopareacode The stopcode used in ovdata.
 * @apiSuccess {String} longitude The y-axis of the stop.
 * @apiSuccess {String} latitude The x-axis of the stop.
 * @apiSuccessExample Success-Response:
 * [
 *   {
 *     "stop_id": 4,
 *     "town": "Rotterdam",
 *     "name": "Beurs",
 *     "stopareacode": "RtBeu",
 *     "longitude": "4.4812500",
 *     "latitude": "51.9182589"
 *   }
 * } 
*/

app.get('/stops', function(req, res) {
  let data = req.body;
  let stop = data.stop;
  let town = data.town;
  let query = "SELECT * FROM Stops WHERE name = '" + stop + "' AND town = '" + town + "';";
  db.exec(query,res);
});

/**
 * @api {post} /starttime/ Set a time of department for a timescheme.
 * @apiName Starttime
 * @apiGroup Internal
 * @apiParam {Integer} id The id of the timescheme.
 * @apiParam {String} time The time of the timescheme in 24 hours format.
*/

app.post('/starttime', function(req, res) {
  let data = req.body;
  let id = data.id;
  let time = data.time;
  let query = "UPDATE Times SET timeofstart = '" + time + "' WHERE id = '" + id + "';";
  db.exec(query,res);
});

/**
 * @api {post} /checked/ Set a timestamp when it has checked the timescheme.
 * @apiName Checked
 * @apiGroup Internal
 * @apiParam {Integer} id The id of the timescheme.
 * @apiParam {String} time The time of the timestamp in epoch format.
*/

app.post('/checked', function(req, res) {
  let data = req.body;
  let id = data.id;
  let time = data.time;
  let query = "UPDATE Times SET last_checked = '" + time + "' WHERE id = '" + id + "';";
  db.exec(query,res);
});

/**
 * @api {post} /notified/ Set a timestamp when it has notified the timescheme.
 * @apiName Notified
 * @apiGroup Internal
 * @apiParam {Integer} id The id of the timescheme.
 * @apiParam {String} time The time of the timestamp in epoch format.
*/

app.post('/notified', function(req, res) {
  let data = req.body;
  let id = data.id;
  let time = data.time;
  let query = "UPDATE Times SET notified = '" + time + "' WHERE id = '" + id + "';";
  db.exec(query,res);
});

/**
 * @api {post} /nullnotified/ Removes a timestamp from the notified section of the timescheme.
 * @apiName NullNotified
 * @apiGroup Internal
 * @apiParam {Integer} id The id of the timescheme.
*/

app.post('/nullnotified', function(req, res) {
  let id = req.body.id;
  let query = "UPDATE Times SET notified=NULL WHERE id = '" + id + "';";
  db.exec(query,res);
});

// PUBLIC ACCESSABLE //
// Get the data of a specific route which a user has to follow
//not implemented.
app.get('/public/route', function(req, res) {
  let data = req.body;
  let from = data.from;
  let to = data.to;
  let date = data.date;
  let time = data.time;
  let type = data.type; /* normal / full / int */
});

/**
 * @api {get/post} /public/all_stops Returns all stops that are known inside the app.
 * @apiName All Stops
 * @apiGroup Public
*/

app.get('/public/all_stops', function(req, res) {
  let query = "SELECT * FROM Stops;";
  db.exec(query,res);
});

/**
 * @api {get/post} /public/auth Authentication api.
 * @apiName Authentication
 * @apiGroup Public
 * @apiParam {String} username The username of the user which you want to use.
 * @apiParam {String} password The password of the user which you want to use.

 * @apiSuccess {Integer} id The id of the user.
 * @apiSuccess {String} auth_token The authentication token of that user
 * @apiSuccessExample Success-Response:
 * [
 *   {
 *     "id": 6,
 *     "auth_token": "c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8"
 *   }
 * ]

 * @apiError Unauthorized The user does not enter the right credentials.
 * @apiErrorExample Error-Response:
 * {
 *   "ERROR": "Unauthorized"
 * }
*/

app.get('/public/auth', function(req, res){
  let data = req.body;
  let username = data.username;
  let password = data.password;
  let string = username + "^63@431%32=21432*8421345fd2sSqla" + password;
  let auth_token = crypto.createHash('sha256').update(string).digest('hex');
  //let db_entry = db.execInternalResponse("SELECT user_id AS id, auth_token FROM Users WHERE username='" + username + "' AND password='" + password + "';");
  let queryDatabase = db.execInternalResponse("UPDATE Users set auth_token='" + auth_token + "' WHERE username='" + username + "' AND password='" + password + "';")
  let db_entry = db.execInternalResponse("SELECT user_id AS id, auth_token FROM Users WHERE username='" + username + "' AND password='" + password + "';");
  db_entry.then(function(output) {
    
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

// Authentication POST
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

/**
 * @api {post} /public/auth/register Register api.
 * @apiName Register
 * @apiGroup Public

 * @apiParam {String} username The username of the user you want to register.
 * @apiParam {String} password The password of the user you want to register.
 
 * @apiSuccess {Integer} id The id of the user.
 * @apiSuccess {String} auth_token The authentication token of that user
 * @apiSuccessExample Success-Response:
 * [
 *   {
 *     "id": 6,
 *     "auth_token": "c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8"
 *   }
 * ]

 * @apiError Unauthorized The user does not enter the right credentials.
 * @apiError Unknown Something went wrong authenticating your new user, please log in manualy
 * @apiErrorExample Error-Response:
 * {
 *   "ERROR": "Unauthorized"
 * }
*/

app.post('/public/auth/register', function(req, res){
  let data = req.body;
  let username = data.username;
  let password = data.password;
  let email = data.email;
  console.log(username);
  console.log(password);
  console.log(email);
  let string = username + "^63@431%32=21432*8421345fd2sSqla" + password;
  let auth_token = crypto.createHash('sha256').update(string).digest('hex');
  let exists = db.execInternalResponse("SELECT user_id AS id FROM Users WHERE username='" + username + "';");
  exists.then(function(output) {
    console.log(output);
    var it_exists = 0;
    if ( output.length > 0 ){
      if ( JSON.parse(JSON.stringify(output[0])) != JSON.parse("{}") ) {
        res.send("{ \"ERROR\": \"User exists\" }");
        it_exists = 1;
      }
    }
    
    if (it_exists == 0){
      if (email && password && username){
        db.execInternalResponse("INSERT INTO Users (username,password,email) VALUES ('" + username + "', '" + password + "', '" + email + "');");
        let queryDatabase = db.execInternalResponse("UPDATE Users set auth_token='" + auth_token + "' WHERE username='" + username + "';")
        queryDatabase.then(function() {
            let db_entry = db.execInternalResponse("SELECT user_id AS id, auth_token FROM Users WHERE username='" + username + "' AND password='" + password + "';");
            db_entry.then(function(output) {
              
              if ( output.length > 0 ){
                if ( JSON.parse(JSON.stringify(output[0])) != JSON.parse("{}") ) {
                  res.send(output);
                } else {
                  res.send("{ \"ERROR\": \"Something went wrong authenticating your new user, please log in manualy\" }");
                }
              } else {
                  res.send("{ \"ERROR\": \"Something went wrong authenticating your new user, please log in manualy\" }");
                }
            });
        });
      } else { res.send("{ \"ERROR\": \"Invalid parameters.\" }"); }
    }
  });
});

/**
 * @api {post} /public/auth/change/password Change password.
 * @apiName Password
 * @apiGroup Public

 * @apiParam {Integer} id The id of the user.
 * @apiParam {String} password The new password for the user.
 * @apiParam {String} auth_token The current authentication token for the user.
 
 * @apiSuccess {String} Message Password updated!
 * @apiSuccess {String} auth_token The new authentication token of that user.
 * @apiSuccessExample Success-Response:
 * [
 *   {
 *     "Message": "Password updated!",
 *     "auth_token": "c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8"
 *   }
 * ]

 * @apiError Token Invalid token.
 * @apiError UserDoesNotExists User does not exists.
 * @apiErrorExample Error-Response:
 * {
 *   "ERROR": "Invalid token."
 * }
*/

app.post('/public/auth/change/password', function(req, res){
  let data = req.body;
  let id = data.user_id; //existing user
  let password = data.password; //new password
  let auth_token = data.auth_token; //old auth token
  //let string = username + "^63@431%32=21432*8421345fd2sSqla" + password;
  //let new_auth_token = crypto.createHash('sha256').update(string).digest('hex');
  let exists = db.execInternalResponse("SELECT auth_token,username FROM Users WHERE user_id='" + id + "';");
  exists.then(function(output) {
    var validated = 0;
    if ( output.length > 0 ){
      if ( JSON.parse(JSON.stringify(output[0])) != JSON.parse("{}") ) {
        //user exists, auth token in output
        let old_auth_token = output[0].auth_token;
        let username = output[0].username;
        let string = username + "^63@431%32=21432*8421345fd2sSqla" + password;
        let new_auth_token = crypto.createHash('sha256').update(string).digest('hex');
        if (auth_token == old_auth_token){
          validated = 1;
        let queryDatabase = db.execInternalResponse("UPDATE Users set auth_token='" + new_auth_token + "' WHERE user_id='" + id + "';");
        queryDatabase.then(function(){let queryDatabase2 = db.execInternalResponse("UPDATE Users set password='" + password + "' WHERE user_id='" + id + "';");}).then(function() {
          res.send("{ \"Message\": \"Password updated!\", \"new_token\": \"" + new_auth_token + "\" }")
        });
        } else { res.send("{ \"ERROR\": \"Invalid token.\" }"); }
      }
    }
    
    if (validated == 0){
      res.send("{ \"ERROR\": \"User does not exist.\" }");
    }
  });
});

/**
 * @api {post} /public/auth/change/username Change username.
 * @apiName Username
 * @apiGroup Public

 * @apiParam {Integer} id The id of the user.
 * @apiParam {String} username The new username for the user.
 * @apiParam {String} auth_token The current authentication token for the user.
 
 * @apiSuccess {String} Message Username updated!
 * @apiSuccess {String} auth_token The new authentication token of that user.
 * @apiSuccessExample Success-Response:
 * [
 *   {
 *     "Message": "Username updated!",
 *     "auth_token": "c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8"
 *   }
 * ]

 * @apiError Token Invalid token.
 * @apiError UserDoesNotExists User does not exists.
 * @apiError UsernameExists Username exists.
 * @apiErrorExample Error-Response:
 * {
 *   "ERROR": "Invalid token."
 * }
*/

app.post('/public/auth/change/username', function(req, res){
  let data = req.body;
  let id = data.user_id; //existing user
  let username = data.username; //new username
  let auth_token = data.auth_token; //old auth token
  var it_exists = 0;
  let user_exists = db.execInternalResponse("SELECT user_id AS id FROM Users WHERE username='" + username + "';");
  user_exists.then(function(output) {
    console.log(output);
    if ( output.length > 0 ){
      if ( JSON.parse(JSON.stringify(output[0])) != JSON.parse("{}") ) {
        res.send("{ \"ERROR\": \"Username exists\" }");
        it_exists = 1;
      }
    }
  });

  if (it_exists == 0){
    let exists = db.execInternalResponse("SELECT auth_token,password FROM Users WHERE user_id='" + id + "';");
    exists.then(function(output) {
      var validated = 0;
      if ( output.length > 0 ){
        if ( JSON.parse(JSON.stringify(output[0])) != JSON.parse("{}") ) {
          //user exists, auth token in output
          let old_auth_token = output[0].auth_token;
          let password = output[0].password;
          let string = username + "^63@431%32=21432*8421345fd2sSqla" + password;
          let new_auth_token = crypto.createHash('sha256').update(string).digest('hex');
          if (auth_token == old_auth_token){
            validated = 1;
            let queryDatabase = db.execInternalResponse("UPDATE Users set auth_token='" + new_auth_token + "' WHERE user_id='" + id + "';");
            queryDatabase.then(function(){ let queryDatabase2 = db.execInternalResponse("UPDATE Users set username='" + username + "' WHERE user_id='" + id + "';"); }).then(function() {
              res.send("{ \"Message\": \"Username updated!\", \"new_token\": \"" + new_auth_token + "\" }");
            });
          } else { res.send("{ \"ERROR\": \"Invalid token.\" }"); }
        }
      }
      
      if (validated == 0){
        res.send("{ \"ERROR\": \"User does not exist.\" }");
      }
    });
  }
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
  {url:"/public/times/remove", queryIndex:functions.Query.TIME_REMOVE},
  {url:"/public/user/change", queryIndex:functions.Query.USER_CHANGE},
  {url:"/public/user/exists", queryIndex:functions.Query.USER_NAME_EXIST, skipTokenCheck:true},
  {url:"/public/user/register", queryIndex:functions.Query.USER_REGISTER, skipTokenCheck:true}
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
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    
    if(!requests[i].skipTokenCheck){
      checkToken(req.body.token, res, req.body.user_id, () => {
        functions.executeAPIQuery(requests[i].queryIndex, callbackObject, req.body);
      });
    }
    else{
      functions.executeAPIQuery(requests[i].queryIndex, callbackObject, req.body);
    }
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
