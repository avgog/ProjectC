# ProjectC

# Api

----------------------

URL PATH,                          Request type,       Parameters
/public/routes/add                 POST                user_id, start_point, end_point, route_name
/public/routes/get/from_id         POST                route_id
/public/routes/get/from_user       POST                user_id
/public/routes/change/start_point  POST                route_id, start_point
/public/routes/change/end_point    POST                route_id, end_point
/public/routes/change/route_name   POST                route_id, route_name
/public/routes/remove              POST                route_id
/public/times/get/from_id          POST                time_id
/public/times/get/from_route       POST                route_id
/public/times/add                  POST                route_id, end_time, date
/public/times/change/time          POST                time_id, end_time, date
/public/times/remove               POST                time_id
/public/auth                       GET                 username, password

----------------------

PATH:/public/routes/add
JSON: {"auth_token": "AUTH_TOKEN", "user_id": "USER_ID", "start_point": "START_PLACE", "end_point": "END_PLACE", "route_name": "ROUTE_NAME"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "user_id": "22095", "start_point": "Capelsebrug, Rotterdam", "end_point": "Beurs, Rotterdam", "route_name": "A naar B"}
OPMERKING: Het toevoegen van een route
*****
PATH:/public/routes/get/from_id
JSON: {"auth_token": "AUTH_TOKEN", "route_id": "ROUTE_ID"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "route_id": "1534"}
OPMERKING: Het verkrijgen van een route via een route id.
*****
PATH:/public/routes/get/from_user
JSON: {"auth_token": "AUTH_TOKEN", "user_id": "USER_ID"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "user_id": "4352"}
OPMERKING: Het verkrijgen van de routes van een specifieke user.
*****
PATH:/public/routes/change/start_point
JSON: {"auth_token": "AUTH_TOKEN", "route_id": "ROUTE_ID", "start_point": "PLACE"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "route_id": "1432", "start_point": "Capelsebrug, Rotterdam"}
OPMERKING: Het wijzigen van een beginpunt
*****
PATH:/public/routes/change/end_point
JSON: {"auth_token": "AUTH_TOKEN", "route_id": "ROUTE_ID", "end_point": "PLACE"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "route_id": "5231", "end_point": "Beurs, Rotterdam"}
OPMERKING: Het wijzigen van een eindbestemming
*****
PATH:/public/routes/change/route_name
JSON: {"auth_token": "AUTH_TOKEN", "route_id": "ROUTE_ID", "route_name": "ROUTE_NAME"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "route_id": "6352", "route_name": "My route"}
OPMERKING: Het wijzigen van een route naam
*****
PATH:/public/routes/remove
JSON: {"auth_token": "AUTH_TOKEN", "route_id": "ROUTE_ID"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "route_id": "6378"}
OPMERKING: Het verwijderen van een route
*****
PATH:/public/times/get/from_id
JSON: {"auth_token": "AUTH_TOKEN", "time_id": "TIME_ID"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "time_id": "526"}
OPMERKING: Het verkrijgen van een tijdschema via een id.
*****
PATH:/public/times/get/from_route
JSON: {"auth_token": "AUTH_TOKEN", "route_id": "ROUTE_ID"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "route_id": "5237"}
OPMERKING: Het verkrijgen van een tijdschema via een route id
*****
PATH:/public/times/add
JSON: {"auth_token": "AUTH_TOKEN", "route_id": "ROUTE_ID", "end_time": "END_TIME", "date": "DATE"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "route_id": "9897", "end_time": "14:00", "date": "2019-05-30"}
OPMERKING: Het toevoegen van een tijdschema
*****
PATH:/public/times/change/time
JSON: {"auth_token": "AUTH_TOKEN", "time_id": "ROUTE_ID", "end_time": "END_TIME", "date": "DATE"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "time_id": "5235", "end_time": "14:00", "date": "2019-05-30"}
OPMERKING: Het veranderen van de datum en tijd van een tijdschema
*****
PATH:/public/times/remove
JSON: {"auth_token": "AUTH_TOKEN", "time_id": "TIME_ID"}
Voorbeeld: {"auth_token": "fTgfd42he34esg5hregsrtfgweg", "time_id": "4132"}
OPMERKING: Het verwijderen van een tijdschema
*****
PATH:/public/auth
JSON: {"username": "USER", "password": "PASSWORD"}
Voorbeeld: {"username": "piet", "password": "pa$$w0rd"}
Response Goedgekeurd: [{"id":1,"auth_token":"tdg46dh6435hd46dg53ggdhs"}]
Response Error: { "ERROR": "Unauthorized" }
OPMERKING: Het verkrijgen van een authenticatie token

----------------------

Een Api call is een HTTP request, deze stuur je naar:
http://HOST:PORT/PATH

In dit geval gelden de volgende gegevens:
HOST: projectc.caslayoort.nl
Externe PORT: 80 (Default HTTP, foreward alleen /public/* via haproxy)
(Interne PORT: 666)
PATH: zie api url paths.

Bijvoorbeeld:
http://projectc.caslayoort.nl:80/routes/get/from_id

----------------------

Response format van de public API (uitzondering van de auth api)
{
    "error":"",
    "result": [
        sql row 1,
        sql row 2,
        ...
    ]
}
----------------------
