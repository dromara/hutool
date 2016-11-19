package com.xiaoleilu.hutool.setting.dialect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.lang.Convert;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.setting.AbsSetting;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.BeanUtil.ValueProvider;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.ReUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.util.URLUtil;

/**
 * 分组设置工具类。 用于支持设置文件<br>
 *  1、支持变量，默认变量命名为 ${变量名}，变量只能识别读入行的变量，例如第6行的变量在第三行无法读取
 *  2、支持分组，分组为中括号括起来的内容，中括号以下的行都为此分组的内容，无分组相当于空字符分组<br>
 *  		若某个key是name，加上分组后的键相当于group.name
 *  3、注释以#开头，但是空行和不带“=”的行也会被跳过，但是建议加#
 *  4、store方法不会保存注释内容，慎重使用
 * @author xiaoleilu
 * 
 */
public class BasicSetting extends AbsSetting{
	private final static Log log = StaticLog.get();
	
	final Map<Object, Object> map = new ConcurrentHashMap<>();
	
	/** 默认字符集 */
	public final static Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;
	/** 数组类型值默认分隔符 */
	public final static String DEFAULT_DELIMITER= ",";

	/** 注释符号（当有此符号在行首，表示此行为注释） */
	private final static String COMMENT_FLAG_PRE = "#";
	/** 赋值分隔符（用于分隔键值对） */
	private final static String ASSIGN_FLAG = "=";
	/** 分组行识别的环绕标记 */
	private final static char[] GROUP_SURROUND = { '[', ']' };
	/** 变量名称的正则 */
	private String reg_var = "\\$\\{(.*?)\\}";
	
	private LinkedList<String> groups = new LinkedList<String>();
	
	public BasicSetting() {
		this(DEFAULT_CHARSET, false);
	}
	
	/**
	 * 基本构造<br/>
	 * 需自定义初始化配置文件<br/>
	 * 
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(Charset charset, boolean isUseVariable) {
		this.charset = charset;
		this.isUseVariable = isUseVariable;
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(String pathBaseClassLoader, Charset charset, boolean isUseVariable) {
		if(null == pathBaseClassLoader) {
			pathBaseClassLoader = StrUtil.EMPTY;
		}
		
		final URL url = URLUtil.getURL(pathBaseClassLoader);
		if(url == null) {
			throw new RuntimeException(StrUtil.format("Can not find Setting file: [{}]", pathBaseClassLoader));
		}
		this.init(url, charset, isUseVariable);
	}

	/**
	 * 构造
	 * 
	 * @param configFile 配置文件对象
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(File configFile, Charset charset, boolean isUseVariable) {
		if (configFile == null) {
			throw new NullPointerException("Null Setting file!");
		}
		final URL url = URLUtil.getURL(configFile);
		if(url == null) {
			throw new RuntimeException(StrUtil.format("Can not find Setting file: [{}]", configFile.getAbsolutePath()));
		}
		this.init(url, charset, isUseVariable);
	}

	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(String path, Class<?> clazz, Charset charset, boolean isUseVariable) {
		final URL url = URLUtil.getURL(path, clazz);
		if(url == null) {
			throw new RuntimeException(StrUtil.format("Can not find Setting file: [{}]", path));
		}
		this.init(url, charset, isUseVariable);
	}

	/**
	 * 构造
	 * 
	 * @param url 设定文件的URL
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(URL url, Charset charset, boolean isUseVariable) {
		if(url == null) {
			throw new RuntimeException("Null url define!");
		}
		this.init(url, charset, isUseVariable);
	}
	
	/**
	 * 构造
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public BasicSetting(String pathBaseClassLoader) {
		this(pathBaseClassLoader, DEFAULT_CHARSET, false);
	}
	
	/*--------------------------公有方法 start-------------------------------*/
	/**
	 * 初始化设定文件
	 * 
	 * @param settingUrl 设定文件的URL
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 * @return 成功初始化与否
	 */
	public boolean init(URL settingUrl, Charset charset, boolean isUseVariable) {
		if (settingUrl == null) {
			throw new RuntimeException("Null setting url or charset define!");
		}
		this.charset = charset;
		this.isUseVariable = isUseVariable;
		this.settingUrl = settingUrl;

		return this.load(settingUrl);
	}

	/**
	 * 加载设置文件
	 * 
	 * @param settingUrl 配置文件URL
	 * @return 加载是否成功
	 */
	synchronized public boolean load(URL settingUrl) {
		if (settingUrl == null) {
			throw new RuntimeException("Null setting url define!");
		}
		log.debug("Load setting file [{}]", settingUrl.getPath());
		InputStream settingStream = null;
		try {
			settingStream = settingUrl.openStream();
			load(settingStream, isUseVariable);
		} catch (IOException e) {
			log.error(e, "Load setting error!");
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
		this.load(settingUrl);
	}

	/**
	 * 加载设置文件。 此方法不会关闭流对象
	 * 
	 * @param settingStream 文件流
	 * @param isUseVariable 是否使用变量（替换配置文件值中含有的变量）
	 * @return 加载成功与否
	 * @throws IOException
	 */
	public boolean load(InputStream settingStream, boolean isUseVariable) throws IOException {
		map.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(settingStream, charset));
			// 分组
			String group = null;
			
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				line = line.trim();
				// 跳过注释行和空行
				if (StrUtil.isBlank(line) || line.startsWith(COMMENT_FLAG_PRE)) {
					continue;
				}
				
				// 记录分组名
				if (line.charAt(0) == GROUP_SURROUND[0] && line.charAt(line.length() - 1) == GROUP_SURROUND[1]) {
					group = line.substring(1, line.length() - 1).trim();
					this.groups.add(group);
					continue;
				}
				
				String[] keyValue = line.split(ASSIGN_FLAG, 2);
				// 跳过不符合简直规范的行
				if (keyValue.length < 2) {
					continue;
				}
				
				String key = keyValue[0].trim();
				if (false == StrUtil.isBlank(group)) {
					key = group + StrUtil.DOT + key;
				}
				String value = keyValue[1].trim();
				
				// 替换值中的所有变量变量（变量必须是此行之前定义的变量，否则无法找到）
				if (isUseVariable) {
					value = replaceVar(value);
				}
				map.put(key, value);
			}
		} finally {
			IoUtil.close(reader);
		}
		return true;
	}

	/**
	 * 设置变量的正则<br/>
	 * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
	 * 
	 * @param regex 正则
	 */
	public void setVarRegex(String regex) {
		this.reg_var = regex;
	}
	
	/**
	 * @return 获得设定文件的路径
	 */
	public String getSettingPath() {
		return settingUrl.getPath();
	}

	@Override
	public int size() {
		return map.size();
	}
	
	@Override
	public Object getObj(String key, Object defaultValue) {
		final Object value = map.get(key);
		if(null == value) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * 获得字符串类型值
	 * @param key KEY
	 * @param group 分组
	 * @param defaultValue 默认值
	 * @return 值或默认值
	 */
	public String getStr(String key, String group, String defaultValue) {
		final String value = getByGroup(key, group);
		if(StrUtil.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 获得指定分组的键对应值
	 * 
	 * @param key 键
	 * @param group 分组
	 * @return 值
	 */
	public String getByGroup(String key, String group) {
		return getStr(keyWithGroup(key, group));
	}
	
	/**
	 * 获得所有键值对
	 * @return map
	 */
	public Map<Object, Object> getMap(){
		return this.map;
	}
	
	/**
	 * 获得指定分组的所有键值对
	 * @param group 分组
	 * @return map
	 */
	public Map<?, ?> getMap(String group){
		if(StrUtil.isBlank(group)){
			return getMap();
		}
		
		String groupDot = group.concat(StrUtil.DOT);
		Map<String, Object> map2 = new HashMap<String, Object>();
		String keyStr;
		for (Object key : map.keySet()) {
			keyStr = Convert.toStr(key);
			if(StrUtil.isNotBlank(keyStr) && keyStr.startsWith(groupDot)){
				map2.put(StrUtil.removePrefix(keyStr, groupDot), map.get(key));
			}
		}
		return map2;
	}
	
	/**
	 * 获得group对应的子Setting
	 * @param group 分组
	 * @return {@link Setting}
	 */
	public Setting getSetting(String group){
		final Setting setting = new Setting();
		setting.putAll(this.getMap(group));
		return setting;
	}
	
	/**
	 * 转换为Properties对象，原分组变为前缀
	 * @return Properties对象
	 */
	public Properties getProperties(String group){
		Properties properties = new Properties();
		properties.putAll(getMap(group));
		return properties;
	}
	
	//--------------------------------------------------------------------------------- Functions
	/**
	 * 持久化当前设置，会覆盖掉之前的设置<br>
	 * 持久化会不会保留之前的分组
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath) {
		Writer writer = null;
		try {
			writer = FileUtil.getWriter(absolutePath, charset, false);
			for (Entry<Object, Object> entry : map.entrySet()) {
				writer.write(StrUtil.format("{} {} {}", entry.getKey(), ASSIGN_FLAG, entry.getValue()));
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(StrUtil.format("Can not find file [{}]!", absolutePath), e);
		} catch (IOException e) {
			throw new RuntimeException("Store Setting error!", e);
		}finally{
			IoUtil.close(writer);
		}
	}

	/**
	 * 存储当前设置，会覆盖掉以前的设置
	 * 
	 * @param path 相对路径
	 * @param clazz 相对的类
	 */
	public void store(String path, Class<?> clazz) {
		this.store(FileUtil.getAbsolutePath(path, clazz));
	}
	
	/**
	 * 加入Map中的键值对
	 * @param map {@link Map}
	 */
	@Override
	public void putAll(Map<? extends Object, ? extends Object> m) {
		this.map.putAll(m);
	}
	
	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br/>
	 * 只支持基本类型的转换
	 * 
	 * @param group 分组
	 * @param bean Bean对象
	 * @return Bean
	 */
	public Object toBean(final String group, Object bean) {
		return BeanUtil.fillBean(bean, new ValueProvider(){
			
			@Override
			public Object value(String name) {
				final String value = getByGroup(name, group);
				if(null != value){
					log.debug("Parse setting to object field [{}={}]", name, value);
				}
				return value;
			}
		});
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br/>
	 * 只支持基本类型的转换
	 * 
	 * @param bean Bean
	 * @return Bean
	 */
	public Object toBean(Object bean) {
		return toBean(null, bean);
	}
	
	/**
	 * 转换为Properties对象，原分组变为前缀
	 * @return Properties对象
	 */
	public Properties toProperties(){
		Properties properties = new Properties();
		properties.putAll(map);
		return properties;
	}
	
	/**
	 * @return 获得所有分组名
	 */
	public LinkedList<String> getGroups() {
		return this.groups;
	}
	
	/**
	 * @return 所有键值对
	 */
	@Override
	public Set<Entry<Object, Object>> entrySet(){
		return map.entrySet();
	}
	
	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return this.map.get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		return this.map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return this.map.remove(key);
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Set<Object> keySet() {
		return this.map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return this.map.values();
	}
	
	@Override
	public String toString() {
		return map.toString();
	}

	/*--------------------------Private Method start-------------------------------*/
	/**
	 * 替换给定值中的变量标识
	 * @param value 值
	 * @return 替换后的字符串
	 */
	private String replaceVar(String value) {
		// 找到所有变量标识
		final Set<String> vars = ReUtil.findAll(reg_var, value, 0, new HashSet<String>());
		for (String var : vars) {
			// 查找变量名对应的值
			Object varValue = map.get(ReUtil.get(reg_var, var, 1));
			if (null != varValue && value instanceof CharSequence) {
				// 替换标识
				value = value.replace(var, (CharSequence)varValue);
			}
		}
		return value;
	}
	
	/**
	 * 组合Key和Group，组合后为group.key
	 * @param key
	 * @param group
	 * @return 组合后的KEY
	 */
	private static  String keyWithGroup(String key, String group){
		String keyWithGroup = key;
		if (!StrUtil.isBlank(group)) {
			keyWithGroup = group.concat(StrUtil.DOT).concat(key);
		}
		return keyWithGroup;
	}

	/*--------------------------Private Method end-------------------------------*/
}
