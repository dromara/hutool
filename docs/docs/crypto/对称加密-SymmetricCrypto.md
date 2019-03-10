对称加密-SymmetricCrypto
===

## 介绍

对称加密(也叫私钥加密)指加密和解密使用相同密钥的加密算法。有时又叫传统密码算法，就是加密密钥能够从解密密钥中推算出来，同时解密密钥也可以从加密密钥中推算出来。而在大多数的对称算法中，加密密钥和解密密钥是相同的，所以也称这种加密算法为秘密密钥算法或单密钥算法。它要求发送方和接收方在安全通信之前，商定一个密钥。对称算法的安全性依赖于密钥，泄漏密钥就意味着任何人都可以对他们发送或接收的消息解密，所以密钥的保密性对通信的安全性至关重要。

对于对称加密，封装了JDK的，具体介绍见：[https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator)：

- AES (默认`AES/ECB/PKCS5Padding`)
- ARCFOUR
- Blowfish
- DES (默认`DES/ECB/PKCS5Padding`)
- DESede
- RC2
- PBEWithMD5AndDES
- PBEWithSHA1AndDESede
- PBEWithSHA1AndRC2_40

## 使用

### 通用使用
以AES算法为例：

```java
String content = "test中文";

//随机生成密钥
byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

//构建
SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

//加密
byte[] encrypt = aes.encrypt(content);
//解密
byte[] decrypt = aes.decrypt(encrypt);

//加密为16进制表示
String encryptHex = aes.encryptHex(content);
//解密为字符串
String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
```

### DESede实现

```java
String content = "test中文";

byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();

SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);

//加密
byte[] encrypt = des.encrypt(content);
//解密
byte[] decrypt = des.decrypt(encrypt);

//加密为16进制字符串（Hex表示）
String encryptHex = des.encryptHex(content);
//解密为字符串
String decryptStr = des.decryptStr(encryptHex);
```

### AES封装

AES全称高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法。

对于Java中AES的默认模式是：`AES/ECB/PKCS5Padding`，如果使用CryptoJS，请调整为：padding: CryptoJS.pad.Pkcs7

1. 快速构建

```java
String content = "test中文";

// 随机生成密钥
byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

// 构建
AES aes = SecureUtil.aes(key);

// 加密
byte[] encrypt = aes.encrypt(content);
// 解密
byte[] decrypt = aes.decrypt(encrypt);

// 加密为16进制表示
String encryptHex = aes.encryptHex(content);
// 解密为字符串
String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
```

2. 自定义模式和偏移

```java
AES aes = new AES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "0102030405060708".getBytes());
```

### DES封装

DES全称为Data Encryption Standard，即数据加密标准，是一种使用密钥加密的块算法，Java中默认实现为：`DES/CBC/PKCS5Padding`

DES使用方法与AES一致，构建方法为：

1. 快速构建

```java
byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
DES des = SecureUtil.des(key);
```

2. 自定义模式和偏移

```java
DES des = new DES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "01020304".getBytes());
```

### SM4

在4.2.1之后，Hutool借助Bouncy Castle库可以支持国密算法，以SM4为例：

我们首先需要引入Bouncy Castle库：

```xml
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bcpkix-jdk15on</artifactId>
  <version>1.60</version>
</dependency>
```

然后可以调用SM4算法，调用方法与其它算法一致：

```java
String content = "test中文";
SymmetricCrypto sm4 = new SymmetricCrypto("SM4");

String encryptHex = sm4.encryptHex(content);
String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);//test中文
```

同样我们可以指定加密模式和偏移：

```java
String content = "test中文";
SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding");

String encryptHex = sm4.encryptHex(content);
String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);//test中文
```