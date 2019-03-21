## 介绍

针对awt中图片处理进行封装，这些封装包括：缩放、裁剪、转为黑白、加水印等操作。

## 方法介绍

1. `scale` 缩放图片，提供两种重载方法：其中一个是按照长宽缩放，另一种是按照比例缩放。
2.  `cut` 剪裁图片
3. `cutByRowsAndCols` 按照行列剪裁（将图片分为20行和20列）
4. `convert` 图片类型转换，支持GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG等
5. `gray` 彩色转为黑白
6. `pressText` 添加文字水印
7. `pressImage` 添加图片水印
8. `rotate` 旋转图片
9. `flip` 水平翻转图片