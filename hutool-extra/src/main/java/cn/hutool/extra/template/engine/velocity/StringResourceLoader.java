package cn.hutool.extra.template.engine.velocity;

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;

public class StringResourceLoader extends ResourceLoader{

	@Override
	public void init(ExtendedProperties configuration) {
	}
	
	@Override
	public InputStream getResourceStream(String source) throws ResourceNotFoundException {
		return IoUtil.toStream(source, CharsetUtil.CHARSET_UTF_8);
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
