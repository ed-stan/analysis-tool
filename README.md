# anaysis-tool

配置分析工具demo

#### Description

该项目为配置分析工具框架demo，可通过插件和脚本方式实现流程自定义分析和自动化执行

#### Config

**入参配置**

示例

```
time,location,person #入参变量名
```

**执行配置**

示例

```json
[
    {
        #执行类型，插件执行或者脚本执行
        "type": "bean", 
        #若为插件执行，提供插件名
        "nodeParam": {
            "name": "demoPlugin"
        },
        #任务执行参数
        "taskParam": [
            {
                #参数名是根据插件逻辑设置，不可随意更改
                "name": "timeParam",
                #该value为引用
                "value": "time",
                "index": 0
            },
            {
                "name": "locationParam",
                #该value为引用
                "value": "location",
                "index": 1
            }
        ],
        #任务输出参数
        "outputParam": {
            "event": "event",
            "pluginParam":"pluginParam"
        }
    },
    {
        #执行类型，插件执行或者脚本执行
        "type": "script",
        #若为脚本执行，提供git仓库地址、路径和token
        "nodeParam": {
            #脚本类型
            "scriptType": "shell",
            "url": "https://%s@gitee.com/edstan/analysis-tool-script.git",
            "path": "/shell/demo.sh",
            "token": "ffddd77a5dc2e020ba9eaed58025ffb6"
        },
        #任务执行参数
        "taskParam": [
            {
                "name": "personParam",
                "value": "person",
                "index": 0
            },
            {
                "name": "eventParam",
                "value": "event",
                "index": 1
            }
        ],
        #输出参数，表示将根据索引将输出对应行数的数据赋值给变量
        "outputParam": {
            "str1": 0,
            "str2": 1,
            "str3": 2
        }
    }
]

```

**结果配置**

示例

```
pluginParam,str1,str2,str3 #结果变量
```



#### API

**地址**

`curl -X GET http://localhost:8080/analysis/tool/execute`

**请求方式**

Post

**Mock参数**

```json
{
    "time":"2024-11-25 15:35:00",
    "location":"New York",
    "person":"stan"
}
```

**参数释义**

| 字段     | 解释     | 是否必填 |
| -------- | -------- | -------- |
| time     | 配置参数 | 是       |
| location | 配置参数 | 是       |
| person   | 配置参数 | 是       |

**接口释义**

根据配置传入任务执行参数后，将任务提交到后台异步执行。

执行完成后，结果将输出到控制台

示例：

```
2024-11-26T14:25:45.348+08:00  INFO 58160 --- [analysis-tool] [nio-8080-exec-2] c.a.t.l.spring.AnalysisEventListener     : task execute over, result is :{str3=This is a demo script., str1=stan, str2=demoPlugin, pluginParam=2024-11-25 15:35:00,New York}
```

（注：demo为做演示并未持久化，实际可根据需要将结果存入数据库。）

