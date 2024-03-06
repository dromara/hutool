package cn.hutool.json;

import lombok.Data;
import org.junit.Test;

/**
 * https://github.com/dromara/hutool/issues/3504
 */
public class Issue3504Test {

	@Test
	public void test3504() {
		JsonBean jsonBean = new JsonBean();
		jsonBean.setName("test");
		jsonBean.setClasses(new Class[]{String.class});
		String huToolJsonStr = JSONUtil.toJsonStr(jsonBean);
		System.out.println("hutool json str-------" + huToolJsonStr);
		System.out.println(JSONUtil.toBean(huToolJsonStr, JsonBean.class));
	}

	@Data
	public static class JsonBean {
		private String name;
		private Class<?>[] classes;
	}
}
