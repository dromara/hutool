package cn.hutool.json;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class Issue2365Test {

	@Test
	public void toBeanTest(){
		String jsonStr = "{\"fileName\":\"aaa\",\"fileBytes\":\"AQ==\"}";
		final FileInfo fileInfo = JSONUtil.toBean(jsonStr, FileInfo.class);
		Assert.assertEquals("aaa", fileInfo.getFileName());
		Assert.assertArrayEquals(new byte[]{1}, fileInfo.getFileBytes());
	}

	@Data
	public static class FileInfo {
		private String fileName;
		private byte[] fileBytes;
	}
}
