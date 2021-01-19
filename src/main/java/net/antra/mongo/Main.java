package net.antra.mongo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ac = getApplicationContext();
        MongoTemplate mt = (MongoTemplate)ac.getBean("mongoTemplate");

        deleteAll(mt);
        String[] color = {"RED","GREEN","YELLOW","WHITE","BLACK"};
        Random r = new Random();
        for(int i = 0 ; i < 100 ; i++) {
            save(new Apple(color[r.nextInt(5)], r.nextInt(100)) ,mt);
        }
        search("BLACK", mt);
    }

    private static void search(String color,MongoTemplate mt ) {
        try{
            Query q = new Query();
            q.addCriteria(Criteria.where("color").is(color));
            List<Apple> res = mt.find(q, Apple.class);
            res.forEach(System.out::println);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void deleteAll(MongoTemplate mt ) {
        try{
            mt.dropCollection("apple");
        }catch(Exception e){
        }
    }

    private static void save(Apple apple,MongoTemplate mt ) {
        try{
            mt.save(apple);
        }catch(Exception e){
            System.out.println("error in save");
        }
    }

    private static ApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }
}
