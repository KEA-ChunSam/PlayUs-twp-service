package com.playus.twpservice.domain.party.repository.read.custom;

import com.playus.twpservice.domain.party.vo.PartyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Optional;

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
    public List<PartyInfo> findPartyInfo(Long matchId) {

        MatchOperation matchOperation = match(new Criteria("match_id").is(matchId));

        LookupOperation partyAgeLookupOperation = lookup("party_age", "_id", "party_id", "partyAge");
        LookupOperation partyJoinLookupOperation = lookup("party_join", "_id", "party_id", "partyJoin");
        LookupOperation partyThumbnailUrlLookupOperation = lookup("party_thumbnailurl", "_id", "party_id", "partyThumbnailUrl");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("partyId")
                .and("title").as("title")
                .and("writer_id").as("writerId")
                .and("match_id").as("matchId")
                .and("current_participants").as("currentParticipantsCount")
                .and("partyJoin.user_id").as("userIdList")
                .and("partyJoinMethod").as("partyJoinMethod")
                .and("party_gender").as("partyGender")
                .and("maximum_participants").as("maximumParticipants")
                .and("partyAge.age").as("ages")
                .and("partyThumbnailUrl.thumbnailUrl").as("thumbnailUrls");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                partyAgeLookupOperation,
                partyJoinLookupOperation,
                partyThumbnailUrlLookupOperation,
                projectionOperation
        );

        AggregationResults<PartyInfo> results = readMongoTemplate.aggregate(aggregation, "party", PartyInfo.class);

        return results.getMappedResults();
    }

    @Override
    public Optional<PartyInfo> findPartyDetail(Long partyId) {
        MatchOperation matchOperation = match(new Criteria("_id").is(partyId));

        LookupOperation partyAgeLookupOperation = lookup("party_age", "_id", "party_id", "partyAge");
        LookupOperation partyJoinLookupOperation = lookup("party_join", "_id", "party_id", "partyJoin");
        LookupOperation partyThumbnailUrlLookupOperation = lookup("party_thumbnailurl", "_id", "party_id", "partyThumbnailUrl");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("partyId")
                .and("title").as("title")
                .and("text").as("text")
                .and("writer_id").as("writerId")
                .and("match_id").as("matchId")
                .and("current_participants").as("currentParticipantsCount")
                .and("partyJoin.user_id").as("userIdList")
                .and("partyJoinMethod").as("partyJoinMethod")
                .and("party_gender").as("partyGender")
                .and("maximum_participants").as("maximumParticipants")
                .and("partyAge.age").as("ages")
                .and("partyThumbnailUrl.thumbnailUrl").as("thumbnailUrls");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                partyAgeLookupOperation,
                partyJoinLookupOperation,
                partyThumbnailUrlLookupOperation,
                projectionOperation
        );

        AggregationResults<PartyInfo> results = readMongoTemplate.aggregate(aggregation, "party", PartyInfo.class);
        List<PartyInfo> resultList = results.getMappedResults();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }
}

