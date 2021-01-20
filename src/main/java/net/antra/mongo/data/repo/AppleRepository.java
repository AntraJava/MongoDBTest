package net.antra.mongo.data.repo;

import net.antra.mongo.Apple;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AppleRepository extends MongoRepository<Apple, String> {
    List<Apple> findByColor(String color);

    @Query("{\"color\": {\"$in\":[\"WHITE\", \"RED\"]}}")
    List<Apple> findUsingQuery();
}
