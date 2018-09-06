package com.LG.async.handler;

import com.LG.async.EventHandler;
import com.LG.async.EventModel;
import com.LG.async.EventType;
import com.LG.model.Message;
import com.LG.model.User;
import com.LG.service.MessageServices;
import com.LG.service.UserService;
import com.LG.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by LG on 2016/7/30.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageServices messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId( WendaUtil.SYSTEM_USERID );
        message.setToId( model.getEntityOwnerId() );
        message.setCreatedDate( new Date() );
        User user = userService.getUser( model.getActorId() );
        message.setContent( "用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt( "questionId" ) );

        messageService.addMessage( message );
    }

    @Override
    public List <EventType> getSupportEventTypes() {
        return Arrays.asList( EventType.LIKE );
    }
}
