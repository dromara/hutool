package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class IssueI59LW4Test {

	@Test
	public void bytesTest(){
		final JSONObject jsonObject = JSONUtil.createObj().set("bytes", new byte[]{1});
		Assert.assertEquals("{\"bytes\":[1]}", jsonObject.toString());

		final byte[] bytes = jsonObject.getBytes("bytes");
		Assert.assertArrayEquals(new byte[]{1}, bytes);
	}

	@Test
	public void bytesInJSONArrayTest(){
		final JSONArray jsonArray = JSONUtil.createArray().set(new byte[]{1});
		Assert.assertEquals("[[1]]", jsonArray.toString());

		final byte[] bytes = jsonArray.getBytes(0);
		Assert.assertArrayEquals(new byte[]{1}, bytes);
	}
}
