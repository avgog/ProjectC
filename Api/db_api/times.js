const database = require("./database")

module.exports.recreate_table = async function(){
    let query1 = "drop table if exists times;"
    let query2 = `create table times(id int PRIMARY KEY AUTO_INCREMENT, route_id int, end_time datetime)`
    database.executeQuery(query1);
    database.executeQuery(query2);
}

//end_time [datetime] format: 'YYYY-MM-DD hh:mm:ss'
module.exports.add_time = async function(route_id, end_time, callbackObject){
    let query = `insert into times(route_id, end_time) values (${route_id}, '${end_time}')`
    database.executeQuery(query, callbackObject)
}

module.exports.get_time = async function(id, callbackObject){
    let query = `select id, route_id, DATE_FORMAT(end_time, '%Y-%m-%d %T') as end_time from times where id = ${id}`
    database.executeQuery(query, callbackObject)
}

module.exports.get_route_times = async function(route_id, callbackObject){
    let query = `select id, route_id, DATE_FORMAT(end_time, '%Y-%m-%d %T') as end_time from times where route_id = ${route_id}`
    database.executeQuery(query, callbackObject)
}

//end_time [datetime] format: 'YYYY-MM-DD hh:mm:ss'
module.exports.change_time = async function(id, end_time, callbackObject){
    let query = `update times set end_time = '${end_time}' where id = ${id}`
    database.executeQuery(query, callbackObject)
}

module.exports.remove_time = async function(id, callbackObject){
    let query = `delete from times where id = ${id}`
    database.executeQuery(query, callbackObject)
}

if(process.argv.length>2){
    args = process.argv;
    switch(args[2]){
        case "recreate_table": this.recreate_table(); break;
        case "add_time": this.add_time(args[3], args[4], null); break;
        case "get_time": this.get_time(args[3], null); break;
        case "get_route_times": this.get_route_times(args[3], null); break;
        case "change_time": this.change_time(args[3], args[4], null); break;
        case "remove_time": this.remove_time(args[3], null); break;
    }
}