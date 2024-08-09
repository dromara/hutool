package cn.hutool.json;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IssueI84V6ITest {
	@Test
	public void formatTest() {
		final String a1 = "{'x':'\\n','y':','}";
		final String formatJsonStr = JSONUtil.formatJsonStr(a1);
//		Console.log(formatJsonStr);
		assertEquals(
			"{\n" +
				"    'x': '\\n',\n" +
				"    'y': ','\n" +
				"}", formatJsonStr);
	}
}
