非对称加密-AsymmetricCrypto
===

## 介绍
对于非对称加密，最常用的就是RSA和DSA，在Hutool中使用`AsymmetricCrypto`对象来负责加密解密。

非对称加密有公钥和私钥两个概念，私钥自己拥有，不能给别人，公钥公开。根据应用的不同，我们可以选择使用不同的密钥加密：

1. 签名：使用私钥加密，公钥解密。用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。

2. 加密：用公钥加密，私钥解密。用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。

Hutool封装了JDK的，详细见[https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator)：

- RSA
- DSA
- EC

## 使用

在非对称加密中，我们可以通过`AsymmetricCrypto(AsymmetricAlgorithm algorithm)`构造方法，通过传入不同的算法枚举，获得其加密解密器。

当然，为了方便，我们针对最常用的RSA和DSA算法构建了单独的对象：`RSA`和`DSA`。

### 基本使用

我们以RSA为例，介绍使用RSA加密和解密 在构建RSA对象时，可以传入公钥或私钥，当使用无参构造方法时，Hutool将自动生成随机的公钥私钥密钥对：

```java
RSA rsa = new RSA();

//获得私钥
rsa.getPrivateKey()
rsa.getPrivateKeyBase64()
//获得公钥
rsa.getPublicKey()
rsa.getPublicKeyBase64()

//公钥加密，私钥解密
byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);

//Junit单元测试
//Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

//私钥加密，公钥解密
byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);

//Junit单元测试
//Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
```

> 对于加密和解密可以完全分开，对于RSA对象，如果只使用公钥或私钥，另一个参数可以为`null`

### 自助生成密钥对
有时候我们想自助生成密钥对可以：

```java
KeyPair pair = SecureUtil.generateKeyPair("RSA");
pair.getPrivate();
pair.getPublic();
```

自助生成的密钥对是byte[]形式，我们可以使用`Base64.encode`方法转为Base64，便于存储为文本。

当然，如果使用`RSA`对象，也可以使用`encryptStr`和`decryptStr`加密解密为字符串。

## 案例

### 案例一：
已知私钥和密文，如何解密密文？

```java
String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIL7pbQ+5KKGYRhw7jE31hmA"
		+ "f8Q60ybd+xZuRmuO5kOFBRqXGxKTQ9TfQI+aMW+0lw/kibKzaD/EKV91107xE384qOy6IcuBfaR5lv39OcoqNZ"
		+ "5l+Dah5ABGnVkBP9fKOFhPgghBknTRo0/rZFGI6Q1UHXb+4atP++LNFlDymJcPAgMBAAECgYBammGb1alndta"
		+ "xBmTtLLdveoBmp14p04D8mhkiC33iFKBcLUvvxGg2Vpuc+cbagyu/NZG+R/WDrlgEDUp6861M5BeFN0L9O4hz"
		+ "GAEn8xyTE96f8sh4VlRmBOvVdwZqRO+ilkOM96+KL88A9RKdp8V2tna7TM6oI3LHDyf/JBoXaQJBAMcVN7fKlYP"
		+ "Skzfh/yZzW2fmC0ZNg/qaW8Oa/wfDxlWjgnS0p/EKWZ8BxjR/d199L3i/KMaGdfpaWbYZLvYENqUCQQCobjsuCW"
		+ "nlZhcWajjzpsSuy8/bICVEpUax1fUZ58Mq69CQXfaZemD9Ar4omzuEAAs2/uee3kt3AvCBaeq05NyjAkBme8SwB0iK"
		+ "kLcaeGuJlq7CQIkjSrobIqUEf+CzVZPe+AorG+isS+Cw2w/2bHu+G0p5xSYvdH59P0+ZT0N+f9LFAkA6v3Ae56OrI"
		+ "wfMhrJksfeKbIaMjNLS9b8JynIaXg9iCiyOHmgkMl5gAbPoH/ULXqSKwzBw5mJ2GW1gBlyaSfV3AkA/RJC+adIjsRGg"
		+ "JOkiRjSmPpGv3FOhl9fsBPjupZBEIuoMWOC8GXK/73DHxwmfNmN7C9+sIi4RBcjEeQ5F5FHZ";

RSA rsa = new RSA(PRIVATE_KEY, null);

String a = "2707F9FD4288CEF302C972058712F24A5F3EC62C5A14AD2FC59DAB93503AA0FA17113A020EE4EA35EB53F"
		+ "75F36564BA1DABAA20F3B90FD39315C30E68FE8A1803B36C29029B23EB612C06ACF3A34BE815074F5EB5AA3A"
		+ "C0C8832EC42DA725B4E1C38EF4EA1B85904F8B10B2D62EA782B813229F9090E6F7394E42E6F44494BB8";

byte[] aByte = HexUtil.decodeHex(a);
byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);

//Junit单元测试
//Assert.assertEquals("虎头闯杭州,多抬头看天,切勿只管种地", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
```

