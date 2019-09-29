package cn.hutool.event.listener;

import java.util.EventListener;

import cn.hutool.event.domain.BaseEvent;

/**
 * Class BaseListener...
 * 基础监听器
 * @author Ted.L
 * Created on 2019-09-28
 */
public interface BaseListener extends EventListener {

    /**
     * 事件处理接口
     *
     * @param event 事件
     */
    void onEventHappened(BaseEvent event);

    /**
     * 是否支持处理该事件类型
     *
     * @param event 事件
     * @return
     */
    boolean isSupportEvent(BaseEvent event);

    /**
     * 处理相同事件，
     * @return 优先级 越小越先处理支持的事件
     */
    int order();
}
