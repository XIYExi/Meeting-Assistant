
我现在给你输入数据库依赖关系，{
    "db": {
        "meeting": {
            "描述": "存储会议信息的表",
            "filter可查询字段": "title,content,createTime,views",
            "此表可以查询": "会议id, 地理位置表geoId，会议标题（title)、内容(content)、开始结束时间(createTime)、海报、浏览量(views)、会议类型、线上线下会议"
        },
        "meeting_agenda": {
            "描述": "存储对应会议的议程信息,通过meetingId关联meeting表",
            "filter可查询字段": "content，beginTime，meta",
            "此表可以查询": "议程id，对应会议id（meetingId），开始时间（beginTime），议程内容（content），主讲人（meta）"
        },
        "meeting_geo": {
            "描述": "存储会议地理位置的表，通过id和meeting关联",
            "filter可查询字段": "country， formatted_address， provience， city",
            "此表可以查询": "地理位置id， 国家country，会议地点formatted_address,省份provience,城市city，经纬度location"
        },
        "meeting_clip": {
            "描述": "存储可下载文件的表,通过meetingId和对应的会议关联，提供过agendaId和议程关联，如果为0，则表示此文件和具体会议或者议程无关",
            "filter可查询字段": "fileName",
            "此表可以查询": "文件表id，会议meetingId，议程agendaId，文件类型fileType, 文件名称fileName,文件存储地址url"
        },
        "news": {
            "描述": "新闻表，存储最新消息",
            "filter可查询字段": "title, createTime, author",
            "此表可以查询": "新闻id，新闻标题title，新闻内容content,发布时间createTime，发布者author"
        }
    }
}，需要注意db字段中的每个keys表示目前我允许用户查询的数据库类型，其中meeting、meeting_geo、meeting_clip对应query为meeting和file的情况，rec和rank我都单独的函数查询。filter表示当前数据库只能通过这些字段查询，可以查询表示查询出来之后可以获得哪些内容。其中依赖关系为：meeting_agenda表存储会议日程，在meeting_agenda里面存储meetingId

你可以不用拘泥于我的设计，参考我的json schema，结合你的设计思路，重新编排智能体编排工作流，你可以分多个模型组合完成这个工作流json的输出，每个工作流的任务和system prompt需要提出来，system prompt需要以txt格式给我，我们使用的模型是Qwen



基于您的需求，我为您设计一个**五阶段抗幻觉工作流**，包含完整的JSON Schema约束和Qwen模型参数推荐。以下是分步方案：

---

### **工作流总架构**
```text
用户输入 
→ 1.意图锚定 → 2.步骤生成 → 3.依赖预判 
→ 4.字段填充 → 5.闭环校验 
→ 最终输出
```

---

### **阶段1：意图锚定（Intent Anchor）**
#### **JSON Schema**
```json
{
  "type": "array",
  "items": {
    "type": "object",
    "required": ["keywords", "possible_dbs"],
    "properties": {
      "intent": {"enum": ["query", "action", "route"]},
      "keywords": {"type": "array", "items": {"type": "string"}},
      "possible_dbs": {
        "type": "array",
        "items": {"enum": ["meeting", "meeting_geo", "meeting_clip", "news"]}
      },
      "implicit_queries": {
        "type": "array",
        "items": {"type": "string", "pattern": "^\\w+→\\w+$"} // 如"meeting→meeting_clip"
      }
    }
  }
}
```

#### **Qwen参数推荐**
| 参数 | 值 | 作用 |
|-----|----|-----|
| Temperature | 0.3 | 限制发散，聚焦核心意图 |
| top_p | 0.8 | 允许适度多样性 |
| max_token | 512 | 控制输出长度 |
| tok_k | 30 | 避免长尾干扰 |

---

### **阶段2：步骤生成（Step Generator）**
#### **JSON Schema**
```json
{
  "type": "array",
  "items": {
    "type": "object",
    "required": ["step", "intent", "db"],
    "properties": {
      "step": {"type": "integer", "minimum": 1},
      "db": {"enum": ["meeting", "meeting_geo", "meeting_clip", "news"]},
      "filters": {
        "type": "array",
        "items": {
          "field": {"type": "string"}, // 根据db动态校验字段白名单
          "operator": {"enum": ["=", ">", "<", "like"]}
        }
      },
      "output_fields": {
        "type": "array", 
        "items": {"type": "string", "pattern": "^[a-zA-Z]+Id$"} // 如meetingId
      }
    }
  }
}
```

#### **Qwen参数推荐**
| 参数 | 值 | 作用 |
|-----|----|-----|
| Temperature | 0.5 | 平衡结构化和创造性 |
| top_p | 0.7 | 聚焦主要查询路径 |
| max_token | 1024 | 支持多步骤生成 |
| 思考次数 | 2 | 增强逻辑连贯性 |

---

### **阶段3：依赖预判（Dependency Predictor）**
#### **JSON Schema**
```json
{
  "type": "array",
  "items": {
    "type": "object",
    "required": ["step", "input_slots"],
    "properties": {
      "input_slots": { // 声明需要哪些前置数据
        "type": "array",
        "items": {"type": "string", "pattern": "^step\\d+\\.[a-zA-Z]+Id$"}
      },
      "output_slots": { // 声明产生哪些数据
        "type": "array",
        "items": {"type": "string", "pattern": "^[a-zA-Z]+Id$"}
      }
    }
  }
}
```

#### **Qwen参数推荐**
| 参数 | 值 | 作用 |
|-----|----|-----|
| Temperature | 0.1 | 严格遵循数据流规则 |
| top_p | 0.5 | 避免无效关联 |
| max_token | 256 | 精确描述数据槽 |

---

### **阶段4：字段填充（Field Populator）**
#### **JSON Schema**
```json
{
  "type": "array",
  "items": {
    "type": "object",
    "required": ["filters", "dependency"],
    "properties": {
      "filters": {
        "type": "array",
        "items": {
          "value": {
            "oneOf": [ // 值类型约束
              {"type": "string", "pattern": "^\\d{4}-\\d{2}-\\d{2}$"}, // 日期
              {"type": "string", "pattern": "^step\\d+\\.[a-zA-Z]+Id$"} // 跨步骤引用
            ]
          }
        }
      },
      "dependency": {"type": "integer", "minimum": -1}
    }
  }
}
```

#### **Qwen参数推荐**
| 参数 | 值 | 作用 |
|-----|----|-----|
| Temperature | 0.2 | 精确填充字段值 |
| top_p | 0.9 | 覆盖更多可能性 |
| max_token | 512 | 支持复杂过滤条件 |

---

### **阶段5：闭环校验（Validation）**
#### **JSON Schema**
```json
{
  "type": "array",
  "items": {
    "type": "object",
    "required": ["valid"],
    "properties": {
      "valid": {"type": "boolean"},
      "errors": {
        "type": "array",
        "items": {
          "type": "object",
          "properties": {
            "code": {"enum": ["INVALID_DEP", "FIELD_MISSING", "DB_MISMATCH"]},
            "ref_step": {"type": "integer"}
          }
        }
      }
    }
  }
}
```

#### **Qwen参数推荐**
| 参数 | 值 | 作用 |
|-----|----|-----|
| Temperature | 0.1 | 严格校验模式 |
| top_p | 0.3 | 只关注关键错误 |
| max_token | 128 | 简明错误描述 |

---

### **全流程参数联动策略**
1. **渐进式降温**  
   从阶段1到阶段5，Temperature参数逐步降低（0.3 → 0.1），实现从发散到收敛的生成控制

2. **token长度控制**  
   生成类阶段（阶段2）放宽max_token至1024，校验类阶段（阶段5）压缩至128

3. **回溯增强机制**  
   在阶段3和阶段5之间建立循环链路，当校验失败时携带错误信息重新生成步骤：
   ```text
   阶段5发现错误 → 携带错误码回到阶段2 → 重新生成修正步骤
   ```

该方案通过**Schema约束压缩生成空间**+**参数精准调控**，可在低代码环境下有效抑制幻觉。建议在可视化编排工具中为每个阶段配置独立的模型调用节点，并通过JSON Schema实现节点间的数据格式校验。