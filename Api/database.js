const fs = require('fs');

/*
Execute a mysql query on the database.
query is an mysql query string
values parameter is an array of query parameters

callback object format:
{
    callback [type: function, params: error,result,data]
    data [type:Object, put your parameters for the callback here] 
}

example usage:
database.executeQuery("select * from myTable where id = ? and color = ?", [1, 'red'], {callback:myFunction,data:{res}})
*/
module.exports.executeQuery = async function(query, values, callbackObject){
    const mysql = require('mysql');
    console.log("query: "+query);
    console.log("query parameters: "+values);
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

        database.query(query, values, (err, res) => {
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

