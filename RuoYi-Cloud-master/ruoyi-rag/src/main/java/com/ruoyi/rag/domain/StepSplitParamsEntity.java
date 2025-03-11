package com.ruoyi.rag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepSplitParamsEntity implements Serializable {
    private String keywords;
    private String db;
    private List<StepSplitParamsFilterEntity> filters;
    private Integer dependency;
}
