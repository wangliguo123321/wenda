package com.nowcoder.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component

@Data
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;
    private int status;// 0有效，1无效
    private String ticket;


}
