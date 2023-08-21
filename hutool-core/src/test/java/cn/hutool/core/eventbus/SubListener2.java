package cn.hutool.core.eventbus;

import cn.hutool.core.lang.Console;
import org.junit.Assert;

/**
 * @author unknowIfGuestInDream
 */
public class SubListener2 {

	public SubListener2() {
		EventBusUtil.getDefault().register(this);
	}

	@Subscribe
	public void sub5(ApplicationPreparedEvent event) {
		Console.log("sub5");
		Assert.assertEquals("applicationPrepare", event.getValue());
	}
}
