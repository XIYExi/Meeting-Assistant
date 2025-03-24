import urllib.parse
import urllib3
import ssl


def get_location_and_citycode(keywords, appcode):
    host = 'https://jmgeocode.market.alicloudapi.com'
    path = '/place/keywords'
    method = 'POST'
    url = host + path

    http = urllib3.PoolManager()
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Authorization': 'APPCODE ' + appcode
    }

    bodys = {
        'keywords': keywords,
        'types': '',
        'region': '',
        'cityLimit': '',
        'showFields': '',
        'pageSize': '',
        'pageNo': ''
    }

    post_data = urllib.parse.urlencode(bodys).encode('utf-8')
    response = http.request(method, url, body=post_data, headers=headers)
    content = response.data.decode('utf-8')

    import json
    data = json.loads(content)
    print(data)

    if 'data' in data and 'list' in data['data'] and len(data['data']['list']) > 0:
        place_info = data['data']['list'][0]
        location = place_info.get('location', None)
        citycode = place_info.get('citycode', None)
        return location, citycode
    else:
        print("未找到相关信息或请求失败")
        return None, None


def get_citycode_by_location(origin, appcode):
    host = 'https://jmgeocode.market.alicloudapi.com'
    path = '/geocode/regeo_query'
    method = 'POST'
    url = host + path

    http = urllib3.PoolManager()
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Authorization': 'APPCODE ' + appcode
    }

    bodys = {
        'location': origin
    }

    post_data = urllib.parse.urlencode(bodys).encode('utf-8')
    response = http.request(method, url, body=post_data, headers=headers)
    content = response.data.decode('utf-8')
    import json
    data = json.loads(content)
    return data['data']['regeocodes'][0]['addressComponent']['citycode']