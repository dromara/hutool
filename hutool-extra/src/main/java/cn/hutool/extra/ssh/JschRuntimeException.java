package cn.hutool.extra.ssh;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Jsch异常
 * @author xiaoleilu
 */
public class JschRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 8247610319171014183L;

	public JschRuntimeException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}
	
	public JschRuntimeException(String message) {
		super(message);
	}
	
	public JschRuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public JschRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public JschRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
