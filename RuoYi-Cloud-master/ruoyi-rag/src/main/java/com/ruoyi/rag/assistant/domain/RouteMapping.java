package com.ruoyi.rag.assistant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("route_mapping")
@ToString
public class RouteMapping {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String pageType;

    private String path;

    private String keywords;

    // 是否为子页面 0 - 不是 1- 是
    private Integer leaf;

    // 子页面路由id去哪查
    private String leafDb;

}
