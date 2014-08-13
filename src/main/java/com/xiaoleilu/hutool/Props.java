package com.xiaoleilu.hutool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;

/**
 * Properties文件读取封装类
 * @author loolly
 */
public final class Props extends Properties{
	private static final long serialVersionUID = 1935981579709590740L;
	private static Logger log = Log.get();
	
	//----------------------------------------------------------------------- 私有属性 start
	/** 属性文件的URL */
	private URL propertiesFileUrl;
	//----------------------------------------------------------------------- 私有属性 end
	
	//----------------------------------------------------------------------- 构造方法 start
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
			FileUtil.close(in);
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
	/**
	 * 获取字符型型属性值
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public String getStr(String key, String defaultValue){
		return super.getProperty(key, defaultValue);
	}
	
	/**
	 * 获取字符型型属性值
	 * @param key 属性名
	 * @return 属性值
	 */
	public String getStr(String key){
		return super.getProperty(key);
	}
	
	/**
	 * 获取数字型型属性值
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Integer getInt(String key, Integer defaultValue){
		return Conver.toInt(getStr(key), defaultValue);
	}
	
	/**
	 * 获取数字型型属性值
	 * @param key 属性名
	 * @return 属性值
	 */
	public Integer getInt(String key){
		return getInt(key, null);
	}
	
	/**
	 * 获取波尔型属性值
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Boolean getBool(String key, Boolean defaultValue){
		return Conver.toBool(getStr(key), defaultValue);
	}
	
	/**
	 * 获取波尔型属性值
	 * @param key 属性名
	 * @return 属性值
	 */
	public Boolean getBool(String key){
		return getBool(key, null);
	}
	
	/**
	 * 获取long类型属性值
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Long getLong(String key, Long defaultValue){
		return Conver.toLong(getStr(key), defaultValue);
	}
	
	/**
	 * 获取long类型属性值
	 * @param key 属性名
	 * @return 属性值
	 */
	public long getLong(String key){
		return getLong(key, null);
	}
	
	/**
	 * 获取char类型属性值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Character getChar(String key, String defaultValue) {
		final String value = getStr(key, defaultValue);
		if(StrUtil.isBlank(value)) {
			return null;
		}
		return value.charAt(0);
	}
	
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
	 * 获取double类型属性值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Double getDouble(String key, Double defaultValue) throws NumberFormatException {
		return Conver.toDouble(getStr(key), defaultValue);
	}
	
	/**
	 * 获取double类型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	public Double getDouble(String key) throws NumberFormatException {
		return getDouble(key, null);
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
			Log.error(log, e, "Store properties to [{}] error!", absolutePath);
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
