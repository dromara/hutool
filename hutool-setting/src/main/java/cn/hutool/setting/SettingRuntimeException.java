package cn.hutool.setting;

import cn.hutool.core.util.StrUtil;

/**
 * 设置异常
 * @author xiaoleilu
 */
public class SettingRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 7941096116780378387L;

	public SettingRuntimeException(Throwable e) {
		super(e);
	}
	
	public SettingRuntimeException(String message) {
		super(message);
	}
	
	public SettingRuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public SettingRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public SettingRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
