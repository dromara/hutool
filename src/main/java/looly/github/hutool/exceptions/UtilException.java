package looly.github.hutool.exceptions;

/**
 * 未初始化异常
 * @author xiaoleilu
 */
public class UtilException extends RuntimeException{
	private static final long serialVersionUID = 8247610319171014183L;

	public UtilException(Throwable e) {
		super(e.getMessage(), e);
	}
	
	public UtilException(String message) {
		super(message);
	}
	
	public UtilException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
