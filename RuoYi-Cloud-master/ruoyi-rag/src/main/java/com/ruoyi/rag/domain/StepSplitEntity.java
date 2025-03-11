package com.ruoyi.rag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StepSplitEntity implements Serializable {
    private String intent;
    private StepSplitParamsEntity params;
}
