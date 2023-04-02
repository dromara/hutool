package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * JSON字符串格式化单元测试
 * @author looly
 *
 */
public class JSONStrFormatterTest {

	@Test
	public void formatTest() {
		final String json = "{'age':23,'aihao':['pashan','movies'],'name':{'firstName':'zhang','lastName':'san','aihao':['pashan','movies','name':{'firstName':'zhang','lastName':'san','aihao':['pashan','movies']}]}}";
		final String result = JSONStrFormatter.format(json);
		Assertions.assertNotNull(result);
	}

	@Test
	public void formatTest2() {
		final String json = "{\"abc\":{\"def\":\"\\\"[ghi]\"}}";
		final String result = JSONStrFormatter.format(json);
		Assertions.assertNotNull(result);
	}

	@Test
	public void formatTest3() {
		final String json = "{\"id\":13,\"title\":\"《标题》\",\"subtitle\":\"副标题z'c'z'xv'c'xv\",\"user_id\":6,\"type\":0}";
		final String result = JSONStrFormatter.format(json);
		Assertions.assertNotNull(result);
	}

	@Test
	@Disabled
	public void formatTest4(){
		final String jsonStr = "{\"employees\":[{\"firstName\":\"Bill\",\"lastName\":\"Gates\"},{\"firstName\":\"George\",\"lastName\":\"Bush\"},{\"firstName\":\"Thomas\",\"lastName\":\"Carter\"}]}";
		Console.log(JSONUtil.formatJsonStr(jsonStr));
	}
}
