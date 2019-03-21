## 介绍
在分布式环境中，唯一ID生成应用十分广泛，生成方法也多种多样，Hutool针对一些常用生成策略做了简单封装。

唯一ID生成器的工具类，涵盖了：

- UUID
- ObjectId（MongoDB）
- Snowflake（Twitter）

## 使用

### UUID

UUID全称通用唯一识别码（universally unique identifier），JDK通过`java.util.UUID`提供了 Leach-Salz 变体的封装。在Hutool中，生成一个UUID字符串方法如下：

```java
//生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
String uuid = IdUtil.randomUUID();

//生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
String simpleUUID = IdUtil.simpleUUID();
```

> 说明
> Hutool重写`java.util.UUID`的逻辑，对应类为`cn.hutool.core.lang.UUID`，使生成不带-的UUID字符串不再需要做字符替换，性能提升一倍左右。

### ObjectId

ObjectId是MongoDB数据库的一种唯一ID生成策略，是UUID version1的变种，详细介绍可见：[服务化框架－分布式Unique ID的生成方法一览](http://calvin1978.blogcn.com/articles/uuid.html)。

Hutool针对此封装了`cn.hutool.core.lang.ObjectId`，快捷创建方法为：

```java
//生成类似：5b9e306a4df4f8c54a39fb0c
String id = ObjectId.next();

//方法2：从Hutool-4.1.14开始提供
String id2 = IdUtil.objectId();
```

### Snowflake

分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。Twitter的Snowflake 算法就是这种生成器。

使用方法如下：

```java
//参数1为终端ID
//参数2为数据中心ID
Snowflake snowflake = IdUtil.createSnowflake(1, 1);
long id = snowflake.nextId();
```