package challenge.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegisteredNodeRepository extends MongoRepository<RegisteredNode, String> {
}
