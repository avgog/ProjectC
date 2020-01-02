define({ "api": [
  {
    "type": "post",
    "url": "/checked/",
    "title": "Set a timestamp when it has checked the timescheme.",
    "name": "Checked",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the timescheme.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "time",
            "description": "<p>The time of the timestamp in epoch format.</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "post",
    "url": "/notified/",
    "title": "Set a timestamp when it has notified the timescheme.",
    "name": "Notified",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the timescheme.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "time",
            "description": "<p>The time of the timestamp in epoch format.</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "post",
    "url": "/nullnotified/",
    "title": "Removes a timestamp from the notified section of the timescheme.",
    "name": "NullNotified",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the timescheme.</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "post",
    "url": "/query/",
    "title": "Execute query and return output in json format.",
    "name": "Query",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "query",
            "description": "<p>De query die moet worden uitgevoerd.</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "get",
    "url": "/routes/",
    "title": "Return a specific route in json format.",
    "name": "Routes",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "route",
            "description": "<p>The id of the route</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "route_id",
            "description": "<p>The id of the route.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "start",
            "description": "<p>The location of department.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "end",
            "description": "<p>The location of arrival.</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "user_id",
            "description": "<p>The id of the user that owns the route.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "route_name",
            "description": "<p>The name of the route.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n {\n   \"route_id\": 1,\n   \"start\": \"Sportcentrum de driesprong, Zoetermeer\",\n   \"end\": \"Nesselande, Rotterdam\",\n   \"user_id\": 1,\n   \"route_name\": \"Mijn route test\"\n }\n]",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "post",
    "url": "/starttime/",
    "title": "Set a time of department for a timescheme.",
    "name": "Starttime",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the timescheme.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "time",
            "description": "<p>The time of the timescheme in 24 hours format.</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "get",
    "url": "/stops/",
    "title": "Return the known data of a stop.",
    "name": "Stops",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "stop",
            "description": "<p>The name of the stop.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "town",
            "description": "<p>The town the stop is.</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "stop_id",
            "description": "<p>The unique id of the stop.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "town",
            "description": "<p>The town where the stop is.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>The name of the stop.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "stopareacode",
            "description": "<p>The stopcode used in ovdata.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "longitude",
            "description": "<p>The y-axis of the stop.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "latitude",
            "description": "<p>The x-axis of the stop.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n  {\n    \"stop_id\": 4,\n    \"town\": \"Rotterdam\",\n    \"name\": \"Beurs\",\n    \"stopareacode\": \"RtBeu\",\n    \"longitude\": \"4.4812500\",\n    \"latitude\": \"51.9182589\"\n  }\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "get",
    "url": "/time/",
    "title": "Return a specific timescheme",
    "name": "Time",
    "group": "Internal",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "id",
            "description": "<p>The uniq id of the timescheme.</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the timescheme</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "route_id",
            "description": "<p>The id of the route of this timescheme.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "timeofarrival",
            "description": "<p>The time of the arrival.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "timeofstart",
            "description": "<p>The time of the department.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "date",
            "description": "<p>Day or date the timescheme is active.</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer/null",
            "optional": false,
            "field": "last_checked",
            "description": "<p>The last time (epoch format) when the time you have to leave is calculated.</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer/null",
            "optional": false,
            "field": "notified",
            "description": "<p>The time (epoch format) you have been notified. (could be null)</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n  {\n   \"id\": 68,\n   \"route_id\": 34,\n   \"timeofarrival\": \"04:42\",\n   \"timeofstart\": \"03:45\",\n   \"date\": \"Wednesday\",\n   \"last_checked\": \"1577657898\",\n   \"notified\": null\n }\n]",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "get",
    "url": "/times/",
    "title": "Return all timeschemes",
    "name": "Times",
    "group": "Internal",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the timescheme</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "route_id",
            "description": "<p>The id of the route of this timescheme.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "timeofarrival",
            "description": "<p>The time of the arrival.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "timeofstart",
            "description": "<p>The time of the department.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "date",
            "description": "<p>Day or date the timescheme is active.</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer/null",
            "optional": false,
            "field": "last_checked",
            "description": "<p>The last time (epoch format) when the time you have to leave is calculated.</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer/null",
            "optional": false,
            "field": "notified",
            "description": "<p>The time (epoch format) you have been notified. (could be null)</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n  {\n   \"id\": 68,\n   \"route_id\": 34,\n   \"timeofarrival\": \"04:42\",\n   \"timeofstart\": \"03:45\",\n   \"date\": \"Wednesday\",\n   \"last_checked\": \"1577657898\",\n   \"notified\": null\n }\n]",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Internal"
  },
  {
    "type": "get/post",
    "url": "/public/all_stops",
    "title": "Returns all stops that are known inside the app.",
    "name": "All_Stops",
    "group": "Public",
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Public"
  },
  {
    "type": "get/post",
    "url": "/public/auth",
    "title": "Authentication api.",
    "name": "Authentication",
    "group": "Public",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "username",
            "description": "<p>The username of the user which you want to use.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>The password of the user which you want to use.</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the user.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "auth_token",
            "description": "<p>The authentication token of that user</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n  {\n    \"id\": 6,\n    \"auth_token\": \"c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8\"\n  }\n]",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "Unauthorized",
            "description": "<p>The user does not enter the right credentials.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "{\n  \"ERROR\": \"Unauthorized\"\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Public"
  },
  {
    "type": "post",
    "url": "/public/auth/change/password",
    "title": "Change password.",
    "name": "Password",
    "group": "Public",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the user.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>The new password for the user.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "auth_token",
            "description": "<p>The current authentication token for the user.</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "Message",
            "description": "<p>Password updated!</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "auth_token",
            "description": "<p>The new authentication token of that user.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n  {\n    \"Message\": \"Password updated!\",\n    \"auth_token\": \"c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8\"\n  }\n]",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "Token",
            "description": "<p>Invalid token.</p>"
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "UserDoesNotExists",
            "description": "<p>User does not exists.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "{\n  \"ERROR\": \"Invalid token.\"\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Public"
  },
  {
    "type": "post",
    "url": "/public/auth/register",
    "title": "Register api.",
    "name": "Register",
    "group": "Public",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "username",
            "description": "<p>The username of the user you want to register.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>The password of the user you want to register.</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the user.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "auth_token",
            "description": "<p>The authentication token of that user</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n  {\n    \"id\": 6,\n    \"auth_token\": \"c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8\"\n  }\n]",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "Unauthorized",
            "description": "<p>The user does not enter the right credentials.</p>"
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "Unknown",
            "description": "<p>Something went wrong authenticating your new user, please log in manualy</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "{\n  \"ERROR\": \"Unauthorized\"\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Public"
  },
  {
    "type": "post",
    "url": "/public/auth/change/username",
    "title": "Change username.",
    "name": "Username",
    "group": "Public",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the user.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "username",
            "description": "<p>The new username for the user.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "auth_token",
            "description": "<p>The current authentication token for the user.</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "Message",
            "description": "<p>Username updated!</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "auth_token",
            "description": "<p>The new authentication token of that user.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n  {\n    \"Message\": \"Username updated!\",\n    \"auth_token\": \"c65f1eb100e84eeec4c3c0067f70e7c0d70579a4c4b61d0dd92013df754649b8\"\n  }\n]",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "Token",
            "description": "<p>Invalid token.</p>"
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "UserDoesNotExists",
            "description": "<p>User does not exists.</p>"
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "UsernameExists",
            "description": "<p>Username exists.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Error-Response:",
          "content": "{\n  \"ERROR\": \"Invalid token.\"\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "/opt/projectc/index.js",
    "groupTitle": "Public"
  }
] });
