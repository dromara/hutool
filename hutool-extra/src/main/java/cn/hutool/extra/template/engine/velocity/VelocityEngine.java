package cn.hutool.extra.template.engine.velocity;

import org.apache.velocity.app.Velocity;

import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;

/**
 * Velocity模板引擎
 * 
 * @author looly
 *
 */
public class VelocityEngine implements Engine {

	org.apache.velocity.app.VelocityEngine engine;

	//--------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public VelocityEngine() {
		this(new TemplateConfig());
	}

	/**
	 * 构造
	 * 
	 * @param config 模板配置
	 */
	public VelocityEngine(TemplateConfig config) {
		this(createEngine(config));
	}

	/**
	 * 构造
	 * 
	 * @param engine {@link org.apache.velocity.app.VelocityEngine}
	 */
	public VelocityEngine(org.apache.velocity.app.VelocityEngine engine) {
		this.engine = engine;
	}
	//--------------------------------------------------------------------------------- Constructor end

	@Override
	public Template getTemplate(String resource) {
		return VelocityTemplate.wrap(engine.getTemplate(resource));
	}

	/**
	 * 创建引擎
	 * 
	 * @param config 模板配置
	 * @return {@link org.apache.velocity.app.VelocityEngine}
	 */
	private static org.apache.velocity.app.VelocityEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = new TemplateConfig();
		}

		final org.apache.velocity.app.VelocityEngine ve = new org.apache.velocity.app.VelocityEngine();
		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true); // 使用缓存

		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, config.getPath());

		final String charsetStr = config.getCharset().toString();
		ve.setProperty(Velocity.INPUT_ENCODING, charsetStr);
		ve.setProperty(Velocity.OUTPUT_ENCODING, charsetStr);

		return ve;
	}
}
