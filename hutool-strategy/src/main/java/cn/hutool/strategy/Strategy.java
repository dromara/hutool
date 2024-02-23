package cn.hutool.strategy;

/**
 * 策略接口，定义了策略的执行方法。
 *
 * @param <R> 策略处理的数据类型。
 * @param <P> 策略执行后返回的数据类型。
 * @param <T> 策略类型
 * @author Zhao QingYun
 */
public interface Strategy<R, P, T> {
    /**
     * 执行策略的方法。
     *
     * @param request 策略处理的数据。
     * @return 策略执行后返回的数据。
     */
    P execute(R request);

    /**
     * 策略类型
     * @return 策略类型
     */
    T getType();
}
