国密算法工具-SmUtil
===

## 介绍
Hutool针对`Bouncy Castle`做了简化包装，用于实现国密算法中的SM2、SM3、SM4。

国密算法工具封装包括：

- 非对称加密和签名：SM2
- 摘要签名算法：SM3
- 对称加密：SM4

国密算法需要引入`Bouncy Castle`库的依赖。

## 使用

### 引入`Bouncy Castle`依赖

```xml
<dependency>
	<groupId>org.bouncycastle</groupId>
	<artifactId>bcprov-jdk15on</artifactId>
	<version>${bouncycastle.version}</version>
</dependency>
```

> 说明
> `bcprov-jdk15on`的版本请前往Maven中央库搜索，查找对应JDK的版本。

### 非对称加密SM2

1. 使用随机生成的密钥对加密或解密

```java
String text = "我是一段测试aaaa";

SM2 sm2 = SmUtil.sm2();
// 公钥加密，私钥解密
String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
```

2. 使用自定义密钥对加密或解密

```java
String text = "我是一段测试aaaa";

KeyPair pair = SecureUtil.generateKeyPair("SM2");
byte[] privateKey = pair.getPrivate().getEncoded();
byte[] publicKey = pair.getPublic().getEncoded();

SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
// 公钥加密，私钥解密
String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
```

### 摘要加密算法SM3

```java
//结果为：136ce3c86e4ed909b76082055a61586af20b4dab674732ebd4b599eef080c9be
String digestHex = SmUtil.sm3("aaaaa");
```

### 对称加密SM4

```java
String content = "test中文";
SymmetricCrypto sm4 = SmUtil.sm4();

String encryptHex = sm4.encryptHex(content);
String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
```