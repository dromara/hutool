## 由来
在日常开发中，我们对身份证的验证主要是正则方式（位数，数字范围等），但是中国身份证，尤其18位身份证每一位都有严格规定，并且最后一位为校验位。而我们在实际应用中，针对身份证的验证理应严格至此。于是`IdcardUtil`应运而生。

> `IdcardUtil`从3.0.4版本起加入Hutool工具家族，请升级至此版本以上可使用。

## 介绍
`IdcardUtil`现在支持大陆15位、18位身份证，港澳台10位身份证。

工具中主要的方法包括：
1. `isValidCard` 验证身份证是否合法
2. `convert15To18` 身份证15位转18位
3. `getBirthByIdCard` 获取生日
4. `getAgeByIdCard` 获取年龄
5. `getYearByIdCard` 获取生日年
6. `getMonthByIdCard` 获取生日月
7. `getDayByIdCard` 获取生日天
8. `getGenderByIdCard` 获取性别
9. `getProvinceByIdCard` 获取省份

## 使用

```java
String ID_18 = "321083197812162119";
String ID_15 = "150102880730303";

//是否有效
boolean valid = IdcardUtil.isValidCard(ID_18);
boolean valid15 = IdcardUtil.isValidCard(ID_15);

//转换
String convert15To18 = IdcardUtil.convert15To18(ID_15);
Assert.assertEquals(convert15To18, "150102198807303035");

//年龄
DateTime date = DateUtil.parse("2017-04-10");
		
int age = IdcardUtil.getAgeByIdCard(ID_18, date);
Assert.assertEquals(age, 38);

int age2 = IdcardUtil.getAgeByIdCard(ID_15, date);
Assert.assertEquals(age2, 28);

//生日
String birth = IdcardUtil.getBirthByIdCard(ID_18);
Assert.assertEquals(birth, "19781216");

String birth2 = IdcardUtil.getBirthByIdCard(ID_15);
Assert.assertEquals(birth2, "19880730");

//省份
String province = IdcardUtil.getProvinceByIdCard(ID_18);
Assert.assertEquals(province, "江苏");

String province2 = IdcardUtil.getProvinceByIdCard(ID_15);
Assert.assertEquals(province2, "内蒙古");
```

> **声明**
> 以上两个身份证号码为随机编造的，如有雷同，纯属巧合。