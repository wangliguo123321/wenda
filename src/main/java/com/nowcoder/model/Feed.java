package com.nowcoder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.Map;

@Data
public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    private String data;
    private JSONObject dataJSON = null;



    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
