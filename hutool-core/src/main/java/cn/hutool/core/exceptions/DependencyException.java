package cn.hutool.core.exceptions;

import cn.hutool.core.util.StrUtil;

/**
 * 依赖异常
 * 
 * @author xiaoleilu
 * @since 4.0.10
 */
public class DependencyException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public DependencyException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public DependencyException(String message) {
		super(message);
	}

	public DependencyException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public DependencyException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DependencyException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
