package com.cwc.AI_Research_Assistance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.OperationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchRequest {
    private String content;
    private OperationType operation;
}
