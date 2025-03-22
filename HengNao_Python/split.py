import json

with open('./data.json', 'r', encoding="utf-8") as f:
    data = json.load(f)

for i in data:
    title = i['会议标题']
    with open(title + ".json", "w", encoding="utf-8") as f:
        json.dump(i, f, ensure_ascii=False, indent=4)