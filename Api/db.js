const fs = require('fs');
const mysql = require('mysql');
const app_settings = JSON.parse( fs.readFileSync(__dirname + "/settings/unsafe.json") );

module.exports.exec = function(query, res){
    db.query(query, function(err,resp){
      if(!err) { console.log("Query [ " + query + " ] succeded!"); res.send(resp); }
      else { console.log(err); res.send("Error!"); }
    });
};

module.exports.execInternalResponse = function(query){
    return new Promise(resolve =>{
      db.query(query, function(err,resp){
        if(!err) { console.log("Query [ " + query + " ] succeded!!"); resolve(resp); }
        else { console.log(err); resolve("ERROR!"); }
      });
    });
};

// NOTE: Make sure the user in the unsafe.json has plugin 'mysql_native_password', otherwise it will crash.
var db = mysql.createConnection({
    host: app_settings.db.host,
    user: app_settings.db.user,
    password: app_settings.db.pass,
    database: app_settings.db.database
});

