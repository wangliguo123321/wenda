package com.nowcoder.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component

public class Comment {
    private int userId;
    private int id;
    private int entityId;
    private int entityType;
    private String content;
    private Date createdDate;
    private int status;

}
