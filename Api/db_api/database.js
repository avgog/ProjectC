const fs = require('fs');

module.exports.executeQuery = async function(query){
    const mysql = require('mysql');

    try{
        const dbConfig = JSON.parse( fs.readFileSync(__dirname + "/temp_db_config.json") );
        
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
            database.end()
        })
    }
    catch(error){
        console.log(error);
    }
}
