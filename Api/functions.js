const database = require(__dirname + "/database.js")
// TIMES //

//end_time [datetime] format: 'YYYY-MM-DD hh:mm:ss'
module.exports.add_time = async function(route_id, date, end_time, callbackObject){
    let query = `insert into Times(route_id, date, timeofarrival) values (${route_id}, '${date}', '${end_time}')`
    database.executeQuery(query, callbackObject)
}

module.exports.get_time = async function(id, callbackObject){
    let query = `select * from Times where id = ${id}`
    database.executeQuery(query, callbackObject)
}

module.exports.get_route_times = async function(route_id, callbackObject){
    let query = `select * from Times where route_id = ${route_id}`
    database.executeQuery(query, callbackObject)
}

//end_time [datetime] format: 'YYYY-MM-DD hh:mm:ss'
module.exports.change_time = async function(id, date, end_time, callbackObject){
    console.log(id + " | " + date + " | " + end_time + " | " + typeof callbackObject); 
    let query = `update Times set timeofarrival = '${end_time}', date = '${date}' where id = ${id}`
    database.executeQuery(query, callbackObject)
}

module.exports.change_date = async function(id, date, callbackObject){
    let query = `update Times set date = '${date}' where id = ${id}`
    database.executeQuery(query, callbackObject)
}

module.exports.remove_time = async function(id, callbackObject){
    let query = `delete from Times where id = ${id}`
    database.executeQuery(query, callbackObject)
}

// ROUTES //

module.exports.add_route = async function(user_id, start_point, end_point, name, callbackObject=null){
    const query = `
    insert into Routes 
    (user_id, start, end, route_name) 
    VALUES (`+user_id+`,'`+start_point+`','`+end_point+`','`+name+`');`;
    database.executeQuery(query, callbackObject);
}

module.exports.get_route = async function(route_id, callbackObject=null){
    const query = `select * from Routes where route_id = ` + route_id;
    database.executeQuery(query, callbackObject);
}

module.exports.get_user_routes = async function(user_id, callbackObject=null){
    const query = `select * from Routes where user_id = ` + user_id;
    database.executeQuery(query, callbackObject);
}

module.exports.change_start_point = async function(route_id, start_point, callbackObject=null){
    const query = `update Routes set start = '` + start_point + `' where route_id = '` + route_id + `'`;
    database.executeQuery(query, callbackObject);
    
}

module.exports.change_end_point = async function(route_id, end_point, callbackObject=null){
    const query = `update Routes set end = '` + end_point + `' where route_id = '` + route_id + `';`;
    database.executeQuery(query, callbackObject);
    
}

module.exports.change_route_name = async function(route_id, route_name, callbackObject=null){
    const query = `update Routes set route_name = '` + route_name + `' where route_id = '` + route_id + `';`;
    database.executeQuery(query, callbackObject);
}

module.exports.remove_route = async function(route_id, callbackObject=null){
    const query = `delete from Routes where route_id = '` + route_id+ `';`;
    database.executeQuery(query, callbackObject);
}

