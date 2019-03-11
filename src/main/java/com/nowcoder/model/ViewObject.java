package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

//vo用来传递对象和视图中间的一个对象；可以将所有东西通过set放进来，直接返回给前端；
//主页广场上面东西的传递；

public class ViewObject {
    private Map <String, Object> objs = new HashMap ();

    public void set(String key, Object value) {
        objs.put( key, value );
    }

    public Object get(String key) {
        return objs.get( key );
    }
}
