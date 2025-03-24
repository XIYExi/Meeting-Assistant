import urllib.parse
import urllib3
import ssl


def get_prohibited_word_detection(text):
    """
    检测是否有违禁词
    :param text:
    :return: True(有） False（没有）
    """
    appcode = 'be492ba3a0d340e08cb4dd45f9c3685d'
    host = 'https://jmwbsh.market.alicloudapi.com'
    path = '/wbsh/text/review'
    method = 'POST'
    url = host + path

    http = urllib3.PoolManager()
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Authorization': 'APPCODE ' + appcode
    }

    bodys = {
        'text': text
    }

    post_data = urllib.parse.urlencode(bodys).encode('utf-8')
    response = http.request(method, url, body=post_data, headers=headers)
    content = response.data.decode('utf-8')

    import json
    data = json.loads(content)
    return str(data['data']['result']) == '2'
