package cn.hutool.setting;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.io.resource.UrlResource;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.setting.dialect.Props;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 设置工具类。 用于支持设置（配置）文件<br>
 * BasicSetting用于替换Properties类，提供功能更加强大的配置文件，同时对Properties文件向下兼容
 *
 * <pre>
 *  1、支持变量，默认变量命名为 ${变量名}，变量只能识别读入行的变量，例如第6行的变量在第三行无法读取
 *  2、支持分组，分组为中括号括起来的内容，中括号以下的行都为此分组的内容，无分组相当于空字符分组，若某个key是name，加上分组后的键相当于group.name
 *  3、注释以#开头，但是空行和不带“=”的行也会被跳过，但是建议加#
 *  4、store方法不会保存注释内容，慎重使用
 * </pre>
 *
 * @author looly
 */
public class Setting extends AbsSetting implements Map<String, String> {
	private static final long serialVersionUID = 3618305164959883393L;

	/**
	 * 默认字符集
	 */
	public final static Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;
	/**
	 * 默认配置文件扩展名
	 */
	public final static String EXT_NAME = "setting";

	/**
	 * 附带分组的键值对存储
	 */
	private final GroupedMap groupedMap = new GroupedMap();

	/**
	 * 本设置对象的字符集
	 */
	protected Charset charset;
	/**
	 * 是否使用变量
	 */
	protected boolean isUseVariable;
	/**
	 * 设定文件的URL
	 */
	protected URL settingUrl;

	private SettingLoader settingLoader;
	private WatchMonitor watchMonitor;

	// ------------------------------------------------------------------------------------- Constructor start

	/**
	 * 空构造
	 */
	public Setting() {
		this.charset = DEFAULT_CHARSET;
	}

	/**
	 * 构造
	 *
	 * @param path 相对路径或绝对路径
	 */
	public Setting(String path) {
		this(path, false);
	}

	/**
	 * 构造
	 *
	 * @param path          相对路径或绝对路径
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(String path, boolean isUseVariable) {
		this(path, DEFAULT_CHARSET, isUseVariable);
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 *
	 * @param path          相对路径或绝对路径
	 * @param charset       字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(String path, Charset charset, boolean isUseVariable) {
		Assert.notBlank(path, "Blank setting path !");
		this.init(ResourceUtil.getResourceObj(path), charset, isUseVariable);
	}

	/**
	 * 构造
	 *
	 * @param configFile    配置文件对象
	 * @param charset       字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(File configFile, Charset charset, boolean isUseVariable) {
		Assert.notNull(configFile, "Null setting file define!");
		this.init(new FileResource(configFile), charset, isUseVariable);
	}

	/**
	 * 构造，相对于classes读取文件
	 *
	 * @param path          相对ClassPath路径或绝对路径
	 * @param clazz         基准类
	 * @param charset       字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(String path, Class<?> clazz, Charset charset, boolean isUseVariable) {
		Assert.notBlank(path, "Blank setting path !");
		this.init(new ClassPathResource(path, clazz), charset, isUseVariable);
	}

	/**
	 * 构造
	 *
	 * @param url           设定文件的URL
	 * @param charset       字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(URL url, Charset charset, boolean isUseVariable) {
		Assert.notNull(url, "Null setting url define!");
		this.init(new UrlResource(url), charset, isUseVariable);
	}
	// ------------------------------------------------------------------------------------- Constructor end

	/**
	 * 初始化设定文件
	 *
	 * @param resource      {@link Resource}
	 * @param charset       字符集
	 * @param isUseVariable 是否使用变量
	 * @return 成功初始化与否
	 */
	public boolean init(Resource resource, Charset charset, boolean isUseVariable) {
		if (resource == null) {
			throw new NullPointerException("Null setting url define!");
		}
		this.settingUrl = resource.getUrl();
		this.charset = charset;
		this.isUseVariable = isUseVariable;

		return load();
	}

	/**
	 * 重新加载配置文件
	 *
	 * @return 是否加载成功
	 */
	synchronized public boolean load() {
		if (null == this.settingLoader) {
			settingLoader = new SettingLoader(this.groupedMap, this.charset, this.isUseVariable);
		}
		return settingLoader.load(new UrlResource(this.settingUrl));
	}

	/**
	 * 在配置文件变更时自动加载
	 *
	 * @param autoReload 是否自动加载
	 */
	public void autoLoad(boolean autoReload) {
		autoLoad(autoReload, null);
	}

	/**
	 * 在配置文件变更时自动加载
	 *
	 * @param callback   加载完成回调
	 * @param autoReload 是否自动加载
	 */
	public void autoLoad(boolean autoReload, Consumer<Boolean> callback) {
		if (autoReload) {
			Assert.notNull(this.settingUrl, "Setting URL is null !");
			if (null != this.watchMonitor) {
				// 先关闭之前的监听
				this.watchMonitor.close();
			}
			this.watchMonitor = WatchUtil.createModify(this.settingUrl, new SimpleWatcher() {
				@Override
				public void onModify(WatchEvent<?> event, Path currentPath) {
					boolean success = load();
					// 如果有回调，加载完毕则执行回调
					if (callback != null) {
						callback.accept(success);
					}
				}
			});
			this.watchMonitor.start();
			StaticLog.debug("Auto load for [{}] listenning...", this.settingUrl);
		} else {
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

	/**
	 * 键值总数
	 *
	 * @return 键值总数
	 */
	@Override
	public int size() {
		return this.groupedMap.size();
	}

	@Override
	public String getByGroup(String key, String group) {
		return this.groupedMap.get(group, key);
	}

	/**
	 * 获取并删除键值对，当指定键对应值非空时，返回并删除这个值，后边的键对应的值不再查找
	 *
	 * @param keys 键列表，常用于别名
	 * @return 值
	 * @since 3.1.2
	 */
	public Object getAndRemove(String... keys) {
		Object value = null;
		for (String key : keys) {
			value = remove(key);
			if (null != value) {
				break;
			}
		}
		return value;
	}

	/**
	 * 获取并删除键值对，当指定键对应值非空时，返回并删除这个值，后边的键对应的值不再查找
	 *
	 * @param keys 键列表，常用于别名
	 * @return 字符串值
	 * @since 3.1.2
	 */
	public String getAndRemoveStr(String... keys) {
		String value = null;
		for (String key : keys) {
			value = remove(key);
			if (null != value) {
				break;
			}
		}
		return value;
	}

	/**
	 * 获得指定分组的所有键值对，此方法获取的是原始键值对，获取的键值对可以被修改
	 *
	 * @param group 分组
	 * @return map
	 */
	public Map<String, String> getMap(String group) {
		final LinkedHashMap<String, String> map = this.groupedMap.get(group);
		return (null != map) ? map : new LinkedHashMap<>(0);
	}

	/**
	 * 获取group分组下所有配置键值对，组成新的{@link Setting}
	 *
	 * @param group 分组
	 * @return {@link Setting}
	 */
	public Setting getSetting(String group) {
		final Setting setting = new Setting();
		setting.putAll(this.getMap(group));
		return setting;
	}

	/**
	 * 获取group分组下所有配置键值对，组成新的{@link Properties}
	 *
	 * @param group 分组
	 * @return Properties对象
	 */
	public Properties getProperties(String group) {
		final Properties properties = new Properties();
		properties.putAll(getMap(group));
		return properties;
	}

	/**
	 * 获取group分组下所有配置键值对，组成新的{@link Props}
	 *
	 * @param group 分组
	 * @return Props对象
	 * @since 4.1.21
	 */
	public Props getProps(String group) {
		final Props props = new Props();
		props.putAll(getMap(group));
		return props;
	}

	// --------------------------------------------------------------------------------- Functions

	/**
	 * 持久化当前设置，会覆盖掉之前的设置<br>
	 * 持久化不会保留之前的分组
	 *
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath) {
		if (null == this.settingLoader) {
			settingLoader = new SettingLoader(this.groupedMap, this.charset, this.isUseVariable);
		}
		settingLoader.store(absolutePath);
	}

	/**
	 * 转换为Properties对象，原分组变为前缀
	 *
	 * @return Properties对象
	 */
	public Properties toProperties() {
		final Properties properties = new Properties();
		String group;
		for (Entry<String, LinkedHashMap<String, String>> groupEntry : this.groupedMap.entrySet()) {
			group = groupEntry.getKey();
			for (Entry<String, String> entry : groupEntry.getValue().entrySet()) {
				properties.setProperty(StrUtil.isEmpty(group) ? entry.getKey() : group + CharUtil.DOT + entry.getKey(), entry.getValue());
			}
		}
		return properties;
	}

	/**
	 * 获取GroupedMap
	 *
	 * @return GroupedMap
	 * @since 4.0.12
	 */
	public GroupedMap getGroupedMap() {
		return this.groupedMap;
	}

	/**
	 * 获取所有分组
	 *
	 * @return 获得所有分组名
	 */
	public List<String> getGroups() {
		return CollUtil.newArrayList(this.groupedMap.keySet());
	}

	/**
	 * 设置变量的正则<br>
	 * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
	 *
	 * @param regex 正则
	 * @return this
	 */
	public Setting setVarRegex(String regex) {
		if (null == this.settingLoader) {
			throw new NullPointerException("SettingLoader is null !");
		}
		this.settingLoader.setVarRegex(regex);
		return this;
	}

	/**
	 * 自定义字符编码
	 *
	 * @param charset 字符编码
	 * @return this
	 * @since 4.6.2
	 */
	public Setting setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	// ------------------------------------------------- Map interface with group

	/**
	 * 某个分组对应的键值对是否为空
	 *
	 * @param group 分组
	 * @return 是否为空
	 */
	public boolean isEmpty(String group) {
		return this.groupedMap.isEmpty(group);
	}

	/**
	 * 指定分组中是否包含指定key
	 *
	 * @param group 分组
	 * @param key   键
	 * @return 是否包含key
	 */
	public boolean containsKey(String group, String key) {
		return this.groupedMap.containsKey(group, key);
	}

	/**
	 * 指定分组中是否包含指定值
	 *
	 * @param group 分组
	 * @param value 值
	 * @return 是否包含值
	 */
	public boolean containsValue(String group, String value) {
		return this.groupedMap.containsValue(group, value);
	}

	/**
	 * 获取分组对应的值，如果分组不存在或者值不存在则返回null
	 *
	 * @param group 分组
	 * @param key   键
	 * @return 值，如果分组不存在或者值不存在则返回null
	 */
	public String get(String group, String key) {
		return this.groupedMap.get(group, key);
	}

	/**
	 * 将键值对加入到对应分组中
	 *
	 * @param group 分组
	 * @param key   键
	 * @param value 值
	 * @return 此key之前存在的值，如果没有返回null
	 */
	public String put(String group, String key, String value) {
		return this.groupedMap.put(group, key, value);
	}

	/**
	 * 从指定分组中删除指定值
	 *
	 * @param group 分组
	 * @param key   键
	 * @return 被删除的值，如果值不存在，返回null
	 */
	public String remove(String group, Object key) {
		return this.groupedMap.remove(group, Convert.toStr(key));
	}

	/**
	 * 加入多个键值对到某个分组下
	 *
	 * @param group 分组
	 * @param m     键值对
	 * @return this
	 */
	public Setting putAll(String group, Map<? extends String, ? extends String> m) {
		this.groupedMap.putAll(group, m);
		return this;
	}

	/**
	 * 添加一个Stting到主配置中
	 *
	 * @param setting Setting配置
	 * @return this
	 * @since 5.2.4
	 */
	public Setting addSetting(Setting setting) {
		for (Entry<String, LinkedHashMap<String, String>> e : setting.getGroupedMap().entrySet()) {
			this.putAll(e.getKey(), e.getValue());
		}
		return this;
	}

	/**
	 * 清除指定分组下的所有键值对
	 *
	 * @param group 分组
	 * @return this
	 */
	public Setting clear(String group) {
		this.groupedMap.clear(group);
		return this;
	}

	/**
	 * 指定分组所有键的Set
	 *
	 * @param group 分组
	 * @return 键Set
	 */
	public Set<String> keySet(String group) {
		return this.groupedMap.keySet(group);
	}

	/**
	 * 指定分组下所有值
	 *
	 * @param group 分组
	 * @return 值
	 */
	public Collection<String> values(String group) {
		return this.groupedMap.values(group);
	}

	/**
	 * 指定分组下所有键值对
	 *
	 * @param group 分组
	 * @return 键值对
	 */
	public Set<Entry<String, String>> entrySet(String group) {
		return this.groupedMap.entrySet(group);
	}

	/**
	 * 设置值
	 *
	 * @param key   键
	 * @param value 值
	 * @return this
	 * @since 3.3.1
	 */
	public Setting set(String key, String value) {
		this.put(key, value);
		return this;
	}

	/**
	 * 将键值对加入到对应分组中
	 *
	 * @param group 分组
	 * @param key   键
	 * @param value 值
	 * @return 此key之前存在的值，如果没有返回null
	 */
	public Setting set(String group, String key, String value) {
		this.put(group, key, value);
		return this;
	}

	// ------------------------------------------------- Override Map interface
	@Override
	public boolean isEmpty() {
		return this.groupedMap.isEmpty();
	}

	/**
	 * 默认分组（空分组）中是否包含指定key对应的值
	 *
	 * @param key 键
	 * @return 默认分组中是否包含指定key对应的值
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.groupedMap.containsKey(DEFAULT_GROUP, Convert.toStr(key));
	}

	/**
	 * 默认分组（空分组）中是否包含指定值
	 *
	 * @param value 值
	 * @return 默认分组中是否包含指定值
	 */
	@Override
	public boolean containsValue(Object value) {
		return this.groupedMap.containsValue(DEFAULT_GROUP, Convert.toStr(value));
	}

	/**
	 * 获取默认分组（空分组）中指定key对应的值
	 *
	 * @param key 键
	 * @return 默认分组（空分组）中指定key对应的值
	 */
	@Override
	public String get(Object key) {
		return this.groupedMap.get(DEFAULT_GROUP, Convert.toStr(key));
	}

	/**
	 * 将指定键值对加入到默认分组（空分组）中
	 *
	 * @param key   键
	 * @param value 值
	 * @return 加入的值
	 */
	@Override
	public String put(String key, String value) {
		return this.groupedMap.put(DEFAULT_GROUP, key, value);
	}

	/**
	 * 移除默认分组（空分组）中指定值
	 *
	 * @param key 键
	 * @return 移除的值
	 */
	@Override
	public String remove(Object key) {
		return this.groupedMap.remove(DEFAULT_GROUP, Convert.toStr(key));
	}

	/**
	 * 将键值对Map加入默认分组（空分组）中
	 *
	 * @param m Map
	 */
	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		this.groupedMap.putAll(DEFAULT_GROUP, m);
	}

	/**
	 * 清空默认分组（空分组）中的所有键值对
	 */
	@Override
	public void clear() {
		this.groupedMap.clear(DEFAULT_GROUP);
	}

	/**
	 * 获取默认分组（空分组）中的所有键列表
	 *
	 * @return 默认分组（空分组）中的所有键列表
	 */
	@Override
	public Set<String> keySet() {
		return this.groupedMap.keySet(DEFAULT_GROUP);
	}

	/**
	 * 获取默认分组（空分组）中的所有值列表
	 *
	 * @return 默认分组（空分组）中的所有值列表
	 */
	@Override
	public Collection<String> values() {
		return this.groupedMap.values(DEFAULT_GROUP);
	}

	/**
	 * 获取默认分组（空分组）中的所有键值对列表
	 *
	 * @return 默认分组（空分组）中的所有键值对列表
	 */
	@Override
	public Set<Entry<String, String>> entrySet() {
		return this.groupedMap.entrySet(DEFAULT_GROUP);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((charset == null) ? 0 : charset.hashCode());
		result = prime * result + groupedMap.hashCode();
		result = prime * result + (isUseVariable ? 1231 : 1237);
		result = prime * result + ((settingUrl == null) ? 0 : settingUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Setting other = (Setting) obj;
		if (charset == null) {
			if (other.charset != null) {
				return false;
			}
		} else if (false == charset.equals(other.charset)) {
			return false;
		}
		if (false == groupedMap.equals(other.groupedMap)) {
			return false;
		}
		if (isUseVariable != other.isUseVariable) {
			return false;
		}
		if (settingUrl == null) {
			return other.settingUrl == null;
		} else {
			return settingUrl.equals(other.settingUrl);
		}
	}

	@Override
	public String toString() {
		return groupedMap.toString();
	}
}
