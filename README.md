# 天猫商品评论爬虫
原本是用于情感分析类的科研项目的需求所写的程序，做了一定修改后，希望可以作为给其它有需要的小伙伴们一个小工具。

Lib:[Jsoup](https://jsoup.org/)

[TOC]

# 使用说明

**输入对应天猫商城商品页面的URL，即可获抓取该商品的评论**，目前可以选择

1. 插入MYSQL数据库
2. 生成Excel.xls表格

两种方式将数据保留下来（详见下方【数据存储】）。 


## 添加商品页面URL
![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/网页url截图.png)

将想要抓取商品的url复制粘贴到项目根目录下的URLs.txt文件中，若要抓取多个商品，则每行粘贴一个商品url。

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/url文件截图.png)

## 执行抓取程序

主函数在InformationSpider.class中。控制台会输出抓取进度。



# 数据存储

## 数据库存储方式

### 1. MYSQL 配置

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


评论的可获取属性包括：content（评论文本内容），data（评论发表日期），appendContent（追加评论），appendDate(追加评论日期），gid(评论所属商品id）

### 2. 配置数据库连接文件

项目根目录下的res目录下，有database.properties数据库连接配置文件，分别填写好数据库连接地址，数据库名，连接登录用户名与密码。

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/数据库配置截图.png)

### 3. 配置运行参数

项目根目录下的res目录下，有setttings.properties抓取程序配置文件，将toDatabase参数设置为true（默认为false），程序运行完毕后即自动入库。

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/settings配置文件截图.png)

入库后

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/数据表截图.png)

## Excel存储方式

项目根目录下的res目录下，有setttings.properties抓取程序配置文件，将toExcel参数设置为true（默认为true），程序运行完毕后即自动生成excel文件，控制台会输出文件生成路径（默认路径在项目根目录下）。

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/示例excel截图.png)






# 示例

**以这款笔记本电脑为例**

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/示例商品截图.png)

将它的url链接粘贴到URLs.txt文件中。

确认database.properties与settings.properties的运行参数后（默认参数），运行抓取程序。

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/运行截图.png)

输出excel结果

![picture](https://github.com/bluemapleman/TMallCommentSpider/blob/master/snapshot/示例excel截图.png)

# 额外说明
- 由于许多评论是空内容评论（买家购买后长期未给出商品评论，系统默认为空评论），同时由于天猫后台的数据传输机制，导致每个商品有一部分的商品评论不能获取到。
- 抓取过程中，由于网络原因，有时可能报java.net.SocketTimeoutException，可不予理会。
- 由于以上原因，可在根目录下的res下的setting文件中配置程序自动的多轮抓取(iterationTimes，默认为1，即进行一轮抓取)，保证一次性抓取尽可能多的评论，或者用户亦可手动多次启动程序进行抓取。
- 若有其他功能需求，或者其他抓取数据需求，欢迎联系我：TomQianMaple@outlook.com
