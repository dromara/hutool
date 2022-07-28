package cn.hutool.core.event;

/**
 * 操作的默认实现，什么都不做
 *
 * @author Create by liuwenhao on 2022/7/25 18:15
 */
public class NoEventProcessor<E, R> implements EventProcessor<E, R> {

	@Override
	public void before(E event, ListenerDecorate<E, R> listenerDecorate) {
		// 什么都不做
	}

	@Override
	public void after(E event, ListenerDecorate<E, R> listenerDecorate) {
		// 什么都不做
	}
}
