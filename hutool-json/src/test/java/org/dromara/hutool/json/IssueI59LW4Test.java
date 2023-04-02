package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI59LW4Test {
	@Test
	public void bytesTest(){
		final JSONObject jsonObject = JSONUtil.ofObj().set("bytes", new byte[]{1});
		Assertions.assertEquals("{\"bytes\":[1]}", jsonObject.toString());

		final byte[] bytes = jsonObject.getBytes("bytes");
		Assertions.assertArrayEquals(new byte[]{1}, bytes);
	}

	@Test
	public void bytesInJSONArrayTest(){
		final JSONArray jsonArray = JSONUtil.ofArray().set(new byte[]{1});
		Assertions.assertEquals("[[1]]", jsonArray.toString());

		final byte[] bytes = jsonArray.getBytes(0);
		Assertions.assertArrayEquals(new byte[]{1}, bytes);
	}
}
