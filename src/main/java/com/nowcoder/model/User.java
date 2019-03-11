package com.nowcoder.model;



@Data
public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;
    public User(){}
    public User(String name) {
        this.name = name;
        this.password = "";
        this.salt = "";
        this.headUrl = "";
    }


}
