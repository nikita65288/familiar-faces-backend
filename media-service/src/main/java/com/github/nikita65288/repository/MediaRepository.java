package com.github.nikita65288.repository;

import com.github.nikita65288.entity.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends MongoRepository<MediaFile, String> {
}
