package cn.hutool.http.webservice;

import cn.hutool.core.text.StrUtil;

/**
 * SOAP异常
 *
 * @author xiaoleilu
 */
public class SoapRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public SoapRuntimeException(final Throwable e) {
		super(e.getMessage(), e);
	}

	public SoapRuntimeException(final String message) {
		super(message);
	}

	public SoapRuntimeException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public SoapRuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	public SoapRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
