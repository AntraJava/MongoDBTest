package net.antra.mongo.data.repo;

import net.antra.mongo.Apple;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppleRepository extends MongoRepository<Apple, String> {

}
