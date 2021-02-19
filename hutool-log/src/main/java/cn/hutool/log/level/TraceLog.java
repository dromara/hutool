package cn.hutool.log.level;

/**
 * TRACE级别日志接口
 * @author Looly
 *
 */
public interface TraceLog {
	/**
	 * @return TRACE 等级是否开启
	 */
	boolean isTraceEnabled();

	/**
	 * 打印 TRACE 等级的日志
	 * 
	 * @param t 错误对象
	 */
	void trace(Throwable t);

	/**
	 * 打印 TRACE 等级的日志
	 * 
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void trace(String format, Object... arguments);

	/**
	 * 打印 TRACE 等级的日志
	 * 
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void trace(Throwable t, String format, Object... arguments);
	
	/**
	 * 打印 TRACE 等级的日志
	 * 
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void trace(String fqcn, Throwable t, String format, Object... arguments);
}
