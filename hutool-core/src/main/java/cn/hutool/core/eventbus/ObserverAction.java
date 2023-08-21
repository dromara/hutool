package cn.hutool.core.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 表示 @Subscribe 注解方法
 * @author unknowIfGuestInDream
 */
public class ObserverAction {
    private final Object target;
    private final Method method;

    public ObserverAction(Object target, Method method) {
        this.target = Objects.requireNonNull(target);
        this.method = method;
        this.method.setAccessible(true);
    }

    public Object getTarget() {
        return target;
    }

    /**
     * event是method方法的参数
     * @param event
     */
    public void execute(Object event) {
        try {
            method.invoke(target, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EventBusException(e);
        }
    }
}
