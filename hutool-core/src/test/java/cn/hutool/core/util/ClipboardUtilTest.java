package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.util.ClipboardUtil;

/**
 * 剪贴板工具类单元测试
 * @author looly
 *
 */
public class ClipboardUtilTest {
	
	@Test
	public void setAndGetStrTest() {
		ClipboardUtil.setStr("test");
		
		String test = ClipboardUtil.getStr();
		Assert.assertEquals("test", test);
	}
}
