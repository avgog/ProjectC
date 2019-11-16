const fs = require('fs');

/*
Execute a mysql query on the database.
callback object format:
{
    callback [type: function, params: error,result,data]
    data [type:Object, put your parameters for the callback here] 
}
*/
module.exports.executeQuery = async function(query, callbackObject){
    const mysql = require('mysql');

    try{
        const dbConfig = JSON.parse( fs.readFileSync(__dirname + "/db_config.json") );
        
        const database = mysql.createConnection({
            user: dbConfig.user,
            host: dbConfig.host,
            database: dbConfig.database,
            password: dbConfig.password,
            port: dbConfig.port
        })
        database.connect(err => {
            if (err) {
                console.error('connection error', err.stack)
            }
        })

        database.query(query, (err, res) => {
            if(err){
                console.log(err, res)
            }
            if(query.toLowerCase().startsWith("select") && res){
                console.log(res)
            }
            if(callbackObject!=null){
                callbackObject.callback(err, res, callbackObject.data);
            }
            database.end()
        })
    }
    catch(error){
        console.log(error);
    }
}
