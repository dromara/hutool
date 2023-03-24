package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class IssueI6H0XFTest {
	@Test
	public void toBeanTest(){
		final Demo demo = JSONUtil.toBean("{\"biz\":\"A\",\"isBiz\":true}", Demo.class);
		Assert.assertEquals("A", demo.getBiz());
		Assert.assertEquals("{\"biz\":\"A\"}", JSONUtil.toJsonStr(demo));
	}

	@Data
	static class Demo {
		String biz;
	}
}
