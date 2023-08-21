package cn.hutool.core.eventbus;

import cn.hutool.core.lang.Console;
import org.junit.Assert;

/**
 * @author unknowIfGuestInDream
 */
public class SubListener1 {

	@Subscribe
	public void sub2(ApplicationPreparedEvent event) {
		Console.log("sub2");
		Assert.assertEquals("applicationPrepare", event.getValue());
	}

	@Subscribe
	public void sub3(ApplicationPreparedEvent event) {
		Console.log("sub3");
		Assert.assertEquals("applicationPrepare", event.getValue());
	}

	@Subscribe
	public void sub4(ApplicationReadyEvent event) {
		Console.log("sub4");
		Assert.assertEquals("applicationReady", event.getValue());
	}
}
