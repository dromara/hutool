package cn.hutool.extra.template;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 模板异常
 * 
 * @author xiaoleilu
 */
public class TemplateException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public TemplateException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public TemplateException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TemplateException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
