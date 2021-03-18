package cn.hutool.extra.flowcontrol;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author WangChen
 **/
public class FlowControlNode {

    /**
     * 请求
     */
    private Long[] access;

    /**
     * 限制请求数
     */
    private int limit;

    /**
     * 指针位置
     */
    private int position;

    /**
     * 时间间隔
     */
    private long period;

    /**
     * lock
     */
    private final Object lock = new Object();

    /**
     * 方法
     */
    private Method method;


    /**
	 * 构建限流节点
     * @param simpleFlowControl 限流注解
     * @param method 方法
     * @return FlowControlNode
     */
    public static FlowControlNode create(SimpleFlowControl simpleFlowControl, Method method){
        return create(simpleFlowControl.limit(), simpleFlowControl.period(), simpleFlowControl.timeUnit(), method);
    }


    /**
     * 构建限流节点
     * @param limit 总共请求数
     * @param period 时间间隔
     * @param timeUnit 时间单位
     * @param method 方法
     * @return SimpleFlowControlNode
     */
    public static FlowControlNode create(int limit, int period, TimeUnit timeUnit, Method method){
        if (limit < 0 || period < 0){
            throw new FlowControlException("flowControlNode incorrect initialization parameters, limit and period cant be less than 0, " +
                    "happened at {}, limit = {}, period = {}", method.getName(), limit, period);
        }
        return new FlowControlNode(limit, period, timeUnit, method);
    }


    /**
     * 私有构造方法
     *
     * @see FlowControlNode#create(int, int, TimeUnit, Method)
     * @param limit 总共请求数
     * @param period 时间间隔
     * @param timeUnit 时间单位
     */
    private FlowControlNode(int limit, int period, TimeUnit timeUnit, Method method) {
        this.limit = limit;
        this.period = timeUnit.toMillis(period);
        this.method = method;
        this.position = 0;
        access = new Long[limit];
        Arrays.fill(access, 0L);
    }


    /**
     * 判断当前是否可接收请求
     */
    public void tryToPass() {
        if (limit == SimpleFlowControl.DEFAULT_REQUEST){
            return;
        }
        final long curTime = System.currentTimeMillis();
        synchronized (this.lock) {
            if (curTime >= period + access[position]) {
                access[position++] = curTime;
                position = position % limit;
                return;
            }
            throw new FlowControlException("happened flow control at the {}", method.toString());
        }
    }
}
