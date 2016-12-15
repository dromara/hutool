package com.xiaoleilu.hutool.setting;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.setting.dialect.BasicSetting;
import com.xiaoleilu.hutool.util.StrUtil;

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
public class Setting extends BasicSetting {
	private final static Log log = StaticLog.get();
	
	/** 数组类型值默认分隔符 */
	public final static String DEFAULT_DELIMITER= ",";
	
	/**
	 * 基本构造<br/>
	 * 需自定义初始化配置文件<br/>
	 */
	public Setting() {
		super();
	}
	
	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(String pathBaseClassLoader, Charset charset, boolean isUseVariable) {
		super(pathBaseClassLoader, charset, isUseVariable);
	}
	
	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(String pathBaseClassLoader, boolean isUseVariable) {
		super(pathBaseClassLoader, DEFAULT_CHARSET, isUseVariable);
	}

	/**
	 * 构造
	 * 
	 * @param configFile 配置文件对象
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(File configFile, Charset charset, boolean isUseVariable) {
		super(configFile, charset, isUseVariable);
	}

	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(String path, Class<?> clazz, Charset charset, boolean isUseVariable) {
		super(path, clazz, charset, isUseVariable);
	}

	/**
	 * 构造
	 * 
	 * @param url 设定文件的URL
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public Setting(URL url, Charset charset, boolean isUseVariable) {
		super(url, charset, isUseVariable);
	}
	
	/**
	 * 构造
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public Setting(String pathBaseClassLoader) {
		super(pathBaseClassLoader);
	}

	/*--------------------------公有方法 start-------------------------------*/
	
	//--------------------------------------------------------------- Get
	/**
	 * 带有日志提示的get，如果没有定义指定的KEY，则打印debug日志
	 * 
	 * @param key 键
	 * @return 值
	 */
	public String getWithLog(String key) {
		final String value = getStr(key);
		if (value == null) {
			log.debug("No key define for [{}]!", key);
		}
		return value;
	}
	
	/**
	 * 带有日志提示的get，如果没有定义指定的KEY，则打印debug日志
	 * 
	 * @param key 键
	 * @return 值
	 */
	public String getByGroupWithLog(String key, String group) {
		final String value = getByGroup(key, group);
		if (value == null) {
			log.debug("No key define for [{}] of group [{}] !", key, group);
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
		final String value = getByGroup(key, group);
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
	 * @param group 分组名
	 * @return 属性值
	 */
	public Integer getInt(String key, String group) {
		return getInt(key, group, null);
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
		return Convert.toInt(getByGroup(key, group), defaultValue);
	}

	//--------------------------------------------------------------- Get bool
	/**
	 * 获取波尔型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Boolean getBool(String key, String group) {
		return getBool(key, group, null);
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
		return Convert.toBool(getByGroup(key, group), defaultValue);
	}

	//--------------------------------------------------------------- Get long
	/**
	 * 获取long类型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Long getLong(String key, String group) {
		return getLong(key, group, null);
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
		return Convert.toLong(getByGroup(key, group), defaultValue);
	}
	//--------------------------------------------------------------- Get char
	/**
	 * 获取char类型属性值
	 * 
	 * @param key 属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Character getChar(String key, String group) {
		final String value = getByGroup(key, group);
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
	 * @param group 分组名
	 * @return 属性值
	 */
	public Double getDouble(String key, String group) {
		return getDouble(key, group, null);
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
		return Convert.toDouble(getByGroup(key, group), defaultValue);
	}
	/*--------------------------公有方法 end-------------------------------*/
}
