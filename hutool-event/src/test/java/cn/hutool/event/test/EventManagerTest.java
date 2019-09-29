package cn.hutool.event.test;

import java.util.Arrays;
import java.util.Collection;

import cn.hutool.event.DefaultEventManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.event.listener.BaseListener;

public class EventManagerTest {
    private DefaultEventManager eventFireHelper;
    private CatchEvent              event;

    @Before
    public void before() {
        eventFireHelper = new DefaultEventManager();
        event = new CatchEvent(this, UUID.fastUUID().toString());
    }

    @Test
    public void testMulticasterCatchEvent() {
        eventFireHelper.addListeners(new SecondCatchListener(), new FirstCatchListener());
        //异步
        for (int i = 0; i < 100; i++) {
            ThreadUtil.execute(new Runnable() {
                @Override
                public void run() {
                    eventFireHelper.broadcastEvent(event, false);
                }
            });
        }

        ThreadUtil.safeSleep(1000);
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
        eventFireHelper.addListener(f);
        eventFireHelper.addListener(s);
        eventFireHelper.removeListener(f);
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 1);
        eventFireHelper.removeListener(f);
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 1);
        eventFireHelper.removeListener(s);
        Assert.assertEquals(eventFireHelper.getAllListeners().size(), 0);
    }

    @Test(expected = ValidateException.class)
    public void testAddListener() throws Exception {
        eventFireHelper.addListener(null);
    }

    @Test
    public void testAddListeners() throws Exception {
        eventFireHelper.addListeners(new FirstCatchListener(), new SecondCatchListener());
        Assert.assertEquals(eventFireHelper.getAllListeners().size(),2);
    }

    @Test(expected = ValidateException.class)
    public void testRemoveListener() throws Exception {
        eventFireHelper.removeListener(null);
    }

    @Test
    public void testRemoveAllListeners() throws Exception {
        eventFireHelper.removeAllListeners();
        Assert.assertEquals(eventFireHelper.getAllListeners().size(),0);
    }

    @Test
    public void testGetAllListeners() throws Exception {
        Collection<BaseListener> result = eventFireHelper.getAllListeners();
        Assert.assertEquals(Arrays.<BaseListener> asList(), result);
    }

    @Test(expected = ValidateException.class)
    public void testGetAllListeners2() throws Exception {
        Collection<BaseListener> result = eventFireHelper.getAllListeners(null);
        Assert.assertEquals(null, result);
    }

    @Test(expected = ValidateException.class)
    public void testMulticastEvent() throws Exception {
        eventFireHelper.broadcastEvent(null, true);
    }
}