package cn.hutool.event.test;

import cn.hutool.event.domain.BaseEvent;
import cn.hutool.event.listener.BaseListener;

/**
 * Class FirstCatchListener...
 *
 * @author Ted.L
 * Created on 2019-09-28
 */
public class FirstCatchListener implements BaseListener<CatchEvent> {

    /**
     * 事件处理接口
     *
     * @param event 事件
     */
    @Override
    public void onEventHappened(CatchEvent event) {
        if(event.getCd().getCount() < 10 || event.getCd().getCount()> 5000){
//            System.out.println(String.format(CatchEvent.class.cast(event).getMsg(), CatchEvent.class.cast(event).getCd().getCount()));
        }
        event.getCd().countDown();
        System.out.println(event.getDealTimes().incrementAndGet());

    }

    /**
     * 是否支持处理该事件类型
     *
     * @param event 事件
     * @return
     */
    @Override
    public boolean isSupportEvent(BaseEvent event) {
        return event instanceof CatchEvent;
    }

    /**
     * @return 优先级 越小越先处理支持的事件
     */
    @Override
    public int order() {
        return 1;
    }
}
