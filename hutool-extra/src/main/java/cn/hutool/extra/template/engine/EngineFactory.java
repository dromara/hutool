package cn.hutool.extra.template.engine;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateException;
import cn.hutool.extra.template.engine.beetl.BeetlEngine;
import cn.hutool.extra.template.engine.enjoy.EnjoyEngine;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import cn.hutool.extra.template.engine.rythm.RythmEngine;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import cn.hutool.log.StaticLog;

/**
 * 简单模板工厂，用于根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
 * 
 * @author looly
 *
 */
public class EngineFactory {
	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
	 * 
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link Engine}
	 */
	public static Engine create(TemplateConfig config) {
		final Engine engine = doCreate(config);
		StaticLog.debug("Use [{}] Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
	 * 
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link Engine}
	 */
	private static Engine doCreate(TemplateConfig config) {
		try {
			return new BeetlEngine(config);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new FreemarkerEngine(config);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new VelocityEngine(config);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new RythmEngine(config);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new EnjoyEngine(config);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new ThymeleafEngine(config);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		throw new TemplateException("No template found ! Please add one of [Beetl,Freemarker,Velocity,Rythm] jar to your project !");
	}
}
