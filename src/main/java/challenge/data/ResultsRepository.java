package challenge.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResultsRepository extends MongoRepository <Results, String> {
    Results getMax();
    Results getMin();
}
