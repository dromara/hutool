package cn.hutool.event;

import cn.hutool.event.domain.BaseEvent;
import cn.hutool.event.exception.EventRejectException;

/**
 * Class DefaultEventBroadcastRejectedHandler...
 *  默认的事件拒绝处理器
 *  默认主线程同步处理
 * @author Ted.L
 * Created on 2019-09-29
 */
public class DefaultEventBroadcastRejectedHandler extends AbstractEventBroadcastRejectedHandler {

    /**
     * @param event
     */
    @Override
    protected void rejectedEvent(BaseEvent event) {
        throw new EventRejectException("event " + event.toString() + "has been rejected");
    }
}
