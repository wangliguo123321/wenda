package com.nowcoder.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component

public class EntityType {
    public static int ENTITY_QUESTION = 1;
    public static int ENTITY_COMMENT = 2;
    public static int ENTITY_USER = 3;
}
