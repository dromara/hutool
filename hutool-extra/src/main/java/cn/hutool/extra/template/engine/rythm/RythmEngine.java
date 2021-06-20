package cn.hutool.extra.template.engine.rythm;

import java.util.Properties;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;

/**
 * Rythm模板引擎<br>
 * 文档：http://rythmengine.org/doc/index
 * 
 * @author looly
 *
 */
public class RythmEngine implements TemplateEngine {

	org.rythmengine.RythmEngine engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public RythmEngine() {}

	/**
	 * 构造
	 * 
	 * @param config 模板配置
	 */
	public RythmEngine(TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 * 
	 * @param engine {@link org.rythmengine.RythmEngine}
	 */
	public RythmEngine(org.rythmengine.RythmEngine engine) {
		init(engine);
	}
	// --------------------------------------------------------------------------------- Constructor end

	@Override
	public TemplateEngine init(TemplateConfig config) {
		if(null == config){
			config = TemplateConfig.DEFAULT;
		}
		init(createEngine(config));
		return this;
	}

	/**
	 * 初始化引擎
	 * @param engine 引擎
	 */
	private void init(org.rythmengine.RythmEngine engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		return RythmTemplate.wrap(this.engine.getTemplate(resource));
	}

	/**
	 * 创建引擎
	 * 
	 * @param config 模板配置
	 * @return {@link org.rythmengine.RythmEngine}
	 */
	private static org.rythmengine.RythmEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = new TemplateConfig();
		}
		
		final Properties props = new Properties();
		final String path = config.getPath();
		if (null != path) {
			props.put("home.template", path);
		}

		return new org.rythmengine.RythmEngine(props);
	}
}
