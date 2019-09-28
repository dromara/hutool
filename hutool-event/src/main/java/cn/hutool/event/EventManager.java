package cn.hutool.event;

import java.util.Collection;

import cn.hutool.event.domain.BaseEvent;
import cn.hutool.event.listener.BaseListener;

/**
 * Interface EventManager ...
 * 事件广播接口，负责事件框架生命周期内的事件监听器注册、注销，事件的推送
 * @author Ted.L
 * Created on 2019-09-28
 */
public interface EventManager {

    /**
     * 注册事件监听器
     *
     * @param listener 事件监听器
     */
    void addListener(BaseListener listener);

    /**
     * 注册多个事件监听器
     *
     * @param listeners 事件监听器集合
     */
    void addListeners(BaseListener... listeners);

    /**
     * 注销事件监听器
     *
     * @param listener 事件监听器
     */
    void removeListener(BaseListener listener);

    /**
     * 注销所有事件监听器
     */
    void removeAllListeners();

    /**
     * 获取所有注册的事件监听器
     *
     * @return 监听器集合
     */
    Collection<BaseListener> getAllListeners();

    /**
     * 获取所有注册支持该事件的事件监听器
     * @param event
     * @return 支持event的监听器集合
     */
    Collection<BaseListener> getAllListeners(BaseEvent event);

    /**
     * 广播事件
     *
     * @param event 事件
     * @param sync 同步或异步广播
     */
    void broadcastEvent(BaseEvent event, boolean sync);
}
