package net.antra.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by daweizhuang on 7/6/17.
 */
@SpringBootApplication
public class Application{



    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}