const database = require("./database")

//create a route table, deletes existing one if it exists before creation
module.exports.recreate_table = async function(){
    let query1 = "drop table if exists routes;"
    let query2 = `
    create table routes(
        route_id int AUTO_INCREMENT not null, 
        user_id int, 
        start_point varchar(255), 
        end_point varchar(255), 
        route_name varchar(255), 
        primary key(route_id)
    );`;
    database.executeQuery(query1, null);
    database.executeQuery(query2, null);
}

module.exports.add_route = async function(user_id, start_point, end_point, name, callbackObject=null){
    const query = `
    insert into routes 
    (user_id, start_point, end_point, route_name) 
    VALUES (`+user_id+`,'`+start_point+`','`+end_point+`','`+name+`');`;
    database.executeQuery(query, callbackObject);
}

module.exports.get_route = async function(route_id, callbackObject=null){
    const query = `select * from routes where route_id = ` + route_id;
    database.executeQuery(query, callbackObject);
}

module.exports.get_user_routes = async function(user_id, callbackObject=null){
    const query = `select * from routes where user_id = ` + user_id;
    database.executeQuery(query, callbackObject);
}

module.exports.change_start_point = async function(route_id, start_point, callbackObject=null){
    const query = `update routes set start_point = '` + start_point + `' where route_id = '` + route_id + `'`;
    database.executeQuery(query, callbackObject);
    
}

module.exports.change_end_point = async function(route_id, end_point, callbackObject=null){
    const query = `update routes set end_point = '` + end_point + `' where route_id = '` + route_id + `'`;
    database.executeQuery(query, callbackObject);
    
}

module.exports.change_route_name = async function(route_id, route_name, callbackObject=null){
    const query = `update routes set route_name = '` + route_name + `' where route_id = '` + route_id + `'`;
    database.executeQuery(query, callbackObject);
}

module.exports.remove_route = async function(route_id, callbackObject=null){
    const query = `delete from routes where route_id = ` + route_id+ ``;
    database.executeQuery(query, callbackObject);
}


/*example usages:
node routes recreate_table
node routes add_route 1 "[5,5]" "[50,50]" "thuis-werk"
*/

if(process.argv.length>2){
    args = process.argv;
    switch(args[2]){
        case "recreate_table": this.recreate_table(); break;
        case "add_route": this.add_route(args[3],args[4],args[5],args[6]);break;
        case "get_route": this.get_route(args[3]);break;
        case "get_user_routes": this.get_user_routes(args[3]);break;
        case "change_start_point": this.change_start_point(args[3], args[4]);break;
        case "change_end_point": this.change_end_point(args[3], args[4]);break;
        case "change_route_name": this.change_route_name(args[3], args[4]);break;
        case "remove_route": this.remove_route(args[3]);break;
        default: console.log("unknown function");
    }
}