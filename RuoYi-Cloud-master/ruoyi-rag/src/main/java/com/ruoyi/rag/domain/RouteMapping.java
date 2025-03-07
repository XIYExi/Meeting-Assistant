package com.ruoyi.rag.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("route_mapping")
public class RouteMapping {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String pageType;

    private String path;

    private String keywords;

}
