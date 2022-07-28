package cn.hutool.core.event;

/**
 * 事件发布顶级接口
 *
 * @author Create by liuwenhao on 2022/7/28 13:41
 */
@FunctionalInterface
public interface EventPublisher {

	void publish(Object o);
}
