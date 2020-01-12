package com.example.design.user;

public class UserToken {
    private int userId;
    private String token;
    public static UserToken currentUser = new UserToken(-1, "");

    public UserToken(int userId, String token){
        this.userId = userId;
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public int getUserId(){
        return userId;
    }
}
