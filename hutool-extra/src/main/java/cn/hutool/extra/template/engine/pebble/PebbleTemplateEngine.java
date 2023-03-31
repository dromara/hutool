package cn.hutool.extra.template.engine.pebble;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateException;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.loader.ClasspathLoader;
import io.pebbletemplates.pebble.loader.FileLoader;
import io.pebbletemplates.pebble.loader.StringLoader;
import org.beetl.core.GroupTemplate;

/**
 * @author：zooooooooy
 */
public class PebbleTemplateEngine implements TemplateEngine {

	private PebbleEngine engine;

	public PebbleTemplateEngine() {
	}

	public PebbleTemplateEngine(TemplateConfig config) {
		init(config);
	}

	@Override
	public TemplateEngine init(TemplateConfig config) {
		init(createEngine(config));
		return this;
	}

	/**
	 * 初始化引擎
	 * @param engine 引擎
	 */
	private void init(PebbleEngine engine){
		this.engine = engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link GroupTemplate}
	 */
	private static PebbleEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = TemplateConfig.DEFAULT;
		}

		PebbleEngine pebbleEngine;
		switch (config.getResourceMode()) {
			case CLASSPATH:
				ClasspathLoader classpathLoader = new ClasspathLoader();
				classpathLoader.setPrefix(StrUtil.addSuffixIfNot(config.getPath(), "/"));
				pebbleEngine = new PebbleEngine.Builder()
						.loader(classpathLoader)
						.autoEscaping(false)
						.build();
				break;
			case FILE:
				FileLoader fileLoader = new FileLoader();
				fileLoader.setPrefix(StrUtil.addSuffixIfNot(config.getPath(), "/"));
				pebbleEngine = new PebbleEngine.Builder()
						.loader(fileLoader)
						.autoEscaping(false)
						.build();
				break;
			case WEB_ROOT:
				fileLoader = new FileLoader();
				fileLoader.setPrefix(StrUtil.addSuffixIfNot(FileUtil.getAbsolutePath(FileUtil.file(FileUtil.getWebRoot(), config.getPath())), "/"));
				pebbleEngine = new PebbleEngine.Builder()
						.loader(fileLoader)
						.autoEscaping(false)
						.build();
				break;
			case STRING:
				StringLoader stringLoader = new StringLoader();
				stringLoader.setPrefix(StrUtil.addSuffixIfNot(config.getPath(), "/"));
				pebbleEngine = new PebbleEngine.Builder()
						.loader(stringLoader)
						.autoEscaping(false)
						.build();
				break;
			default:
				classpathLoader = new ClasspathLoader();
				classpathLoader.setPrefix(StrUtil.addSuffixIfNot(config.getPath(), "/"));
				pebbleEngine = new PebbleEngine.Builder()
						.loader(classpathLoader)
						.autoEscaping(false)
						.build();
				break;
		}

		return pebbleEngine;
	}

	/**
	 * 通过路径获取对应模板操作类
	 * @param resource 资源，根据实现不同，此资源可以是模板本身，也可以是模板的相对路径
	 * @return
	 */
	@Override
	public Template getTemplate(String resource) {

		if (null == this.engine) {
			init(TemplateConfig.DEFAULT);
		}

		return PebbleTemplate.wrap(engine.getTemplate(resource));
	}

	@Override
	public Object getRawEngine() {
		return this.engine;
	}

}
