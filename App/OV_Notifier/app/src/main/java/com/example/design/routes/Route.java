package com.example.design.routes;

public class Route {
    private int id;
    public String start_point = "[0,0]";
    public String end_point = "[1,1]";
    public String route_name;

    public Route(int id){
        this.id = id;
        route_name = "new route ("+id+")";
    }

    public int getId(){
        return id;
    }
}
