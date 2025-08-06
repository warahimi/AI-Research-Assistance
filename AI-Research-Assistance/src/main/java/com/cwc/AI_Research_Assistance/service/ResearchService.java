package com.cwc.AI_Research_Assistance.service;

import com.cwc.AI_Research_Assistance.dto.ResearchRequest;
import com.cwc.AI_Research_Assistance.gemini.GeminiResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResearchService {
    @Value("${gemini.api.url}")
    private String geminiAPIUrl;
    @Value("${gemini.api.key}")
    private String geminiAPIKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    /*
    This method will build the prompt and query the AI model (Gemini) get the response from AI Model.
    Parse it and then return it in the form of String.
     */
    public String processContent(ResearchRequest researchRequest) {
        // Build the prompt
        String prompt = buildPrompt(researchRequest);
        log.info("############### Prompt Created ###############");
        log.info(prompt);
        // Query the AI Model
        /*
            {
                "contents": [
                  {
                    "parts": [
                      {
                        "text": "Content"
                      }
                    ]
                  }
                ]
             }
         */
        Map<String, Object> requestBody = Map.of("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", prompt)
                })
        });

        // API Call to Gemini using WebClient
        String response = webClient.post()
                .uri(geminiAPIUrl + geminiAPIKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        // Parse the Response
        // Return the response
        log.info("################# Response from Gemini ##################");
        log.info(response);
        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try{
            // Converting the JSON formt to Object of type GeminiResponseModel
            GeminiResponseModel geminiResponse = objectMapper.readValue(response, GeminiResponseModel.class);
            if(geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty())
            {
                GeminiResponseModel.Candidates firstCandidate = geminiResponse.getCandidates().get(0);
                if(firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty() &&
                        !firstCandidate.getContent().getParts().get(0).getText().isEmpty())
                {
                    // return the text
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
        }catch (Exception e)
        {
            return "Error parsing the response: "+ e.getMessage();
        }
        return "No content found in the response";
    }

    private String buildPrompt(ResearchRequest researchRequest)
    {
        StringBuilder prompt = new StringBuilder();

        switch (researchRequest.getOperation())
        {
            case "summarize":
                prompt.append("Provide a clear and concise summary of the following text in few sentences:\n\n");
                break;
            case "suggest":
                prompt.append("Based on the following content: suggest related topics and further reading. Format the response wiht clear headings and bullet points.");
                break;
            default:
                throw new IllegalArgumentException("UNKNOW OPERATION: " + researchRequest.getOperation());
        }

        // Appending the main text / content
        prompt.append(researchRequest.getContent());
        return prompt.toString();
    }
}
