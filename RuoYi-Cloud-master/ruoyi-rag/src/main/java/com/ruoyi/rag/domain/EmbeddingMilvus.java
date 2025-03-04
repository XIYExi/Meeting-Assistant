package com.ruoyi.rag.domain;

import lombok.Data;

@Data
public class EmbeddingMilvus {
    public static final String COLLECTION_NAME = "meeting_home";

    public static final Integer FEATURE_DIM = 1024;

    public static final Integer SEARCH_K = 5;                       // TopK

    public static final String SEARCH_PARAM = "{\"nprobe\":10}";    // Params

    @Data
    public static class Field {
        public static final String composite_id = "composite_id";
        public static final String original_id = "original_id";
        public static final String type = "type";
        public static final String feature = "meeting_feature";
    }
}
