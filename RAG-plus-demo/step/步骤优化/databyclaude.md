# Java代码实现 - 解析和处理会议查询JSON

下面是基于SpringBoot和MyBatis-Plus实现的处理函数，专注于处理intent为"query"的请求。

```java
package com.example.meeting.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.entity.*;
import com.example.meeting.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MeetingQueryService {

    @Autowired
    private MeetingMapper meetingMapper;
    
    @Autowired
    private MeetingGeoMapper meetingGeoMapper;
    
    @Autowired
    private MeetingClipMapper meetingClipMapper;
    
    /**
     * 处理查询请求的主函数
     * @param jsonStr JSON字符串
     * @return 查询结果
     */
    public Map<String, Object> processQuery(String jsonStr) {
        JSONArray steps = JSON.parseArray(jsonStr);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> stepResults = new HashMap<>();
        
        for (int i = 0; i < steps.size(); i++) {
            JSONObject step = steps.getJSONObject(i);
            String intent = step.getString("intent");
            int stepNum = step.getIntValue("step");
            String stepKey = "step" + stepNum;
            
            // 检查依赖
            int dependency = step.getIntValue("dependency");
            if (dependency > 0 && !stepResults.containsKey("step" + dependency)) {
                resultMap.put("error", "依赖步骤 " + dependency + " 的结果不存在");
                return resultMap;
            }
            
            // 根据intent类型处理
            Object stepResult = null;
            if ("query".equals(intent)) {
                stepResult = handleQueryIntent(step, stepResults);
            } else if ("route".equals(intent)) {
                stepResult = handleRouteIntent(step, stepResults);
            } else if ("action".equals(intent)) {
                stepResult = handleActionIntent(step, stepResults);
            }
            
            stepResults.put(stepKey, stepResult);
        }
        
        resultMap.put("results", stepResults);
        return resultMap;
    }
    
    /**
     * 处理查询意图
     * @param step 步骤JSON对象
     * @param stepResults 之前步骤的结果
     * @return 查询结果
     */
    private Object handleQueryIntent(JSONObject step, Map<String, Object> stepResults) {
        String subtype = step.getString("subtype");
        String db = step.getString("db");
        
        switch (subtype) {
            case "meeting":
                return handleMeetingQuery(step, stepResults);
            case "file":
                return handleFileQuery(step, stepResults);
            default:
                return null;
        }
    }
    
    /**
     * 处理会议查询
     * @param step 步骤JSON对象
     * @param stepResults 之前步骤的结果
     * @return 查询结果
     */
    private Object handleMeetingQuery(JSONObject step, Map<String, Object> stepResults) {
        String db = step.getString("db");
        int dependency = step.getIntValue("dependency");
        JSONArray filters = step.getJSONArray("filters");
        JSONObject dataBindings = step.getJSONObject("data_bindings");
        
        // 输出字段处理
        List<String> outputFields = new ArrayList<>();
        if (step.containsKey("output_fields")) {
            JSONArray outputFieldsArray = step.getJSONArray("output_fields");
            for (int i = 0; i < outputFieldsArray.size(); i++) {
                outputFields.add(outputFieldsArray.getString(i));
            }
        }
        
        // 根据数据库类型选择不同的处理方式
        if ("meeting".equals(db)) {
            QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
            
            // 处理依赖和数据绑定
            if (dependency > 0) {
                Map<String, Object> dependencyResult = (Map<String, Object>) stepResults.get("step" + dependency);
                if (dependencyResult != null) {
                    for (Map.Entry<String, Object> entry : dataBindings.entrySet()) {
                        String bindValue = entry.getValue().toString();
                        if (bindValue.startsWith("step")) {
                            String[] parts = bindValue.split("\\.");
                            if (parts.length == 2) {
                                String dependencyField = parts[1];
                                Object value = dependencyResult.get(dependencyField);
                                if (value != null) {
                                    String targetField = entry.getKey().split("\\.")[1];
                                    queryWrapper.eq(camelToUnderline(targetField), value);
                                }
                            }
                        }
                    }
                }
            }
            
            // 处理过滤条件
            for (int i = 0; i < filters.size(); i++) {
                JSONObject filter = filters.getJSONObject(i);
                String field = filter.getString("field");
                String operator = filter.getString("operator");
                String value = filter.getString("value");
                
                // 处理动态值引用
                if (value != null && value.startsWith("step")) {
                    String[] parts = value.split("\\.");
                    if (parts.length == 2) {
                        String refStep = parts[0];
                        String refField = parts[1];
                        Map<String, Object> refStepResult = (Map<String, Object>) stepResults.get(refStep);
                        if (refStepResult != null && refStepResult.containsKey(refField)) {
                            value = refStepResult.get(refField).toString();
                        }
                    }
                }
                
                // 处理特殊函数
                if (value != null && value.startsWith("escape(")) {
                    String innerValue = value.substring(7, value.length() - 1);
                    if ("tomorrow%".equals(innerValue)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        value = sdf.format(calendar.getTime()) + "%";
                    }
                }
                
                // 处理标题查询 - 使用Milvus优化
                if ("title".equals(field) && "LIKE".equals(operator) && value.contains("%")) {
                    String searchTitle = value.replace("%", "");
                    List<String> meetingIds = queryMeeting(searchTitle);
                    if (meetingIds != null && !meetingIds.isEmpty()) {
                        queryWrapper.in("meeting_id", meetingIds);
                        continue;  // 跳过下面的普通查询条件
                    }
                }
                
                // 构建普通查询条件
                switch (operator) {
                    case "=":
                        if ("NULL".equals(value)) {
                            queryWrapper.isNull(camelToUnderline(field));
                        } else {
                            queryWrapper.eq(camelToUnderline(field), value);
                        }
                        break;
                    case "LIKE":
                        queryWrapper.like(camelToUnderline(field), value);
                        break;
                    case ">":
                        queryWrapper.gt(camelToUnderline(field), value);
                        break;
                    case "<":
                        queryWrapper.lt(camelToUnderline(field), value);
                        break;
                    case ">=":
                        queryWrapper.ge(camelToUnderline(field), value);
                        break;
                    case "<=":
                        queryWrapper.le(camelToUnderline(field), value);
                        break;
                }
            }
            
            // 执行查询
            List<Meeting> meetings = meetingMapper.selectList(queryWrapper);
            
            // 处理结果
            if (!outputFields.isEmpty()) {
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Meeting meeting : meetings) {
                    Map<String, Object> resultItem = new HashMap<>();
                    for (String field : outputFields) {
                        switch (field) {
                            case "meeting_id":
                                resultItem.put("meeting_id", meeting.getMeetingId());
                                break;
                            case "title":
                                resultItem.put("title", meeting.getTitle());
                                break;
                            case "beginTime":
                                resultItem.put("beginTime", meeting.getBeginTime());
                                break;
                            case "geo_id":
                                resultItem.put("geo_id", meeting.getGeoId());
                                break;
                            // 添加其他需要的字段
                        }
                    }
                    resultList.add(resultItem);
                }
                return resultList;
            } else {
                return meetings;
            }
        } else if ("meeting_geo".equals(db)) {
            QueryWrapper<MeetingGeo> queryWrapper = new QueryWrapper<>();
            
            // 处理依赖和数据绑定
            if (dependency > 0) {
                Map<String, Object> dependencyResult = (Map<String, Object>) stepResults.get("step" + dependency);
                if (dependencyResult != null) {
                    for (Map.Entry<String, Object> entry : dataBindings.entrySet()) {
                        String bindValue = entry.getValue().toString();
                        if (bindValue.startsWith("step")) {
                            String[] parts = bindValue.split("\\.");
                            if (parts.length == 2) {
                                String dependencyField = parts[1];
                                Object value = dependencyResult.get(dependencyField);
                                if (value != null) {
                                    String targetField = entry.getKey().split("\\.")[1];
                                    queryWrapper.eq(camelToUnderline(targetField), value);
                                }
                            }
                        }
                    }
                }
            }
            
            // 处理过滤条件
            for (int i = 0; i < filters.size(); i++) {
                JSONObject filter = filters.getJSONObject(i);
                String field = filter.getString("field");
                String operator = filter.getString("operator");
                String value = filter.getString("value");
                
                // 处理标题查询 - 使用Milvus优化
                if ("title".equals(field) && "=".equals(operator)) {
                    List<String> meetingIds = queryMeeting(value);
                    if (meetingIds != null && !meetingIds.isEmpty()) {
                        queryWrapper.in("meeting_id", meetingIds);
                        continue;  // 跳过下面的普通查询条件
                    }
                }
                
                // 构建普通查询条件
                switch (operator) {
                    case "=":
                        queryWrapper.eq(camelToUnderline(field), value);
                        break;
                    case "LIKE":
                        queryWrapper.like(camelToUnderline(field), value);
                        break;
                }
            }
            
            // 执行查询
            List<MeetingGeo> meetingGeos = meetingGeoMapper.selectList(queryWrapper);
            
            // 处理结果
            if (!outputFields.isEmpty()) {
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (MeetingGeo meetingGeo : meetingGeos) {
                    Map<String, Object> resultItem = new HashMap<>();
                    for (String field : outputFields) {
                        switch (field) {
                            case "geo_id":
                                resultItem.put("geo_id", meetingGeo.getGeoId());
                                break;
                            case "address":
                                resultItem.put("address", meetingGeo.getAddress());
                                break;
                            case "location":
                                resultItem.put("location", meetingGeo.getLocation());
                                break;
                            // 添加其他需要的字段
                        }
                    }
                    resultList.add(resultItem);
                }
                return resultList;
            } else {
                return meetingGeos;
            }
        }
        
        return null;
    }
    
    /**
     * 处理文件查询
     * @param step 步骤JSON对象
     * @param stepResults 之前步骤的结果
     * @return 查询结果
     */
    private Object handleFileQuery(JSONObject step, Map<String, Object> stepResults) {
        String db = step.getString("db");
        int dependency = step.getIntValue("dependency");
        JSONArray filters = step.getJSONArray("filters");
        JSONObject dataBindings = step.getJSONObject("data_bindings");
        
        // 输出字段处理
        List<String> outputFields = new ArrayList<>();
        if (step.containsKey("output_fields")) {
            JSONArray outputFieldsArray = step.getJSONArray("output_fields");
            for (int i = 0; i < outputFieldsArray.size(); i++) {
                outputFields.add(outputFieldsArray.getString(i));
            }
        }
        
        if ("meeting_clip".equals(db)) {
            QueryWrapper<MeetingClip> queryWrapper = new QueryWrapper<>();
            
            // 处理依赖和数据绑定
            if (dependency > 0) {
                Map<String, Object> dependencyResult = (Map<String, Object>) stepResults.get("step" + dependency);
                if (dependencyResult != null) {
                    for (Map.Entry<String, Object> entry : dataBindings.entrySet()) {
                        String bindValue = entry.getValue().toString();
                        if (bindValue.startsWith("step")) {
                            String[] parts = bindValue.split("\\.");
                            if (parts.length == 2) {
                                String dependencyField = parts[1];
                                Object value = dependencyResult.get(dependencyField);
                                if (value != null) {
                                    String targetField = entry.getKey();
                                    queryWrapper.eq(camelToUnderline(targetField), value);
                                }
                            }
                        }
                    }
                }
            }
            
            // 处理过滤条件
            for (int i = 0; i < filters.size(); i++) {
                JSONObject filter = filters.getJSONObject(i);
                String field = filter.getString("field");
                String operator = filter.getString("operator");
                String value = filter.getString("value");
                
                // 处理标题查询 - 使用Milvus优化
                if ("title".equals(field) && "LIKE".equals(operator) && value.contains("%")) {
                    String searchTitle = value.replace("%", "");
                    List<String> meetingIds = queryMeeting(searchTitle);
                    if (meetingIds != null && !meetingIds.isEmpty()) {
                        queryWrapper.in("meeting_id", meetingIds);
                        continue;  // 跳过下面的普通查询条件
                    }
                }
                
                // 构建普通查询条件
                switch (operator) {
                    case "=":
                        if ("NULL".equals(value)) {
                            queryWrapper.isNull(camelToUnderline(field));
                        } else {
                            queryWrapper.eq(camelToUnderline(field), value);
                        }
                        break;
                    case "LIKE":
                        queryWrapper.like(camelToUnderline(field), value);
                        break;
                }
            }
            
            // 执行查询
            List<MeetingClip> meetingClips = meetingClipMapper.selectList(queryWrapper);
            
            // 处理结果
            if (!outputFields.isEmpty()) {
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (MeetingClip meetingClip : meetingClips) {
                    Map<String, Object> resultItem = new HashMap<>();
                    for (String field : outputFields) {
                        switch (field) {
                            case "file_id":
                                resultItem.put("file_id", meetingClip.getFileId());
                                break;
                            case "meeting_id":
                                resultItem.put("meeting_id", meetingClip.getMeetingId());
                                break;
                            case "file_name":
                                resultItem.put("file_name", meetingClip.getFileName());
                                break;
                            // 添加其他需要的字段
                        }
                    }
                    resultList.add(resultItem);
                }
                return resultList;
            } else {
                return meetingClips;
            }
        }
        
        return null;
    }
    
    /**
     * 处理路由意图
     * @param step 步骤JSON对象
     * @param stepResults 之前步骤的结果
     * @return 路由结果
     */
    private Object handleRouteIntent(JSONObject step, Map<String, Object> stepResults) {
        // 这里实现路由逻辑
        String path = step.getJSONObject("params").getString("path");
        String query = step.getJSONObject("params").getString("query");
        
        // 处理查询参数中的变量
        if (query != null && query.contains("${")) {
            int startIdx = query.indexOf("${");
            int endIdx = query.indexOf("}", startIdx);
            if (startIdx >= 0 && endIdx > startIdx) {
                String varName = query.substring(startIdx + 2, endIdx);
                String[] parts = varName.split("\\.");
                if (parts.length == 2) {
                    Map<String, Object> stepResult = (Map<String, Object>) stepResults.get(parts[0]);
                    if (stepResult != null && stepResult.containsKey(parts[1])) {
                        String value = stepResult.get(parts[1]).toString();
                        query = query.replace("${" + varName + "}", value);
                    }
                }
            }
        }
        
        // 调用路由函数
        return routeFunc(path + "?" + query);
    }
    
    /**
     * 处理动作意图
     * @param step 步骤JSON对象
     * @param stepResults 之前步骤的结果
     * @return 动作结果
     */
    private Object handleActionIntent(JSONObject step, Map<String, Object> stepResults) {
        // 这里实现动作逻辑
        String subtype = step.getString("subtype");
        JSONObject params = step.getJSONObject("params");
        
        // 处理下载动作
        if ("download".equals(subtype)) {
            String fileId = null;
            JSONObject dataBindings = step.getJSONObject("data_bindings");
            if (dataBindings.containsKey("file_id")) {
                String bindValue = dataBindings.getString("file_id");
                if (bindValue.startsWith("step")) {
                    String[] parts = bindValue.split("\\.");
                    if (parts.length == 2) {
                        Map<String, Object> stepResult = (Map<String, Object>) stepResults.get(parts[0]);
                        if (stepResult != null && stepResult.containsKey(parts[1])) {
                            fileId = stepResult.get(parts[1]).toString();
                        }
                    }
                }
            }
            
            if (fileId != null) {
                return actionFunc("download", fileId);
            }
        }
        
        // 处理预约动作
        if ("schedule".equals(subtype)) {
            String meetingId = null;
            JSONObject dataBindings = step.getJSONObject("data_bindings");
            for (String key : dataBindings.keySet()) {
                String bindValue = dataBindings.getString(key);
                if (bindValue.startsWith("step")) {
                    String[] parts = bindValue.split("\\.");
                    if (parts.length == 2) {
                        Map<String, Object> stepResult = (Map<String, Object>) stepResults.get(parts[0]);
                        if (stepResult != null && stepResult.containsKey(parts[1])) {
                            meetingId = stepResult.get(parts[1]).toString();
                            break;
                        }
                    }
                }
            }
            
            if (meetingId != null) {
                return actionFunc("schedule", meetingId);
            }
        }
        
        return null;
    }
    
    /**
     * 通过Milvus查询会议标题
     * @param title 会议标题
     * @return 会议ID列表
     */
    private List<String> queryMeeting(String title) {
        // 这里是Milvus查询的模拟实现
        // 实际应用中应该调用Milvus API
        List<String> meetingIds = new ArrayList<>();
        meetingIds.add("meeting_" + title.hashCode());
        return meetingIds;
    }
    
    /**
     * 路由函数
     * @param path 路由路径
     * @return 路由结果
     */
    private Object routeFunc(String path) {
        // 模拟路由函数
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("path", path);
        return result;
    }
    
    /**
     * 动作函数
     * @param action 动作类型
     * @param param 动作参数
     * @return 动作结果
     */
    private Object actionFunc(String action, String param) {
        // 模拟动作函数
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("action", action);
        result.put("param", param);
        return result;
    }
    
    /**
     * 驼峰转下划线
     * @param camelCase 驼峰命名
     * @return 下划线命名
     */
    private String camelToUnderline(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0)));
        
        for (int i = 1; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append("_");
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        
        return result.toString();
    }
}
```

## 实体类

为了完整性，下面是相关的实体类定义：

```java
package com.example.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("meeting")
public class Meeting {
    @TableId
    private String meetingId;
    
    private String title;
    
    private Date beginTime;
    
    private Date endTime;
    
    private String geoId;
    
    // 其他会议相关字段
}

@Data
@TableName("meeting_geo")
public class MeetingGeo {
    @TableId
    private String geoId;
    
    private String address;
    
    private String location;
    
    // 其他地理位置相关字段
}

@Data
@TableName("meeting_clip")
public class MeetingClip {
    @TableId
    private String clipId;
    
    private String meetingId;
    
    private String fileId;
    
    private String fileName;
    
    // 其他文件相关字段
}
```

## Mapper接口

```java
package com.example.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.meeting.entity.Meeting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingMapper extends BaseMapper<Meeting> {
}

@Mapper
public interface MeetingGeoMapper extends BaseMapper<MeetingGeo> {
}

@Mapper
public interface MeetingClipMapper extends BaseMapper<MeetingClip> {
}
```

这个实现包含了处理intent为"query"的完整逻辑，包括处理依赖关系、过滤条件、数据绑定等。代码结构清晰，使用了MyBatis-Plus的QueryWrapper构建查询条件，并针对不同的数据库表和查询类型进行了处理。