package cn.hutool.json;

import cn.hutool.core.convert.Convert;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Issue2377Test {
	@Test
	public void bytesTest() {
		final Object[] paramArray = new Object[]{1, new byte[]{10, 11}, "报表.xlsx"};
		final String paramsStr = JSONUtil.toJsonStr(paramArray);

		final List<Object> paramList = JSONUtil.toList(paramsStr, Object.class);

		final String paramBytesStr = JSONUtil.toJsonStr(paramList.get(1));

		final byte[] convert = Convert.convert(byte[].class, paramBytesStr);
		Assert.assertArrayEquals((byte[]) paramArray[1], convert);
	}
}
