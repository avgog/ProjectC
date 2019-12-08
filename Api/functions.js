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
        case Query.ROUTE_ADD:           query=`insert into Routes(user_id, start, end, route_name) VALUES (?,?,?,?);`; values=[body.user_id, body.start_point, body.end_point, body.route_name]; break;
        case Query.ROUTE_GET_BY_ID:     query=`select * from Routes where user_id = ? and route_id = ?;`; values=[body.user_id, body.route_id]; break;
        case Query.ROUTE_GET_BY_USER:   query=`select * from Routes where user_id = ?;`; values=[body.user_id]; break;
        case Query.ROUTE_CHANGE_START:  query=`update Routes set start = ? where user_id = ? and route_id = ?;`; values=[body.start_point, body.user_id, body.route_id]; break;
        case Query.ROUTE_CHANGE_END:    query=`update Routes set end = ? where user_id = ? and route_id = ?;`; values=[body.end_point, body.user_id, body.route_id]; break;
        case Query.ROUTE_CHANGE_NAME:   query=`update Routes set route_name = ? where user_id = ? and route_id = ?;`; values=[body.route_name, body.user_id, body.route_id]; break;
        case Query.ROUTE_REMOVE:        query=`delete from Routes where user_id = ? and route_id = ?;`; values=[body.user_id, body.route_id]; break;
        //case Query.TIME_ADD:          query=`insert into Times(route_id, date, timeofarrival) values (?,?,?)`; values=[body.route_id, body.date, body.end_time, body.user_id]; break;
        case Query.TIME_ADD:
            query=`insert into Times(route_id, date, timeofarrival) select ?,?,? where exists 
                   (select * from Routes where Routes.route_id = ? and Routes.user_id = ?)`; 
            values=[body.route_id, body.date, body.end_time, body.route_id, body.user_id]; break;
        case Query.TIME_GET_BY_ID:      query=`select Times.* from Times ${routeJoin} where Times.id = ?;`; values=[body.user_id,body.time_id]; break;
        case Query.TIME_GET_BY_ROUTE:   query=`select Times.* from Times ${routeJoin} where Times.route_id = ?;`; values=[body.user_id,body.route_id]; break;
        case Query.TIME_CHANGE_TIME:    query=`update Times ${routeJoin} set Times.timeofarrival = ?, Times.date = ? where Times.id = ?;`; values=[body.user_id,body.end_time,body.date,body.time_id]; break;
        case Query.TIME_REMOVE:         
            query=`delete from Times where Times.id = ? and exists (select * from Times join Routes on Routes.user_id = ? and Routes.route_id = Times.route_id and Times.id = ?);`; 
            values=[body.time_id,body.user_id, body.time_id]; break;
        default: executeQuery=false; callbackObject.callback("Server error: unknown queryIndex ("+queryIndex+")", "", callbackObject.data); //invalid queryIndex, don't execute any query.
    }
    if(executeQuery){ //if nothing went wrong, execute the query
        database.executeQuery(query, values, callbackObject);
    }
}