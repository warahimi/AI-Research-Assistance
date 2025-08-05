package com.cwc.AI_Research_Assistance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchRequest {
    private String contents;
    private String operation;
}
