package com.ruoyi.rag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StepSplitEntity implements Serializable {
    private Integer step;
    private String intent;
    private String query;
    private String route;
    private String db;
    private String keywords;
    private Integer dependency;
    private List<StepSplitParamsFilterEntity> filters;

}
