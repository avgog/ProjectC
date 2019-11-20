package com.example.design.routes;

public class Route {
    private int route_id;
    private int user_id;
    public String start_point;
    public String end_point;
    public String route_name;

    public Route(int route_id, int user_id, String start_point, String end_point, String route_name){
        this.route_id = route_id;
        this.user_id = user_id;
        this.start_point = start_point;
        this.end_point = end_point;
        this.route_name = route_name;
    }

    public int getRouteId(){
        return route_id;
    }

    public int getUserId(){
        return  user_id;
    }
}
