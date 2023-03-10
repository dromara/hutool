<p align="center">
	<a href="https://hutool.cn/"><img src="https://cdn.jsdelivr.net/gh/looly/hutool-site/images/logo.jpg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬A set of tools that keep Java sweet.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

## 📚Hutool-swing 模块介绍

`Hutool-swing`提供了swing桌面和图片多媒体相关封装，此模块大部分工具依赖于桌面环境，也要注意不同操作系统的差异。

-------------------------------------------------------------------------------

## 🛠️包含内容

### 剪贴板(clipboard)

提供了桌面环境下剪贴板的读取、写入和监听功能。

### 图片(img)

- `ImgUtil`              提供了图片工具（旋转、灰度、裁切、缩放、描边）等功能。
- `ImgMetaUtil`          封装`metadata-extractor`提供图片元信息读取。
- `FontUtil`             提供字体相关工具，如字体创建等
- `GraphicsUtil`         提供绘图相关封装，包括绘制图片、字符串等。
- `BackgroundRemoval`    提供背景色去除功能。
- `RenderingHintsBuilder`提供定义和管理键和关联值的集合构建器。
- `ImgWriter`            提供图片写出封装。
- `Img`                  提供图片操作封装。

### 图片验证码(captcha)

提供干扰验证码、GIF动态验证码、数字计算验证码等相关功能。

### 其它工具

- `DesktopUtil` 提供桌面相关工具，如打开文件、打开浏览器等操作
- `RobotUtil`   提供自动化相关工具，如鼠标移动、键盘敲击、截屏等功能
- `ScreenUtil`  提供屏幕相关工具，如截屏、获取分辨率等