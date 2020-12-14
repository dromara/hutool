package cn.hutool.core.lang;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ObjectUtil;

import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源类加载器，可以加载任意类型的资源类
 *
 * @param <T> {@link Resource}接口实现类
 * @author looly
 * @since 5.5.2
 */
public class ResourceClassLoader<T extends Resource> extends SecureClassLoader {

	private final Map<String, T> resourceMap;

	/**
	 * 构造
	 *
	 * @param parentClassLoader 父类加载器，null表示默认当前上下文加载器
	 * @param resourceMap       资源map
	 */
	public ResourceClassLoader(ClassLoader parentClassLoader, Map<String, T> resourceMap) {
		super(ObjectUtil.defaultIfNull(parentClassLoader, ClassLoaderUtil.getClassLoader()));
		this.resourceMap = ObjectUtil.defaultIfNull(resourceMap, new HashMap<>());
	}

	/**
	 * 增加需要加载的类资源
	 * @param resource 资源，可以是文件、流或者字符串
	 * @return this
	 */
	public ResourceClassLoader<T> addResource(T resource){
		this.resourceMap.put(resource.getName(), resource);
		return this;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		final Resource resource = resourceMap.get(name);
		if (null != resource) {
			final byte[] bytes = resource.readBytes();
			return defineClass(name, bytes, 0, bytes.length);
		}
		return super.findClass(name);
	}
}
