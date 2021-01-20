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

	/**
	 * 获取当前环境下的配置文件<br>
	 * name可以为不包括扩展名的文件名（默认.properties），也可以是文件名全称
	 *
	 * @param name 文件名，如果没有扩展名，默认为.properties
	 * @return 当前环境下配置文件
	 */
	public static Props get(String name) {
		return propsMap.computeIfAbsent(name, (filePath)->{
			final String extName = FileUtil.extName(filePath);
			if (StrUtil.isEmpty(extName)) {
				filePath = filePath + "." + Props.EXT_NAME;
			}
			return new Props(filePath);
		});
	}

	/**
	 * 获取给定路径找到的第一个配置文件<br>
	 * * name可以为不包括扩展名的文件名（默认.properties为结尾），也可以是文件名全称
	 *
	 * @param names 文件名，如果没有扩展名，默认为.properties
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

	/**
	 * 获取系统参数，例如用户在执行java命令时定义的 -Duse=hutool
	 *
	 * @return 系统参数Props
	 * @since 5.5.2
	 */
	public static Props getSystemProps(){
		return new Props(System.getProperties());
	}
}
