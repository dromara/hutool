package cn.hutool.json;

import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
		final JsonBean bean = JSONUtil.toBean(huToolJsonStr, JsonBean.class);
		assertNotNull(bean);
		assertEquals("test", bean.getName());
	}

	@Data
	public static class JsonBean {
		private String name;
		private Class<?>[] classes;
	}
}
