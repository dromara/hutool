package cn.hutool.json;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IssueI59LW4Test {

	@Test
	public void bytesTest(){
		final JSONObject jsonObject = JSONUtil.createObj().set("bytes", new byte[]{1});
		assertEquals("{\"bytes\":[1]}", jsonObject.toString());

		final byte[] bytes = jsonObject.getBytes("bytes");
		assertArrayEquals(new byte[]{1}, bytes);
	}

	@Test
	public void bytesInJSONArrayTest(){
		final JSONArray jsonArray = JSONUtil.createArray().set(new byte[]{1});
		assertEquals("[[1]]", jsonArray.toString());

		final byte[] bytes = jsonArray.getBytes(0);
		assertArrayEquals(new byte[]{1}, bytes);
	}
}
