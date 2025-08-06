package com.cwc.AI_Research_Assistance.controller;

import com.cwc.AI_Research_Assistance.dto.ResearchRequest;
import com.cwc.AI_Research_Assistance.service.ResearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
