package com.cwc.AI_Research_Assistance.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Document(collection = "notes")
public class Research {
    @Id
    private String id;
    private String content;
    private OperationType operation;
}
