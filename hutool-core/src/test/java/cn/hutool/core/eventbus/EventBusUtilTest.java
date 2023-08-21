package cn.hutool.core.eventbus;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

/**
 * 消息总线工具单元测试<br>
 *
 * @author unknowIfGuestInDream
 */
public class EventBusUtilTest {

	@Test
	public void register() {
		EventBusUtil.getDefault().register(this);
		EventBusUtil.getDefault().post(new ApplicationPreparedEvent("applicationPrepare"));
		EventBusUtil.getDefault().post(new ApplicationReadyEvent("applicationReady"));
		Assert.assertEquals(1, EventBusUtil.getDefault().size());

		EventBusUtil.getDefault().register(new SubListener1());
		EventBusUtil.getDefault().post(new ApplicationPreparedEvent("applicationPrepare"));
		EventBusUtil.getDefault().post(new ApplicationReadyEvent("applicationReady"));
		Assert.assertEquals(2, EventBusUtil.getDefault().size());

		new SubListener2();
		EventBusUtil.getDefault().post(new ApplicationPreparedEvent("applicationPrepare"));
		EventBusUtil.getDefault().post(new ApplicationReadyEvent("applicationReady"));
		Assert.assertEquals(2, EventBusUtil.getDefault().size());
	}

	@Test
	public void unregister() {
		EventBusUtil.getDefault().register(this);
		EventBusUtil.getDefault().post(new ApplicationPreparedEvent("applicationPrepare"));
		EventBusUtil.getDefault().post(new ApplicationReadyEvent("applicationReady"));
		Assert.assertEquals(1, EventBusUtil.getDefault().size());
		EventBusUtil.getDefault().unregister(this);
		Assert.assertEquals(0, EventBusUtil.getDefault().size());

		SubListener1 subListener1 = new SubListener1();
		EventBusUtil.getDefault().register(subListener1);
		EventBusUtil.getDefault().post(new ApplicationPreparedEvent("applicationPrepare"));
		EventBusUtil.getDefault().post(new ApplicationReadyEvent("applicationReady"));
		Assert.assertEquals(2, EventBusUtil.getDefault().size());
		EventBusUtil.getDefault().unregister(subListener1);
		Assert.assertEquals(0, EventBusUtil.getDefault().size());

		SubListener2 subListener2 = new SubListener2();
		EventBusUtil.getDefault().post(new ApplicationPreparedEvent("applicationPrepare"));
		EventBusUtil.getDefault().post(new ApplicationReadyEvent("applicationReady"));
		Assert.assertEquals(1, EventBusUtil.getDefault().size());
		EventBusUtil.getDefault().unregister(subListener2);
		Assert.assertEquals(0, EventBusUtil.getDefault().size());
	}

	@Test
	public void async() {
		EventBusUtil.getDefaultAsync().register(this);
		EventBusUtil.getDefaultAsync().register(new SubListener1());
		EventBusUtil.getDefaultAsync().post(new ApplicationPreparedEvent("applicationPrepare"));
		EventBusUtil.getDefaultAsync().post(new ApplicationReadyEvent("applicationReady"));
		Assert.assertEquals(2, EventBusUtil.getDefaultAsync().size());
		Assert.assertEquals(0, EventBusUtil.getDefault().size());
	}

	@Subscribe
	public void sub1(ApplicationPreparedEvent event) {
		Console.log("sub1");
		Assert.assertEquals("applicationPrepare", event.getValue());
	}
}
