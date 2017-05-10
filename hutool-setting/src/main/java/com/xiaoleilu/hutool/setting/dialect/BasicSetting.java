package com.xiaoleilu.hutool.setting.dialect;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.io.resource.ClassPathResource;
import com.xiaoleilu.hutool.io.resource.UrlResource;
import com.xiaoleilu.hutool.io.watch.SimpleWatcher;
import com.xiaoleilu.hutool.io.watch.WatchMonitor;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.setting.AbsSetting;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.setting.SettingLoader;
import com.xiaoleilu.hutool.setting.SettingRuntimeException;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

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
public class BasicSetting extends AbsSetting implements Map<Object, Object>{
	private static final long serialVersionUID = 3618305164959883393L;

	/** 默认字符集 */
	public final static Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

	/** 分组 */
	private final LinkedList<String> groups = new LinkedList<String>();
	/** 键值对存储 */
	private final Map<Object, Object> map = new ConcurrentHashMap<>();
	
	/** 本设置对象的字符集 */
	protected Charset charset;
	/** 是否使用变量 */
	protected boolean isUseVariable;
	/** 设定文件的URL */
	protected URL settingUrl;

	private SettingLoader settingLoader;
	private WatchMonitor watchMonitor;
	
	public BasicSetting() {
	}
	
	/**
	 * 构造
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public BasicSetting(String pathBaseClassLoader) {
		this(pathBaseClassLoader, DEFAULT_CHARSET, false);
	}
	
	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(String pathBaseClassLoader, Charset charset, boolean isUseVariable) {
		Assert.notBlank(pathBaseClassLoader, "Blank setting path !");
		this.init(new ClassPathResource(pathBaseClassLoader), charset, isUseVariable);
	}
	
	/**
	 * 构造
	 * 
	 * @param configFile 配置文件对象
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(File configFile, Charset charset, boolean isUseVariable) {
		Assert.notNull(configFile, "Null setting file define!");
		this.init(new UrlResource(configFile), charset, isUseVariable);
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
		Assert.notBlank(path, "Blank setting path !");
		this.init(new ClassPathResource(path, clazz), charset, isUseVariable);
	}

	/**
	 * 构造
	 * 
	 * @param url 设定文件的URL
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(URL url, Charset charset, boolean isUseVariable) {
		Assert.notNull(url, "Null setting url define!");
		this.init(new UrlResource(url), charset, isUseVariable);
	}
	
	/*--------------------------公有方法 start-------------------------------*/
	/**
	 * 初始化设定文件
	 * 
	 * @param urlResource 设定文件的URL
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 * @return 成功初始化与否
	 */
	public boolean init(UrlResource urlResource, Charset charset, boolean isUseVariable) {
		if (urlResource == null) {
			throw new NullPointerException("Null setting url define!");
		}
		this.settingUrl = urlResource.getUrl();
		this.charset = charset;
		this.isUseVariable = isUseVariable;

		return load();
	}

	/**
	 * 重新加载配置文件
	 */
	synchronized public boolean load() {
		if(null == this.settingLoader){
			settingLoader = new SettingLoader(this, this.charset, this.isUseVariable);
		}
		return settingLoader.load(new UrlResource(this.settingUrl));
	}
	
	/**
	 * 在配置文件变更时自动加载
	 * @param autoReload 是否自动加载
	 */
	public void autoLoad(boolean autoReload){
		if(autoReload){
			if(null != this.watchMonitor){
				this.watchMonitor.close();
			}
			try {
				watchMonitor = WatchMonitor.create(this.settingUrl, StandardWatchEventKinds.ENTRY_MODIFY);
				watchMonitor.setWatcher(new SimpleWatcher(){
					@Override
					public void onModify(WatchEvent<?> event) {
						load();
					}
				}).start();
			} catch (Exception e) {
				throw new SettingRuntimeException(e, "Setting auto load not support url: [{}]", this.settingUrl);
			}
			StaticLog.debug("Auto load for [{}] listenning...", this.settingUrl);
		}else{
			IoUtil.close(this.watchMonitor);
			this.watchMonitor = null;
		}
	}
	
	/**
	 * @return 获得设定文件的路径
	 */
	public String getSettingPath() {
		return (null == this.settingUrl) ? null : this.settingUrl.getPath();
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
	 * 获得指定分组的所有键值对
	 * @param group 分组
	 * @return map
	 */
	public Map<?, ?> getMap(String group){
		if(StrUtil.isBlank(group)){
			return this;
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
	public BasicSetting getSetting(String group){
		final BasicSetting setting = new BasicSetting();
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
		if(null == this.settingLoader){
			settingLoader = new SettingLoader(this, this.charset, this.isUseVariable);
		}
		settingLoader.store(absolutePath);
	}
	
	/**
	 * 设置变量的正则<br/>
	 * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
	 * 
	 * @param regex 正则
	 */
	public void setVarRegex(String regex) {
		if(null == this.settingLoader){
			throw new NullPointerException("SettingLoader is null !");
		}
		this.settingLoader.setVarRegex(regex);
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
	
	//------------------------------------------------- Override Map interface
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
	
	/**
	 * 加入Map中的键值对
	 * @param map {@link Map}
	 */
	@Override
	public void putAll(Map<? extends Object, ? extends Object> map) {
		this.map.putAll(map);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((charset == null) ? 0 : charset.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + (isUseVariable ? 1231 : 1237);
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((settingUrl == null) ? 0 : settingUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BasicSetting other = (BasicSetting) obj;
		if (charset == null) {
			if (other.charset != null) {
				return false;
			}
		} else if (!charset.equals(other.charset)) {
			return false;
		}
		if (groups == null) {
			if (other.groups != null) {
				return false;
			}
		} else if (!groups.equals(other.groups)) {
			return false;
		}
		if (isUseVariable != other.isUseVariable) {
			return false;
		}
		if (map == null) {
			if (other.map != null) {
				return false;
			}
		} else if (!map.equals(other.map)) {
			return false;
		}
		if (settingUrl == null) {
			if (other.settingUrl != null) {
				return false;
			}
		} else if (!settingUrl.equals(other.settingUrl)) {
			return false;
		}
		return true;
	}
}
