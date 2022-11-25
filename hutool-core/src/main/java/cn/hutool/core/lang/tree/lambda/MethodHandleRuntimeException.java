package cn.hutool.core.lang.tree.lambda;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * MethodHandle调用异常包装
 *
 * @author adoph
 * @version 1.0
 * @since 2022/11/25
 */
public class MethodHandleRuntimeException extends RuntimeException {

	public MethodHandleRuntimeException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public MethodHandleRuntimeException(String message) {
		super(message);
	}

	public MethodHandleRuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public MethodHandleRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public MethodHandleRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}

}
