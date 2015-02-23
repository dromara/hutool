hutool
======

一个类似于[jodd](http://jodd.org/)和[Apache commons lang](http://commons.apache.org/)的Java工具类。

### 简介
[Hutool](https://github.com/looly/hutool)是一个工具包，我日常写项目的一些积累，参考了一些[Apache Commons Lang](http://commons.apache.org/)和[JODD](http://jodd.org/)里的一些写法，不过大部分还是自己写的，希望你看了之后会有所启发或者能给你工作中带来帮助。说实话我现在写代码已经离不开自己这个工具包了，叫做Hutool也有“糊涂”之意，表示很多功能糊里糊涂就实现了。好吧，言归正传，说说里面一些好玩的方法（工具包中大部分是一些静态方法）。

### 设计哲学
[Hutool](https://github.com/looly/hutool)的设计思想是尽量减少重复的定义，让项目中的`util`这个package尽量少，总的来说有如下的几个思想：

1. 减少代码录入。
2. 常用功能组合起来，实现一个功能只用一个方法。
3. 简化Java API，原来需要几个类实现的功能我也只是用一个类甚至一个方法（想想为了个线程池我得new多少类……而且名字还不好记）
4. 对于null的处理我没有可以回避，而是采取“你给我null我也给你返回null”这种思想，尽量不在工具类里抛空指针异常（这思想稍猥琐啊……直接把包袱扔给调用者了，好吧，谁让你给我null了）。


### 文档请移步 [Hutool Wiki](https://github.com/looly/hutool/wiki) ###
