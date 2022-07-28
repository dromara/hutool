package cn.hutool.core.event;

/**
 * 为事件执行前添加前置操作和后置操作<br>
 * <ol>
 *     <li>前置操作的执行时机为判断是否异步执行之后，监听器执行之前</li>
 *     <li>后置操作的执行时机为执行传播机制或异常处理之后，监听器结束之前，当执行出现异常时也会执行后置操作的内容</li>
 * </ol>
 * 前置任务和后置任务可以用于对一次监听器执行的包装，比如在前置任务中开启事务，在后置任务中提交。<br>
 * 同样也可以在前后的执行中加入日志，和某些记录等。<br>
 * 前置任务和后置任务允许在内部对监听器和事件进行修改，但是不推荐进行这样的操作。
 *
 * @author Create by liuwenhao on 2022/7/25 15:56
 * @see NoEventProcessor
 * @see TransactionalEventProcessor
 */
public interface EventProcessor<E, R> {

	/**
	 * 前置操作
	 *
	 * @param event            事件对象
	 * @param listenerDecorate 监听器包装器
	 */
	void before(E event, ListenerDecorate<E, R> listenerDecorate);

	/**
	 * 后置操作
	 *
	 * @param event            事件对象
	 * @param listenerDecorate 监听器包装器
	 */
	void after(E event, ListenerDecorate<E, R> listenerDecorate);


}
