## 查询位于安恒大厦的会议信息

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "meeting",
        "db": "meeting_geo",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "address",
                "operator": "=",
                "value": "安恒大厦"
            }
        ],
        "output_fields": [
            "geo_id"
        ],
        "auth_type": "user_verify"
    },
    {
        "step": 2,
        "intent": "query",
        "subtype": "meeting",
        "db": "meeting",
        "dependency": 1,
        "data_bindings": {
            "geo_id": "step1.geo_id"
        },
        "filters": [
            {
                "field": "geo_id",
                "operator": "=",
                "value": "step1.geo_id"
            }
        ],
        "output_fields": [
            "meeting_id"
        ],
        "auth_type": "user_verify"
    }
]
```


## 查询明天的会议信息

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "meeting",
        "db": "meeting",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "beginTime",
                "operator": "LIKE",
                "value": "escape(tomorrow%)"
            }
        ],
        "time_constraints": {
            "max_execution_ms": 5000
        },
        "auth_condition": {
            "required_level": "user",
            "verify_mode": "password"
        }
    }
]
```

## 查询年度新品发布会信息

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "meeting",
        "db": "meeting",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "title",
                "operator": "LIKE",
                "value": "%年度新品发布会%"
            }
        ],
        "output_fields": [
            "meeting_id"
        ],
        "auth_type": "user_verify"
    }
]
```

## 打开用户个人中心

```json
[
    {
        "step": 1,
        "intent": "route",
        "subtype": "page",
        "db": "meeting",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "meetingId",
                "operator": "=",
                "value": "${user.meetingId}"
            }
        ],
        "auth_condition": {
            "verify_mode": "password"
        },
        "input_fields": [],
        "output_fields": [
            "meeting_id"
        ],
        "params": {
            "path": "/pages/userCenter",
            "query": "id=meeting_id"
        }
    }
]
```

## 开车导航到网络安全大会

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "meeting",
        "db": "meeting_geo",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "title",
                "operator": "=",
                "value": "网络安全大会"
            }
        ],
        "output_fields": [
            "geo_id"
        ]
    },
    {
        "step": 2,
        "intent": "route",
        "subtype": "page",
        "db": "meeting_geo",
        "dependency": 1,
        "data_bindings": {
            "step1.geo_id": "step1.geo_id"
        },
        "filters": [
            {
                "field": "geo_id",
                "operator": "=",
                "value": "${step1.geo_id}"
            }
        ],
        "output_fields": [],
        "params": {
            "path": "/导航至会议地点",
            "query": "id=${step1.geo_id}"
        }
    }
]
```

## 下载网络安全大会的ppt文件

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "file",
        "db": "meeting_clip",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "title",
                "operator": "LIKE",
                "value": "%网络安全大会%"
            },
            {
                "field": "fileId",
                "operator": "=",
                "value": "NULL"
            }
        ],
        "output_fields": [
            "file_id"
        ],
        "auth_type": "user_verify"
    },
    {
        "step": 2,
        "intent": "action",
        "subtype": "download",
        "db": "meeting_clip",
        "dependency": 1,
        "data_bindings": {
            "file_id": "step1.file_id"
        },
        "filters": [
            {
                "field": "fileId",
                "operator": "=",
                "value": "${step1.file_id}"
            }
        ],
        "params": {
            "path": "/文件下载",
            "query": "id=${step1.file_id}"
        },
        "auth_type": "user_verify"
    }
]
```

## 预约网络安全大会

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "meeting",
        "db": "meeting",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "title",
                "operator": "LIKE",
                "value": "%网络安全大会%"
            }
        ],
        "output_fields": [
            "meeting_id"
        ],
        "auth_type": "user_verify"
    },
    {
        "step": 2,
        "intent": "action",
        "subtype": "schedule",
        "db": "meeting",
        "dependency": 1,
        "data_bindings": {
            "step1.meeting_id": "step1.meeting_id"
        },
        "filters": [
            {
                "field": "meetingId",
                "operator": "=",
                "value": "step1.meeting_id"
            }
        ],
        "output_fields": [],
        "params": {
            "path": "/预约会议",
            "query": "id=step1.meeting_id"
        },
        "auth_type": "user_verify"
    }
]
```

## 标题为网络安全的新闻讲了什么
```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "news",
        "db": "news",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "title",
                "operator": "LIKE",
                "value": "%网络安全%"
            }
        ],
        "output_fields": [
            "news_id"
        ],
        "auth_type": "user_verify"
    }
]
```

## 给我推荐几个会议，并介绍浏览量最高的会议信息
```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "rec",
        "db": "meeting",
        "dependency": -1,
        "data_bindings": {},
        "filters": [],
        "output_fields": [
            "meeting_id"
        ]
    },
    {
        "step": 2,
        "intent": "query",
        "subtype": "rank",
        "db": "meeting",
        "dependency": 1,
        "data_bindings": {
            "meeting_id": "step1.meeting_id"
        },
        "filters": [],
        "input_fields": [
            "meeting_id"
        ],
        "output_fields": [
            "meeting_id"
        ]
    }
]
```

## 查询名为《锡流千里》的文件

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "file",
        "db": "meeting_clip",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "fileName",
                "operator": "=",
                "value": "锡流千里"
            }
        ],
        "auth_condition": {
            "verify_mode": "password"
        }
    }
]
```


## 查询教育系统数据安全专题会议的会议文件

```json
[
    {
        "step": 1,
        "intent": "query",
        "subtype": "meeting",
        "db": "meeting",
        "dependency": -1,
        "data_bindings": {},
        "filters": [
            {
                "field": "title",
                "operator": "LIKE",
                "value": "教育系统数据安全专题会议"
            }
        ],
        "output_fields": [
            "meeting_id"
        ]
    },
    {
        "step": 2,
        "intent": "query",
        "subtype": "file",
        "db": "meeting_clip",
        "dependency": 1,
        "data_bindings": {
            "meeting_id": "step1.meeting_id"
        },
        "filters": [
            {
                "field": "meetingId",
                "operator": "=",
                "value": "step1.meeting_id"
            }
        ],
        "output_fields": []
    }
]
```

## 下载名为《锡流千里》的文件

```json

```


上面几个例子是我的最终输出，现在我要尝试再java中对上述案例进行处理，你需要按照mybatis-plus，springboot规范给我解析代码。

代码中使用if-else判断分支，其中auth_type校验不需要实现，再不同类型的操作中你可以选择不同的字段，其中intent为query主要设计查询数据库并返回数据。intent为route主要设计查询数据库然后执行this.routeFunc(x)函数。intent为action主要涉及对之前查出来的数据执行this.actionFunc(x)函数，你可以适当使用伪代码，但是主要的查询逻辑必须使用java代码完成。

我们提供了一系列功能，如会议的title我们都存到milvus中，可以通过this.queryMeeting(title)直接获得最为匹配的会议id，再对title进行LIKE操作的时候你可以使用。

如果上述案例实现一个统一的处理函数比较困难，那你可以涉及第五步骤对上述json在进行处理