SFTP封装-Sftp
===

## 介绍

SFTP是Secure File Transfer Protocol的缩写，安全文件传送协议。可以为传输文件提供一种安全的加密方法。

SFTP 为 SSH的一部份，是一种传输文件到服务器的安全方式。SFTP是使用加密传输认证信息和传输的数据，所以，使用SFTP是非常安全的。

但是，由于这种传输方式使用了加密/解密技术，所以传输效率比普通的FTP要低得多，如果您对网络安全性要求更高时，可以使用SFTP代替FTP。

## 使用

### 引入jsch

```xml
<dependency>
	<groupId>com.jcraft</groupId>
	<artifactId>jsch</artifactId>
	<version>0.1.54</version>
</dependency>
```

### 使用

```
Sftp sftp= JschUtil.createSftp("172.0.0.1", 22, "root", "123456");
//进入远程目录
sftp.cd("/opt/upload");
//上传本地文件
sftp.put("e:/test.jpg", "/opt/upload");
//下载远程文件
sftp.get("/opt/upload/test.jpg", "e:/test2.jpg");

//关闭连接
sftp.close();
```

