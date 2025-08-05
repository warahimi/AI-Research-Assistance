package com.cwc.AI_Research_Assistance.service;

import com.cwc.AI_Research_Assistance.dto.ResearchRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class ResearchService {
    /*
    This method will build the prompt and query the AI model (Gemini) get the response from AI Model.
    Parse it and then return it in the form of String.
     */
    public String processContent(ResearchRequest researchRequest) {
        // Build the prompt
        String prompt = buildPrompt(researchRequest);

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
                        Map.of("test", prompt)
                })
        });
        // Parse the Response
        // Return the response
        return "";
    }

    private String buildPrompt(ResearchRequest researchRequest)
    {
        StringBuilder prompt = new StringBuilder();

        switch (researchRequest.getOperation())
        {
            case "summerize":
                prompt.append("Provide a clear and concise summary of the following text in few sentences:\n\n");
                break;
            case "suggest":
                prompt.append("Based on the following content: suggest related topics and further reading. Format the response wiht clear headings and bullet points.");
                break;
            default:
                throw new IllegalArgumentException("UNKNOW OPERATION: " + researchRequest.getOperation());
        }

        // Appending the main text / content
        prompt.append(researchRequest.getContents());
        return prompt.toString();
    }
}
