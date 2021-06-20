package cn.hutool.json;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

/**
 * JSON字符串格式化单元测试
 * @author looly
 *
 */
public class JSONStrFormatterTest {

	@Test
	public void formatTest() {
		String json = "{'age':23,'aihao':['pashan','movies'],'name':{'firstName':'zhang','lastName':'san','aihao':['pashan','movies','name':{'firstName':'zhang','lastName':'san','aihao':['pashan','movies']}]}}";
		String result = JSONStrFormatter.format(json);
		Assert.assertNotNull(result);
	}

	@Test
	public void formatTest2() {
		String json = "{\"abc\":{\"def\":\"\\\"[ghi]\"}}";
		String result = JSONStrFormatter.format(json);
		Assert.assertNotNull(result);
	}

	@Test
	public void formatTest3() {
		String json = "{\"id\":13,\"title\":\"《标题》\",\"subtitle\":\"副标题z'c'z'xv'c'xv\",\"user_id\":6,\"type\":0}";
		String result = JSONStrFormatter.format(json);
		Assert.assertNotNull(result);
	}

	@Test
	public void formatTest4(){
		String jsonStr = "{\"employees\":[{\"firstName\":\"Bill\",\"lastName\":\"Gates\"},{\"firstName\":\"George\",\"lastName\":\"Bush\"},{\"firstName\":\"Thomas\",\"lastName\":\"Carter\"}]}";
		Console.log(JSONUtil.formatJsonStr(jsonStr));
	}
}
