package cn.hutool.core.event;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

/**
 * 传播模式<br>
 * 决定了监听器执行事件完成后的传播机制<br>
 * 在整个监听器完成之后可以通过其所在的上下文重新绑定，解绑或发布事件<br>
 *
 * @author Create by liuwenhao on 2022/7/20 12:24
 * @see NoSpreadPattern
 * @see EdgeSpreadPattern
 */
@FunctionalInterface
public interface SpreadPattern {


	/**
	 * 在监听器执行完成后处理传播行为
	 *
	 * @param context 上下文
	 * @param r       监听器返回值
	 * @param e       事件实体
	 * @param <R>     返回值类型，此处为了提高辨识度使用泛型，本质和Object没有区别
	 * @param <E>     事件类型
	 * @throws Throwable 过程中产生的异常
	 */
	<E, R> void spread(TypeEventContext context, R r, E e) throws Throwable;


	/**
	 * 对特殊的事件进行处理：<br>
	 * <ol>
	 *     <li>{@link CompletionStage}：获取结果并发布结果</li>
	 *     <li>{@link Future}：获取结果并发布结果</li>
	 *     <li>{@link Array}：每个元素都会发布</li>
	 *     <li>{@link Collection}：每个元素都会发布</li>
	 *     <li>其他：发布</li>
	 * </ol>
	 * 该代码参照springboot对事件机制的实现
	 *
	 * @param context 上下文
	 * @param o       事件
	 * @throws Throwable 过程中产生的异常
	 */
	default void publishEvents(TypeEventContext context, Object o) throws Throwable {
		if (o == null) {
			return;
		}
		if (o instanceof CompletionStage) {
			context.publish(((CompletionStage<?>) o).toCompletableFuture().join());
		} else if (o instanceof Future) {
			context.publish(((Future<?>) o).get());
		} else if (o.getClass().isArray()) {
			Object[] events = new Object[0];
			if (o instanceof Object[]) {
				events = (Object[]) o;
			}
			int length = Array.getLength(o);
			if (length > 0) {
				Class<?> wrapperType = Array.get(o, 0).getClass();
				Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
				for (int i = 0; i < length; i++) {
					newArray[i] = Array.get(o, i);
				}
				events = newArray;
			}

			for (Object e : events) {
				context.publish(e);
			}
		} else if (o instanceof Collection<?>) {
			Collection<?> events = (Collection<?>) o;
			for (Object e : events) {
				context.publish(e);
			}
		} else {
			context.publish(o);
		}
	}

}
