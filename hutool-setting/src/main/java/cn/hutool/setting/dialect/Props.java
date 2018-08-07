package cn.hutool.setting.dialect;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.Date;
import java.util.Properties;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.getter.BasicTypeGetter;
import cn.hutool.core.getter.OptBasicTypeGetter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.io.resource.UrlResource;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.SettingRuntimeException;

/**
 * Properties文件读取封装类
 * 
 * @author loolly
 */
public final class Props extends Properties implements BasicTypeGetter<String>, OptBasicTypeGetter<String> {
	private static final long serialVersionUID = 1935981579709590740L;
	private final static Log log = LogFactory.get();

	// ----------------------------------------------------------------------- 私有属性 start
	/** 属性文件的URL */
	private URL propertiesFileUrl;
	private WatchMonitor watchMonitor;
	/** properties文件编码 */
	private Charset charset = CharsetUtil.CHARSET_ISO_8859_1;
	// ----------------------------------------------------------------------- 私有属性 end

	/**
	 * 获得Classpath下的Properties文件
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return Properties
	 */
	public static Properties getProp(String resource) {
		return new Props(resource);
	}

	/**
	 * 获得Classpath下的Properties文件
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @param charsetName 字符集
	 * @return Properties
	 */
	public static Properties getProp(String resource, String charsetName) {
		return new Props(resource, charsetName);
	}

	/**
	 * 获得Classpath下的Properties文件
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @param charset 字符集
	 * @return Properties
	 */
	public static Properties getProp(String resource, Charset charset) {
		return new Props(resource, charset);
	}

	// ----------------------------------------------------------------------- 构造方法 start
	/**
	 * 构造
	 */
	public Props() {
		super();
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param path
	 */
	public Props(String path) {
		this(path, CharsetUtil.CHARSET_ISO_8859_1);
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param path 相对或绝对路径
	 * @param charsetName 字符集
	 */
	public Props(String path, String charsetName) {
		this(path, CharsetUtil.charset(charsetName));
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param path 相对或绝对路径
	 * @param charset 字符集
	 */
	public Props(String path, Charset charset) {
		Assert.notBlank(path, "Blank properties file path !");
		if(null != charset) {
			this.charset = charset;
		}
		this.load(ResourceUtil.getResourceObj(path));
	}

	/**
	 * 构造
	 * 
	 * @param propertiesFile 配置文件对象
	 */
	public Props(File propertiesFile) {
		this(propertiesFile, StandardCharsets.ISO_8859_1);
	}

	/**
	 * 构造
	 * 
	 * @param propertiesFile 配置文件对象
	 * @param charsetName 字符集
	 */
	public Props(File propertiesFile, String charsetName) {
		this(propertiesFile, Charset.forName(charsetName));
	}

	/**
	 * 构造
	 * 
	 * @param propertiesFile 配置文件对象
	 * @param charset 字符集
	 */
	public Props(File propertiesFile, Charset charset) {
		Assert.notNull(propertiesFile, "Null properties file!");
		this.charset = charset;
		this.load(new FileResource(propertiesFile));
	}

	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 */
	public Props(String path, Class<?> clazz) {
		this(path, clazz, CharsetUtil.ISO_8859_1);
	}

	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 * @param charsetName 字符集
	 */
	public Props(String path, Class<?> clazz, String charsetName) {
		this(path, clazz, CharsetUtil.charset(charsetName));
	}

	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 * @param charset 字符集
	 */
	public Props(String path, Class<?> clazz, Charset charset) {
		Assert.notBlank(path, "Blank properties file path !");
		if(null != charset) {
			this.charset = charset;
		}
		this.load(new ClassPathResource(path, clazz));
	}

	/**
	 * 构造，使用URL读取
	 * 
	 * @param propertiesUrl 属性文件路径
	 */
	public Props(URL propertiesUrl) {
		this(propertiesUrl, StandardCharsets.ISO_8859_1);
	}

	/**
	 * 构造，使用URL读取
	 * 
	 * @param propertiesUrl 属性文件路径
	 * @param charsetName 字符集
	 */
	public Props(URL propertiesUrl, String charsetName) {
		this(propertiesUrl, CharsetUtil.charset(charsetName));
	}

	/**
	 * 构造，使用URL读取
	 * 
	 * @param propertiesUrl 属性文件路径
	 * @param charset 字符集
	 */
	public Props(URL propertiesUrl, Charset charset) {
		Assert.notNull(propertiesUrl, "Null properties URL !");
		if(null != charset) {
			this.charset = charset;
		}
		this.load(new UrlResource(propertiesUrl));
	}

	/**
	 * 构造，使用URL读取
	 * 
	 * @param properties 属性文件路径
	 */
	public Props(Properties properties) {
		if (CollectionUtil.isNotEmpty(properties)) {
			this.putAll(properties);
		}
	}

	// ----------------------------------------------------------------------- 构造方法 end

	/**
	 * 初始化配置文件
	 * 
	 * @param urlResource {@link UrlResource}
	 */
	public void load(Resource urlResource) {
		this.propertiesFileUrl = urlResource.getUrl();
		if (null == this.propertiesFileUrl) {
			throw new SettingRuntimeException("Can not find properties file: [{}]", urlResource);
		}
		log.debug("Load properties [{}]", propertiesFileUrl.getPath());
		try (final BufferedReader reader = urlResource.getReader(charset)) {
			super.load(reader);
		} catch (Exception e) {
			log.error(e, "Load properties error!");
		}
	}

	/**
	 * 重新加载配置文件
	 */
	public void load() {
		this.load(new UrlResource(this.propertiesFileUrl));
	}

	/**
	 * 在配置文件变更时自动加载
	 * 
	 * @param autoReload 是否自动加载
	 */
	public void autoLoad(boolean autoReload) {
		if (autoReload) {
			if (null != this.watchMonitor) {
				this.watchMonitor.close();
			}
			try {
				watchMonitor = WatchMonitor.create(Paths.get(this.propertiesFileUrl.toURI()));
				watchMonitor.setWatcher(new SimpleWatcher(){
					@Override
					public void onModify(WatchEvent<?> event, Path currentPath) {
						load();
					}
				}).start();
			} catch (Exception e) {
				throw new SettingRuntimeException(e, "Setting auto load not support url: [{}]", this.propertiesFileUrl);
			}
		} else {
			IoUtil.close(this.watchMonitor);
			this.watchMonitor = null;
		}
	}

	// ----------------------------------------------------------------------- Get start
	@Override
	public Object getObj(String key, Object defaultValue) {
		return getStr(key, null == defaultValue ? null : defaultValue.toString());
	}

	@Override
	public Object getObj(String key) {
		return getObj(key, null);
	}

	@Override
	public String getStr(String key, String defaultValue) {
		return super.getProperty(key, defaultValue);
	}

	@Override
	public String getStr(String key) {
		return super.getProperty(key);
	}

	@Override
	public Integer getInt(String key, Integer defaultValue) {
		return Convert.toInt(getStr(key), defaultValue);
	}

	@Override
	public Integer getInt(String key) {
		return getInt(key, null);
	}

	@Override
	public Boolean getBool(String key, Boolean defaultValue) {
		return Convert.toBool(getStr(key), defaultValue);
	}

	@Override
	public Boolean getBool(String key) {
		return getBool(key, null);
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		return Convert.toLong(getStr(key), defaultValue);
	}

	@Override
	public Long getLong(String key) {
		return getLong(key, null);
	}

	@Override
	public Character getChar(String key, Character defaultValue) {
		final String value = getStr(key);
		if (StrUtil.isBlank(value)) {
			return defaultValue;
		}
		return value.charAt(0);
	}

	@Override
	public Character getChar(String key) {
		return getChar(key, null);
	}

	@Override
	public Float getFloat(String key) {
		return getFloat(key, null);
	}

	@Override
	public Float getFloat(String key, Float defaultValue) {
		return Convert.toFloat(getStr(key), defaultValue);
	}

	@Override
	public Double getDouble(String key, Double defaultValue) throws NumberFormatException {
		return Convert.toDouble(getStr(key), defaultValue);
	}

	@Override
	public Double getDouble(String key) throws NumberFormatException {
		return getDouble(key, null);
	}

	@Override
	public Short getShort(String key, Short defaultValue) {
		return Convert.toShort(getStr(key), defaultValue);
	}

	@Override
	public Short getShort(String key) {
		return getShort(key, null);
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		return Convert.toByte(getStr(key), defaultValue);
	}

	@Override
	public Byte getByte(String key) {
		return getByte(key, null);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		final String valueStr = getStr(key);
		if (StrUtil.isBlank(valueStr)) {
			return defaultValue;
		}

		try {
			return new BigDecimal(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return getBigDecimal(key, null);
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		final String valueStr = getStr(key);
		if (StrUtil.isBlank(valueStr)) {
			return defaultValue;
		}

		try {
			return new BigInteger(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public BigInteger getBigInteger(String key) {
		return getBigInteger(key, null);
	}

	@Override
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key, E defaultValue) {
		return Convert.toEnum(clazz, getStr(key), defaultValue);
	}

	@Override
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) {
		return getEnum(clazz, key, null);
	}
	
	@Override
	public Date getDate(String key, Date defaultValue) {
		return Convert.toDate(getStr(key), defaultValue);
	}
	
	@Override
	public Date getDate(String key) {
		return getDate(key, null);
	}

	// ----------------------------------------------------------------------- Get end

	// ----------------------------------------------------------------------- Set start
	/**
	 * 设置值，无给定键创建之。设置后未持久化
	 * 
	 * @param key 属性键
	 * @param value 属性值
	 */
	public void setProperty(String key, Object value) {
		super.setProperty(key, value.toString());
	}

	/**
	 * 持久化当前设置，会覆盖掉之前的设置
	 * 
	 * @param absolutePath 设置文件的绝对路径
	 * @throws IORuntimeException IO异常，可能为文件未找到
	 */
	public void store(String absolutePath) throws IORuntimeException{
		Writer writer = null;
		try {
			writer = FileUtil.getWriter(absolutePath, charset, false);
			super.store(writer, null);
		} catch (IOException e) {
			throw new IORuntimeException(e, "Store properties to [{}] error!", absolutePath);
		} finally {
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
	// ----------------------------------------------------------------------- Set end
}
