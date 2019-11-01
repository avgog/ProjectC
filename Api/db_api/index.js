const express = require('express');
const fs = require('fs');
const bodyparser = require('body-parser');
const mysql = require('mysql');

var app = express();

app.use(bodyparser.json());
app.use(bodyparser.urlencoded({ extended: false }));

const app_settings = JSON.parse( fs.readFileSync(__dirname + "/settings/unsafe.json") );
const db_tables = JSON.parse( fs.readFileSync(__dirname + "/settings/db.json") );

const exec = (query, res) => {
    db.connect();
    db.query(query, function(err, result){
      db.end();
      console.log(query);
      console.log(result);
      if(!err) { res.send(JSON.stringify(result)); }
      else { console.log(err); res.send("ERROR"); }
    });
  };
  
// NOTE: Make sure the user in the unsafe.json has plugin 'mysql_native_password', otherwise it will crash.
var db = mysql.createConnection({
  host: app_settings.db.host,
  user: app_settings.db.user,
  password: app_settings.db.pass,
  database: app_settings.db.database
});

app.get('/select', function (req, res) {
  let data = req.body;
  let data_field = data.data;
  let table = data.key;

  let user = data.user;
  let user_pass = data.pass;

  let query = "SELECT ";

  let fields = data_field.fields;
  for (let i = 0; i < fields.length; i++ ) {
    if (i + 1 == fields.length){ query = query + fields[i]; }
    else { query = query + fields[i] + ", ";}
  }
  
  query = query + " FROM " + table;
  let filter = data_field.filter;
  if (Object.keys(filter).length > 0) {
    let names = Object.keys(filter);
    for (let i = 0; i < names.length; i++){
      if (i == 0) {
        query = query + " WHERE " + names[i] + " = '" + filter[names[i]] + "'";
      }
      else {
        query = query + " AND " + names[i] + " = '" + filter[names[i]] + "'";
      }
    }
  }
  query = query + ";"
  //res.send(query);
  
  //let RESULT = exec(query);
  //console.log("Result: " + RESULT);
  //res.send(RESULT);
  exec(query, res);
});

app.post('/insert', function(req, res) {
  let data = req.body;
  let data_field = data.data;
  let table = data.key;

  let headers = Object.keys(data_field);
  let table_headers = db_tables[table];
  
  if ( String(headers) == String(table_headers) ) {

    let user = data.user;
    let user_pass = data.pass;

    let val1 = "";
    let val2 = "";
    for ( let i = 0; i < headers.length; i++ ) {
      if ( i == 0 ){
        val1 = headers[i];
        val2 = "'" + data_field[headers[i]] + "'";
      }
      else {
        let tmp = headers[i];
        val1 = val1 + ", " + tmp;
        val2 = val2 + ", " + "'" + data_field[tmp] + "'";
      }
    }
    let query = "INSERT INTO " + table + " (" + val1 + ") VALUES (" + val2 + ");";
    
    exec(query, res);
  }
  else {
    res.send("ERROR: the given body is not valid.");
  }

});

app.post('/delete', function (req, res) {
  let data = req.body;
  let data_field = data.data;
  let table = data.key;

  let user = data.user;
  let user_pass = data.pass;

  query = "DELETE FROM " + table;
  let filter = data_field.filter;
  if (Object.keys(filter).length > 0) {
    let names = Object.keys(filter);
    for (let i = 0; i < names.length; i++){
      if (i == 0) {
        query = query + " WHERE " + names[i] + " = '" + filter[names[i]] + "'";
      }
      else {
        query = query + " AND " + names[i] + " = '" + filter[names[i]] + "'";
      }
    }
  }
  query = query + ";"
  res.send(query);
  
  exec(query, res);
});

app.post('/query', function(req, res) {
  let data = req.body;
  let query = data.query;
    
  console.log(Object.keys(data));
  console.log(req); console.log(data)
  console.log(query);
  
  exec(query, res);
});

app.get('/auth', (req, res) => {
  let data = req.body;
  console.log(data);
  let username = data.user;
  let password = data.password;
  let query = "SELECT * FROM Users WHERE username='" + username + "' AND password='" + password + "';";
    
  console.log(query);
  exec(query, res);
});


app.listen(666, function () {
  console.log('Ready...');
});
