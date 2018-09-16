package com.LG.async.handler;

import com.LG.async.EventHandler;
import com.LG.async.EventModel;
import com.LG.async.EventType;
import com.LG.util.JedisAdapter;
import com.LG.util.RedisKeyUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liguo
 * @Description
 * @Data 2018-09-07 7:46
 * 将队列中的event取出,将event与handler关联起来
 * <p>
 * 总体实现思路：
 * 先从上下文获取一系列实现了 Handler 接口的类，每个 Handler 实现类都关注着多个不同类型的事件
 * 我们的目标就是：找出某个类型的事件所对应的一系列 Handler
 * 这样可以根据 EventModel 中的事件类型，交由一系列 Handler 去处理。

 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(() -> {
            while(true) {
                String key = RedisKeyUtil.getEventQueueKey();
                List<String> events = jedisAdapter.brpop(0, key);

                for (String message : events) {
                    if (message.equals(key)) {
                        continue;
                    }

                    EventModel eventModel = JSON.parseObject(message, EventModel.class);
                    if (!config.containsKey(eventModel.getType())) {
                        logger.error("不能识别的事件");
                        continue;
                    }

                    for (EventHandler handler : config.get(eventModel.getType())) {
                        handler.doHandle(eventModel);
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}