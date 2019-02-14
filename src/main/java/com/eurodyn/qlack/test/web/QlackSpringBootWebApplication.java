package com.eurodyn.qlack.test.web;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories("com.eurodyn.qlack.fuse.aaa.repository")
@EntityScan("com.eurodyn.qlack.fuse.aaa.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.aaa",
    "com.eurodyn.qlack.fuse.security",
    "com.eurodyn.qlack.test.web.*"
})

public class QlackSpringBootWebApplication {

    public static void main(String[] args) {
    	SpringApplication app = new SpringApplication();
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}