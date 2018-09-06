package com.LG.service;

import com.LG.dao.MessageDAO;
import com.LG.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liguo
 * @Description
 * @Data 2018-09-06 14:55
 */

@Service
public class MessageServices {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent( sensitiveService.filter( message.getContent() ) );
        return messageDAO.addMessage( message ) > 0 ? message.getId() : 0;
    }

    public List <Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail( conversationId, offset, limit );
    }

    public List <Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList( userId, offset, limit );
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnreadCount( userId, conversationId );
    }
}
