package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * https://gitee.com/dromara/hutool/issues/I4RBZ4
 */
public class IssueI4RBZ4Test {

	@Test
	public void sortTest(){
		String jsonStr = "{\"id\":\"123\",\"array\":[1,2,3],\"outNum\":356,\"body\":{\"ava1\":\"20220108\",\"use\":1,\"ava2\":\"20230108\"},\"name\":\"John\"}";

		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr, JSONConfig.create().setNatureKeyComparator());
		Assert.assertEquals("{\"array\":[1,2,3],\"body\":{\"ava1\":\"20220108\",\"ava2\":\"20230108\",\"use\":1},\"id\":\"123\",\"name\":\"John\",\"outNum\":356}", jsonObject.toString());
	}
}
