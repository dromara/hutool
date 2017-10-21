package com.xiaoleilu.hutool.convert;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.convert.Convert;

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
		Assert.assertEquals("１２３４５６７８９", sbc);
	}
	
	@Test
	public void toDBCTest() {
		String a = "１２３４５６７８９";
		String dbc = Convert.toDBC(a);
		Assert.assertEquals("123456789", dbc);
	}
}
