基于spring boot 城市五级联动数据查询.<br>
使用了spring 缓存注解<br>
默认请求数据路径为 <br>
1 {project}/ws/cascader/address/getData/{code}<br>
   code 为null 查询全国省份数据<br>
示例 {project}/ws/cascader/address/getData/43<br>
查询湖南省下的所有地址市<br>
2 增加接口 查询不需要下一级节点接口,为了适应element ui 中的级联组件<br>
示例 {project}/ws/cascader/address/getDataNoNext/43<br>
3 增加查询所有数据信息 根据type返回对应所有的值 <br>
type 1 查询所有省份  2查询所有省份+城市  3 查询所有省份+城市+区县 4 查询所有省份+城市+区县+街道<br>
示例 {project}/ws/cascader/address/getAll/1<br> 查询所有省份，<br>
4根据code查询对应值  <br>
注入对应的service 使用findVOByCode方法  

数据库<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据库文件为mysql 数据库文件 数据更新时间  2018-04-04

maven 使用方式

```
级联选择
<dependency>
    <groupId>com.exec8</groupId>
    <artifactId>com-bh-cascader-address</artifactId>
    <version>1.8</version>
</dependency>
工具类
<dependency>
    <groupId>com.exec8</groupId>
    <artifactId>com.bh.core.tools</artifactId>
    <version>1.8</version>
</dependency>

阿里云oss spring boot starter包
<dependency>
    <groupId>com.exec8</groupId>
    <artifactId>ali-oss-spring-boot-starter</artifactId>
    <version>1.8</version>
</dependency>

```
