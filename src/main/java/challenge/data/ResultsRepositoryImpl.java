package challenge.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResultsRepositoryImpl implements ResultsRepositoryCustom {

    final
    MongoTemplate mongoTemplate;

    @Autowired
    public ResultsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Results getMax() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "count"));
        query.limit(1);
        Results maxObject = mongoTemplate.findOne(query, Results.class);
        return maxObject;
    }

    @Override
    public Results getMin() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "count"));
        query.limit(1);
        Results maxObject = mongoTemplate.findOne(query, Results.class);
        return maxObject;
    }
}
