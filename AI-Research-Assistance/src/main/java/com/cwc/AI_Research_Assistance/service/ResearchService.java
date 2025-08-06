package com.cwc.AI_Research_Assistance.service;

import com.cwc.AI_Research_Assistance.dto.ResearchRequest;
import com.cwc.AI_Research_Assistance.gemini.GeminiResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.OperationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
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
    private OperationType operationType;

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
//        Map<String, Object> requestBody = Map.of("contents", new Object[]{
//                Map.of("parts", new Object[]{
//                        Map.of("text", prompt)
//                })
//        });
        Map<String, Object> requestBody = Map.of("contents", List.of(
                Map.of("parts", List.of(
                        Map.of("text", prompt)
                ))
        ));

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
            case SUMMARIZE:
                prompt.append("Summarize the following content into few concise sentences that capture the core message and main points. Avoid copying the exact phrases.\n\n");
                break;
            case SUGGEST:
                prompt.append("Analyze the following text and provide:\n" +
                        "1. Related topic suggestions\n" +
                        "2. Suggestions for further reading or research\n\n" +
                        "Use headings and bullet points.\n\n");
                break;
            case REPHRASE:
                prompt.append("Rephrase the following text to improve clarity and professionalism, while keeping the original meaning:\n\n");
                break;
            case EXTRACT_KEYWORDS:
                prompt.append("Extract and list the most relevant keywords or key phrases from the following text. Format the output as a comma-separated list:\n\n");
                break;
            case TRANSLATE:
                prompt.append("Translate the following text into French. Use natural and fluent language:\n\n");
                break;
            case DETECT_SENTIMENT:
                prompt.append("Analyze the sentiment of the following text and classify it as Positive, Negative, or Neutral. Provide a short explanation:\n\n");
                break;

            default:
                throw new IllegalArgumentException("UNKNOWN OPERATION: " + researchRequest.getOperation());
        }

        // Appending the main text / content
        prompt.append(researchRequest.getContent());
        return prompt.toString();
    }
}
