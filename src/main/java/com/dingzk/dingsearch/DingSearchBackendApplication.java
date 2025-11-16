package com.dingzk.dingsearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.dingzk.dingsearch.mapper")
@EnableScheduling
public class DingSearchBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DingSearchBackendApplication.class, args);
    }

}
