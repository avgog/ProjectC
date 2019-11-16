const express = require('express')
const bodyParser = require("body-parser")
const app = express()
const port = 2900

const routes = require("./routes")
const times = require("./times")

app.use(bodyParser.urlencoded({
    extended: true
}));

function defaultCallback(err, res,callbackData){
    if(!err){
        err=""
    }
    if(!res){
        res=[]
    }
    callbackData.res.send({"error":err, "result":res})
}

//use this to test the connection between the client and server
app.get("/", (req, res) => {
    res.send({"error":"", "result":"callback from server"})
});

app.post("/routes/add", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    routes.add_route(req.body.user_id, req.body.start_point, req.body.end_point, req.body.route_name, callbackObject)
});

app.post("/routes/get/from_id", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    routes.get_route(req.body.route_id, callbackObject)
});

app.post("/routes/get/from_user", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    routes.get_user_routes(req.body.user_id, callbackObject)
});

app.post("/routes/change/start_point", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    routes.change_start_point(req.body.route_id, req.body.start_point, callbackObject)
});

app.post("/routes/change/end_point", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    routes.change_end_point(req.body.route_id, req.body.end_point, callbackObject)
});

app.post("/routes/change/route_name", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    routes.change_route_name(req.body.route_id, req.body.route_name, callbackObject)
});

app.post("/routes/remove", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    routes.remove_route(req.body.route_id, callbackObject)
});



app.post("/times/get/from_id", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    times.get_route(req.body.time_id, callbackObject)
});

app.post("/times/get/from_route", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    times.get_route_times(req.body.route_id, callbackObject)
});

app.post("/times/add", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    times.add_time(req.body.route_id, req.body.end_time, callbackObject)
});

app.post("/times/change/time", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    times.change_time(req.body.time_id, req.body.end_time, callbackObject)
});

app.post("/times/remove", (req, res) => {
    console.log("got post request [url: '" + req.url +"']")
    let callbackObject = {"callback":defaultCallback, "data":{res}}
    times.remove_time(req.body.time_id, callbackObject)
});


app.listen(port, () => console.log(`listening on port ${port}`))