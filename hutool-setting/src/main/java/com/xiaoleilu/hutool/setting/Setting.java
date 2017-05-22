package com.xiaoleilu.hutool.setting;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

import com.xiaoleilu.hutool.setting.dialect.BasicSetting;

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
	private static final long serialVersionUID = 6473518262551553107L;

	/**
	 * 基本构造<br>
	 * 需自定义初始化配置文件<br>
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
	/**
	 * 获得group对应的子Setting
	 * @param group 分组
	 * @return {@link Setting}
	 */
	@Override
	public Setting getSetting(String group){
		final Setting setting = new Setting();
		setting.putAll(this.getMap(group));
		return setting;
	}
	/*--------------------------公有方法 end-------------------------------*/
}
