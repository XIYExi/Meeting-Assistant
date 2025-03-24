import json

# 定义原始键到中文键的映射
key_mapping = {
    'walking_distance': '步行距离',
    'distance': '距离',
    'nightflag': '夜间标志',
    'segments': '区间',
    'bus': '公交',
    'buslines': '公交线路',
    'start_time': '开始时间',
    'bustimetag': '公交时间标记',
    'departure_stop': '出发站',
    'arrival_stop': '到达站',
    'name': '名称',
    'end_time': '结束时间',
    'via_num': '经过站数',
    'via_stops': '经过站点',
    'id': '站点ID',
    'type': '类型',
    'bus_time_tips': '公交时间提示',
    'walking': '步行',
    'origin': '起点',
    'destination': '终点',
    'steps': '步骤',
    'road': '道路',
    'instruction': '指示',
    'trip': '车次',
    'spaces': '座位',
    'cost': '费用',
    'adcode': '行政区代码',
    'start': '起始时间',
    'end': '结束时间',
    'location': '地址'
}


def translate_keys(data, mapping):
    """
    遍历字典，递归地将键替换为中文。
    """
    if isinstance(data, dict):
        return {
            mapping.get(key, key): translate_keys(value, mapping)
            for key, value in data.items()
        }
    elif isinstance(data, list):
        return [translate_keys(item, mapping) for item in data]
    else:
        return data


def replace_single_quotes_with_double(input_string):
    return input_string.replace("'", '"')




