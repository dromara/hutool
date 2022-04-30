package cn.hutool.extra.pinyin;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 模板异常
 *
 * @author xiaoleilu
 */
public class PinyinException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PinyinException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public PinyinException(final String message) {
		super(message);
	}

	public PinyinException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public PinyinException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public PinyinException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public PinyinException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
