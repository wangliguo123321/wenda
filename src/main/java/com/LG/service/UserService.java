package com.LG.service;

import com.LG.dao.LoginTicketDAO;
import com.LG.dao.UserDAO;
import com.LG.model.LoginTicket;
import com.LG.model.User;
import com.LG.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger( UserService.class );
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public User selectByName(String name) {
        return userDAO.selectByName( name );
    }

    public Map <String, Object> register(String username, String password) {
        Map <String, Object> map = new HashMap <>();
        //做判断的类，判断账号密码非空；
        if (StringUtils.isBlank( username )) {
            map.put( "msg", "用户名不能为空" );
            return map;
        }
        if (StringUtils.isBlank( password )) {
            map.put( "msg", "密码不能为空" );
            return map;
        }
        //做判断，判断注册用户数据库中不存在；
        User user = userDAO.selectByName( username );

        if (user != null) {
            map.put( "msg", "用户名已经被注册" );
            return map;
        }

        //增强密码强度，加sort
        //将用户增加到数据库；
        user = new User();
        user.setName( username );
        user.setSalt( UUID.randomUUID().toString().substring( 0, 5 ) );
        String head = String.format( "http://images.LG.com/head/%dt.png", new Random().nextInt( 1000 ) );
        user.setHeadUrl( head );
        user.setPassword( WendaUtil.MD5( password + user.getSalt() ) );
        userDAO.addUser( user );

        // 登陆
        String ticket = addLoginTicket( user.getId() );
        map.put( "ticket", ticket );
        return map;
    }


    public Map <String, Object> login(String username, String password) {
        Map <String, Object> map = new HashMap <String, Object>();
        if (StringUtils.isBlank( username )) {
            map.put( "msg", "用户名不能为空" );
            return map;
        }

        if (StringUtils.isBlank( password )) {
            map.put( "msg", "密码不能为空" );
            return map;
        }

        User user = userDAO.selectByName( username );

        if (user == null) {
            map.put( "msg", "用户名不存在" );
            return map;
        }

        if (!WendaUtil.MD5( password + user.getSalt() ).equals( user.getPassword() )) {
            map.put( "msg", "密码不正确" );
            return map;
        }

        String ticket = addLoginTicket( user.getId() );
        map.put( "ticket", ticket );
        map.put( "userId", user.getId() );
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId( userId );
        Date date = new Date();
        date.setTime( date.getTime() + 1000 * 3600 * 24 );
        ticket.setExpired( date );
        ticket.setStatus( 0 );
        ticket.setTicket( UUID.randomUUID().toString().replaceAll( "-", "" ) );
        loginTicketDAO.addTicket( ticket );
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById( id );
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus( ticket, 1 );
    }
}