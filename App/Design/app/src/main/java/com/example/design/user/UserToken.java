package com.example.design.user;

public class UserToken {
    //Use those variables below for executing api functions (most of them need a user id and token)
    private int userId;
    private String token;

    //token + userid of the currently logged in user.
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
