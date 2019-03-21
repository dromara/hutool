## 说明
`RandomUtil`主要针对JDK中`Random`对象做封装，严格来说，Java产生的随机数都是伪随机数，因此Hutool封装后产生的随机结果也是伪随机结果。不过这种随机结果对于大多数情况已经够用。

## 使用

- `RandomUtil.randomInt` 获得指定范围内的随机数
- `RandomUtil.randomBytes` 随机bytes
- `RandomUtil.randomEle` 随机获得列表中的元素
- `RandomUtil.randomEleSet` 随机获得列表中的一定量的不重复元素，返回Set
- `RandomUtil.randomString` 获得一个随机的字符串（只包含数字和字符）
- `RandomUtil.randomNumbers` 获得一个只包含数字的字符串
- `RandomUtil.randomUUID` 随机UUID
- `RandomUtil.weightRandom` 权重随机生成器，传入带权重的对象，然后根据权重随机获取对象
