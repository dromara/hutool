package cn.hutool.core.convert;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 转换异常
 * @author xiaoleilu
 */
public class ConvertException extends RuntimeException{
	private static final long serialVersionUID = 4730597402855274362L;

	public ConvertException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ConvertException(final String message) {
		super(message);
	}

	public ConvertException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ConvertException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public ConvertException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
