package com.antra.test;

import net.antra.mongo.Apple;
import net.antra.mongo.Application;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongoTestSave {

    @Autowired
    MongoTemplate mt;
    Logger log = LoggerFactory.getLogger("Test");

    @Test
    @Disabled
    public void setUp() {
        dropAll();
        String[] color = {"RED","GREEN","YELLOW","WHITE","BLACK"};
        Random r = new Random();
        for(int i = 0 ; i < 100 ; i++) {
            mt.save(new Apple(color[r.nextInt(5)], r.nextInt(100)));
        }
    }

    @Test
    public void dropAll() {
        mt.dropCollection("apple");
    }

    @Test
    public void addOne() {
        Apple apple = new Apple();
        apple.setColor("RED");
        apple.setWeight(10);
        mt.save(apple);
    }

}
