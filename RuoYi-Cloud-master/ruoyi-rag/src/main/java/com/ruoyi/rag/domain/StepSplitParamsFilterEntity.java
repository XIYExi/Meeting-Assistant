package com.ruoyi.rag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepSplitParamsFilterEntity implements Serializable {
    private String filter;
    private String value;
    private String order;
    private String operator;
}
