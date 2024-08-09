package cn.hutool.json;

import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Issue2365Test {

	@Test
	public void toBeanTest(){
		String jsonStr = "{\"fileName\":\"aaa\",\"fileBytes\":\"AQ==\"}";
		final FileInfo fileInfo = JSONUtil.toBean(jsonStr, FileInfo.class);
		assertEquals("aaa", fileInfo.getFileName());
		assertArrayEquals(new byte[]{1}, fileInfo.getFileBytes());
	}

	@Data
	public static class FileInfo {
		private String fileName;
		private byte[] fileBytes;
	}
}
