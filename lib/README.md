# 天猫商品评论爬虫
原本是用于情感分析类的科研项目的需求所写的程序，做了一定修改后，希望可以作为给其它有需要的小伙伴们一个小工具。

[TomQianMaple@outlook.com](TomQianMaple@outlook.com)

[TOC]

# 使用说明
**输入对应天猫商城商品页面的URL，即可获抓取该商品的指定数量的评论**，目前只有直接插入MYSQL数据库的方式，**马上推出输出到Excel文件中的方式。**



## MYSQL 配置
### 建表
首先在MYSQL中执行以下建表语句，用于评论插入用。

    Drop TABLE comments;
    CREATE TABLE comments
    (
    id VARCHAR(20) PRIMARY KEY,
    content VARCHAR(0),
    date TIMESTAMP null,
    tagId VARCHAR(20),
    appendContent VARCHAR(0),
    appendDate TIMESTAMP null,
    gid VARCHAR(20)
    )


![picture]()

评论的可获取属性包括：content（评论文本内容），data(评论发表日期），tagId（评论对应标签，如果没有为null），appendContent（追加评论），appendDate(追加评论日期），gid(评论所属商品id）

    
## 添加商品页面URL

## 执行抓取程序