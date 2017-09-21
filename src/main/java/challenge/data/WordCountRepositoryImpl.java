package challenge.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

@Repository
public class WordCountRepositoryImpl implements WordCountRepositoryCustom {

    final
    MongoTemplate mongoTemplate;

    @Autowired
    public WordCountRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Results> getResults() {
        Aggregation agg = Aggregation.newAggregation(group("word").sum("count").as("count"));
        AggregationResults<Results> groupResults = mongoTemplate.aggregate(agg, "wordCount", Results.class);
        return groupResults.getMappedResults();
    }
}
