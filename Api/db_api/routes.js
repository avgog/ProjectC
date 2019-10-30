const request = require("request");

function test(){
    let headers = {
        'Content-type': 'application.json'
    };
    let data =  '{"key": "test", "user": "test_user", "pass": "pa$$w0rd", "data": {"fields": ["id"], "filter": {"test": "this is a test"} } }';
    let options = {
        url: 'localhost:666',
        method:'GET',
        headers:headers,
        body: data
    }
    function callback(error, response,body){
        if (!error && response.statusCode == 200) {
            console.log(body);
        }
        else{
            console.log("statuscode: "+response+"," + error)
        }
    }
    request(options, callback);
}
test();

function add_route(user_id, password, start_point, end_point, name){
    
}

function display_routes(user_id, password){
    
}

function modify_route(user_id, password, route_id){
    
}

function remove_route(user_id, password, route_id){

}