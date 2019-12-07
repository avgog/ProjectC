const database = require("./database")

//Enum for selecting a query of this API
//This is needed for the executeAPIQuery function
const Query = {
    ROUTE_ADD                   : 0,
    ROUTE_GET_BY_ID             : 1,
    ROUTE_GET_BY_USER           : 2,
    ROUTE_CHANGE_START          : 3,
    ROUTE_CHANGE_END            : 4,
    ROUTE_CHANGE_NAME           : 5,
    ROUTE_REMOVE                : 6,
    TIME_ADD                    : 7,
    TIME_GET_BY_ID              : 8,
    TIME_GET_BY_ROUTE           : 9,
    TIME_CHANGE_TIME            :10,
    TIME_REMOVE                 :11
};
module.exports.Query = Query;

//execute a sql query of the API.
//queryIndex is used to select a query in the switch statement. Usage of the Query enum is recommended
//callbackobject contains a callbackfunction and a data field. Go to database.js to view the format.
//body must contain all the parameters for the sql query
module.exports.executeAPIQuery = async function(queryIndex, callbackObject, body){
    let query = "";
    let values = [];
    let executeQuery = true;
    const routeJoin = `join Routes on Routes.user_id = ? and Routes.route_id = Times.route_id`;

    //select a sql query
    switch(queryIndex){
        case Query.ROUTE_ADD:           query=`insert into Routes(user_id, start, end, route_name) VALUES (?,?,?,?);`; values=[body.user_id, body.start_point, body.end_point, body.name]; break;
        case Query.ROUTE_GET_BY_ID:     query=`select * from Routes where user_id = ? and route_id = ?;`; values=[body.user_id, body.route_id]; break;
        case Query.ROUTE_GET_BY_USER:   query=`select * from Routes where user_id = ?;`; values=[body.user_id]; break;
        case Query.ROUTE_CHANGE_START:  query=`update Routes set start = ? where user_id = ? and route_id = ?;`; values=[body.start_point, body.user_id, body.route_id]; break;
        case Query.ROUTE_CHANGE_END:    query=`update Routes set end = ? where user_id = ? and route_id = ?;`; values=[body.end_point, body.user_id, body.route_id]; break;
        case Query.ROUTE_CHANGE_NAME:   query=`update Routes set route_name = ? user_id = ? and route_id = ?;`; values=[body.route_name, body.user_id, body.route_id]; break;
        case Query.ROUTE_REMOVE:        query=`delete from Routes where user_id = ? and route_id = ?;`; values=[body.user_id, body.route_id]; break;
        //case Query.TIME_ADD:          query=`insert into Times(route_id, date, timeofarrival) values (?,?,?)`; values=[body.route_id, body.date, body.end_time, body.user_id]; break;
        case Query.TIME_ADD: //dual = create a temporary table for the new values. SELECT is used instead of VALUES because the insert needs to be conditional
            query=`insert into Times(route_id, date, timeofarrival) select ?,?,? from dual where exists 
                   (select * from Routes join Times on Routes.route_id = Times.route_id and Routes.user_id = ?)`; 
            values=[body.route_id, body.date, body.end_time, body.user_id]; break;
        case Query.TIME_GET_BY_ID:      query=`select Times.* from Times ${routeJoin} where Times.id = ?;`; values=[body.user_id,body.id]; break;
        case Query.TIME_GET_BY_ROUTE:   query=`select Times.* from Times ${routeJoin} where Times.route_id = ?;`; values=[body.user_id,body.route_id]; break;
        case Query.TIME_CHANGE_TIME:    query=`update Times ${routeJoin} set Times.timeofarrival = ?, Times.date = ? where Times.id = ?;`; values=[body.user_id,body.end_time,body.date,body.id]; break;
        case Query.TIME_REMOVE:         query=`delete from Times ${routeJoin} where Times.id = ?;`; values=[body.user_id,body.id]; break;
        default: executeQuery=false; callbackObject.callback("Server error: unknown queryIndex ("+queryIndex+")", "", callbackObject.data); //invalid queryIndex, don't execute any query.
    }
    if(executeQuery){ //if nothing went wrong, execute the query
        database.executeQuery(query, values, callbackObject);
    }
}

// TIMES //

//end_time [datetime] format: 'YYYY-MM-DD hh:mm:ss'
/*module.exports.add_time = async function(route_id, date, end_time, callbackObject){
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
*/
