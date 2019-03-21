模板引擎封装-TemplateUtil
===

## 介绍

随着前后分离的流行，JSP技术和模板引擎慢慢变得不再那么重要，但是早某些场景中（例如邮件模板、页面静态化等）依旧无可可替代，但是各种模板引擎语法大相径庭，使用方式也不尽相同，学习成本很高。Hutool旨在封装各个引擎的共性，使用户只关注模板语法即可，减少学习成本。

Hutool现在封装的引擎有：

- [Beetl](http://ibeetl.com/)
- [Enjoy](https://gitee.com/jfinal/enjoy)
- [Rythm](http://rythmengine.org/)
- [FreeMarker](https://freemarker.apache.org/)
- [Velocity](http://velocity.apache.org/)
- [Thymeleaf](https://www.thymeleaf.org/)

## 原理

类似于Java日志门面的思想，Hutool将模板引擎的渲染抽象为两个概念：

- TemplateEngine 模板引擎，用于封装模板对象，配置各种配置
- Template 模板对象，用于配合参数渲染产生内容

通过实现这两个接口，用户便可抛开模板实现，从而渲染模板。Hutool同时会通过`TemplateFactory`**根据用户引入的模板引擎库的jar来自动选择用哪个引擎来渲染**。

## 使用

### 从字符串模板渲染内容
```
//自动根据用户引入的模板引擎库的jar来自动选择使用的引擎
//TemplateConfig为模板引擎的选项，可选内容有字符编码、模板路径、模板加载方式等，默认通过模板字符串渲染
TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());

//假设我们引入的是Beetl引擎，则：
Template template = engine.getTemplate("Hello ${name}");
//Dict本质上为Map，此处可用Map
String result = template.render(Dict.create().set("name", "Hutool"));
//输出：Hello Hutool
```

也就是说，使用Hutool之后，无论你用任何一种模板引擎，代码不变（只变更模板内容）。

### 从classpath查找模板渲染

只需修改TemplateConfig配置文件内容即可更换（这里以Velocity为例）：

```
TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
Template template = engine.getTemplate("templates/velocity_test.vtl");
String result = template.render(Dict.create().set("name", "Hutool"));
```

### 其它方式查找模板

查找模板的方式由ResourceMode定义，包括：

- CLASSPATH 从ClassPath加载模板
- FILE 从File本地目录加载模板
- WEB_ROOT 从WebRoot目录加载模板
- STRING 从模板文本加载模板
- COMPOSITE 复合加载模板（分别从File、ClassPath、Web-root、String方式尝试查找模板）