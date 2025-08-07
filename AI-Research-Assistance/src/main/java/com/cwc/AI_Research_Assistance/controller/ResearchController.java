package com.cwc.AI_Research_Assistance.controller;

import com.cwc.AI_Research_Assistance.dto.ResearchRequest;
import com.cwc.AI_Research_Assistance.model.Research;
import com.cwc.AI_Research_Assistance.service.ResearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.cwc.AI_Research_Assistance.model.OperationType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/researches")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ResearchController {
    private final ResearchService researchService;

    @PostMapping
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest researchRequest)
    {
        log.info("############### Received Request #####################");
        log.info(researchRequest.toString());
        String result = researchService.processContent(researchRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/save-note")
    public ResponseEntity<Research> saveNote(@RequestBody ResearchRequest researchRequest)
    {
        log.info("############### Received Request to Save Note #####################");
        log.info(researchRequest.toString());
        Research result = researchService.saveReasearch(researchRequest);
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<List<Research>> getAllResearches() {
        log.info("############### Fetching All Researches #####################");
        List<Research> researches = researchService.getAllResearches();
        return ResponseEntity.ok(researches);
    }
    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<Research>> getResearchByOperation(@PathVariable String operation) {
        log.info("############### Fetching Researches by Operation: {} #####################", operation);
        List<Research> researches = researchService.getResearchByOperation(OperationType.valueOf(operation.toUpperCase()));
        return ResponseEntity.ok(researches);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Research> getResearchById(@PathVariable String id) {
        log.info("############### Fetching Research by ID: {} #####################", id);
        Research research = researchService.getResearchById(id);
        if (research != null) {
            return ResponseEntity.ok(research);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteResearchById(@PathVariable String id) {
        log.info("############### Deleting Research by ID: {} #####################", id);
        boolean deleted = researchService.deleteResearchById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
