package com.ruoyi.rag.domain;

import lombok.Data;

public class EmbeddingRouteMappingMilvus {
    public static final String COLLECTION_NAME = "route_mapping_home";

    public static final Integer FEATURE_DIM = 1024;

    public static final Integer SEARCH_K = 2;                       // TopK

    public static final String SEARCH_PARAM = "{\"nprobe\":10}";    // Params

    @Data
    public static class Field {
        public static final String id = "id";
        public static final String originalId = "original_id";
        public static final String feature = "route_feature";
    }
}
