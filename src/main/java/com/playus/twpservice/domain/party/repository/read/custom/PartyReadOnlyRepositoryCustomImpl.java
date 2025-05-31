package com.playus.twpservice.domain.party.repository.read.custom;

import com.playus.twpservice.domain.party.vo.PartyInfo;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@RequiredArgsConstructor
public class PartyReadOnlyRepositoryCustomImpl implements PartyReadOnlyRepositoryCustom {

    private final MongoTemplate readMongoTemplate;

    /**
     * field는 class field 기준이 아닌, collection field 기준!
     *
     * @param matchId
     * @return
     */
    @Override
    public List<PartyInfo> findPartyInfoList(Long matchId) {

        MatchOperation matchOperation = match(new Criteria("match_id").is(matchId)
                .and("deleted_at").is(null));

        AggregationOperation partyAgeLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_age")
                        .append("let", new Document("partyId", "$_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyAge")
        );


        AggregationOperation partyJoinLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_join")
                        .append("let", new Document("partyId", "$_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyJoin")
        );

        AggregationOperation partyThumbnailUrlLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_thumbnailurl")
                        .append("let", new Document("partyId", "$_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyThumbnailUrl")
        );

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("partyId")
                .and("title").as("title")
                .and("writer_id").as("writerId")
                .and("match_id").as("matchId")
                .and("current_participants").as("currentParticipantsCount")
                .and("partyJoin.user_id").as("userIdList")
                .and("party_join_method").as("partyJoinMethod")
                .and("party_gender").as("partyGender")
                .and("maximum_participants").as("maximumParticipants")
                .and("partyAge.age").as("ages")
                .and("partyThumbnailUrl.thumbnailUrl").as("thumbnailUrls");

        Aggregation aggregation = newAggregation(
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

        MatchOperation matchOperation = match(new Criteria("_id").is(partyId)
                .and("deleted_at").is(null));

        AggregationOperation partyAgeLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_age")
                        .append("let", new Document("partyId", "$_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyAge")
        );

        AggregationOperation partyJoinLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_join")
                        .append("let", new Document("partyId", "$_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyJoin")
        );

        AggregationOperation partyThumbnailUrlLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_thumbnailurl")
                        .append("let", new Document("partyId", "$_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyThumbnailUrl")
        );

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id").as("partyId")
                .and("title").as("title")
                .and("text").as("text")
                .and("writer_id").as("writerId")
                .and("match_id").as("matchId")
                .and("chat_room_id").as("chatRoomId")
                .and("current_participants").as("currentParticipantsCount")
                .and("partyJoin.user_id").as("userIdList")
                .and("party_join_method").as("partyJoinMethod")
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


//    LookupOperation partyLookupOperation = lookup("party", "_id", "_id", "party");
//    LookupOperation partyAgeLookupOperation = lookup("party_age", "party_id", "party_id", "partyAge");
//    LookupOperation partyThumbnailLookupOperation = lookup("party_thumbnailUrl", "party_id", "party_id", "partyThumbnailUrl");

    @Override
    public List<PartyInfo> findAppliedParties(Long userId) {

        MatchOperation matchOperation = match(new Criteria("user_id").is(userId)
                .and("deleted_at").is(null));

        AggregationOperation partyLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party")
                        .append("let", new Document("partyId", "$party_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "party")
        );

        AggregationOperation partyAgeLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_age")
                        .append("let", new Document("partyId", "$party_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyAge")
        );

        AggregationOperation partyThumbnailLookupOperation = context -> new Document(
                "$lookup",
                new Document("from", "party_thumbnailUrl")
                        .append("let", new Document("partyId", "$party_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$party_id", "$$partyId")),
                                        new Document("$eq", Arrays.asList("$deleted_at", null))
                                ))))
                        ))
                        .append("as", "partyThumbnailUrl")
        );

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("party_id").as("partyId")
                .and("party.title").arrayElementAt(0).as("title")
                .and("partyAge.age").as("ages")
                .and("party.party_gender").arrayElementAt(0).as("partyGender")
                .and("party_join_request_status").as("partyJoinRequestStatus")
                .and("party.writer_id").arrayElementAt(0).as("writerId")
                .and("party.current_participants").arrayElementAt(0).as("currentParticipantsCount");

        Aggregation aggregation = Aggregation.newAggregation(
             matchOperation,
             partyLookupOperation,
             partyAgeLookupOperation,
             partyThumbnailLookupOperation,
             projectionOperation
        );

        AggregationResults<PartyInfo> results = readMongoTemplate.aggregate(aggregation, "party_join", PartyInfo.class);

        return results.getMappedResults();
    }
}

