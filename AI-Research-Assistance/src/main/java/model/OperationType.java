package model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OperationType {
    SUMMARIZE, SUGGEST, REPHRASE, TRANSLATE, EXTRACT_KEYWORDS,DETECT_SENTIMENT;

    //This allows JSON like "summarize" or "rephrase" to map correctly.
    @JsonCreator
    public static OperationType fromString(String key) {
        return OperationType.valueOf(key.toUpperCase());
    }
}
