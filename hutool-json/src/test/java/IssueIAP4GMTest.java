import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAP4GMTest {
	@Test
	void parse() {
		String res = "{\"uid\":\"asdf\\n\"}";
		final JSONObject entries = JSONUtil.parseObj(res);
		Assertions.assertEquals("asdf\n", entries.getStr("uid"));
	}
}
