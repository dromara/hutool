package cn.hutool.extra.template.engine.velocity;

import org.apache.velocity.app.Velocity;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;

/**
 * Velocity模板引擎
 * 
 * @author looly
 *
 */
public class VelocityEngine implements TemplateEngine {

	private org.apache.velocity.app.VelocityEngine engine;

	// --------------------------------------------------------------------------------- Constructor start
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
	// --------------------------------------------------------------------------------- Constructor end

	/**
	 * 获取原始的引擎对象
	 * 
	 * @return 原始引擎对象
	 * @since 4.3.0
	 */
	public org.apache.velocity.app.VelocityEngine getRowEngine() {
		return this.engine;
	}

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
		// 编码
		final String charsetStr = config.getCharset().toString();
		ve.setProperty(Velocity.INPUT_ENCODING, charsetStr);
		ve.setProperty(Velocity.OUTPUT_ENCODING, charsetStr);
		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true); // 使用缓存

		// loader
		switch (config.getResourceMode()) {
		case CLASSPATH:
			ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			break;
		case FILE:
			// path
			final String path = config.getPath();
			if (null != path) {
				ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
			}
			break;
		case WEB_ROOT:
			ve.setProperty(Velocity.RESOURCE_LOADER, "webapp");
			ve.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.servlet.WebappLoader");
			ve.setProperty("webapp.resource.loader.path", StrUtil.nullToDefault(config.getPath(), StrUtil.SLASH));
			break;
		case STRING:
			ve.setProperty(Velocity.RESOURCE_LOADER, "str");
			 ve.setProperty("str.resource.loader.class", SimpleStringResourceLoader.class.getName());
			break;
		default:
			break;
		}

		ve.init();
		return ve;
	}
}
