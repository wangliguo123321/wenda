package com.nowcoder.model;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component

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
