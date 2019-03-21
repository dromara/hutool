签名和验证-Sign
===

## 介绍
Hutool针对`java.security.Signature`做了简化包装，包装类为：`Sign`，用于生成签名和签名验证。

对于签名算法，Hutool封装了JDK的，具体介绍见：[https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature)：

```
// The RSA signature algorithm
NONEwithRSA

// The MD2/MD5 with RSA Encryption signature algorithm
MD2withRSA
MD5withRSA

// The signature algorithm with SHA-* and the RSA
SHA1withRSA
SHA256withRSA
SHA384withRSA
SHA512withRSA

// The Digital Signature Algorithm
NONEwithDSA

// The DSA with SHA-1 signature algorithm
SHA1withDSA

// The ECDSA signature algorithms
NONEwithECDSA
SHA1withECDSA
SHA256withECDSA
SHA384withECDSA
SHA512withECDSA
```

## 使用

```java
byte[] data = "我是一段测试字符串".getBytes();
Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
//签名
byte[] signed = sign.sign(data);
//验证签名
boolean verify = sign.verify(data, signed);
```

