## 由来

数字工具针对数学运算做工具性封装

## 使用

### 加减乘除

- `NumberUtil.add`  针对double类型做加法
- `NumberUtil.sub`  针对double类型做减法
- `NumberUtil.mul`  针对double类型做乘法
- `NumberUtil.div`  针对double类型做除法，并提供重载方法用于规定除不尽的情况下保留小数位数和舍弃方式。

以上四种运算都会将double转为BigDecimal后计算，解决float和double类型无法进行精确计算的问题。这些方法常用于商业计算。

### 保留小数

保留小数的方法主要有两种：

- `NumberUtil.round` 方法主要封装BigDecimal中的方法来保留小数，返回double，这个方法更加灵活，可以选择四舍五入或者全部舍弃等模式。

```java
double te1=123456.123456;
double te2=123456.128456;
Console.log(round(te1,4));//结果:123456.12
Console.log(round(te2,4));//结果:123456.13
```

- `NumberUtil.roundStr` 方法主要封装`String.format`方法,舍弃方式采用四舍五入。

```java
double te1=123456.123456;
double te2=123456.128456;
Console.log(roundStr(te1,2));//结果:123456.12
Console.log(roundStr(te2,2));//结果:123456.13
```

### decimalFormat

针对 `DecimalFormat.format`进行简单封装。按照固定格式对double或long类型的数字做格式化操作。

```java
long c=299792458;//光速
String format = NumberUtil.decimalFormat(",###", c);//299,792,458
```

格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。

- 0 -> 取一位整数
- 0.00 -> 取一位整数和两位小数
- 00.000 -> 取两位整数和三位小数
- \# -> 取所有整数部分
- \#.##% -> 以百分比方式计数，并取两位小数
- \#.#####E0 -> 显示为科学计数法，并取五位小数
- ,### -> 每三位以逗号进行分隔，例如：299,792,458
- 光速大小为每秒,###米 -> 将格式嵌入文本

关于格式的更多说明，请参阅：[Java DecimalFormat的主要功能及使用方法](http://blog.csdn.net/evangel_z/article/details/7624503)

### 是否为数字
- `NumberUtil.isNumber` 是否为数字
- `NumberUtil.isInteger` 是否为整数
- `NumberUtil.isDouble` 是否为浮点数
- `NumberUtil.isPrimes` 是否为质数

### 随机数
- `NumberUtil.generateRandomNumber` 生成不重复随机数 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组。
- `NumberUtil.generateBySet` 生成不重复随机数 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组。

### 整数列表

`NumberUtil.range` 方法根据范围和步进，生成一个有序整数列表。
`NumberUtil.appendRange` 将给定范围内的整数添加到已有集合中

### 其它
- `NumberUtil.factorial` 阶乘
- `NumberUtil.sqrt` 平方根
- `NumberUtil.divisor` 最大公约数
- `NumberUtil.multiple` 最小公倍数
- `NumberUtil.getBinaryStr` 获得数字对应的二进制字符串
- `NumberUtil.binaryToInt` 二进制转int
- `NumberUtil.binaryToLong` 二进制转long
- `NumberUtil.compare` 比较两个值的大小
- `NumberUtil.toStr` 数字转字符串，自动并去除尾小数点儿后多余的0