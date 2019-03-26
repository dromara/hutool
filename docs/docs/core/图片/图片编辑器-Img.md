## 介绍

针对awt中图片处理进行封装，这些封装包括：缩放、裁剪、转为黑白、加水印等操作。

## 方法介绍

### 图像切割

```java
// 将face.jpg切割为原型保存为face_radis.png
Img.from(FileUtil.file("e:/pic/face.jpg"))
    .cut(0, 0, 200)//
    .write(FileUtil.file("e:/pic/face_radis.png"));
```

### 图片压缩

图片压缩只支持Jpg文件。

```java
Img.from(FileUtil.file("e:/pic/1111.png"))
    .setQuality(0.8)//压缩比率
    .write(FileUtil.file("e:/pic/1111_target.jpg"));
```