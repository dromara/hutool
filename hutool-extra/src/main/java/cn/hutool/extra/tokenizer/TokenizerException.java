package cn.hutool.extra.tokenizer;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrUtil;

/**
 * 分词异常
 *
 * @author Looly
 */
public class TokenizerException extends RuntimeException {
	private static final long serialVersionUID = 8074865854534335463L;

	public TokenizerException(final Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public TokenizerException(final String message) {
		super(message);
	}

	public TokenizerException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public TokenizerException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public TokenizerException(final String message, final Throwable throwable, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public TokenizerException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
