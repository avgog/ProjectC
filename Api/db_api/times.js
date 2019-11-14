const database = require("./database")

module.exports.recreate_table = async function(){
    let query1 = "drop table if exists times;"
    let query2 = `create table times(id int PRIMARY KEY AUTO_INCREMENT, route_id int, end_time datetime)`
    database.executeQuery(query1);
    database.executeQuery(query2);
}

//end_time [datetime] format: 'YYYY-MM-DD hh:mm:ss'
module.exports.add_time = async function(route_id, end_time){
    let query = `insert into times(route_id, end_time) values (${route_id}, '${end_time}')`
    database.executeQuery(query)
}

module.exports.get_time = async function(id){
    let query = `select * from times where id = ${id}`
    database.executeQuery(query)
}

module.exports.get_route_times = async function(route_id){
    let query = `select * from times where route_id = ${route_id}`
    database.executeQuery(query)
}

module.exports.change_time = async function(id, end_time){
    let query = `update times set end_time = '${end_time}' where id = ${id}`
    database.executeQuery(query)
}

module.exports.remove_time = async function(id){
    let query = `delete from times where id = ${id}`
    database.executeQuery(query)
}