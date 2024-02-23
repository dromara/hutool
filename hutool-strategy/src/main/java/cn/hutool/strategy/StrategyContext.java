package cn.hutool.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略上下文类，用于注册和执行策略。
 * 使用ConcurrentHashMap来存储策略，以支持并发操作。
 *
 * @param <R> 策略处理的数据类型。
 * @param <P> 策略执行后返回的数据类型。
 * @param <T> 策略类型
 * @author Zhao QingYun
 */
public class StrategyContext<R, P, T> {

    /**
     * 上下文类型
     */
    private final String contextType;


    /**
     * 存储策略的映射，键为策略类型，值为策略实例。
     */
    private final Map<T, Strategy<?, ?, ?>> strategies = new ConcurrentHashMap<>();

    public StrategyContext(String contextType) {
        this.contextType = contextType;
    }

    /**
     * 注册策略的方法。
     *
     * @param strategy 要注册的策略实例。
     */
    public void register(Strategy<R, P, T> strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Type and strategy cannot be null");
        }
        T type = strategy.getType();
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (strategies.containsKey(type)) {
            throw new IllegalArgumentException("Strategy for type " + type + " is already registered");
        }
        strategies.put(type, strategy);
    }

    /**
     * 执行策略的方法。
     *
     * @param type    要执行的策略类型。
     * @param request 策略处理的数据。
     * @return 策略执行后返回的数据。
     * @throws IllegalArgumentException 如果传入的策略类型未注册，则抛出此异常。
     */
    @SuppressWarnings("unchecked")
    public P execute(T type, R request) {
        Strategy<R, P, T> strategy = (Strategy<R, P, T>) strategies.get(type);
        if (strategy != null) {
            return strategy.execute(request);
        } else {
            throw new IllegalArgumentException("Unknown strategy type: " + type);
        }
    }

    public String getContextType() {
        return contextType;
    }
}

