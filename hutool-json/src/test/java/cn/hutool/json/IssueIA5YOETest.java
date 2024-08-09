package cn.hutool.json;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IssueIA5YOETest {
	@Test
	public void parseObjTest() {
		String res ="{\"id\":\"chatcmpl-9azySxFBm5hjtaMCccREpCHF3gbQ3\",\"object\":\"chat.completion.chunk\",\"created\":1718605064,\"model\":\"gpt-4o-2024-05-13\",\"system_fingerprint\":\"fp_319be4768e\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"\\\"\"},\"logprobs\":null,\"finish_reason\":null}]}";
		assertNotNull(JSONUtil.parseObj(res));
	}
}
