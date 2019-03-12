package com.nowcoder.async;

import java.util.List;


//第四步：对应事件的处理
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
