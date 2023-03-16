package cn.hutool.core.lang.page;

import org.junit.Assert;
import org.junit.Test;

public class NavigatePageInfoTest {

	@Test
	public void naviTest1(){
		// 首页
		final NavigatePageInfo navigatePageInfo = new NavigatePageInfo(10, 2, 6);
		Assert.assertEquals("[1] 2 3 4 5 >>", navigatePageInfo.toString());

		// 中间页
		navigatePageInfo.nextPage();
		Assert.assertEquals("<< 1 [2] 3 4 5 >>", navigatePageInfo.toString());

		// 尾页
		navigatePageInfo.setPageNo(5);
		Assert.assertEquals("<< 1 2 3 4 [5]", navigatePageInfo.toString());
	}

	@Test
	public void naviTest2(){
		// 首页
		final NavigatePageInfo navigatePageInfo = new NavigatePageInfo(10, 2, 4);
		Assert.assertEquals("[1] 2 3 4 >>", navigatePageInfo.toString());

		// 中间页
		navigatePageInfo.nextPage();
		Assert.assertEquals("<< 1 [2] 3 4 >>", navigatePageInfo.toString());

		// 尾页
		navigatePageInfo.setPageNo(5);
		Assert.assertEquals("<< 2 3 4 [5]", navigatePageInfo.toString());
	}
}
