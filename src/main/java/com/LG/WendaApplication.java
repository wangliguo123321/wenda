package com.LG;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.LG.dao")
public class WendaApplication {

    public static void main(String[] args) {
        SpringApplication.run( WendaApplication.class, args );
    }
}
