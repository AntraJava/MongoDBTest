package com.antra.test;

import com.mongodb.DBObject;
import net.antra.mongo.Apple;
import net.antra.mongo.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongoTest {

    @Autowired
    MongoTemplate mt;
    Logger log = LoggerFactory.getLogger("Test");

    /**
     * Where color = white
     */
    @Test
    public void testSearch() {
        Query q = new Query();
        q.addCriteria(Criteria.where("color").is("WHITE").and("weight").gt(50));
        List<Apple> res = mt.find(q, Apple.class);
        res.stream().map(Apple::toString).forEach(log::error);
        log.error(res.size()+"");
    }
    /**
     * Where color = white and Weight gt 50;
     */
    @Test
    public void testAnd() {
        Query q = new Query();
        Criteria c = new Criteria();
        c.andOperator(Criteria.where("color").is("WHITE"), Criteria.where("weight").gt(50));
        q.addCriteria(c);
        List<Apple> res = mt.find(q, Apple.class);
        res.stream().map(Apple::toString).forEach(log::error);
        log.error(res.size()+"");
    }
    /**
     * Where color = white and Weight gt 50;  OR  color = GREEN and weight lt 50
     * { "$or" : [ { "$and" : [ { "color" : "WHITE"} , { "weight" : { "$gt" : 50}}]} , { "$and" : [ { "color" : "GREEN"} , { "weight" : { "$lt" : 50}}]}]}
     */
    @Test
    public void testOr() {
        Query q = new Query();
        Criteria c1 = new Criteria();
        c1.andOperator(Criteria.where("color").is("WHITE"), Criteria.where("weight").gt(50));
        Criteria c2 = new Criteria();
        c2.andOperator(Criteria.where("color").is("GREEN"), Criteria.where("color").lt(50));
        Criteria mainCri = new Criteria();
        mainCri.orOperator(c1, c2);
        q.addCriteria(mainCri);
        List<Apple> res = mt.find(q, Apple.class);
        res.stream().map(Apple::toString).forEach(log::error);
        log.error(res.size()+"");
    }
    /**
     * Where color = white and Weight gt 50;  OR  color = GREEN and weight lt 50
     * { "$or" : [ { "color" : "WHITE" , "weight" : { "$gt" : 50}} , { "color" : "GREEN" , "weight" : { "$lt" : 50}}]}
     */
    @Test
    public void testOr2() {
        Query q = new Query();
        Criteria c1 = Criteria.where("color").is("WHITE").and("weight").gt(50);
        Criteria c2 = Criteria.where("color").is("GREEN").and("weight").lt(50);
        Criteria mainCri = new Criteria();
        mainCri.orOperator(c1, c2);
        q.addCriteria(mainCri);
        List<Apple> res = mt.find(q, Apple.class);
        res.stream().map(Apple::toString).forEach(log::error);
        log.error(res.size()+"");
    }
    /**
     * where color = white or color = black, order by color asc
     */
    @Test
    public void testSearch2() {
        Query q = new Query();
        Criteria c = new Criteria();
        c.orOperator(Criteria.where("color").is("WHITE"), Criteria.where("color").is("BLACK"));
        Sort s = new Sort(new Sort.Order(Sort.Direction.ASC,"color"));
        q.with(s);
        q.addCriteria(c);
        List<Apple> res = mt.find(q, Apple.class);
        res.stream().map(Apple::toString).forEach(log::error);
        log.error(res.size()+"");
    }

    /**
     * where color = black and group by color and sum weight
     * projection  _id = color, weight = weight
     * { "aggregate" : "apple" , "pipeline" : [ { "$match" : { "color" : "BLACK"}} , { "$group" : { "_id" : "$color" , "weight" : { "$sum" : "$weight"}}} , { "$project" : { "color" : "$_id" , "_id" : 0 , "weight" : "$weight"}}]}
     */
    @Test
    public void testSearch3() {
        MatchOperation match = Aggregation.match(Criteria.where("color").is("BLACK"));
        ProjectionOperation projection = Aggregation.project().andExpression("_id").as("color").andExclude("_id").andExpression("weight").as("weight");
        Aggregation a = Aggregation.newAggregation(match, Aggregation.group("color").sum("weight").as("weight"),projection);
        List<Apple> res = mt.aggregate(a, Apple.class, Apple.class).getMappedResults();
        res.stream().map((i)->i+"").forEach(log::error);
    }

    /**
     * group by color and sum weight having total > 1000
     * Executing aggregation: { "aggregate" : "apple" , "pipeline" : [ { "$group" : { "_id" : "$color" , "total" : { "$sum" : "$weight"}}} , { "$match" : { "total" : { "$gt" : 1000}}} , { "$project" : { "color" : "$_id" , "_id" : 0 , "weight" : "$total"}}]}
     */
    @Test
    public void testSearch4() {
        AggregationOperation agg = Aggregation.group("color").sum("weight").as("total");
        ProjectionOperation projection = Aggregation.project().andExpression("_id").as("color").andExclude("_id").andExpression("total").as("weight");
        MatchOperation match = Aggregation.match(Criteria.where("total").gt(1000));
        Aggregation a = Aggregation.newAggregation(agg, match ,projection);
        List<Apple> res = mt.aggregate(a, Apple.class, Apple.class).getMappedResults();
        res.stream().map((i)->i+"").forEach(log::error);
    }

    /**
     * get max average weight
     */
    @Test
    public void testSearch5() {
        AggregationOperation agg = Aggregation.group("color").avg("weight").as("avgW");
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC,"avgW");
        LimitOperation limit = Aggregation.limit(1);
        ProjectionOperation proj = Aggregation.project("avgW").andExclude("_id").andExpression("_id").as("color");
        Aggregation a = Aggregation.newAggregation(agg, sort, limit, proj);
        AggregationResults<DBObject> result = mt.aggregate(a, Apple.class, DBObject.class);
        System.out.println(result.getUniqueMappedResult());
    }


}
