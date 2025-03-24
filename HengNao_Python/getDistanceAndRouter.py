import urllib.parse
import urllib3
from getLocationAndCitycode import get_location_and_citycode, get_citycode_by_location
from translate import replace_single_quotes_with_double, translate_keys, key_mapping
import json

def getOrigin():
    originInfo = {
        'origin': '120.268898,31.476590',
        'origCityCode': '0510',
        'appcode': 'be492ba3a0d340e08cb4dd45f9c3685d'
    }
    return originInfo


def byAddressGetDistanceAndRouter(address, origin):
    originInfo = getOrigin()
    # 获得起始地经纬度
    # origin = originInfo['origin']
    # originCityCode = originInfo['origCityCode']
    appcode = originInfo['appcode']
    originCityCode = get_citycode_by_location(origin, appcode)
    # 获得终点经纬度
    destination, destCityCode = get_location_and_citycode(address, appcode)
    # 获得方案（不能通过这个获得距离，这个输出的距离不准确！可能为空！！！）
    transits = get_route_plan(origin, destination, originCityCode, destCityCode, appcode)
    # 获得距离
    distance_km = get_distance(origin, destination, appcode)

    # 转化成字符串并处理
    json_data = str(transits)
    json_data = replace_single_quotes_with_double(json_data)
    # 将 JSON 数据转换为 Python 对象
    data = json.loads(json_data)
    # 使用 translate_keys 函数将键翻译为中文
    translated_data = translate_keys(data, key_mapping)

    return distance_km, translated_data



def get_route_plan(origin, destination, origCityCode, destCityCode, appcode):
    host = 'https://jmlxgh.market.alicloudapi.com'
    path = '/route/public-transit'
    method = 'POST'
    url = host + path
    http = urllib3.PoolManager()

    # 设置请求头和请求体
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Authorization': 'APPCODE ' + appcode
    }
    bodys = {
        'origin': origin,
        'destination': destination,
        'origCityCode': origCityCode,
        'destCityCode': destCityCode,
        'strategy': '',
        'alternativeRoute': '',
        'multiexPort': '',
        'maxTrans': '',
        'nightFlag': '',
        'date': '',
        'time': '',
        'showFields': ''
    }
    post_data = urllib.parse.urlencode(bodys).encode('utf-8')

    # 发起请求并获取响应
    response = http.request(method, url, body=post_data, headers=headers)
    content = response.data.decode('utf-8')

    # 解析响应内容，这里假设使用json解析
    import json
    data = json.loads(content)
    print(data)

    if data['code'] == 200:
        # 提取距离和公交换乘信息，并将距离转换为千米
        strategy_list = data['data']['strategyList']
        # distance_km = float(strategy_list['distance']) / 1000  # 转换为千米
        first_transits = strategy_list['transits'][0] if len(strategy_list['transits']) > 0 else []

        return first_transits
    else:
        print("Error:", data.get('msg', '未知错误'))
        return None


def get_distance(origin, destination, appcode):
    host = 'https://jmlxgh.market.alicloudapi.com'
    path = '/route/distance-measurement'
    method = 'POST'
    url = host + path
    http = urllib3.PoolManager()

    # 设置请求头和请求体
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Authorization': 'APPCODE ' + appcode
    }
    bodys = {
        'origins': origin,
        'destination': destination
    }
    post_data = urllib.parse.urlencode(bodys).encode('utf-8')

    # 发起请求并获取响应
    response = http.request(method, url, body=post_data, headers=headers)
    content = response.data.decode('utf-8')

    # 解析响应内容，这里假设使用json解析
    import json
    data = json.loads(content)
    return float(data['data']['results'][0]['distance']) / 1000

# 使用示例
# address = '江南大学江阴校区'
# distance_km, transits = byAddressGetDistanceAndRouter(address, "120.268898,31.476590")
# print("distance_km:", distance_km)
# print(json.dumps(transits, ensure_ascii=False, indent=4))
