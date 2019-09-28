package cn.hutool.event;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import cn.hutool.event.domain.BaseEvent;

/**
 * Class AbstractEventMulticasterRejectedHandler...
 *
 * @author Ted.L
 * Created on 2019-09-28
 */
public abstract class AbstractEventMulticasterRejectedHandler implements RejectedExecutionHandler {

    @Override
    public final void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        DefaultEventManager.AnonymityEventHandler eventHandler = (DefaultEventManager.AnonymityEventHandler) r;
        rejectedEvent(eventHandler.getEvent());
    }

    /**
     * @param event
     */
    protected abstract void rejectedEvent(BaseEvent event);
}
