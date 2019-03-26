## 介绍

针对awt中图片处理进行封装，这些封装包括：缩放、裁剪、转为黑白、加水印等操作。

## 方法介绍

### `scale` 缩放图片

提供两种重载方法：其中一个是按照长宽缩放，另一种是按照比例缩放。

```java
ImgUtil.scale(
    FileUtil.file("d:/face.jpg"), 
    FileUtil.file("d:/face_result.jpg"), 
    0.5f//缩放比例
);
```

### `cut` 剪裁图片

```java
ImgUtil.cut(
    FileUtil.file("d:/face.jpg"), 
    FileUtil.file("d:/face_result.jpg"), 
    new Rectangle(200, 200, 100, 100)//裁剪的矩形区域
);
```

### `slice` 按照行列剪裁切片（将图片分为20行和20列）

```java
ImgUtil.slice(FileUtil.file("e:/test2.png"), FileUtil.file("e:/dest/"), 10, 10);
```

### `convert` 图片类型转换，支持GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG等

```java
ImgUtil.convert(FileUtil.file("e:/test2.png"), FileUtil.file("e:/test2Convert.jpg"));
```

### `gray` 彩色转为黑白

```java
ImgUtil.gray(FileUtil.file("d:/logo.png"), FileUtil.file("d:/result.png"));
```

### `pressText` 添加文字水印

```java
ImgUtil.pressText(//
    FileUtil.file("e:/pic/face.jpg"), //
    FileUtil.file("e:/pic/test2_result.png"), //
    "版权所有", Color.WHITE, //文字
    new Font("黑体", Font.BOLD, 100), //字体
    0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
    0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
    0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
);
```

### `pressImage` 添加图片水印

```java
ImgUtil.pressImage(
    FileUtil.file("d:/picTest/1.jpg"), 
    FileUtil.file("d:/picTest/dest.jpg"), 
    ImgUtil.read(FileUtil.file("d:/picTest/1432613.jpg")), //水印图片
    0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
    0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
    0.1f
);
```

### `rotate` 旋转图片

```java
// 旋转180度
BufferedImage image = ImgUtil.rotate(ImageIO.read(FileUtil.file("e:/pic/366466.jpg")), 180);
ImgUtil.write(image, FileUtil.file("e:/pic/result.png"));
```

### `flip` 水平翻转图片

```java
ImgUtil.flip(FileUtil.file("d:/logo.png"), FileUtil.file("d:/result.png"));
```