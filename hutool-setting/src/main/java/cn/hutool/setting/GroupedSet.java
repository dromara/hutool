package cn.hutool.setting;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 分组化的Set集合类<br>
 * 在配置文件中可以用中括号分隔不同的分组，每个分组会放在独立的Set中，用group区别<br>
 * 无分组的集合和`[]`分组集合会合并成员，重名的分组也会合并成员<br>
 * 分组配置文件如下：
 * 
 * <pre>
 * [group1]
 * aaa
 * bbb
 * ccc
 * 
 * [group2]
 * aaa
 * ccc
 * ddd
 * </pre>
 * 
 * @author Looly
 * @since 3.1.0
 */
public class GroupedSet extends HashMap<String, LinkedHashSet<String>> {
	private static final long serialVersionUID = -8430706353275835496L;
	// private final static Log log = StaticLog.get();

	/** 注释符号（当有此符号在行首，表示此行为注释） */
	private final static String COMMENT_FLAG_PRE = "#";
	/** 分组行识别的环绕标记 */
	private final static char[] GROUP_SURROUND = { '[', ']' };

	/** 本设置对象的字符集 */
	private Charset charset;
	/** 设定文件的URL */
	private URL groupedSetUrl;

	/**
	 * 基本构造<br>
	 * 需自定义初始化配置文件
	 * 
	 * @param charset 字符集
	 */
	public GroupedSet(Charset charset) {
		this.charset = charset;
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 * @param charset 字符集
	 */
	public GroupedSet(String pathBaseClassLoader, Charset charset) {
		if (null == pathBaseClassLoader) {
			pathBaseClassLoader = StrUtil.EMPTY;
		}

		final URL url = URLUtil.getURL(pathBaseClassLoader);
		if (url == null) {
			throw new RuntimeException(StrUtil.format("Can not find GroupSet file: [{}]", pathBaseClassLoader));
		}
		this.init(url, charset);
	}

	/**
	 * 构造
	 * 
	 * @param configFile 配置文件对象
	 * @param charset 字符集
	 */
	public GroupedSet(File configFile, Charset charset) {
		if (configFile == null) {
			throw new RuntimeException("Null GroupSet file!");
		}
		final URL url = URLUtil.getURL(configFile);
		this.init(url, charset);
	}

	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 * @param charset 字符集
	 */
	public GroupedSet(String path, Class<?> clazz, Charset charset) {
		final URL url = URLUtil.getURL(path, clazz);
		if (url == null) {
			throw new RuntimeException(StrUtil.format("Can not find GroupSet file: [{}]", path));
		}
		this.init(url, charset);
	}

	/**
	 * 构造
	 * 
	 * @param url 设定文件的URL
	 * @param charset 字符集
	 */
	public GroupedSet(URL url, Charset charset) {
		if (url == null) {
			throw new RuntimeException("Null url define!");
		}
		this.init(url, charset);
	}

	/**
	 * 构造
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public GroupedSet(String pathBaseClassLoader) {
		this(pathBaseClassLoader, CharsetUtil.CHARSET_UTF_8);
	}

	/*--------------------------公有方法 start-------------------------------*/
	/**
	 * 初始化设定文件
	 * 
	 * @param groupedSetUrl 设定文件的URL
	 * @param charset 字符集
	 * @return 成功初始化与否
	 */
	public boolean init(URL groupedSetUrl, Charset charset) {
		if (groupedSetUrl == null) {
			throw new RuntimeException("Null GroupSet url or charset define!");
		}
		this.charset = charset;
		this.groupedSetUrl = groupedSetUrl;

		return this.load(groupedSetUrl);
	}

	/**
	 * 加载设置文件
	 * 
	 * @param groupedSetUrl 配置文件URL
	 * @return 加载是否成功
	 */
	synchronized public boolean load(URL groupedSetUrl) {
		if (groupedSetUrl == null) {
			throw new RuntimeException("Null GroupSet url define!");
		}
		// log.debug("Load GroupSet file [{}]", groupedSetUrl.getPath());
		InputStream settingStream = null;
		try {
			settingStream = groupedSetUrl.openStream();
			load(settingStream);
		} catch (IOException e) {
			// log.error(e, "Load GroupSet error!");
			return false;
		} finally {
			IoUtil.close(settingStream);
		}
		return true;
	}

	/**
	 * 重新加载配置文件
	 */
	public void reload() {
		this.load(groupedSetUrl);
	}

	/**
	 * 加载设置文件。 此方法不会关闭流对象
	 * 
	 * @param settingStream 文件流
	 * @return 加载成功与否
	 * @throws IOException IO异常
	 */
	public boolean load(InputStream settingStream) throws IOException {
		super.clear();
		BufferedReader reader = null;
		try {
			reader = IoUtil.getReader(settingStream, charset);
			// 分组
			String group;
			LinkedHashSet<String> valueSet = null;

			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				line = line.trim();
				// 跳过注释行和空行
				if (StrUtil.isBlank(line) || line.startsWith(COMMENT_FLAG_PRE)) {
					// 空行和注释忽略
					continue;
				} else if (line.startsWith(StrUtil.BACKSLASH + COMMENT_FLAG_PRE)) {
					// 对于值中出现开头为#的字符串，需要转义处理，在此做反转义
					line = line.substring(1);
				}

				// 记录分组名
				if (line.charAt(0) == GROUP_SURROUND[0] && line.charAt(line.length() - 1) == GROUP_SURROUND[1]) {
					// 开始新的分组取值，当出现重名分组时候，合并分组值
					group = line.substring(1, line.length() - 1).trim();
					valueSet = super.get(group);
					if (null == valueSet) {
						valueSet = new LinkedHashSet<>();
					}
					super.put(group, valueSet);
					continue;
				}

				// 添加值
				if (null == valueSet) {
					// 当出现无分组值的时候，会导致valueSet为空，此时group为""
					valueSet = new LinkedHashSet<>();
					super.put(StrUtil.EMPTY, valueSet);
				}
				valueSet.add(line);
			}
		} finally {
			IoUtil.close(reader);
		}
		return true;
	}

	/**
	 * @return 获得设定文件的路径
	 */
	public String getPath() {
		return groupedSetUrl.getPath();
	}

	/**
	 * @return 获得所有分组名
	 */
	public Set<String> getGroups() {
		return super.keySet();
	}

	/**
	 * 获得对应分组的所有值
	 * 
	 * @param group 分组名
	 * @return 分组的值集合
	 */
	public LinkedHashSet<String> getValues(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}
		return super.get(group);
	}

	/**
	 * 是否在给定分组的集合中包含指定值<br>
	 * 如果给定分组对应集合不存在，则返回false
	 * 
	 * @param group 分组名
	 * @param value 测试的值
	 * @param otherValues 其他值
	 * @return 是否包含
	 */
	public boolean contains(String group, String value, String... otherValues) {
		if (ArrayUtil.isNotEmpty(otherValues)) {
			// 需要测试多个值的情况		
			final List<String> valueList = ListUtil.toList(otherValues);
			valueList.add(value);
			return contains(group, valueList);
		} else {
			// 测试单个值
			final LinkedHashSet<String> valueSet = getValues(group);
			if (CollectionUtil.isEmpty(valueSet)) {
				return false;
			}

			return valueSet.contains(value);
		}
	}

	/**
	 * 是否在给定分组的集合中全部包含指定值集合<br>
	 * 如果给定分组对应集合不存在，则返回false
	 * 
	 * @param group 分组名
	 * @param values 测试的值集合
	 * @return 是否包含
	 */
	public boolean contains(String group, Collection<String> values) {
		final LinkedHashSet<String> valueSet = getValues(group);
		if (CollectionUtil.isEmpty(values) || CollectionUtil.isEmpty(valueSet)) {
			return false;
		}

		return valueSet.containsAll(values);
	}
}
