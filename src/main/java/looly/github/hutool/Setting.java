package looly.github.hutool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import looly.github.hutool.exceptions.SettingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设置工具类。 用于支持设置文件<br>
 *  1、支持变量，默认变量命名为 ${变量名}，变量只能识别读入行的变量，例如第6行的变量在第三行无法读取
 *  2、支持分组，分组为中括号括起来的内容，中括号以下的行都为此分组的内容，无分组相当于空字符分组<br>
 *  		若某个key是name，加上分组后的键相当于group.name
 *  3、注释以#开头，但是空行和不带“=”的行也会被跳过，但是建议加#
 *  4、store方法不会保存注释内容，慎重使用
 * @author xiaoleilu
 * 
 */
public class Setting extends HashMap<String, String> {
	private static final long serialVersionUID = -477527787843971824L;
	private static Logger log = LoggerFactory.getLogger(Setting.class);

	/** 默认字符集 */
	public final static String DEFAULT_CHARSET = "utf8";
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

	/** 本设置对象的字符集 */
	private Charset charset;
	/** 是否使用变量 */
	private boolean isUseVariable;
	/** 设定文件的URL */
	private URL settingUrl;
	
	private LinkedList<String> groups = new LinkedList<String>();
	/**
	 * 基本构造<br/>
	 * 需自定义初始化配置文件<br/>
	 * 
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(Charset charset, boolean isUseVariable) {
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
	public Setting(String pathBaseClassLoader, String charset, boolean isUseVariable) {
		URL url = URLUtil.getURL(pathBaseClassLoader);
		if(url == null) {
			throw new RuntimeException("Can not find Setting file: ["+pathBaseClassLoader+"]");
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
	public Setting(File configFile, String charset, boolean isUseVariable) {
		if (configFile == null) {
			throw new RuntimeException("Null Setting file!");
		}
		URL url = URLUtil.getURL(configFile);
		if(url == null) {
			throw new RuntimeException("Can not find Setting file: ["+configFile.getAbsolutePath()+"]");
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
	public Setting(String path, Class<?> clazz, String charset, boolean isUseVariable) {
		URL url = URLUtil.getURL(path, clazz);
		if(url == null) {
			throw new RuntimeException("Can not find Setting file: ["+path+"]");
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
	public Setting(URL url, String charset, boolean isUseVariable) {
		if(url == null) {
			throw new RuntimeException("Null url define!");
		}
		this.init(url, charset, isUseVariable);
	}
	
	/**
	 * 构造
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public Setting(String pathBaseClassLoader) {
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
	public boolean init(URL settingUrl, String charset, boolean isUseVariable) {
		if (settingUrl == null) {
			throw new RuntimeException("Null setting url or charset define!");
		}
		try {
			this.charset = Charset.forName(charset);
		} catch (Exception e) {
			log.warn("User custom charset [{}] parse error, use default charset: [{}]", charset, DEFAULT_CHARSET);
			this.charset = Charset.forName(DEFAULT_CHARSET);
		}
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
			log.error("Load setting error!", e);
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
		this.clear();
		BufferedReader reader = new BufferedReader(new InputStreamReader(settingStream, charset));
		
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
			if (!StrUtil.isBlank(group)) {
				key = group + "." + key;
			}
			String value = keyValue[1].trim();

			// 替换值中的所有变量变量（变量必须是此行之前定义的变量，否则无法找到）
			if (isUseVariable) {
				// 找到所有变量标识
				Set<String> vars = ReUtil.findAll(reg_var, value, 0, new HashSet<String>());
				for (String var : vars) {
					// 查找变量名对应的值
					String varValue = this.get(ReUtil.get(reg_var, var, 1));
					if (varValue != null) {
						// 替换标识
						value = value.replace(var, varValue);
					}
				}
			}
			put(key, value);
		}
		FileUtil.close(reader);
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

	//--------------------------------------------------------------- Get
	/**
	 * 带有日志提示的get，如果没有定义指定的KEY，则打印debug日志
	 * 
	 * @param key 键
	 * @return 值
	 */
	public String getWithLog(String key) {
		final String value = super.get(key);
		if (value == null) {
			log.debug("No key define for [{}]!", key);
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
	public String get(String key, String group) {
		String keyWithGroup = key;
		if (!StrUtil.isBlank(group)) {
			keyWithGroup = group + "." + keyWithGroup;
		}
		return get(keyWithGroup);
	}

	//--------------------------------------------------------------- Get String
	/**
	 * 获取字符型型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	public String getString(String key) {
		return getString(key, null);
	}
	
	/**
	 * 获取字符型型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public String getStringWithDefault(String key, String defaultValue) {
		return getStringWithDefault(key, null, defaultValue);
	}

	/**
	 * 获取字符型型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public String getString(String key, String group) {
		return get(key, group);
	}
	
	/**
	 * 获取字符型型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public String getStringWithDefault(String key, String group, String defaultValue) {
		final String value = getString(key, group);
		if(StrUtil.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	//--------------------------------------------------------------- Get string array
	/**
	 * 获得数组型
	 * @param key 属性名
	 * @return 属性值
	 */
	public String[] getStrings(String key) {
		return getStrings(key, null);
	}
	
	/**
	 * 获得数组型
	 * @param key 属性名
	 * @param defaultValue 默认的值
	 * @return 属性值
	 */
	public String[] getStringsWithDefault(String key, String[] defaultValue) {
		String[] value = getStrings(key, null);
		if(null == value) {
			value = defaultValue; 
		}
		
		return value;
	}
	
	/**
	 * 获得数组型
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public String[] getStrings(String key, String group) {
		return getStrings(key, group, DEFAULT_DELIMITER);
	}
	
	/**
	 * 获得数组型
	 * @param key 属性名
	 * @param group 分组名
	 * @param delimiter 分隔符
	 * @return 属性值
	 */
	public String[] getStrings(String key, String group, String delimiter) {
		final String value = getString(key, group);
		if(StrUtil.isBlank(value)) {
			return null;
		}
		return StrUtil.split(value, delimiter);
	}
	
	//--------------------------------------------------------------- Get int
	/**
	 * 获取数字型型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	public Integer getInt(String key) throws NumberFormatException {
		return getInt(key, null);
	}
	
	/**
	 * 获取数字型型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Integer getInt(String key, String group) throws NumberFormatException {
		final String value = get(key, group);
		if(StrUtil.isBlank(value)) {
			return null;
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * 获取数字型型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Integer getInt(String key, String group, Integer defaultValue) {
		return Conver.toInt(get(key, group), defaultValue);
	}

	//--------------------------------------------------------------- Get bool
	/**
	 * 获取波尔型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	public Boolean getBool(String key) throws NumberFormatException {
		return getBool(key, null);
	}

	/**
	 * 获取波尔型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Boolean getBool(String key, String group) throws NumberFormatException {
		final String value = get(key, group);
		if(StrUtil.isBlank(value)) {
			return null;
		}
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * 获取波尔型型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Boolean getBool(String key, String group, Boolean defaultValue) {
		return Conver.toBool(get(key, group), defaultValue);
	}

	//--------------------------------------------------------------- Get long
	/**
	 * 获取long类型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	public Long getLong(String key) throws NumberFormatException {
		return getLong(key, null);
	}

	/**
	 * 获取long类型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Long getLong(String key, String group) throws NumberFormatException {
		final String value = get(key, group);
		if(StrUtil.isBlank(value)) {
			return null;
		}
		return Long.parseLong(value);
	}
	
	/**
	 * 获取long类型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Long getLong(String key, String group, Long defaultValue) {
		return Conver.toLong(get(key, group), defaultValue);
	}
	//--------------------------------------------------------------- Get char
	/**
	 * 获取char类型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	public Character getChar(String key) {
		return getChar(key, null);
	}

	/**
	 * 获取char类型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Character getChar(String key, String group) {
		final String value = get(key, group);
		if(StrUtil.isBlank(value)) {
			return null;
		}
		return value.charAt(0);
	}

	//--------------------------------------------------------------- Get double
	/**
	 * 获取double类型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	public Double getDouble(String key) throws NumberFormatException {
		return getDouble(key, null);
	}

	/**
	 * 获取double类型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Double getDouble(String key, String group) throws NumberFormatException {
		final String value = get(key, group);
		if(StrUtil.isBlank(value)) {
			return null;
		}
		return Double.parseDouble(value);
	}
	
	/**
	 * 获取double类型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Double getDouble(String key, String group, Double defaultValue) {
		return Conver.toDouble(get(key, group), defaultValue);
	}

	//--------------------------------------------------------------- Set
	/**
	 * 设置值，无给定键创建之。设置后未持久化
	 * 
	 * @param key 键
	 * @param value 值
	 */
	public void setSetting(String key, Object value) {
		put(key, value.toString());
	}

	//--------------------------------------------------------------------------------- Functions
	/**
	 * 持久化当前设置，会覆盖掉之前的设置<br>
	 * 持久化会不会保留之前的分组
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath) {
		try {
			FileUtil.touch(absolutePath);
			OutputStream out = FileUtil.getOutputStream(absolutePath);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, charset));
			Set<java.util.Map.Entry<String, String>> entrySet = this.entrySet();
			for (java.util.Map.Entry<String, String> entry : entrySet) {
				writer.write(entry.getKey() + ASSIGN_FLAG + entry.getValue());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(StrUtil.format("Can not find file [{}]!", absolutePath), e);
		} catch (IOException e) {
			throw new RuntimeException("Store Setting error!", e);
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
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br/>
	 * 只支持基本类型的转换
	 * 
	 * @param object 被调用的对象
	 * @throws SettingException
	 */
	public void toObject(String group, Object object) throws SettingException {
		try {
			Method[] methods = object.getClass().getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("set")) {
					String field = StrUtil.getGeneralField(methodName);
					Object value = get(field, group);
					if (value != null) {
						Class<?>[] parameterTypes = method.getParameterTypes();
						if(parameterTypes.length != 1) {
							continue;
						}
						Object castedValue = ClassUtil.parse(parameterTypes[0], value);
						method.invoke(object, castedValue);
						log.debug("Parse setting to object field [{}={}]", field, value);
					}
				}
			}
		} catch (Exception e) {
			throw new SettingException("Parse setting to object error!", e);
		}
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br/>
	 * 只支持基本类型的转换
	 * 
	 * @param object
	 * @throws SettingException
	 */
	public void toObject(Object object) throws SettingException {
		toObject(null, object);
	}
	
	/**
	 * @return 获得所有分组名
	 */
	public LinkedList<String> getGroups() {
		return this.groups;
	}
	/*--------------------------公有方法 end-------------------------------*/
}
