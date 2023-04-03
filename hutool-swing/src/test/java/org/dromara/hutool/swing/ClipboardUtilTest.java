package org.dromara.hutool.swing;

import org.dromara.hutool.swing.clipboard.ClipboardUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 剪贴板工具类单元测试
 *
 * @author looly
 *
 */
public class ClipboardUtilTest {

	@Test
	public void setAndGetStrTest() {
		try {
			ClipboardUtil.setStr("test");

			final String test = ClipboardUtil.getStr();
			Assertions.assertEquals("test", test);
		} catch (final java.awt.HeadlessException e) {
			// 忽略 No X11 DISPLAY variable was set, but this program performed an operation which requires it.
			// ignore
		}
	}
}
