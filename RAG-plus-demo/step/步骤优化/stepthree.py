import json

def validate_step3(json_str):
    # --------------------------
    # 1. 基础结构校验
    # --------------------------
    try:
        data = json.loads(json_str)
    except:
        return False, "Invalid JSON format"

    if not isinstance(data, list):
        return False, "Root must be array"
    
    # --------------------------
    # 2. 字段白名单定义
    # --------------------------
    INTENT_ENUM = {"query", "route", "action"}
    SUBTYPE_MAP = {
        "query": ["meeting", "file", "news", "rank", "rec"],
        "action": ["download", "schedule", "summarize", "checkin"],
        "route": ["page", "geo"]
    }
    DB_ENUM = {"meeting", "meeting_geo", "meeting_clip", "news"}
    STEP_RANGE = (1, 5)
    
    # --------------------------
    # 3. 校验主逻辑
    # --------------------------
    errors = []
    step_outputs = {}  # 记录每个步骤的输出字段
    
    for idx, item in enumerate(data):
        step_num = idx + 1
        
        # 3.1 必填字段检查
        required_fields = ["step", "intent", "subtype", "db", "dependency", "data_bindings"]
        for field in required_fields:
            if field not in item:
                errors.append(f"Step{step_num}: Missing required field '{field}'")
        
        # 3.2 字段类型校验
        if not isinstance(item.get("step", 0), int):
            errors.append(f"Step{step_num}: 'step' must be integer")
        if item.get("step") not in range(STEP_RANGE[0], STEP_RANGE[1]+1):
            errors.append(f"Step{step_num}: step must be between {STEP_RANGE[0]}~{STEP_RANGE[1]}")
        
        # 3.3 枚举值校验
        if item.get("intent") not in INTENT_ENUM:
            errors.append(f"Step{step_num}: Invalid intent value")
        if item.get("subtype") not in SUBTYPE_MAP.get(item.get("intent", ""), []):
            errors.append(f"Step{step_num}: Subtype不匹配Intent类型")
        if item.get("db") not in DB_ENUM:
            errors.append(f"Step{step_num}: Invalid database value")
        
        # 3.4 依赖关系校验
        dep = item.get("dependency", -2)
        if dep < -1 or dep >= step_num:
            errors.append(f"Step{step_num}: dependency值越界")
        if step_num == 1 and dep != -1:
            errors.append("Step1的dependency必须为-1")
        
        # 3.5 数据绑定校验
        bindings = item.get("data_bindings", {})
        for local_field, ref_field in bindings.items():
            if not ref_field.startswith("step"):
                errors.append(f"Step{step_num}: 绑定字段'{ref_field}'格式错误")
            else:
                ref_step = int(ref_field.split(".")[0][4:])
                if ref_step >= step_num:
                    errors.append(f"Step{step_num}: 不能引用后续步骤字段{ref_field}")
        
        # 记录当前步骤的输出字段
        step_outputs[step_num] = item.get("output_fields", [])
    
    # --------------------------
    # 4. 跨步骤依赖校验
    # --------------------------
    for idx, item in enumerate(data):
        step_num = idx + 1
        dep_step = item.get("dependency")
        
        # 4.1 检查依赖步骤是否存在
        if dep_step != -1 and dep_step not in step_outputs:
            errors.append(f"Step{step_num}: 依赖的步骤{dep_step}不存在")
        
        # 4.2 检查字段引用有效性
        for ref_field in item.get("data_bindings", {}).values():
            ref_parts = ref_field.split(".")
            if len(ref_parts) != 2:
                continue
            ref_step = int(ref_parts[0][4:])
            field_name = ref_parts[1]
            
            if ref_step not in step_outputs:
                errors.append(f"Step{step_num}: 引用了不存在的步骤{ref_step}")
            elif field_name not in step_outputs[ref_step]:
                errors.append(f"Step{step_num}: 字段{field_name}在步骤{ref_step}中未定义")

    return len(errors) == 0, errors

# --------------------------
# 使用示例
# --------------------------
test_data = '''
[
  {
    "step": 1,
    "intent": "query",
    "subtype": "meeting",
    "db": "meeting",
    "dependency": -1,
    "data_bindings": {},
    "output_fields": ["meeting_id"]
  }
]
'''

is_valid, error_list = validate_step3(test_data)
if is_valid:
    print("校验通过")
else:
    print("校验失败，错误列表：")
    for err in error_list:
        print(f" - {err}")