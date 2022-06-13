package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Issue2377Test {

	@Test
	public void bytesTest() {
		final Object[] paramArray = new Object[]{1, new byte[]{10, 11}, "报表.xlsx"};
		final String paramsStr = JSONUtil.toJsonStr(paramArray);
		Assert.assertEquals("[1,[10,11],\"报表.xlsx\"]", paramsStr);

		final List<Object> paramList = JSONUtil.toList(paramsStr, Object.class);

		final String paramBytesStr = JSONUtil.toJsonStr(paramList.get(1));
		Assert.assertEquals("[10,11]", paramBytesStr);

		final byte[] paramBytes = JSONUtil.toBean(paramBytesStr, byte[].class, false);
		Assert.assertArrayEquals((byte[]) paramArray[1], paramBytes);
	}

}
