package com.xiaoleilu.hutool.setting;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import com.xiaoleilu.hutool.getter.OptNullBasicTypeFromObjectGetter;

/**
 * 设定抽象类
 * @author Looly
 *
 */
public abstract class AbsSetting extends OptNullBasicTypeFromObjectGetter<String> implements Map<Object, Object>{
	
	/** 本设置对象的字符集 */
	protected Charset charset;
	/** 是否使用变量 */
	protected boolean isUseVariable;
	/** 设定文件的URL */
	protected URL settingUrl;
	
	@Override
	public abstract Object getObj(String key, Object defaultValue);

}
