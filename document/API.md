# 初见后台系统接口说明

## 登录接口

#### 功能说明

1. 登录初见后台管理系统
2. 登陆指定系统

#### 接口路径

```
192.168.1.218:18888/sso/auth
```

#### 请求方式

```markdown
POST,GET
```

#### 参数说明

| 参数   | 说明   | 是否必须 | 数据类型 | 默认值 |
| ------ | ------ | -------- | -------- | ------ |
| sys_id | 系统id | 否       | String   | 无     |

#### 返回结果

```json
进入初见后台系统或者直接进入指定的系统
```



## 获取部门信息接口

#### 功能说明

1. 获取全部部门信息

2. 获取当前部门信息

#### 接口路径

```
192.168.1.218:18888/sso/depts
```

#### 请求方式

```markdown
POST
```

#### 参数说明

| 参数    | 说明          | 是否必须 | 数据类型 | 默认值 |
| ------- | ------------- | -------- | -------- | ------ |
| token   | token字符串   | 是       | String   | 无     |
| dept_id | 部门的dept_id | 否       | String   | 无     |

#### 返回结果

部门树形json数据

```json
[
    {
        "id": "107",
        "parentId": "chujian",
        "deptId": "southeast_asia_operation",
        "deptName": "东南亚运营部",
        "deptOrderNum": "7",
        "childList": [
            {
                "id": "157",
                "parentId": "southeast_asia_operation",
                "deptId": "southeast_asia_operation_1",
                "deptName": "运营",
                "deptOrderNum": "0",
                "childList": null
            }
        ]
    }
]
```



## 获取系统菜单接口

#### 功能说明

1. 获取指定系统的菜单的树形数据

#### 接口路径

```
192.168.1.218:18888/sso/menus
```

#### 请求方式

```markdown
GET,POST
```

#### 参数说明

| 参数            | 说明              | 是否必须 | 数据类型 | 默认值 |
| --------------- | ----------------- | -------- | -------- | ------ |
| token           | token字符串       | 是       | String   | 无     |
| sys_resource_id | 系统的resource_id | 是       | String   | 无     |

#### 返回结果

系统菜单的树形json数据

```json
{"menuList":[{"id":"271","parentId":"202","resourceId":"ups_dev_client","resourceName":"客户端管理","resourceUrl":"","resourceOrderNum":"0","childList":[{"id":"272","parentId":"271","resourceId":"ups_dev_client_view","resourceName":"客户端管理","resourceUrl":"client_query","resourceOrderNum":"ups_dev_client_view","childList":null}]},{"id":"273","parentId":"202","resourceId":"ups_dev_shoppe","resourceName":"店铺管理","resourceUrl":"","resourceOrderNum":"1","childList":[{"id":"274","parentId":"273","resourceId":"ups_dev_shoppe_view","resourceName":"店铺管理","resourceUrl":"shoppe_query","resourceOrderNum":"ups_dev_shoppe_view","childList":null},{"id":"275","parentId":"273","resourceId":"ups_dev_product_view","resourceName":"产品管理","resourceUrl":"product_query","resourceOrderNum":"ups_dev_product_view","childList":null},{"id":"276","parentId":"273","resourceId":"ups_dev_goods_view","resourceName":"商品管理","resourceUrl":"goods_query","resourceOrderNum":"ups_dev_goods_view","childList":null}]},{"id":"277","parentId":"202","resourceId":"ups_dev_notice","resourceName":"公告管理","resourceUrl":"","resourceOrderNum":"2","childList":[{"id":"278","parentId":"277","resourceId":"ups_dev_notice_view","resourceName":"公告管理","resourceUrl":"notice_query","resourceOrderNum":"ups_dev_notice_view","childList":null}]},{"id":"279","parentId":"202","resourceId":"ups_dev_sdk_chujian","resourceName":"初见SDK配置管理","resourceUrl":"","resourceOrderNum":"3","childList":[{"id":"280","parentId":"279","resourceId":"ups_dev_sdk_chujian_view","resourceName":"初见SDK","resourceUrl":"sdksetting_query/chujian","resourceOrderNum":"ups_dev_sdk_chujian_view","childList":null},{"id":"281","parentId":"279","resourceId":"ups_dev_sdk_chujian_wx_view","resourceName":"微信支付","resourceUrl":"sdksetting_query/chujian-wx","resourceOrderNum":"ups_dev_sdk_chujian_wx_view","childList":null},{"id":"282","parentId":"279","resourceId":"ups_dev_sdk_chujian_wxwap_view","resourceName":"微信H5支付","resourceUrl":"sdksetting_query/chujian-wxwap","resourceOrderNum":"ups_dev_sdk_chujian_wxwap_view","childList":null},{"id":"283","parentId":"279","resourceId":"ups_dev_sdk_chujian_zfb_view","resourceName":"支付宝支付","resourceUrl":"sdksetting_query/chujian-zfb","resourceOrderNum":"ups_dev_sdk_chujian_zfb_view","childList":null},{"id":"284","parentId":"279","resourceId":"ups_dev_sdk_chujian_zfbwap_view","resourceName":"支付宝H5支付","resourceUrl":"sdksetting_query/chujian-zfbwap","resourceOrderNum":"ups_dev_sdk_chujian_zfbwap_view","childList":null},{"id":"285","parentId":"279","resourceId":"ups_dev_sdk_chujian_google_view","resourceName":"谷歌支付","resourceUrl":"sdksetting_query/chujian-google","resourceOrderNum":"ups_dev_sdk_chujian_google_view","childList":null},{"id":"286","parentId":"279","resourceId":"ups_dev_sdk_chujian_apple_view","resourceName":"苹果支付","resourceUrl":"sdksetting_query/chujian-apple","resourceOrderNum":"ups_dev_sdk_chujian_apple_view","childList":null},{"id":"287","parentId":"279","resourceId":"ups_dev_sdk_chujian_facebook_view","resourceName":"Facebook用户","resourceUrl":"sdksetting_query/chujian-facebook","resourceOrderNum":"ups_dev_sdk_chujian_facebook_view","childList":null},{"id":"288","parentId":"279","resourceId":"ups_dev_sdk_chujian_mycard_view","resourceName":"MyCard","resourceUrl":"sdksetting_query/chujian-mycard","resourceOrderNum":"ups_dev_sdk_chujian_mycard_view","childList":null}]},{"id":"289","parentId":"202","resourceId":"ups_dev_sdk_channel","resourceName":"三方SDK配置管理","resourceUrl":"","resourceOrderNum":"4","childList":[{"id":"290","parentId":"289","resourceId":"ups_dev_sdk_channel_huawei_view","resourceName":"华为","resourceUrl":"sdksetting_query/huawei","resourceOrderNum":"ups_dev_sdk_channel_huawei_view","childList":null},{"id":"291","parentId":"289","resourceId":"ups_dev_sdk_channel_oppo_view","resourceName":"OPPO","resourceUrl":"sdksetting_query/oppo","resourceOrderNum":"ups_dev_sdk_channel_oppo_view","childList":null},{"id":"292","parentId":"289","resourceId":"ups_dev_sdk_channel_vivo_view","resourceName":"VIVO","resourceUrl":"sdksetting_query/vivo","resourceOrderNum":"ups_dev_sdk_channel_vivo_view","childList":null},{"id":"293","parentId":"289","resourceId":"ups_dev_sdk_channel_uc_view","resourceName":"UC","resourceUrl":"sdksetting_query/uc","resourceOrderNum":"ups_dev_sdk_channel_uc_view","childList":null},{"id":"294","parentId":"289","resourceId":"ups_dev_sdk_channel_yyb_view","resourceName":"应用宝","resourceUrl":"sdksetting_query/yyb","resourceOrderNum":"ups_dev_sdk_channel_yyb_view","childList":null},{"id":"295","parentId":"289","resourceId":"ups_dev_sdk_channel_xiaomi_view","resourceName":"小米","resourceUrl":"sdksetting_query/xiaomi","resourceOrderNum":"ups_dev_sdk_channel_xiaomi_view","childList":null},{"id":"296","parentId":"289","resourceId":"ups_dev_sdk_channel_samsung_view","resourceName":"三星","resourceUrl":"sdksetting_query/samsung","resourceOrderNum":"ups_dev_sdk_channel_samsung_view","childList":null},{"id":"297","parentId":"289","resourceId":"ups_dev_sdk_channel_meizu_view","resourceName":"魅族","resourceUrl":"sdksetting_query/meizu","resourceOrderNum":"ups_dev_sdk_channel_meizu_view","childList":null},{"id":"298","parentId":"289","resourceId":"ups_dev_sdk_channel_nubia_view","resourceName":"努比亚","resourceUrl":"sdksetting_query/nubia","resourceOrderNum":"ups_dev_sdk_channel_nubia_view","childList":null},{"id":"299","parentId":"289","resourceId":"ups_dev_sdk_channel_pps_view","resourceName":"PPS","resourceUrl":"sdksetting_query/pps","resourceOrderNum":"ups_dev_sdk_channel_pps_view","childList":null},{"id":"300","parentId":"289","resourceId":"ups_dev_sdk_channel_qihoo360_view","resourceName":"奇虎360","resourceUrl":"sdksetting_query/qihoo360","resourceOrderNum":"ups_dev_sdk_channel_qihoo360_view","childList":null},{"id":"301","parentId":"289","resourceId":"ups_dev_sdk_channel_lenovo_view","resourceName":"联想","resourceUrl":"sdksetting_query/lenovo","resourceOrderNum":"ups_dev_sdk_channel_lenovo_view","childList":null},{"id":"302","parentId":"289","resourceId":"ups_dev_sdk_channel_gionee_view","resourceName":"金立","resourceUrl":"sdksetting_query/gionee","resourceOrderNum":"ups_dev_sdk_channel_gionee_view","childList":null},{"id":"303","parentId":"289","resourceId":"ups_dev_sdk_channel_yuewen_view","resourceName":"阅文","resourceUrl":"sdksetting_query/yuewen","resourceOrderNum":"ups_dev_sdk_channel_yuewen_view","childList":null},{"id":"304","parentId":"289","resourceId":"ups_dev_sdk_channel_baidu_view","resourceName":"百度","resourceUrl":"sdksetting_query/baidu","resourceOrderNum":"ups_dev_sdk_channel_baidu_view","childList":null},{"id":"305","parentId":"289","resourceId":"ups_dev_sdk_channel_f4399_view","resourceName":"4399","resourceUrl":"sdksetting_query/f4399","resourceOrderNum":"ups_dev_sdk_channel_f4399_view","childList":null},{"id":"306","parentId":"289","resourceId":"ups_dev_sdk_channel_papa_view","resourceName":"啪啪","resourceUrl":"sdksetting_query/papa","resourceOrderNum":"ups_dev_sdk_channel_papa_view","childList":null},{"id":"307","parentId":"289","resourceId":"ups_dev_sdk_channel_coolpad_view","resourceName":"酷派","resourceUrl":"sdksetting_query/coolpad","resourceOrderNum":"ups_dev_sdk_channel_coolpad_view","childList":null},{"id":"308","parentId":"289","resourceId":"ups_dev_sdk_channel_bilibili_view","resourceName":"哔哩哔哩","resourceUrl":"sdksetting_query/bilibili","resourceOrderNum":"ups_dev_sdk_channel_bilibili_view","childList":null},{"id":"309","parentId":"289","resourceId":"ups_dev_sdk_channel_dangle_view","resourceName":"当乐","resourceUrl":"sdksetting_query/dangle","resourceOrderNum":"ups_dev_sdk_channel_dangle_view","childList":null},{"id":"310","parentId":"289","resourceId":"ups_dev_sdk_channel_bluepay_view","resourceName":"Bluepay","resourceUrl":"sdksetting_query/bluepay","resourceOrderNum":"ups_dev_sdk_channel_bluepay_view","childList":null},{"id":"311","parentId":"289","resourceId":"ups_dev_sdk_channel_douyu_view","resourceName":"斗鱼","resourceUrl":"sdksetting_query/douyu","resourceOrderNum":"ups_dev_sdk_channel_douyu_view","childList":null}]},{"id":"312","parentId":"202","resourceId":"ups_dev_sdk_switch","resourceName":"切SDK配置管理","resourceUrl":"","resourceOrderNum":"5","childList":[{"id":"313","parentId":"312","resourceId":"ups_dev_sdk_switch_view","resourceName":"应用宝切SDK配置","resourceUrl":"sdkswitch_query","resourceOrderNum":"ups_dev_sdk_switch_view","childList":null}]},{"id":"314","parentId":"202","resourceId":"ups_dev_shipping","resourceName":"发货管理","resourceUrl":"","resourceOrderNum":"6","childList":[{"id":"315","parentId":"314","resourceId":"ups_dev_shipping_view","resourceName":"发货管理","resourceUrl":"shipping_query","resourceOrderNum":"ups_dev_shipping_view","childList":null}]},{"id":"316","parentId":"202","resourceId":"ups_dev_mta","resourceName":"MTA管理","resourceUrl":"","resourceOrderNum":"7","childList":[{"id":"317","parentId":"316","resourceId":"ups_dev_mtasetting_view","resourceName":"MTA配置管理","resourceUrl":"mtasetting_query","resourceOrderNum":"ups_dev_mtasetting_view","childList":null},{"id":"318","parentId":"316","resourceId":"ups_dev_mtarule_view","resourceName":"渠道打点规则配置管理","resourceUrl":"mtarule_query","resourceOrderNum":"ups_dev_mtarule_view","childList":null},{"id":"319","parentId":"316","resourceId":"ups_dev_mtatestsetting_view","resourceName":"MTA打点测试配置管理","resourceUrl":"mtatestsetting_query","resourceOrderNum":"ups_dev_mtatestsetting_view","childList":null},{"id":"320","parentId":"316","resourceId":"ups_dev_mtatestlog_view","resourceName":"查询打点日志","resourceUrl":"mtatestlog_query","resourceOrderNum":"ups_dev_mtatestlog_view","childList":null}]},{"id":"321","parentId":"202","resourceId":"ups_dev_channel_grpc","resourceName":"渠道GRPC管理","resourceUrl":"","resourceOrderNum":"8","childList":[{"id":"322","parentId":"321","resourceId":"ups_dev_channel_grpc_view","resourceName":"渠道GRPC配置管理","resourceUrl":"channelgrpc_query","resourceOrderNum":"ups_dev_channel_grpc_view","childList":null}]},{"id":"323","parentId":"202","resourceId":"ups_dev_user","resourceName":"用户管理","resourceUrl":"","resourceOrderNum":"9","childList":[{"id":"324","parentId":"323","resourceId":"ups_dev_user_view","resourceName":"用户管理","resourceUrl":"user_query","resourceOrderNum":"ups_dev_user_view","childList":null},{"id":"325","parentId":"323","resourceId":"ups_dev_useremail_view","resourceName":"用户邮件模板管理","resourceUrl":"useremail_query","resourceOrderNum":"ups_dev_useremail_view","childList":null}]},{"id":"326","parentId":"202","resourceId":"ups_dev_order","resourceName":"订单管理","resourceUrl":"","resourceOrderNum":"10","childList":[{"id":"327","parentId":"326","resourceId":"ups_dev_order_view","resourceName":"查询订单","resourceUrl":"order_query","resourceOrderNum":"ups_dev_order_view","childList":null},{"id":"328","parentId":"326","resourceId":"ups_dev_order_simulate_recharge_view","resourceName":"模拟充值","resourceUrl":"order_simulate_recharge","resourceOrderNum":"ups_dev_order_simulate_recharge_view","childList":null}]},{"id":"329","parentId":"202","resourceId":"ups_dev_share","resourceName":"游戏分享管理","resourceUrl":"","resourceOrderNum":"11","childList":[{"id":"330","parentId":"329","resourceId":"ups_dev_share_gameinfo_view","resourceName":"游戏分享配置","resourceUrl":"gameshare/gameinfo_query","resourceOrderNum":"ups_dev_share_gameinfo_view","childList":null},{"id":"331","parentId":"329","resourceId":"ups_dev_share_gamesetting_view","resourceName":"游戏分享控制配置","resourceUrl":"gameshare/gamesetting_query","resourceOrderNum":"ups_dev_share_gamesetting_view","childList":null},{"id":"332","parentId":"329","resourceId":"ups_dev_share_gamegift_view","resourceName":"游戏分享礼包配置","resourceUrl":"gameshare/gamegift_query","resourceOrderNum":"ups_dev_share_gamegift_view","childList":null}]}]}
```



## 获取token字符串接口

#### 功能说明

1. 获取token字符串

#### 接口路径

```
192.168.1.218:18888/sso/token
```

#### 请求方式

```markdown
GET
```

#### 参数说明

| 参数      | 说明                       | 是否必须 | 数据类型 | 默认值 |
| --------- | -------------------------- | -------- | -------- | ------ |
| auth_code | 访问系统时生成的 auth_code | 是       | String   | 无     |

#### 返回结果

JWT加密后token的json串

```json
{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiYWRtaW4iLCJ1c2VyX25hbWUiOiLliJ3op4HlkI7lj7Dns7vnu5_nrqHnkIblkZgiLCJpYXQiOjE1NzQ0MTg1NzQ3NTcsImV4cCI6MTU3ODAxODU3NDc1NywidmVyc2lvbiI6Miwicm9sZXMiOlt7ImlkIjoiYWRtaW4iLCJuYW1lIjoi5Yid6KeB5ZCO5Y-w566h55CG5ZGYIn1dLCJyZXNvdXJjZXMiOlt7ImlkIjoiOTMiLCJwYXJlbnRJZCI6IjAiLCJyZXNvdXJjZUlkIjoic3lzIiwidHlwZSI6Im1lbnUiLCJuYW1lIjoi5Yid6KeB5ZCO5Y-w57O757uf566h55CGIiwidXJsIjoiIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6W3siaWQiOiI5NCIsInBhcmVudElkIjoiOTMiLCJyZXNvdXJjZUlkIjoic3lzX3VzZXIiLCJ0eXBlIjoibWVudSIsIm5hbWUiOiLnlKjmiLfnrqHnkIYiLCJ1cmwiOiIiLCJvcmRlck51bSI6MCwiY2hpbGRMaXN0IjpbeyJpZCI6Ijk1IiwicGFyZW50SWQiOiI5NCIsInJlc291cmNlSWQiOiJzeXNfdXNlcl92aWV3IiwidHlwZSI6Im1lbnUiLCJuYW1lIjoi55So5oi3566h55CGIiwidXJsIjoidXNlcl9tYW5hZ2UiLCJvcmRlck51bSI6MCwiY2hpbGRMaXN0IjpbeyJpZCI6IjExNiIsInBhcmVudElkIjoiOTUiLCJyZXNvdXJjZUlkIjoidXNlcl9hZGQiLCJ0eXBlIjoiYnV0dG9uIiwibmFtZSI6Iua3u-WKoOeUqOaItyIsInVybCI6IiIsIm9yZGVyTnVtIjowLCJjaGlsZExpc3QiOm51bGx9LHsiaWQiOiIxMTgiLCJwYXJlbnRJZCI6Ijk1IiwicmVzb3VyY2VJZCI6InVzZXJfZWRpdCIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi57yW6L6R55So5oi3IiwidXJsIjoiIiwib3JkZXJOdW0iOjEsImNoaWxkTGlzdCI6bnVsbH0seyJpZCI6IjExOSIsInBhcmVudElkIjoiOTUiLCJyZXNvdXJjZUlkIjoidXNlcl9kZWwiLCJ0eXBlIjoiYnV0dG9uIiwibmFtZSI6IuWIoOmZpOeUqOaItyIsInVybCI6IiIsIm9yZGVyTnVtIjoyLCJjaGlsZExpc3QiOm51bGx9LHsiaWQiOiIxMjAiLCJwYXJlbnRJZCI6Ijk1IiwicmVzb3VyY2VJZCI6InVzZXJfcm9sZSIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi5Li655So5oi35YiG6YWN6KeS6ImyIiwidXJsIjoiIiwib3JkZXJOdW0iOjMsImNoaWxkTGlzdCI6bnVsbH1dfV19LHsiaWQiOiI5NiIsInBhcmVudElkIjoiOTMiLCJyZXNvdXJjZUlkIjoic3lzX3JvbGUiLCJ0eXBlIjoibWVudSIsIm5hbWUiOiLop5LoibLnrqHnkIYiLCJ1cmwiOiIiLCJvcmRlck51bSI6MSwiY2hpbGRMaXN0IjpbeyJpZCI6IjEwMCIsInBhcmVudElkIjoiOTYiLCJyZXNvdXJjZUlkIjoic3lzX3JvbGVfdmlldyIsInR5cGUiOiJtZW51IiwibmFtZSI6IuinkuiJsueuoeeQhiIsInVybCI6InJvbGVfbWFuYWdlIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6W3siaWQiOiIxMTciLCJwYXJlbnRJZCI6IjEwMCIsInJlc291cmNlSWQiOiJyb2xlX2FkZCIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi5re75Yqg6KeS6ImyIiwidXJsIjoiIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6bnVsbH0seyJpZCI6IjEwOSIsInBhcmVudElkIjoiMTAwIiwicmVzb3VyY2VJZCI6InJvbGVfZWRpdCIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi57yW6L6R6KeS6ImyIiwidXJsIjoiIiwib3JkZXJOdW0iOjEsImNoaWxkTGlzdCI6bnVsbH0seyJpZCI6IjExMCIsInBhcmVudElkIjoiMTAwIiwicmVzb3VyY2VJZCI6InJvbGVfZGVsIiwidHlwZSI6ImJ1dHRvbiIsIm5hbWUiOiLliKDpmaTop5LoibIiLCJ1cmwiOiIiLCJvcmRlck51bSI6MiwiY2hpbGRMaXN0IjpudWxsfSx7ImlkIjoiMTExIiwicGFyZW50SWQiOiIxMDAiLCJyZXNvdXJjZUlkIjoicm9sZV9yZXNvdXJjZSIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi5Li66KeS6Imy5YiG6YWN6LWE5rqQIiwidXJsIjoiIiwib3JkZXJOdW0iOjMsImNoaWxkTGlzdCI6bnVsbH0seyJpZCI6IjExMiIsInBhcmVudElkIjoiMTAwIiwicmVzb3VyY2VJZCI6InJvbGVfZ2FtZSIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi5Li66KeS6Imy5YiG6YWN5ri45oiPIiwidXJsIjoiIiwib3JkZXJOdW0iOjQsImNoaWxkTGlzdCI6bnVsbH1dfV19LHsiaWQiOiI5NyIsInBhcmVudElkIjoiOTMiLCJyZXNvdXJjZUlkIjoic3lzX3Jlc291cmNlIiwidHlwZSI6Im1lbnUiLCJuYW1lIjoi6LWE5rqQ566h55CGIiwidXJsIjoiIiwib3JkZXJOdW0iOjIsImNoaWxkTGlzdCI6W3siaWQiOiIxMDEiLCJwYXJlbnRJZCI6Ijk3IiwicmVzb3VyY2VJZCI6InN5c19yZXNvdXJjZV92aWV3IiwidHlwZSI6Im1lbnUiLCJuYW1lIjoi6LWE5rqQ566h55CGIiwidXJsIjoicmVzb3VyY2VfbWFuYWdlIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6W3siaWQiOiIxNDgiLCJwYXJlbnRJZCI6IjEwMSIsInJlc291cmNlSWQiOiJyZXNvdXJjZV9jb3B5IiwidHlwZSI6ImJ1dHRvbiIsIm5hbWUiOiLlpI3liLbotYTmupAiLCJ1cmwiOiIiLCJvcmRlck51bSI6MCwiY2hpbGRMaXN0IjpudWxsfSx7ImlkIjoiMjEwIiwicGFyZW50SWQiOiIxMDEiLCJyZXNvdXJjZUlkIjoicmVzb3VyY2VfYWRkIiwidHlwZSI6ImJ1dHRvbiIsIm5hbWUiOiLmt7vliqDotYTmupAiLCJ1cmwiOiIiLCJvcmRlck51bSI6MSwiY2hpbGRMaXN0IjpudWxsfSx7ImlkIjoiMjExIiwicGFyZW50SWQiOiIxMDEiLCJyZXNvdXJjZUlkIjoicmVzb3VyY2VfZWRpdCIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi57yW6L6R6LWE5rqQIiwidXJsIjoiIiwib3JkZXJOdW0iOjIsImNoaWxkTGlzdCI6bnVsbH1dfV19LHsiaWQiOiI5OCIsInBhcmVudElkIjoiOTMiLCJyZXNvdXJjZUlkIjoic3lzX2dhbWUiLCJ0eXBlIjoibWVudSIsIm5hbWUiOiLmuLjmiI_nrqHnkIYiLCJ1cmwiOiIiLCJvcmRlck51bSI6MywiY2hpbGRMaXN0IjpbeyJpZCI6IjEwMiIsInBhcmVudElkIjoiOTgiLCJyZXNvdXJjZUlkIjoic3lzX2dhbWVfdmlldyIsInR5cGUiOiJtZW51IiwibmFtZSI6Iua4uOaIj-euoeeQhiIsInVybCI6ImdhbWVfbWFuYWdlIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6W3siaWQiOiIxMTMiLCJwYXJlbnRJZCI6IjEwMiIsInJlc291cmNlSWQiOiJnYW1lX2FkZCIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi5re75Yqg5ri45oiPIiwidXJsIjoiIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6bnVsbH0seyJpZCI6IjExNCIsInBhcmVudElkIjoiMTAyIiwicmVzb3VyY2VJZCI6ImdhbWVfZWRpdCIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi57yW6L6R5ri45oiPIiwidXJsIjoiIiwib3JkZXJOdW0iOjEsImNoaWxkTGlzdCI6bnVsbH0seyJpZCI6IjExNSIsInBhcmVudElkIjoiMTAyIiwicmVzb3VyY2VJZCI6ImdhbWVfZGVsIiwidHlwZSI6ImJ1dHRvbiIsIm5hbWUiOiLliKDpmaTmuLjmiI8iLCJ1cmwiOiIiLCJvcmRlck51bSI6MiwiY2hpbGRMaXN0IjpudWxsfV19XX0seyJpZCI6Ijk5IiwicGFyZW50SWQiOiI5MyIsInJlc291cmNlSWQiOiJzeXNfZGVwdCIsInR5cGUiOiJtZW51IiwibmFtZSI6IumDqOmXqOeuoeeQhiIsInVybCI6IiIsIm9yZGVyTnVtIjo0LCJjaGlsZExpc3QiOlt7ImlkIjoiMTAzIiwicGFyZW50SWQiOiI5OSIsInJlc291cmNlSWQiOiJzeXNfZGVwdF92aWV3IiwidHlwZSI6Im1lbnUiLCJuYW1lIjoi6YOo6Zeo566h55CGIiwidXJsIjoiZGVwdF9tYW5hZ2UiLCJvcmRlck51bSI6MCwiY2hpbGRMaXN0IjpbeyJpZCI6IjIxMyIsInBhcmVudElkIjoiMTAzIiwicmVzb3VyY2VJZCI6ImRlcHRfYWRkIiwidHlwZSI6ImJ1dHRvbiIsIm5hbWUiOiLmt7vliqDpg6jpl6giLCJ1cmwiOiIiLCJvcmRlck51bSI6MCwiY2hpbGRMaXN0IjpudWxsfSx7ImlkIjoiMjE0IiwicGFyZW50SWQiOiIxMDMiLCJyZXNvdXJjZUlkIjoiZGVwdF9lZGl0IiwidHlwZSI6ImJ1dHRvbiIsIm5hbWUiOiLnvJbovpHpg6jpl6giLCJ1cmwiOiIiLCJvcmRlck51bSI6MSwiY2hpbGRMaXN0IjpudWxsfV19XX1dfSx7ImlkIjoiMjA1IiwicGFyZW50SWQiOiIwIiwicmVzb3VyY2VJZCI6ImFkIiwidHlwZSI6InN5c3RlbSIsIm5hbWUiOiJBROezu-e7nyIsInVybCI6Imh0dHA6Ly93d3cuYmFpZHUuY29tIiwib3JkZXJOdW0iOjEsImNoaWxkTGlzdCI6W3siaWQiOiIyMDYiLCJwYXJlbnRJZCI6IjIwNSIsInJlc291cmNlSWQiOiJhZF9sankiLCJ0eXBlIjoic3lzdGVtIiwibmFtZSI6IkFE57O757ufLem-mee6quWFgyIsInVybCI6Imh0dHA6Ly93d3cud2VpYm8uY29tIiwib3JkZXJOdW0iOjEsImNoaWxkTGlzdCI6bnVsbH1dfSx7ImlkIjoiMjAxIiwicGFyZW50SWQiOiIwIiwicmVzb3VyY2VJZCI6InVwcyIsInR5cGUiOiJzeXN0ZW0iLCJuYW1lIjoiVVBT57O757ufIiwidXJsIjoiIiwib3JkZXJOdW0iOjIsImNoaWxkTGlzdCI6W3siaWQiOiIyMDIiLCJwYXJlbnRJZCI6IjIwMSIsInJlc291cmNlSWQiOiJ1cHNfZGV2IiwidHlwZSI6InN5c3RlbSIsIm5hbWUiOiJVUFPns7vnu58t5byA5Y-RIiwidXJsIjoiaHR0cDovLzE5Mi4xNjguMS4yMTg6OTg5OCIsIm9yZGVyTnVtIjoxLCJjaGlsZExpc3QiOlt7ImlkIjoiMjE1IiwicGFyZW50SWQiOiIyMDIiLCJyZXNvdXJjZUlkIjoidXBzX3VzZXIiLCJ0eXBlIjoibWVudSIsIm5hbWUiOiLnlKjmiLfnrqHnkIYiLCJ1cmwiOiIiLCJvcmRlck51bSI6MCwiY2hpbGRMaXN0IjpbeyJpZCI6IjIxNiIsInBhcmVudElkIjoiMjE1IiwicmVzb3VyY2VJZCI6InVwc191c2VyX3ZpZXciLCJ0eXBlIjoibWVudSIsIm5hbWUiOiLnlKjmiLfnrqHnkIYiLCJ1cmwiOiJ1cHNfbWFuYWdlIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6W3siaWQiOiIyMTkiLCJwYXJlbnRJZCI6IjIxNiIsInJlc291cmNlSWQiOiJ1c2VyX2FkZCIsInR5cGUiOiJidXR0b24iLCJuYW1lIjoi5re75Yqg55So5oi3IiwidXJsIjoiIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6bnVsbH1dfV19LHsiaWQiOiIyMTciLCJwYXJlbnRJZCI6IjIwMiIsInJlc291cmNlSWQiOiJ1cHNfZ2FtZSIsInR5cGUiOiJtZW51IiwibmFtZSI6Iua4uOaIj-euoeeQhiIsInVybCI6IiIsIm9yZGVyTnVtIjoxLCJjaGlsZExpc3QiOlt7ImlkIjoiMjE4IiwicGFyZW50SWQiOiIyMTciLCJyZXNvdXJjZUlkIjoidXBzX2dhbWVfdmlldyIsInR5cGUiOiJtZW51IiwibmFtZSI6Iua4uOaIj-euoeeQhiIsInVybCI6ImdhbWVfbWFuYWdlIiwib3JkZXJOdW0iOjAsImNoaWxkTGlzdCI6bnVsbH1dfV19LHsiaWQiOiIyMDMiLCJwYXJlbnRJZCI6IjIwMSIsInJlc291cmNlSWQiOiJ1cHNfb2NfZGV2IiwidHlwZSI6InN5c3RlbSIsIm5hbWUiOiJVUFPns7vnu58t5rW35aSW5byA5Y-RIiwidXJsIjoiaHR0cDovLzExOS4yOC4xMjIuODY6OTg5OCIsIm9yZGVyTnVtIjoyLCJjaGlsZExpc3QiOm51bGx9LHsiaWQiOiIyMDQiLCJwYXJlbnRJZCI6IjIwMSIsInJlc291cmNlSWQiOiJ1cHNfb2NfZ2F0bGp5IiwidHlwZSI6InN5c3RlbSIsIm5hbWUiOiJVUFPns7vnu58t5riv5r6z5Y-w6b6Z57qq5YWDIiwidXJsIjoiaHR0cDovLzEyOS4yMjYuNjcuMjA6OTg5OCIsIm9yZGVyTnVtIjozLCJjaGlsZExpc3QiOm51bGx9LHsiaWQiOiIyMDciLCJwYXJlbnRJZCI6IjIwMSIsInJlc291cmNlSWQiOiJ1cHNfb2NfdGhsankiLCJ0eXBlIjoic3lzdGVtIiwibmFtZSI6IlVQU-ezu-e7ny3ms7Dlm73pvpnnuqrlhYMiLCJ1cmwiOiIiLCJvcmRlck51bSI6MywiY2hpbGRMaXN0IjpudWxsfV19XSwiZ2FtZXMiOlt7ImlkIjoidGdzdyIsIm5hbWUiOiLlpKrlj6TnpZ7njosifSx7ImlkIjoidHgiLCJuYW1lIjoi5ZCe5pifIn0seyJpZCI6ImxqeSIsIm5hbWUiOiLpvpnnuqrlhYMifV0sImRlcHQiOnsiZGVwdElkIjoiY2h1amlhbiIsIm5hbWUiOiLliJ3op4EifX0.CQ3QJSakqQaz1NJGrB9SKV2vcoG_MsAWYMk-2EGD8IEWz2H7Ev_AO1GARObGXDA6dcCST5ibY45IXO5Mf0Pq4ALR6e2LQ0Y8CRSlPkNP0gSU7huKm5dFSCgfM2F4bf4MCoYTz-0q28bkxd7ewS3DuxTA8_M5f3br2rMLuJ1LG1nm971sX3CsN3nuz9Rm2A6la10YgvAk6TiD472YOArK6a7Oig5HXAq784A40bFDrMk2wemkCHiHV3AHjs0fQGuxBFFYxtQcZki5YYwIEjxM4PmFuzfbDhwjZ7IEGC0iW8y7cMtD3SXyp0wf9rA4u4z8fjqxegjDsq4YYMQEbCcmmw"}
```



## 检查token字符串接口

#### 功能说明

1. 检查token字符串是否正确

#### 接口路径

```
192.168.1.218:18888/sso/check_token
```

#### 请求方式

```markdown
GET,POST
```

#### 参数说明

| 参数  | 说明        | 是否必须 | 数据类型 | 默认值 |
| ----- | ----------- | -------- | -------- | ------ |
| token | token字符串 | 是       | String   | 无     |

#### 返回结果

JWT加密前token的json数据

```json
{"valid":true,"user_id":"admin","user_name":"初见后台系统管理员","roles":[{"id":"admin","name":"初见后台管理员"}],"games":[{"id":"tgsw","name":"太古神王"},{"id":"tx","name":"吞星"},{"id":"ljy","name":"龙纪元"}],"resources":[{"id":"93","parentId":"0","resourceId":"sys","type":"menu","name":"初见后台系统管理","url":"","orderNum":0,"childList":[{"id":"94","parentId":"93","resourceId":"sys_user","type":"menu","name":"用户管理","url":"","orderNum":0,"childList":[{"id":"95","parentId":"94","resourceId":"sys_user_view","type":"menu","name":"用户管理","url":"user_manage","orderNum":0,"childList":[{"id":"116","parentId":"95","resourceId":"user_add","type":"button","name":"添加用户","url":"","orderNum":0,"childList":null},{"id":"118","parentId":"95","resourceId":"user_edit","type":"button","name":"编辑用户","url":"","orderNum":1,"childList":null},{"id":"119","parentId":"95","resourceId":"user_del","type":"button","name":"删除用户","url":"","orderNum":2,"childList":null},{"id":"120","parentId":"95","resourceId":"user_role","type":"button","name":"为用户分配角色","url":"","orderNum":3,"childList":null}]}]},{"id":"96","parentId":"93","resourceId":"sys_role","type":"menu","name":"角色管理","url":"","orderNum":1,"childList":[{"id":"100","parentId":"96","resourceId":"sys_role_view","type":"menu","name":"角色管理","url":"role_manage","orderNum":0,"childList":[{"id":"117","parentId":"100","resourceId":"role_add","type":"button","name":"添加角色","url":"","orderNum":0,"childList":null},{"id":"109","parentId":"100","resourceId":"role_edit","type":"button","name":"编辑角色","url":"","orderNum":1,"childList":null},{"id":"110","parentId":"100","resourceId":"role_del","type":"button","name":"删除角色","url":"","orderNum":2,"childList":null},{"id":"111","parentId":"100","resourceId":"role_resource","type":"button","name":"为角色分配资源","url":"","orderNum":3,"childList":null},{"id":"112","parentId":"100","resourceId":"role_game","type":"button","name":"为角色分配游戏","url":"","orderNum":4,"childList":null}]}]},{"id":"97","parentId":"93","resourceId":"sys_resource","type":"menu","name":"资源管理","url":"","orderNum":2,"childList":[{"id":"101","parentId":"97","resourceId":"sys_resource_view","type":"menu","name":"资源管理","url":"resource_manage","orderNum":0,"childList":[{"id":"148","parentId":"101","resourceId":"resource_copy","type":"button","name":"复制资源","url":"","orderNum":0,"childList":null},{"id":"210","parentId":"101","resourceId":"resource_add","type":"button","name":"添加资源","url":"","orderNum":1,"childList":null},{"id":"211","parentId":"101","resourceId":"resource_edit","type":"button","name":"编辑资源","url":"","orderNum":2,"childList":null}]}]},{"id":"98","parentId":"93","resourceId":"sys_game","type":"menu","name":"游戏管理","url":"","orderNum":3,"childList":[{"id":"102","parentId":"98","resourceId":"sys_game_view","type":"menu","name":"游戏管理","url":"game_manage","orderNum":0,"childList":[{"id":"113","parentId":"102","resourceId":"game_add","type":"button","name":"添加游戏","url":"","orderNum":0,"childList":null},{"id":"114","parentId":"102","resourceId":"game_edit","type":"button","name":"编辑游戏","url":"","orderNum":1,"childList":null},{"id":"115","parentId":"102","resourceId":"game_del","type":"button","name":"删除游戏","url":"","orderNum":2,"childList":null}]}]},{"id":"99","parentId":"93","resourceId":"sys_dept","type":"menu","name":"部门管理","url":"","orderNum":4,"childList":[{"id":"103","parentId":"99","resourceId":"sys_dept_view","type":"menu","name":"部门管理","url":"dept_manage","orderNum":0,"childList":[{"id":"213","parentId":"103","resourceId":"dept_add","type":"button","name":"添加部门","url":"","orderNum":0,"childList":null},{"id":"214","parentId":"103","resourceId":"dept_edit","type":"button","name":"编辑部门","url":"","orderNum":1,"childList":null}]}]}]},{"id":"205","parentId":"0","resourceId":"ad","type":"system","name":"AD系统","url":"http://www.baidu.com","orderNum":1,"childList":[{"id":"206","parentId":"205","resourceId":"ad_ljy","type":"system","name":"AD系统-龙纪元","url":"http://www.weibo.com","orderNum":1,"childList":null}]},{"id":"201","parentId":"0","resourceId":"ups","type":"system","name":"UPS系统","url":"","orderNum":2,"childList":[{"id":"202","parentId":"201","resourceId":"ups_dev","type":"system","name":"UPS系统-开发","url":"http://192.168.1.218:9898","orderNum":1,"childList":[{"id":"215","parentId":"202","resourceId":"ups_user","type":"menu","name":"用户管理","url":"","orderNum":0,"childList":[{"id":"216","parentId":"215","resourceId":"ups_user_view","type":"menu","name":"用户管理","url":"ups_manage","orderNum":0,"childList":[{"id":"219","parentId":"216","resourceId":"user_add","type":"button","name":"添加用户","url":"","orderNum":0,"childList":null}]}]},{"id":"217","parentId":"202","resourceId":"ups_game","type":"menu","name":"游戏管理","url":"","orderNum":1,"childList":[{"id":"218","parentId":"217","resourceId":"ups_game_view","type":"menu","name":"游戏管理","url":"game_manage","orderNum":0,"childList":null}]}]},{"id":"203","parentId":"201","resourceId":"ups_oc_dev","type":"system","name":"UPS系统-海外开发","url":"http://119.28.122.86:9898","orderNum":2,"childList":null},{"id":"204","parentId":"201","resourceId":"ups_oc_gatljy","type":"system","name":"UPS系统-港澳台龙纪元","url":"http://129.226.67.20:9898","orderNum":3,"childList":null},{"id":"207","parentId":"201","resourceId":"ups_oc_thljy","type":"system","name":"UPS系统-泰国龙纪元","url":"","orderNum":3,"childList":null}]}],"dept":{"deptId":"chujian","name":"初见"},"exp":1578018574757,"iat":1574418574757,"version":2}
```



## 退出接口

#### 功能说明

1. 退出系统

#### 接口路径

```
192.168.1.218:18888/sso/logout
```

#### 请求方式

```markdown
GET,POST
```

#### 参数说明

| 参数  | 说明        | 是否必须 | 数据类型 | 默认值 |
| ----- | ----------- | -------- | -------- | ------ |
| token | token字符串 | s是      | String   | 无     |

#### 返回结果

状态码:

```json
成功: 204
```

