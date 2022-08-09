<!--
 * @Author: firefly-ing fireflyinging@163.com
 * @Date: 2022-08-08 15:01:38
 * @LastEditors: firefly-ing fireflyinging@163.com
 * @LastEditTime: 2022-08-08 15:25:03
 * @FilePath: /pdf2html/src/main/resources/md/TNS.md
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
# 1.Oracle-TNS
## 1.1 TNS简介
TNS:Transparent Network Substrate,即透明网络底层,就是上层的应用层不关心底层的网络层使用了什么协议
TNS是oracle服务端和客户端通讯的应用层协议,可以使用TCP/IP协议或者SSL的TCP/IP协议传输.<font color="red">注意:oracle的TNS有许多版本，注意兼容.</font>
在对oracletns协议进行分析中，在oracle11g或oracle12c下、使用的客户端有可能是32位或64位，而现有技术方案对tns分析不区分客户端位数和服务端位数，会造成在不同客户端和服务器的情况下无法正确获取用户名的情况
TNS有一个通用的头，包含一个请求数据的类型,不同的服务请求和数据传输使用不同的请求数据类型.
## 报文头
总共8个字节

|  名称   | 描述  |字节数  |
|  ----  | ----  |----  
| packet length  | 包长|2
| packet chksm  | 检测包 |2
| type  | 包类型 |1
| rscd  | 未使用 |1
#### 报文中type字段说明
![](WechatIMG61.png)
#### 类型变换流程如下
①客户端进行连接时，发起建立请求,此时type=1
②服务端接收后，向客户端发送接收请求，此事type=2;如果重新发送,type=11
③服务端拒绝请求,type=4;
④服务端接收请求后,客户端开始发送数据,type=6
## 数据包
|  名称   | 描述  |字节数  |
|  ----  | ----  |----  
| Data Flag  | 数据标识|2
| id  | 包id |2
| TTI  | TTIId |1
| data  | 有效数据 |可变

#2. SqlServer-TDS
# 2.1、jdbc连接源码分析
#### 2.1 SQLServerConnection.java
组装预登录包
![](pictures/WechatIMG45.png)
开启ssl分层协议，进行ssl安全通信
![](pictures/WechatIMG46.png)
#### 2.2 客户端通过ssl加密，发送用户名和密码到服务端完成登录

# 3、有jdbc源码推断服务端
3.1 原始通信如下
![](pictures/WechatIMG47.png)
3.2 通过代理后通信如下
![](pictures/WechatIMG48.png)
#### 交互流程程序设计
1、客户端第一次pre-login-request请求到代理，代理返回给客户端响应报文pre-response,伪代码如下
![](pictures/WechatIMG49.png)
2、预登录响应完成后，客户端会切换成ssl安全通信，开启startHandshake();相应的代理服务端也要开启ssl通道等待客户端连接并握手,伪代码如下
![](pictures/WechatIMG50.png)

3、若握手成功以后，代理服务端会接收到客户端发送的用户名和密码,然后代理服务端解析报文获取到用户名


# 4、jdbc连接代理服务器通信
![](pictures/WechatIMG51.png)
jdbc驱动报错如下图
![](pictures/WechatIMG52.png)
代理服务端报错如下图
![](pictures/WechatIMG53.png)

# 5、错误分析
代理服务器第一次是以普通的socket接受客户端的预登录，然后客户端经过ssl协议对用户名和密码加密，代理服务端也要ssl解密获取用户名。
从错误日志上看，应该是代理服务器不能支持ssl通信，无法完成客户端发送的握手过程，普通socket切换sslSocket失败了。
# 6、SuProxy + Nginx代理实现
Nginx+Lua或Openresty已经是网关代理的流行架构，比如Openresty，KONG，API6等，现有大多数网关均在http协议上工作，虽然Nginx已经支持了TCP、IP stream，但由于对除http协议外，其他协议解析和分析模块的缺乏，使得其应用条件非常有限，除基本的代理功能外，其他一些在http下可以实现的功能，均无法在其他协议，比如在SSH2、TDS（SQL server）、TNS（ORACLE）、LDAP等协议上实现、劫持、修改、响应、负载及会话管理等能力。而对上述能力的支持，均需要建立在对各种协议的详尽解析之上。SuProxy正是在这个条件下出现，目标是建立一个基于nginx+lua的四层的协议分析与代理库，把nginx的稳定性能引入到除http外更广泛的领域，并作为进一步审计，安全攻防，身份管理，协议分析的基础组件。同时Suproxy也设计为一个能灵活的可扩展架构，第三方可以自行扩展协议解析，进一步丰富开源协议解析领域的公共知识
————————————————
版权声明：本文为CSDN博主「yizhu2000」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/yizhu2000/article/details/109312762
![](pictures/WechatIMG64.png)
## 6.1使用
lua脚本配置响应端口号
![](pictures/WechatIMG68.png)
nginx.conf引用lua脚本
![](pictures/WechatIMG69.png)
这一部分是先配置数据库主机地址


# 7.协议解析的难点
目的：实现navicat客户端连接数据库堡垒机操作，遵循数据库的通信协议，模拟前后的交互流程，获取报文中存在的用户名
协议的版本---->组装服务端报文
现在所有开源的代理都只是简单转发服务
#### 7.1 思路
能不能表面是mysql通信，底层用各种数据库驱动包驱动关系型数据库

公司培训:为了帮助我更快的融入到公司；首先俞老师对我进行了大约3天的新人训，首先从公司文化上,让我了解了公司的企业文化、定位上;对公司的价值观;落实到我个人 脚踏实地完成产品的研发工作 持续优化产品功能 结合客户需要和产品要求 提高产品的用户体验 保持产品的可持续发展
公司介绍,让我整体上对公司有了清晰的认识和未来的发展方向，作为一个创业公司，让我看到了公司的未来规划
对公司有了安全感;行业发展上让我觉得公司更加有信息和激情去工作
安全意识，让我对网络安全有了更深的理解和对自己的高要求;能够帮助我提前规避一些风险，进而间接规避了损失，让我更加清楚的认识到网络安全的重要性
部门管理：每天的晨会沟通工作进度和问题 周会帮助了解做了什么；有团队问题也会很快直接跟领导反应得到及时的反馈
外部们的协同:接触的较多的还是产品,我觉产品对开发中的疑问也能给予肯定的回复 产品的设计背景和原因 能让开发更好的对产品有更深的了解 及时的沟通让产品不会因为理解变形造成不必要的返工 开发和测试是不可分割的团体 是保证产品正常上线的合作 及时沟通 加强合作
未来的规划：从公司和产品要求是 主要负责数据库堡垒机的开发，前期牵扯到网络通信和数据库通信协议的解析 负责调研的工作 努力争取把理论变成实现 嵌入到我们的tiops产品中 早日转正 长期计划：沉入技术 自身和公司发展需要 网络通信这快深入学习以及考证