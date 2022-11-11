package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lenovo
 * @Title: SkeyeImBackgroundServiceApplication
 * @Package com.controller
 * @Description: ProjectModuleController
 * @date 2022/11/9 15:53
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SkeyeImBackgroundServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkeyeImBackgroundServiceApplication.class, args);
    }

}
