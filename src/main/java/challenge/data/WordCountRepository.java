package challenge.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordCountRepository extends MongoRepository<WordCount, String>{

}
