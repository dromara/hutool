package cn.hutool.core.event;


import java.util.function.Function;

/**
 * 创建监听器的辅助类<br>
 * 为了不直接暴露{@link ListenerDecorate}而存在的构建器
 *
 * @author Create by liuwenhao on 2022/7/23 18:27
 */
public class ListenerBuilder<E, R> {

	/**
	 * 优先级，数值越大优先级越低
	 */
	int order;

	/**
	 * 是否异步执行
	 */
	Boolean isAsync;

	/**
	 * 传播模式
	 */
	SpreadPattern spreadPattern;

	/**
	 * 监听器执行的前置操作和后置操作
	 */
	EventProcessor<E, R> eventProcessor;

	/**
	 * 异常处理
	 */
	Function<Throwable, R> throwableFn;

	/**
	 * 监听器
	 */
	Listener<E, R> listener;

	private ListenerBuilder(Listener<E, R> listener) {
		this.listener = listener;
	}

	protected static <E, R> ListenerBuilder<E, R> build(Listener<E, R> listener) {
		return new ListenerBuilder<>(listener);
	}

	public ListenerBuilder<E, R> order(int order) {
		this.order = order;
		return this;
	}

	public ListenerBuilder<E, R> async(Boolean async) {
		isAsync = async;
		return this;
	}

	public ListenerBuilder<E, R> spreadPattern(SpreadPattern spreadPattern) {
		this.spreadPattern = spreadPattern;
		return this;
	}

	public ListenerBuilder<E, R> throwableFn(Function<Throwable, R> throwableFn) {
		this.throwableFn = throwableFn;
		return this;
	}

	public ListenerBuilder<E, R> listener(Listener<E, R> listener) {
		this.listener = listener;
		return this;
	}

	public ListenerBuilder<E, R> eventProcessor(EventProcessor<E, R> eventProcessor) {
		this.eventProcessor = eventProcessor;
		return this;
	}

	public int getOrder() {
		return order;
	}

	public Boolean isAsync() {
		return isAsync;
	}

	public SpreadPattern getSpreadPattern() {
		return spreadPattern;
	}

	public Function<Throwable, R> getThrowableFn() {
		return throwableFn;
	}

	public Listener<E, R> getListener() {
		return listener;
	}

	public EventProcessor<E, R> getEventProcessor() {
		return eventProcessor;
	}

	public void setEventProcessor(EventProcessor<E, R> eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	/**
	 * 装换，将ListenerModel转换为ListenerDecorate，并设置默认值
	 *
	 * @return ListenerDecorate
	 */
	protected ListenerDecorate<E, R> listenerDecorate() {
		ListenerDecorate<E, R> listenerDecorate = ListenerDecorate.build();

		int decorateOrder = getOrder() == 0 ? listenerDecorate.order : getOrder();
		boolean decorateAsync = isAsync() == null ? listenerDecorate.async : isAsync();
		Function<Throwable, R> decorateFn = getThrowableFn() == null ? listenerDecorate.getThrowHandler() : getThrowableFn();
		SpreadPattern decorateSpreadPattern = getSpreadPattern() == null ? listenerDecorate.getSpreadPattern() : getSpreadPattern();
		EventProcessor<E, R> decorateProcessor = getEventProcessor() == null ? listenerDecorate.getEventProcessor() : getEventProcessor();

		return listenerDecorate
			.listener(getListener())
			.fn(decorateFn)
			.async(decorateAsync)
			.order(decorateOrder)
			.eventProcessor(decorateProcessor)
			.spreadPattern(decorateSpreadPattern);
	}
}
