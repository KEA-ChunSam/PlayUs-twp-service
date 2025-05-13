package com.playus.twpservice.domain.party.repository.read.custom;

import com.playus.twpservice.domain.party.vo.PartySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@RequiredArgsConstructor
public class PartyReadOnlyRepositoryCustomImpl implements PartyReadOnlyRepositoryCustom {

    private final MongoTemplate readMongoTemplate;

    /**
     *
     * field는 class field 기준이 아닌, collection field 기준!
     * @param matchId
     * @return
     */
    @Override
    public List<PartySummary> findPartySummariesByMatchId(Long matchId) {

        MatchOperation matchOperation = match(new Criteria("match_id").is(matchId));

        LookupOperation partyAgeLookupOperation = lookup("party_age", "_id", "party_id", "partyAge");
        LookupOperation partyJoinLookupOperation = lookup("party_join", "_id", "party_id", "partyJoin");
        LookupOperation partyThumbnailUrlLookupOperation = lookup("party_thumbnailurl", "_id", "party_id", "partyThumbnailUrl");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("partyId")
                .and("title").as("title")
                .and("writer_id").as("writerId")
                .and("partyJoinMethod").as("partyJoinMethod")
                .and("party_gender").as("partyGender")
                .and("maximum_participants").as("maximumParticipants")
                .and("partyAge.age").as("ages")
                .and("partyJoin").size().as("currentParticipantsCount")
                .and("partyThumbnailUrl.thumbnailUrl").as("thumbnailUrls");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                partyAgeLookupOperation,
                partyJoinLookupOperation,
                partyThumbnailUrlLookupOperation,
                projectionOperation
        );

        AggregationResults<PartySummary> results = readMongoTemplate.aggregate(aggregation, "party", PartySummary.class);

        return results.getMappedResults();
    }
}

