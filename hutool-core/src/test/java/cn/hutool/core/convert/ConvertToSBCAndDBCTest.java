package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

/**
 * 类型转换工具单元测试
 * 全角半角转换
 *
 * @author Looly
 *
 */
public class ConvertToSBCAndDBCTest {

	@Test
	public void toSBCTest() {
		final String a = "123456789";
		final String sbc = Convert.toSBC(a);
		Assert.assertEquals("１２３４５６７８９", sbc);
	}

	@Test
	public void toDBCTest() {
		final String a = "１２３４５６７８９";
		final String dbc = Convert.toDBC(a);
		Assert.assertEquals("123456789", dbc);
	}
}
