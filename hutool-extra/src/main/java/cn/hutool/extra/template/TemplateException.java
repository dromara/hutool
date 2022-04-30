package cn.hutool.extra.template;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 模板异常
 *
 * @author xiaoleilu
 */
public class TemplateException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public TemplateException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public TemplateException(final String message) {
		super(message);
	}

	public TemplateException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public TemplateException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public TemplateException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public TemplateException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
