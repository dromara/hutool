package cn.hutool.extra.template.engine.jetbrick.loader;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import jetbrick.io.resource.AbstractResource;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.loader.AbstractResourceLoader;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 字符串模板加载器
 *
 * @author looly
 * @since 5.7.21
 */
public class StringResourceLoader extends AbstractResourceLoader {

	private Charset charset;

	@Override
	public Resource load(String name) {
		return new StringTemplateResource(name, charset);
	}

	/**
	 * 设置编码
	 * @param charset 编码
	 */
	public void setCharset(Charset charset){
		this.charset = charset;
	}

	/**
	 * 字符串模板
	 *
	 * @author looly
	 */
	static class StringTemplateResource extends AbstractResource {

		private final String content;
		private final Charset charset;

		/**
		 * 构造
		 * @param content 模板内容
		 * @param charset 编码
		 */
		public StringTemplateResource(String content, Charset charset){
			this.content = content;
			this.charset = charset;
		}

		@Override
		public InputStream openStream() throws ResourceNotFoundException {
			return IoUtil.toStream(content, charset);
		}

		@Override
		public URL getURL() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean exist() {
			return StrUtil.isEmpty(content);
		}

		@Override
		public String toString() {
			return this.content;
		}

		@Override
		public long lastModified() {
			return 1;
		}
	}
}
