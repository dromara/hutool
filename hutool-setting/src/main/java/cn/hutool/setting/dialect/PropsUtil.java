package cn.hutool.setting.dialect;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Props工具类<br>
 * 提供静态方法获取配置文件
 *
 * @author looly
 * @since 5.1.3
 */
public class PropsUtil {

	/**
	 * 配置文件缓存
	 */
	private static final Map<String, Props> propsMap = new ConcurrentHashMap<>();
	private static final Object lock = new Object();

	/**
	 * 获取当前环境下的配置文件<br>
	 * name可以为不包括扩展名的文件名（默认.properties），也可以是文件名全称
	 *
	 * @param name 文件名，如果没有扩展名，默认为.properties
	 * @return 当前环境下配置文件
	 */
	public static Props get(String name) {
		Props props = propsMap.get(name);
		if (null == props) {
			synchronized (lock) {
				props = propsMap.get(name);
				if (null == props) {
					String filePath = name;
					String extName = FileUtil.extName(filePath);
					if (StrUtil.isEmpty(extName)) {
						filePath = filePath + "." + Props.EXT_NAME;
					}
					props = new Props(filePath);
					propsMap.put(name, props);
				}
			}
		}
		return props;
	}

	/**
	 * 获取给定路径找到的第一个配置文件<br>
	 * * name可以为不包括扩展名的文件名（默认.setting为结尾），也可以是文件名全称
	 *
	 * @param names 文件名，如果没有扩展名，默认为.setting
	 *
	 * @return 当前环境下配置文件
	 */
	public static Props getFirstFound(String... names) {
		for (String name : names) {
			try {
				return get(name);
			} catch (NoResourceException e) {
				//ignore
			}
		}
		return null;
	}
}
