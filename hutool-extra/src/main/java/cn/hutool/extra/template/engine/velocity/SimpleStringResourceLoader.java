package cn.hutool.extra.template.engine.velocity;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ExtProperties;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;

/**
 * {@link ResourceLoader} 的字符串实现形式<br>
 * 用于直接获取字符串模板
 * 
 * @author looly
 *
 */
public class SimpleStringResourceLoader extends ResourceLoader {

	@Override
	public void init(ExtProperties configuration) {
	}

	/**
	 * 获取资源流
	 * 
	 * @param source 字符串模板
	 * @return 流
	 * @throws ResourceNotFoundException 资源未找到
	 */
	public InputStream getResourceStream(String source) throws ResourceNotFoundException {
		return IoUtil.toStream(source, CharsetUtil.CHARSET_UTF_8);
	}
	
	@Override
	public Reader getResourceReader(String source, String encoding) throws ResourceNotFoundException {
		return new StringReader(source);
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return false;
	}

	@Override
	public long getLastModified(Resource resource) {
		return 0;
	}

}
