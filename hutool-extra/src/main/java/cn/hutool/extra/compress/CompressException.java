package cn.hutool.extra.compress;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 压缩解压异常语言异常
 * 
 * @author Looly
 */
public class CompressException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CompressException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CompressException(String message) {
		super(message);
	}

	public CompressException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CompressException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CompressException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
