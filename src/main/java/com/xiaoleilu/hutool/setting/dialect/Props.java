package com.xiaoleilu.hutool.setting.dialect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Properties;

import com.xiaoleilu.hutool.getter.BasicTypeGetter;
import com.xiaoleilu.hutool.getter.OptBasicTypeGetter;
import com.xiaoleilu.hutool.lang.Conver;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.util.FileUtil;
import com.xiaoleilu.hutool.util.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.util.URLUtil;

/**
 * Properties文件读取封装类
 * @author loolly
 */
public final class Props extends Properties implements BasicTypeGetter<String>, OptBasicTypeGetter<String>{
	private static final long serialVersionUID = 1935981579709590740L;
	private final static Log log = StaticLog.get();
	
	//----------------------------------------------------------------------- 私有属性 start
	/** 属性文件的URL */
	private URL propertiesFileUrl;
	//----------------------------------------------------------------------- 私有属性 end
	
	//----------------------------------------------------------------------- 构造方法 start
	
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
	 * 构造，使用相对于Class文件根目录的相对路径
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public Props(String pathBaseClassLoader){
		if(null == pathBaseClassLoader) {
			pathBaseClassLoader = StrUtil.EMPTY;
		}
		
		final URL url = URLUtil.getURL(pathBaseClassLoader);
		if(url == null) {
			throw new RuntimeException(StrUtil.format("Can not find properties file: [{}]", pathBaseClassLoader));
		}
		this.load(url);
	}
	
	/**
	 * 构造
	 * @param propertiesFile 配置文件对象
	 */
	public Props(File propertiesFile){
		if (propertiesFile == null) {
			throw new RuntimeException("Null properties file!");
		}
		final URL url = URLUtil.getURL(propertiesFile);
		if(url == null) {
			throw new RuntimeException(StrUtil.format("Can not find Setting file: [{}]", propertiesFile.getAbsolutePath()));
		}
		this.load(url);
	}
	
	/**
	 * 构造，相对于classes读取文件
	 * @param path 相对路径
	 * @param clazz 基准类
	 */
	public Props(String path, Class<?> clazz){
		final URL url = URLUtil.getURL(path, clazz);
		if(url == null) {
			throw new RuntimeException(StrUtil.format("Can not find Setting file: [{}]", path));
		}
		this.load(url);
	}
	
	/**
	 * 构造，使用URL读取
	 * @param propertiesUrl 属性文件路径
	 */
	public Props(URL propertiesUrl){
		this.load(propertiesUrl);
	}
	//----------------------------------------------------------------------- 构造方法 end
	
	/**
	 * 初始化配置文件
	 * @param propertiesFileUrl 配置文件URL
	 */
	public  void load(URL propertiesFileUrl){
		if(propertiesFileUrl == null){
			throw new RuntimeException("Null properties file url define!");
		}
		log.debug("Load properties [{}]", propertiesFileUrl.getPath());
		InputStream in = null;
		try {
			in = propertiesFileUrl.openStream();
			super.load(in);
		} catch (IOException e) {
			log.error("Load properties error!", e);
		}finally{
			IoUtil.close(in);
		}
		this.propertiesFileUrl = propertiesFileUrl;
	}
	
	/**
	 * 重新加载配置文件
	 */
	public void reload() {
		this.load(propertiesFileUrl);
	}
	
	//----------------------------------------------------------------------- Get start
	@Override
	public Object getObj(String key, Object defaultValue) {
		return getStr(key, null == defaultValue ? null : defaultValue.toString());
	}

	@Override
	public Object getObj(String key) {
		return getObj(key, null);
	}
	
	@Override
	public String getStr(String key, String defaultValue){
		return super.getProperty(key, defaultValue);
	}
	
	@Override
	public String getStr(String key){
		return super.getProperty(key);
	}
	
	@Override
	public Integer getInt(String key, Integer defaultValue){
		return Conver.toInt(getStr(key), defaultValue);
	}
	
	@Override
	public Integer getInt(String key){
		return getInt(key, null);
	}
	
	@Override
	public Boolean getBool(String key, Boolean defaultValue){
		return Conver.toBool(getStr(key), defaultValue);
	}
	
	@Override
	public Boolean getBool(String key){
		return getBool(key, null);
	}
	
	@Override
	public Long getLong(String key, Long defaultValue){
		return Conver.toLong(getStr(key), defaultValue);
	}
	
	@Override
	public Long getLong(String key){
		return getLong(key, null);
	}
	
	@Override
	public Character getChar(String key, Character defaultValue) {
		final String value = getStr(key);
		if(StrUtil.isBlank(value)) {
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
		return Conver.toFloat(getStr(key), defaultValue);
	}
	
	@Override
	public Double getDouble(String key, Double defaultValue) throws NumberFormatException {
		return Conver.toDouble(getStr(key), defaultValue);
	}
	
	@Override
	public Double getDouble(String key) throws NumberFormatException {
		return getDouble(key, null);
	}
	
	@Override
	public Short getShort(String key, Short defaultValue) {
		return Conver.toShort(getStr(key), defaultValue);
	}
	
	@Override
	public Short getShort(String key) {
		return getShort(key, null);
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		return Conver.toByte(getStr(key), defaultValue);
	}
	
	@Override
	public Byte getByte(String key) {
		return getByte(key, null);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		final String valueStr = getStr(key);
		if(StrUtil.isBlank(valueStr)) {
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
		if(StrUtil.isBlank(valueStr)) {
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
	
	//----------------------------------------------------------------------- Get end
	
	//----------------------------------------------------------------------- Set start
	/**
	 * 设置值，无给定键创建之。设置后未持久化
	 * @param key 属性键
	 * @param value 属性值
	 */
	public void setProperty(String key, Object value){
		super.setProperty(key, value.toString());
	}
	
	/**
	 * 持久化当前设置，会覆盖掉之前的设置
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath){
		try {
			FileUtil.touch(absolutePath);
			super.store(FileUtil.getOutputStream(absolutePath), null);
		} catch (FileNotFoundException e) {
			//不会出现这个异常
		} catch (IOException e) {
			log.error(e, "Store properties to [{}] error!", absolutePath);
		}
	}
	
	/**
	 * 存储当前设置，会覆盖掉以前的设置
	 * @param path 相对路径
	 * @param clazz 相对的类
	 */
	public void store(String path, Class<?> clazz){
		this.store(FileUtil.getAbsolutePath(path, clazz));
	}
	//----------------------------------------------------------------------- Set end
}
