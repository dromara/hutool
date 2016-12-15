package com.xiaoleilu.hutool.setting;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import com.xiaoleilu.hutool.getter.OptNullBasicTypeFromObjectGetter;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * Setting抽象类
 * @author Looly
 *
 */
public abstract class AbsSetting extends OptNullBasicTypeFromObjectGetter<String> implements Map<Object, Object>{
	
	/** 默认字符集 */
	public final static Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;
	
	/** 设定文件的URL */
	protected URL settingUrl;
	
	@Override
	public abstract Object getObj(String key, Object defaultValue);

}
