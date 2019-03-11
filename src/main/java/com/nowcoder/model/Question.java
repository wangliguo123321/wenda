package com.nowcoder.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component

@Data
public class Question {
    private int id;
    private String title;
    private String content;
    private Date createdDate;
    private int userId;
    private int commentCount;


}
