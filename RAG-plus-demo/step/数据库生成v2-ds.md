## 一、步骤拆分规则
1. **`action`类意图**（包括下载/预约/总结/翻译/生成）：
   - 必须拆分为两个步骤：
     1. 前置查询步骤query
     2. 动作执行步骤action
   - 示例：
     - 输入："预约下一场会议" → 
       ```json
       [
         {"step":1, "intent":"query", ...},
         {"step":2, "intent":"action",  ...}
       ]
       ```

   
2. **文件类查询**：
   - 必须拆分为两个步骤：
     1. 查询`meeting`表 → 
     2. 查询`meeting_clip`表

---

## 二、依赖关系规则
1. **强制依赖指向**：
   - `meeting_geo`/`meeting_clip`必须依赖`meeting`表的`meetingId`
   - `action`必须依赖提供一个query步骤

2. **字段限制**：
   - `最新`语义仅允许用于`createTime`字段（按`desc`排序）
   - `下一场`语义必须用`beginTime > now`且`order:asc`

---

## 三、时间语义映射表
| 关键词  | 目标字段    | 操作符  | 排序   | 值转换       |
|---------|-------------|---------|--------|--------------|
| 明天    | beginTime   | eq      | -      | tomorrow     |
| 下一场  | beginTime   | gt      | asc    | now          |
| 最新    | createTime  | last    | desc   | -            |

---

## 四、示例库
### 示例1：预约会议
```json
[
  {
    "step":1, 
    "intent":"query", 
    "db":"meeting",
    "filters":[
      {"filter":"beginTime", "operator":"gt", "value":"now", "order":"asc"}
    ],
    "dependency":-1,
    "reason":"解析'下一场'为开始时间大于当前时间且升序排列"
  },
  {
    "step":2, 
    "intent":"action",
    "dependency":1,
    "reason":"用step1的meetingId执行预约"
  }
]