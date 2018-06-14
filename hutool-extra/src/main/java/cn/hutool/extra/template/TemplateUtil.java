package cn.hutool.extra.template;

import cn.hutool.extra.template.engine.beetl.BeetlEngine;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import cn.hutool.extra.template.engine.rythm.RythmEngine;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import cn.hutool.log.StaticLog;

/**
 * 模板工具类
 * 
 * @author looly
 * @since 4.1.0
 */
public class TemplateUtil {

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
	 * 
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link Engine}
	 */
	public static Engine createEngine(TemplateConfig config) {
		Engine engine = null;
		try {
			engine = new BeetlEngine(config);
			StaticLog.debug("{} Engine Created.", "Beetl");
		} catch (NoClassDefFoundError e) {
			try {
				engine = new FreemarkerEngine(config);
				StaticLog.debug("{} Engine Created.", "Freemarker");
			} catch (NoClassDefFoundError e2) {
				try {
					engine = new VelocityEngine(config);
					StaticLog.debug("{} Engine Created.", "Velocity");
				} catch (NoClassDefFoundError e3) {
					try {
						engine = new RythmEngine(config);
						StaticLog.debug("{} Engine Created.", "Rythm");
					} catch (NoClassDefFoundError e4) {
						throw new TemplateException("No template found ! Please add one of [Beetl,Freemarker,Velocity,Rythm] jar to your project !");
					}
				}
			}
		}

		return engine;
	}
}
