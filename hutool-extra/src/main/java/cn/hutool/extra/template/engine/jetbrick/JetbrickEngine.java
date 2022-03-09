package cn.hutool.extra.template.engine.jetbrick;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import jetbrick.template.JetEngine;

import java.util.Properties;

/**
 * Jetbrick模板引擎封装<br>
 * 见：https://github.com/subchen/jetbrick-template-2x
 *
 * @author looly
 * @since 5.7.21
 */
public class JetbrickEngine implements TemplateEngine {

	private JetEngine engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public JetbrickEngine() {}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public JetbrickEngine(TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link JetEngine}
	 */
	public JetbrickEngine(JetEngine engine) {
		init(engine);
	}
	// --------------------------------------------------------------------------------- Constructor end


	@Override
	public TemplateEngine init(TemplateConfig config) {
		init(createEngine(config));
		return this;
	}

	/**
	 * 初始化引擎
	 * @param engine 引擎
	 */
	private void init(JetEngine engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		return JetbrickTemplate.wrap(engine.getTemplate(resource));
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link JetEngine}
	 */
	private static JetEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = TemplateConfig.DEFAULT;
		}

		Properties props = new Properties();
		props.setProperty("jetx.input.encoding", config.getCharsetStr());
		props.setProperty("jetx.output.encoding", config.getCharsetStr());
		props.setProperty("jetx.template.loaders", "$loader");

		switch (config.getResourceMode()){
			case CLASSPATH:
				props.setProperty("$loader", "jetbrick.template.loader.ClasspathResourceLoader");
				props.setProperty("$loader.root", config.getPath());
				break;
			case FILE:
				props.setProperty("$loader", "jetbrick.template.loader.FileSystemResourceLoader");
				props.setProperty("$loader.root", config.getPath());
				break;
			case WEB_ROOT:
				props.setProperty("$loader", "jetbrick.template.loader.ServletResourceLoader");
				props.setProperty("$loader.root", config.getPath());
				break;
			case STRING:
				props.setProperty("$loader", "cn.hutool.extra.template.engine.jetbrick.loader.StringResourceLoader");
				props.setProperty("$loader.charset", config.getCharsetStr());
				break;
			default:
				// 默认
				return JetEngine.create();
		}

		return JetEngine.create(props);
	}
}
