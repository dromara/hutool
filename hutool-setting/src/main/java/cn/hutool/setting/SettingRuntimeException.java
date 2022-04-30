package cn.hutool.setting;

import cn.hutool.core.text.StrUtil;

/**
 * 设置异常
 *
 * @author xiaoleilu
 */
public class SettingRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 7941096116780378387L;

	public SettingRuntimeException(final Throwable e) {
		super(e);
	}

	public SettingRuntimeException(final String message) {
		super(message);
	}

	public SettingRuntimeException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public SettingRuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public SettingRuntimeException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public SettingRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
