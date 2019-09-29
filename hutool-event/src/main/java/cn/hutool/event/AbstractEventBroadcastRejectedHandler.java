package cn.hutool.event;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import cn.hutool.event.domain.BaseEvent;

/**
 * Class AbstractEventBroadcastRejectedHandler...
 *
 * @author Ted.L
 * Created on 2019-09-28
 */
public abstract class AbstractEventBroadcastRejectedHandler implements RejectedExecutionHandler {

    @Override
    public final void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        DefaultEventManager.AnonymityEventHandler eventHandler = (DefaultEventManager.AnonymityEventHandler) r;
        rejectedEvent(eventHandler.getEvent());
    }

    /**
     * 开放事件、监听器、事件管理器给使用方，自定义处理方式
     * @param event 被拒绝的事件
     */
    protected abstract void rejectedEvent(BaseEvent event);
}
