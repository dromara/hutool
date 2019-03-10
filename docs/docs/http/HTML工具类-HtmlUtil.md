HTML工具类-HtmlUtil
===

## 由来
针对Http请求中返回的Http内容，Hutool使用此工具类来处理一些HTML页面相关的事情。

## 方法

- `HtmlUtil.restoreEscaped` 还原被转义的HTML特殊字符
- `HtmlUtil.encode` 转义文本中的HTML字符为安全的字符
- `HtmlUtil.cleanHtmlTag` 清除所有HTML标签
- `HtmlUtil.removeHtmlTag` 清除指定HTML标签和被标签包围的内容
- `HtmlUtil.unwrapHtmlTag` 清除指定HTML标签，不包括内容
- `HtmlUtil.removeHtmlAttr` 去除HTML标签中的属性
- `HtmlUtil.removeAllHtmlAttr` 去除指定标签的所有属性
- `HtmlUtil.filter` 过滤HTML文本，防止XSS攻击

