package com.xiaoleilu.hutool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

/**
 * 分组化的Set集合类<br>
 * 在配置文件中可以用中括号分隔不同的分组，每个分组会放在独立的Set中，用group区别<br>
 * 无分组的集合和`[]`分组集合会合并成员，重名的分组也会合并成员
 * @author Looly
 *
 */
public class GroupedSet extends HashMap<String, LinkedHashSet<String>>{
	private static final long serialVersionUID = -8430706353275835496L;
	private static Logger log = Log.get();
	
	/** 本设置对象的字符集 */
	private Charset charset;
	/** 设定文件的URL */
	private URL groupedSetUrl;
	
	/**
	 * 基本构造<br/>
	 * 需自定义初始化配置文件<br/>
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
	public GroupedSet(String pathBaseClassLoader, String charset) {
		if(null == pathBaseClassLoader) {
			pathBaseClassLoader = StrUtil.EMPTY;
		}
		
		final URL url = URLUtil.getURL(pathBaseClassLoader);
		if(url == null) {
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
	public GroupedSet(File configFile, String charset) {
		if (configFile == null) {
			throw new RuntimeException("Null GroupSet file!");
		}
		final URL url = URLUtil.getURL(configFile);
		if(url == null) {
			throw new RuntimeException(StrUtil.format("Can not find GroupSet file: [{}]", configFile.getAbsolutePath()));
		}
		this.init(url, charset);
	}
	
	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 * @param charset 字符集
	 */
	public GroupedSet(String path, Class<?> clazz, String charset) {
		final URL url = URLUtil.getURL(path, clazz);
		if(url == null) {
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
	public GroupedSet(URL url, String charset) {
		if(url == null) {
			throw new RuntimeException("Null url define!");
		}
		this.init(url, charset);
	}
	
	/**
	 * 构造
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public GroupedSet(String pathBaseClassLoader) {
		this(pathBaseClassLoader, Setting.DEFAULT_CHARSET);
	}
	
	/*--------------------------公有方法 start-------------------------------*/
	/**
	 * 初始化设定文件
	 * 
	 * @param groupedSetUrl 设定文件的URL
	 * @param charset 字符集
	 * @return 成功初始化与否
	 */
	public boolean init(URL groupedSetUrl, String charset) {
		if (groupedSetUrl == null) {
			throw new RuntimeException("Null GroupSet url or charset define!");
		}
		try {
			this.charset = Charset.forName(charset);
		} catch (Exception e) {
			log.warn("User custom charset [{}] parse error, use default charset: [{}]", charset, Setting.DEFAULT_CHARSET);
			this.charset = Charset.forName(Setting.DEFAULT_CHARSET);
		}
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
		log.debug("Load GroupSet file [{}]", groupedSetUrl.getPath());
		InputStream settingStream = null;
		try {
			settingStream = groupedSetUrl.openStream();
			load(settingStream);
		} catch (IOException e) {
			log.error("Load GroupSet error!", e);
			return false;
		} finally {
			FileUtil.close(settingStream);
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
	 * @throws IOException
	 */
	public boolean load(InputStream settingStream) throws IOException {
		super.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(settingStream, charset));
			// 分组
			String group = null;
			LinkedHashSet<String> valueSet = null;
			
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				line = line.trim();
				// 跳过注释行和空行
				if (StrUtil.isBlank(line) || line.startsWith(Setting.COMMENT_FLAG_PRE)) {
					//空行和注释忽略
					continue;
				}else if(line.startsWith(StrUtil.BACKSLASH + Setting.COMMENT_FLAG_PRE)) {
					//对于值中出现开头为#的字符串，需要转义处理，在此做反转义
					line = line.substring(1);
				}
				
				// 记录分组名
				if (line.charAt(0) == Setting.GROUP_SURROUND[0] && line.charAt(line.length() - 1) == Setting.GROUP_SURROUND[1]) {
					//开始新的分组取值，当出现重名分组时候，合并分组值
					group = line.substring(1, line.length() - 1).trim();
					valueSet = super.get(group);
					if(null == valueSet) {
						valueSet = new LinkedHashSet<String>();
					}
					super.put(group, valueSet);
					continue;
				}
				
				//添加值
				if(null == valueSet) {
					//当出现无分组值的时候，会导致valueSet为空，此时group为""
					valueSet = new LinkedHashSet<String>();
					super.put(StrUtil.EMPTY, valueSet);
				}
				valueSet.add(line);
			}
		} finally {
			FileUtil.close(reader);
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
	 * @param group 分组名
	 * @return 分组的值集合
	 */
	public LinkedHashSet<String> getValues(String group) {
		if(group == null) {
			group = StrUtil.EMPTY;
		}
		return super.get(group);
	}
	
	/**
	 * 是否在给定分组的集合中包含指定值<br>
	 * 如果给定分组对应集合不存在，则返回false
	 * @param group 分组名
	 * @param value 测试的值
	 * @param otherValues 其他值
	 * @return 是否包含
	 */
	public boolean contains(String group, String value, String... otherValues) {
		if(CollectionUtil.isNotEmpty(otherValues)) {
			//需要测试多个值的情况
			final List<String> valueList = Arrays.asList(otherValues);
			valueList.add(value);
			return contains(group, valueList);
		}else {
			//测试单个值
			final LinkedHashSet<String> valueSet = getValues(group);
			if(CollectionUtil.isEmpty(valueSet)) {
				return false;
			}
			
			return valueSet.contains(value);
		}
	}
	
	/**
	 * 是否在给定分组的集合中全部包含指定值集合<br>
	 * 如果给定分组对应集合不存在，则返回false
	 * @param group 分组名
	 * @param values 测试的值集合
	 * @return 是否包含
	 */
	public boolean contains(String group, Collection<String> values) {
		final LinkedHashSet<String> valueSet = getValues(group);
		if(CollectionUtil.isEmpty(values) || CollectionUtil.isEmpty(valueSet)) {
			return false;
		}
		
		return valueSet.containsAll(values);
	}
}
