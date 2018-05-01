基于spring boot 城市五级联动数据查询.<br>
使用了spring 缓存注解<br>
默认请求数据路径为 {project}/ws/cascader/address/getData/{code}<br>
code 为null 查询全国省份数据<br>
示例 {project}/ws/cascader/address/getData/43<br>
查询湖南省下的所有地址市<br>
数据库<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据库文件为mysql 数据库文件 数据更新时间  2018-04-04

maven 使用 方式

<pre>
<dependency>
    <groupId>com.exec8</groupId>
    <artifactId>com-bh-cascader-address</artifactId>
    <version>1.0.0</version>
</dependency>
</pre>
