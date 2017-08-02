---
layout: post
date: 2017-07-28
title: ElasticSearch实用手册
categories: manual
tags: [elasticsearch,manual]
avatarimg: "/img/head.jpg"
author: wangyifan
published: false
---



## 什么是ElasticSearch

## ElasticSearch能做什么

## ElasticSearch相关概念

- **Near Realtime (NRT)**:近实时搜索平台
- **Cluster**:node集合，默认集群名称为"elasticsearch",node通过配置集群名称来加入到对应集群
- **Node**:搜索服务器，默认通过UUID作为唯一标识符。启动一个node，实际上启动的是一个只包含一个node的集群。
- **Index**:包含类似特性的document的集合。由全小写字母组成的名称来唯一标识。该名字在执行索引、搜索、更新和删除document操作时会使用到
- **Type**:在index上，你可以定义一个或多个type。type是index上的一个逻辑目录/区间，具体语义由你自由决定。一般type是用来定义document的通用字段的
- **Document**:可被索引的JSON格式的信息单元。index/type里可保存任意多的document。document虽然在物理上属于index，但是每一个document都必须被分配一个type
- **Shards & Replicas**:为实现水平扩容以及分布式存储，es将一个index切分为多片（默认为5），每片称为一个shard！每个shard可以有0个或多个replica（默认1个）replica可提高数据安全性以及并发搜索的吞吐！当shard有replica时，原始shard称为primary shard，其余为replica shard！shard数量和replica数量可以在index创建时定义，replica数量定义后不可修改,shard数量可以修改!
- **Mapping**:定义一个document及其field如何被保存和索引。

## ElasticSearch安装

下载ES

```
curl -L -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.5.1.tar.gz
```

解压

```
tar -xvf elasticsearch-5.5.1.tar.gz
```

进入解压目录下的bin目录

```
cd elasticsearch-5.5.1/bin
```

启动es

```
./elasticsearch
```

## ElasticSearch文档的CRUD命令

- 创建/索引

```
PUT twitter/tweet/1
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

- 获取

```
GET twitter/tweet/1
```

- 多索引获取

```
GET _mget
{
    "docs" : [
        {
            "_index" : "test",
            "_type" : "type",
            "_id" : "1"
        },
        {
            "_index" : "test",
            "_type" : "type",
            "_id" : "2"
        }
    ]
}
```



- 根据id删除

```
DELETE /twitter/tweet/1
```

- 基于搜索的删除

```
POST twitter/_delete_by_query
{
  "query": { 
    "match": {
      "message": "some message"
    }
  }
}
```

- 更新

```
//新建一个文档
PUT test/type1/1
{
    "counter" : 1,
    "tags" : ["red"]
}
//更新count为4
POST test/type1/1/_update
{
    "script" : {
        "inline": "ctx._source.counter += params.count",
        "lang": "painless",
        "params" : {
            "count" : 4
        }
    }
}
```



## ElasticSearch的搜索

## ElasticSearch打分
