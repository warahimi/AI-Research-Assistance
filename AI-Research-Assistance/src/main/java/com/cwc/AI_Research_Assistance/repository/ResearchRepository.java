package com.cwc.AI_Research_Assistance.repository;

import com.cwc.AI_Research_Assistance.model.Research;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResearchRepository extends MongoRepository<Research, String> {
    List<Research> findByOperation(String name);
}
