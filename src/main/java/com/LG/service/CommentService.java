package com.LG.service;

import com.LG.dao.CommentDAO;
import com.LG.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author liguo
 * @Description
 * @Data 2018-09-06 8:54
 */

@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List <Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectCommentByEntity( entityId, entityType );
    }

    //过滤敏感词后进行插入
    public int addComment(Comment comment) {
        comment.setContent( HtmlUtils.htmlEscape( comment.getContent() ) );
        comment.setContent( sensitiveService.filter( comment.getContent() ) );
        return commentDAO.addComment( comment ) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount( entityId, entityType );
    }

    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount( userId );
    }

    public boolean deleteComment(int commentId) {
        return commentDAO.updateStatus( commentId, 1 ) > 0;
    }

    public Comment getCommentById(int id) {
        return commentDAO.getCommentById( id );
    }

}
