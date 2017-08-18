# 天猫商品评论爬虫
原本是用于情感分析类的科研项目的需求所写的程序，做了一定修改后，希望可以作为给其它有需要的小伙伴们一个小工具。

[TOC]

# 使用说明
**输入对应天猫商城商品页面的URL，即可获抓取该商品的评论**，目前只有直接插入MYSQL数据库的方式。

## MYSQL 配置
### 建表
首先在MYSQL中执行以下建表语句，用于评论插入用。

    CREATE TABLE comments
    (
    id VARCHAR(20) PRIMARY KEY,
    content VARCHAR(1000),
    date TIMESTAMP null,
    appendContent VARCHAR(1000),
    appendDate TIMESTAMP null,
    gid VARCHAR(20)
    )


![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/数据表截图.png)

评论的可获取属性包括：content（评论文本内容），data（评论发表日期），appendContent（追加评论），appendDate(追加评论日期），gid(评论所属商品id）

## 添加商品页面URL
![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/网页url截图.png)
将想要抓取商品的url复制粘贴到项目根目录下的URLs.txt文件中，若要抓取多个商品，则每行粘贴一个商品url。
![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/url文件截图.png)

## 配置数据库
项目根目录下的res目录下，有database.properties数据库连接配置文件，分别填写好数据库连接地址，数据库名，连接登录用户名与密码。
![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/数据库配置截图.png)
## 执行抓取程序

主函数在InformationSpider.class中。

# 额外说明
由于许多评论是空内容评论（买家购买后长期未给出商品评论，系统默认为空评论），同时由于天猫后台的数据传输机制，导致每个商品有一部分的商品评论不能获取到。