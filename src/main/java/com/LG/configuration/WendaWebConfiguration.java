package com.LG.configuration;

import com.LG.interceptor.LoginRequiredInterceptor;
import com.LG.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by LG on 2016/7/3.
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( passportInterceptor );
        registry.addInterceptor( loginRequiredInterceptor ).addPathPatterns( "/user/*" );
        super.addInterceptors( registry );
    }
}
