package cn.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		String a = "123456789";
		String sbc = Convert.toSBC(a);
		Assertions.assertEquals("１２３４５６７８９", sbc);
	}

	@Test
	public void toDBCTest() {
		String a = "１２３４５６７８９";
		String dbc = Convert.toDBC(a);
		Assertions.assertEquals("123456789", dbc);
	}
}
