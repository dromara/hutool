package cn.hutool.setting.dialect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.io.resource.UrlResource;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.LambdaInfo;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.lang.func.SerFunction;
import cn.hutool.core.lang.func.SerSupplier;
import cn.hutool.core.lang.getter.TypeGetter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.log.StaticLog;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Arrays;
import java.util.Properties;

/**
 * Properties文件读取封装类
 *
 * @author loolly
 */
public final class Props extends Properties implements TypeGetter<CharSequence> {
	private static final long serialVersionUID = 1935981579709590740L;

	/**
	 * 默认配置文件扩展名
	 */
	public final static String EXT_NAME = "properties";

	/**
	 * 构建一个空的Props，用于手动加入参数
	 *
	 * @return Setting
	 * @since 5.4.3
	 */
	public static Props of() {
		return new Props();
	}

	// ----------------------------------------------------------------------- 私有属性 start
	/**
	 * 属性文件的Resource
	 */
	private Resource resource;
	private WatchMonitor watchMonitor;
	/**
	 * properties文件编码<br>
	 * issue#1701，此属性不能被序列化，故忽略序列化
	 */
	private transient Charset charset = CharsetUtil.ISO_8859_1;
	// ----------------------------------------------------------------------- 私有属性 end

	/**
	 * 获得Classpath下的Properties文件
	 *
	 * @param resource 资源（相对Classpath的路径）
	 * @return Props
	 */
	public static Props of(final String resource) {
		return new Props(resource);
	}

	/**
	 * 获得Classpath下的Properties文件
	 *
	 * @param resource 资源（相对Classpath的路径）
	 * @param charset  自定义编码
	 * @return Properties
	 */
	public static Props of(final String resource, final Charset charset) {
		return new Props(resource, charset);
	}

	// ----------------------------------------------------------------------- 构造方法 start

	/**
	 * 构造
	 */
	public Props() {
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 *
	 * @param path 配置文件路径，相对于ClassPath，或者使用绝对路径
	 */
	public Props(final String path) {
		this(path, null);
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 *
	 * @param path    相对或绝对路径
	 * @param charset 自定义编码
	 */
	public Props(final String path, final Charset charset) {
		Assert.notBlank(path, "Blank properties file path !");
		if (null != charset) {
			this.charset = charset;
		}
		this.load(ResourceUtil.getResource(path));
	}

	/**
	 * 构造
	 *
	 * @param propertiesFile 配置文件对象
	 */
	public Props(final File propertiesFile) {
		this(propertiesFile, null);
	}

	/**
	 * 构造
	 *
	 * @param propertiesFile 配置文件对象
	 * @param charset        自定义编码
	 */
	public Props(final File propertiesFile, final Charset charset) {
		Assert.notNull(propertiesFile, "Null properties file!");
		if (null != charset) {
			this.charset = charset;
		}
		this.load(ResourceUtil.getResource(propertiesFile));
	}

	/**
	 * 构造，使用URL读取
	 *
	 * @param resource {@link Resource}
	 * @param charset  自定义编码
	 */
	public Props(final Resource resource, final Charset charset) {
		Assert.notNull(resource, "Null properties URL !");
		if (null != charset) {
			this.charset = charset;
		}
		this.load(resource);
	}

	/**
	 * 构造，使用URL读取
	 *
	 * @param properties 属性文件路径
	 */
	public Props(final Properties properties) {
		if (MapUtil.isNotEmpty(properties)) {
			this.putAll(properties);
		}
	}

	// ----------------------------------------------------------------------- 构造方法 end

	/**
	 * 初始化配置文件
	 *
	 * @param url {@link URL}
	 * @since 5.5.2
	 */
	public void load(final URL url) {
		load(new UrlResource(url));
	}

	/**
	 * 初始化配置文件
	 *
	 * @param resource {@link Resource}
	 */
	public void load(final Resource resource) {
		Assert.notNull(resource, "Props resource must be not null!");
		this.resource = resource;
		PropsLoaderUtil.loadTo(this, resource, this.charset);
	}

	/**
	 * 重新加载配置文件
	 */
	public void load() {
		this.load(this.resource);
	}

	/**
	 * 在配置文件变更时自动加载
	 *
	 * @param autoReload 是否自动加载
	 */
	public void autoLoad(final boolean autoReload) {
		if (autoReload) {
			Assert.notNull(this.resource, "Properties resource must be not null!");
			if (null != this.watchMonitor) {
				// 先关闭之前的监听
				this.watchMonitor.close();
			}
			this.watchMonitor = WatchUtil.createModify(this.resource.getUrl(), new SimpleWatcher() {
				@Override
				public void onModify(final WatchEvent<?> event, final Path currentPath) {
					load();
				}
			});
			this.watchMonitor.start();
		} else {
			IoUtil.close(this.watchMonitor);
			this.watchMonitor = null;
		}
	}

	@Override
	public Object getObj(final CharSequence key, final Object defaultValue) {
		return ObjUtil.defaultIfNull(getProperty(StrUtil.str(key)), defaultValue);
	}

	/**
	 * 根据lambda的方法引用，获取
	 *
	 * @param func 方法引用
	 * @param <P>  参数类型
	 * @param <T>  返回值类型
	 * @return 获取表达式对应属性和返回的对象
	 */
	public <P, T> T get(final SerFunction<P, T> func) {
		final LambdaInfo lambdaInfo = LambdaUtil.resolve(func);
		return get(lambdaInfo.getFieldName(), lambdaInfo.getReturnType());
	}

	/**
	 * 获取并删除键值对，当指定键对应值非空时，返回并删除这个值，后边的键对应的值不再查找
	 *
	 * @param keys 键列表，常用于别名
	 * @return 字符串值
	 * @since 4.1.21
	 */
	public String getAndRemoveStr(final String... keys) {
		Object value = null;
		for (final String key : keys) {
			value = remove(key);
			if (null != value) {
				break;
			}
		}
		return (String) value;
	}

	/**
	 * 转换为标准的{@link Properties}对象
	 *
	 * @return {@link Properties}对象
	 * @since 5.7.4
	 */
	public Properties toProperties() {
		final Properties properties = new Properties();
		properties.putAll(this);
		return properties;
	}

	/**
	 * 将配置文件转换为Bean，支持嵌套Bean<br>
	 * 支持的表达式：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * ['person']['friends'][5]['name']
	 * </pre>
	 *
	 * @param <T>       Bean类型
	 * @param beanClass Bean类
	 * @return Bean对象
	 * @since 4.6.3
	 */
	public <T> T toBean(final Class<T> beanClass) {
		return toBean(beanClass, null);
	}

	/**
	 * 将配置文件转换为Bean，支持嵌套Bean<br>
	 * 支持的表达式：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * ['person']['friends'][5]['name']
	 * </pre>
	 *
	 * @param <T>       Bean类型
	 * @param beanClass Bean类
	 * @param prefix    公共前缀，不指定前缀传null，当指定前缀后非此前缀的属性被忽略
	 * @return Bean对象
	 * @since 4.6.3
	 */
	public <T> T toBean(final Class<T> beanClass, final String prefix) {
		final T bean = ConstructorUtil.newInstanceIfPossible(beanClass);
		return fillBean(bean, prefix);
	}

	/**
	 * 将配置文件转换为Bean，支持嵌套Bean<br>
	 * 支持的表达式：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * ['person']['friends'][5]['name']
	 * </pre>
	 *
	 * @param <T>    Bean类型
	 * @param bean   Bean对象
	 * @param prefix 公共前缀，不指定前缀传null，当指定前缀后非此前缀的属性被忽略
	 * @return Bean对象
	 * @since 4.6.3
	 */
	public <T> T fillBean(final T bean, String prefix) {
		prefix = StrUtil.emptyIfNull(StrUtil.addSuffixIfNot(prefix, StrUtil.DOT));

		String key;
		for (final java.util.Map.Entry<Object, Object> entry : this.entrySet()) {
			key = (String) entry.getKey();
			if (false == StrUtil.startWith(key, prefix)) {
				// 非指定开头的属性忽略掉
				continue;
			}
			try {
				BeanUtil.setProperty(bean, StrUtil.subSuf(key, prefix.length()), entry.getValue());
			} catch (final Exception e) {
				// 忽略注入失败的字段（这些字段可能用于其它配置）
				StaticLog.debug("Ignore property: [{}]", key);
			}
		}

		return bean;
	}

	// ----------------------------------------------------------------------- Get end

	// ----------------------------------------------------------------------- Set start

	/**
	 * 设置值，无给定键创建之。设置后未持久化
	 *
	 * @param key   属性键
	 * @param value 属性值
	 */
	public void set(final String key, final Object value) {
		super.setProperty(key, value.toString());
	}

	/**
	 * 通过lambda批量设置值<br>
	 * 实际使用时，可以使用getXXX的方法引用来完成键值对的赋值：
	 * <pre>
	 *     User user = GenericBuilder.of(User::new).with(User::setUsername, "hutool").build();
	 *     Setting.of().setFields(user::getNickname, user::getUsername);
	 * </pre>
	 *
	 * @param fields lambda,不能为空
	 * @return this
	 */
	public Props setFields(final SerSupplier<?>... fields) {
		Arrays.stream(fields).forEach(f -> set(LambdaUtil.getFieldName(f), f.get()));
		return this;
	}

	/**
	 * 持久化当前设置，会覆盖掉之前的设置
	 *
	 * @param absolutePath 设置文件的绝对路径
	 * @throws IORuntimeException IO异常，可能为文件未找到
	 */
	public void store(final String absolutePath) throws IORuntimeException {
		Writer writer = null;
		try {
			writer = FileUtil.getWriter(absolutePath, charset, false);
			super.store(writer, null);
		} catch (final IOException e) {
			throw new IORuntimeException(e, "Store properties to [{}] error!", absolutePath);
		} finally {
			IoUtil.close(writer);
		}
	}

	/**
	 * 存储当前设置，会覆盖掉以前的设置
	 *
	 * @param path  相对路径
	 * @param clazz 相对的类
	 */
	public void store(final String path, final Class<?> clazz) {
		this.store(FileUtil.getAbsolutePath(path, clazz));
	}
	// ----------------------------------------------------------------------- Set end
}
