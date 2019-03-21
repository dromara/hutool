加密解密工具-SecureUtil
===

## 介绍
`SecureUtil`主要针对常用加密算法构建快捷方式，还有提供一些密钥生成的快捷工具方法。

## 方法介绍

### 对称加密

- `SecureUtil.aes`
- `SecureUtil.des`

### 摘要算法

- `SecureUtil.md5`
- `SecureUtil.sha1`
- `SecureUtil.hmac`
- `SecureUtil.hmacMd5`
- `SecureUtil.hmacSha1`

### 非对称加密

- `SecureUtil.rsa`
- `SecureUtil.dsa`

### UUID
- `SecureUtil.simpleUUID` 方法提供无“-”的UUID

### 密钥生成
- `SecureUtil.generateKey` 针对对称加密生成密钥
- `SecureUtil.generateKeyPair` 生成密钥对（用于非对称加密）
- `SecureUtil.generateSignature` 生成签名（用于非对称加密）

其它方法为针对特定加密方法的一些密钥生成和签名相关方法，详细请参阅API文档。

