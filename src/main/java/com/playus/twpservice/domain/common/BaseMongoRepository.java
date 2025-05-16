package com.playus.twpservice.domain.common;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseMongoRepository<T, ID> extends MongoRepository<T, ID> {

    @Query("{ 'id': ?0 }")
    @Update("{ '$set' : { 'deleted_at' : new Date(), 'is_deleted': true } }")
    void deleteById(ID id);

    @Query("{ 'id': ?0, 'deleted_at': null }")
    Optional<T> findById(ID id);

    @Query("{ 'id': ?0, 'deleted_at': { $ne: null } }")
    Optional<T> findByIdDeleted(ID id);

    @Query("{ 'id': ?0, 'deleted_at': null }")
    T findOne(ID id);

    @Override
    @Query("{ 'deleted_at': null }")
    List<T> findAll();

    @Override
    @Query(value = "{ deleted_at: null }", count = true)
    long count();

    @Query(value = "{ 'id': ?0, 'deleted_at': null }", exists = true)
    boolean existsById(ID id);

    @Query("{ 'id': ?0 }")
    @Update("{ '$set' : { 'deleted_at' : null } }")
    void restoreById(ID id);
}
