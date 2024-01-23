# lagouselftest
高薪提升自测项目

### TCP/IP 四层协议

数据链路层 -> 网络层 -> 传输层 -> 应用层

### OSI 国际化网络标准 网络通信七层协议

数据链路层 -> 网络层 -> 传输层 -> 会话层 -> 表示层 -> 应用层

### tomcat 源码配置启动项
启动类路径:
org.apache.catalina.startup.Bootstrap
vm参数
-Dcatalina.home=D:/workspace/lagouselftest/tomcat-source-code/source
-Dcatalina.base=D:/workspace/lagouselftest/tomcat-source-code/source
-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager
-Djava.util.logging.config.file=D:/workspace/lagouselftest/tomcat-source-code/source/conf/logging.properties
-Dfile.encoding=UTF-8
-Dsun.jnu.encoding=UTF-8

控制台部分日志乱码情况 修改以下内容会部分乱码可恢复 但是系统前缀会乱码
java.util.logging.ConsoleHandler.encoding = ISO-8859-1