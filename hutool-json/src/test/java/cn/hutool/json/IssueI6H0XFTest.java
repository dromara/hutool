package cn.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI6H0XFTest {

	@Test
	public void toBeanTest(){
		final Demo demo = JSONUtil.toBean("{\"biz\":\"A\",\"isBiz\":true}", Demo.class);
		Assertions.assertEquals("A", demo.getBiz());
		Assertions.assertEquals("{\"biz\":\"A\"}", JSONUtil.toJsonStr(demo));
	}

	@Data
	static class Demo {
		String biz;
	}
}
