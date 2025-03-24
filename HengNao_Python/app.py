from flask import Flask, request


from getDistanceAndRouter import byAddressGetDistanceAndRouter

# from selenium import webdriver
# from selenium.webdriver.edge.service import Service
# from selenium.webdriver.common.by import By
# from selenium.webdriver.support.ui import WebDriverWait
# from selenium.webdriver.support import expected_conditions as EC
# from webdriver_manager.microsoft import EdgeChromiumDriverManager

app = Flask(__name__)
driver = None

@app.route("/navToRealAddress")
def navToRealAddress():
    """
    从当前位置导航到指定会议的地点
    返回距离和路线规划
    :return:
    """
    # 用户需要导航到什么地方，直接给地址
    address = request.args['address']
    origin = request.args['origin']
    # 用什么交通工具 car/bus/footer
    # tool = request.args['tool']
    # 调用aliyun借口 地点 -> 经纬度

    # 当前位置经纬度 + 目标地点经纬度 -> 计算距离、怎么去
    distance_km, transits = byAddressGetDistanceAndRouter(address, origin)
    # 返回 dict
    result = {
        "distance": distance_km,
        "road": transits
    }
    return result


@app.route("/calDistance")
def calDistance():
    """
    计算当前位置距离目的地有多远
    :return:
    """
    address = request.args['address']
    origin = request.args['origin']
    # 直接目标地址 + 当前位置经纬度坐标 -> 计算距离
    distance_km, transits = byAddressGetDistanceAndRouter(address, origin)
    distance = distance_km
    return str(distance)


if __name__ == "__main__":
    # driver = init_edgedriver()
    app.run(host="0.0.0.0", port=5000, debug=True)


# ========== 配置 Edge 浏览器选项 ==========
# def configure_edge_options():
#     options = webdriver.EdgeOptions()
#     # 模拟 Edge 浏览器正式环境
#     options.add_argument("--headless=new")  # 启用无头模式加速
#     options.add_argument("--disable-gpu")
#     options.add_argument("--no-sandbox")
#     options.add_argument("--disable-blink-features=AutomationControlled")  # 禁用自动化控制标记
#     options.add_argument("--start-maximized")  # 窗口最大化
#     options.add_argument("--inprivate")  # 无痕模式
#     options.add_argument("--disable-extensions")  # 禁用扩展
#     options.add_argument("--disable-popup-blocking")  # 禁用弹窗拦截
#     # 高级反反爬配置
#     options.add_argument("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0")
#     options.add_argument("--lang=zh-CN")  # 设置中文语言
#     options.add_experimental_option("excludeSwitches", ["enable-automation"])  # 隐藏自动化标识
#     return options
#
#
# # ========== 初始化 Edge 驱动 ==========
# def init_edgedriver():
#     service = Service(EdgeChromiumDriverManager().install())
#     options = configure_edge_options()
#     driver = webdriver.Edge(service=service, options=options)
#     # 注入 JavaScript 绕过 WebDriver 检测
#     driver.execute_script("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")
#     return driver
#
# # ========== 模拟人类操作 ==========
# def human_like_operation(driver):
#     # 随机滚动页面
#     scroll_height = random.randint(500, 1000)
#     driver.execute_script(f"window.scrollBy(0, {scroll_height})")
#     time.sleep(random.uniform(0.5, 1.5))
#
#
#
# @app.route("/seleniumMeeting")
# def seleniumMeeting():
#     """
#     通过爬虫获取到2024官网上所有的会议列表
#     :return:
#     """
#     url = "https://www.gcsis.cn/agenda/"
#     driver.set_page_load_timeout(30)
#     driver.set_script_timeout(20)
#     driver.get(url)
#     WebDriverWait(driver, 15).until(
#         EC.presence_of_element_located((By.CSS_SELECTOR, "div.agenda_content"))
#     )
#     # 防爬 模拟操作
#     human_like_operation(driver)
#
#     items = driver.find_elements(By.CSS_SELECTOR, "div.agenda_content > div.item")
#     data = []
#     for item in items:
#         morning = item.find_element(By.CSS_SELECTOR, "div.morning") if item.find_elements(By.CSS_SELECTOR, "div.morning") else None
#         afternoon = item.find_element(By.CSS_SELECTOR, "div.afternoon") if item.find_elements(By.CSS_SELECTOR, "div.afternoon") else None
#         if morning != None:
#             date = morning.find_element(By.CSS_SELECTOR, "div.tit > div.left h1").text.strip()
#             meetings = item.find_elements(By.CSS_SELECTOR, "div.morning > div.item")
#             for meeting in meetings:
#                 elem = meeting.find_element(By.CSS_SELECTOR, "div.top_tit > div.left")
#                 title = elem.find_element(By.CSS_SELECTOR, "h1 span").text.strip()
#                 tag = elem.find_elements(By.CSS_SELECTOR, "h1 > div.tag")[0].find_elements(By.CSS_SELECTOR, "span")[1].text.strip()
#                 beginTime = meeting.find_elements(By.CSS_SELECTOR, "div.top_tit > div.left > div.tag")[0].find_elements(By.TAG_NAME, "span")[1].text.strip()
#
#                 # 为agenda添加active从而展开元素，以获取agenda
#                 agenda_wrapper = meeting.find_element(By.CSS_SELECTOR, "div.ax-step")
#                 driver.execute_script("arguments[0].classList.add('active');", agenda_wrapper)
#
#                 agendas = meeting.find_elements(By.CSS_SELECTOR, "div.ax-step > div.ax-item")
#                 agendaList = []
#                 for agenda in agendas:
#                     agenda_title = agenda.find_element(By.CSS_SELECTOR, "div.ax-text > div.ax-title")
#                     agenda_name = agenda.find_element(By.CSS_SELECTOR, "div.ax-text > div.ax-name")
#                     agenda_des = agenda.find_element(By.CSS_SELECTOR, "div.ax-text > div.ax-des")
#                     agenda_title_time_text = agenda_title.find_element(By.TAG_NAME, "span").text.strip()
#                     agenda_title_text = agenda_title.find_element(By.TAG_NAME, "h1").text.strip()
#                     # print(agenda_title.find_element(By.TAG_NAME, "span").text)
#                     name_spans = agenda_name.find_elements(By.TAG_NAME, "span")
#                     des_spans = agenda_des.find_elements(By.TAG_NAME, "span")
#
#                     peoples = []
#                     for i in range(len(name_spans)):
#                         people_name = name_spans[i].text.strip()
#                         people_title = des_spans[i].text.strip()
#                         peoples.append(people_name + " " + people_title)
#                     agendaList.append({
#                         "议程时间": agenda_title_time_text,
#                         "议程内容": agenda_title_text,
#                         "议程嘉宾": peoples
#                     })
#
#                 data.append({
#                     "会议标题": title,
#                     "会议类型": tag,
#                     "会议日期": date,
#                     "会议时间": beginTime,
#                     "会议日程": agendaList
#                 })
#         if afternoon != None:
#             date = afternoon.find_element(By.CSS_SELECTOR, "div.tit > div.left h1").text.strip()
#             meetings = item.find_elements(By.CSS_SELECTOR, "div.afternoon > div.item")
#             for meeting in meetings:
#                 elem = meeting.find_element(By.CSS_SELECTOR, "div.top_tit > div.left")
#                 title = elem.find_element(By.CSS_SELECTOR, "h1 span").text.strip()
#                 tag = elem.find_elements(By.CSS_SELECTOR, "h1 > div.tag")[0].find_elements(By.CSS_SELECTOR, "span")[1].text.strip()
#                 beginTime = meeting.find_elements(By.CSS_SELECTOR, "div.top_tit > div.left > div.tag")[0].find_elements(By.CSS_SELECTOR, "span")[1].text.strip()
#                 # 为agenda添加active从而展开元素，以获取agenda
#                 agenda_wrapper = meeting.find_element(By.CSS_SELECTOR, "div.ax-step")
#                 driver.execute_script("arguments[0].classList.add('active');", agenda_wrapper)
#
#                 agendas = meeting.find_elements(By.CSS_SELECTOR, "div.ax-step > div.ax-item")
#                 agendaList = []
#                 for agenda in agendas:
#                     agenda_title = agenda.find_element(By.CSS_SELECTOR, "div.ax-text > div.ax-title")
#                     agenda_name = agenda.find_element(By.CSS_SELECTOR, "div.ax-text > div.ax-name")
#                     agenda_des = agenda.find_element(By.CSS_SELECTOR, "div.ax-text > div.ax-des")
#                     agenda_title_time_text = agenda_title.find_element(By.TAG_NAME, "span").text.strip()
#                     agenda_title_text = agenda_title.find_element(By.TAG_NAME, "h1").text.strip()
#
#                     name_spans = agenda_name.find_elements(By.TAG_NAME, "span")
#                     des_spans = agenda_des.find_elements(By.TAG_NAME, "span")
#
#                     peoples = []
#                     for i in range(len(name_spans)):
#                         people_name = name_spans[i].text.strip()
#                         people_title = des_spans[i].text.strip()
#                         peoples.append(people_name + " " + people_title)
#                     agendaList.append({
#                         "议程时间": agenda_title_time_text,
#                         "议程内容": agenda_title_text,
#                         "议程嘉宾": peoples
#                     })
#
#                 data.append({
#                     "会议标题": title,
#                     "会议类型": tag,
#                     "会议日期": date,
#                     "会议时间": beginTime,
#                     "会议日程": agendaList
#                 })
#     driver.close()
#     driver.quit()
#     with open("data.json", "w", encoding="utf-8") as f:
#         json.dump(data, f)
#     return data




