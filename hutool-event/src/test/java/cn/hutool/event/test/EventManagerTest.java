package cn.hutool.event.test;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.event.AbstractEventBroadcastRejectedHandler;
import cn.hutool.event.DefaultEventManager;
import cn.hutool.event.EventManager;
import cn.hutool.event.domain.BaseEvent;
import cn.hutool.event.listener.BaseListener;

public class EventManagerTest {
    private EventManager   eventFireHelper;
    private CatchEvent     event;
    private CountDownLatch cd;
    private int            startThread = 2000;
    private AtomicLong     rejectTimes = new AtomicLong(0);

    @Before
    public void before() {
        eventFireHelper = DefaultEventManager.toBuilder().eventBroadcastRejectedHandler(new AbstractEventBroadcastRejectedHandler() {
            @Override
            protected void rejectedEvent(BaseEvent event) {
                CatchEvent.class.cast(event).getDealTimes().incrementAndGet();
                CatchEvent.class.cast(event).getCd().countDown();
                System.out.println("拒绝了 " + rejectTimes.incrementAndGet());
            }
        }).build();
        eventFireHelper.addListeners(new SecondCatchListener(), new FirstCatchListener());
        cd = new CountDownLatch(eventFireHelper.getAllListeners().size() * startThread);
        event = new CatchEvent(this, UUID.fastUUID().toString(), cd);
    }

    @Test
    public void testMulticasterCatchEvent() {
        //异步
        for (int i = 0; i < startThread; i++) {
            eventFireHelper.broadcastEvent(event, false);
        }

        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(CatchEvent.class.cast(event).getDealTimes().get(), eventFireHelper.getAllListeners().size() * startThread);

        //同步
        eventFireHelper.broadcastEvent(event, true);

        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 2);

        for (BaseListener allListener : eventFireHelper.getAllListeners()) {
            System.out.println(allListener.getClass().getName());
        }
        ThreadUtil.safeSleep(1000);
        for (BaseListener allListener : eventFireHelper.getAllListeners(event)) {
            System.out.println(allListener.getClass().getName());
        }
        eventFireHelper.removeAllListeners();
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 0);
        BaseListener f = new FirstCatchListener();
        BaseListener s = new SecondCatchListener();
        eventFireHelper.addListeners(f);
        eventFireHelper.addListeners(s);
        eventFireHelper.removeListener(f);
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 1);
        eventFireHelper.removeListener(f);
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 1);
        eventFireHelper.removeListener(s);
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 0);
    }

    @Test(expected = ValidateException.class)
    public void testAddListener() throws Exception {
        eventFireHelper.addListeners(null);
    }

    @Test
    public void testAddListeners() throws Exception {
        eventFireHelper.addListeners(new FirstCatchListener(), new SecondCatchListener());
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 4);
    }

    @Test(expected = ValidateException.class)
    public void testRemoveListener() throws Exception {
        eventFireHelper.removeListener(null);
    }

    @Test
    public void testRemoveAllListeners() throws Exception {
        eventFireHelper.removeAllListeners();
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 0);
    }

    @Test
    public void testGetAllListeners() throws Exception {
        Collection<BaseListener<BaseEvent>> result = eventFireHelper.getAllListeners();
        Assert.assertEquals(2, result.size());
    }

    @Test(expected = ValidateException.class)
    public void testGetAllListeners2() throws Exception {
        Collection<BaseListener<BaseEvent>> result = eventFireHelper.getAllListeners(null);
        Assert.assertEquals(null, result);
    }

    @Test(expected = ValidateException.class)
    public void testMulticastEvent() throws Exception {
        eventFireHelper.broadcastEvent(null, true);
    }
}