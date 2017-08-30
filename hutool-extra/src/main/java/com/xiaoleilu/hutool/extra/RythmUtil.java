package com.xiaoleilu.hutool.extra;

import java.util.Properties;

import org.rythmengine.Rythm;
import org.rythmengine.RythmEngine;

/**
 * Rythm模板引擎工具类<br>
 * 文档：http://rythmengine.org/doc/index
 * @author Looly
 *
 */
public class RythmUtil {
	
	/**
	 * 新建自定义的Rythm模板引擎
	 * @param userConfiguration 配置文件，可以使用Props类
	 * @return
	 */
	public static RythmEngine newEngine(Properties userConfiguration) {
		return new RythmEngine(userConfiguration);
	}
	
	/**
	 * 使用全局单例{@link RythmEngine}渲染模板
	 * @param template 模板内容
	 * @param args 变量值
	 * @return 渲染后的内容
	 */
	public static String render(String template, Object... args) {
		return Rythm.render(template, args);
	}
}
