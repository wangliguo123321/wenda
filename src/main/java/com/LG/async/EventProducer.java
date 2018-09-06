package com.LG.async;

import com.LG.util.JedisAdapter;
import com.LG.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LG on 2016/7/30.
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString( eventModel );
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush( key, json );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
