package com.antra.test;

import net.antra.mongo.Apple;
import net.antra.mongo.Application;
import net.antra.mongo.data.repo.AppleRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongoRepositoryTest {
    @Autowired
    AppleRepository repo;
    Logger log = LoggerFactory.getLogger("Test");

    /**
     * count
     */
    @Test
    public void testWorking() {
        long count = repo.count();
        log.error(String.valueOf(count));
    }

    /**
     * where color = white
     */
    @Test
    public void testSearch() {
        List<Apple> res = repo.findByColor("WHITE");
        res.stream().map(Apple::toString).forEach(log::error);
        log.error(res.size()+"");
    }

    /**
     * where color = white
     */
    @Test
    public void testSearchQuery() {
        List<Apple> res = repo.findUsingQuery();
        res.stream().map(Apple::toString).forEach(log::error);
        log.error(res.size()+"");
    }
}
