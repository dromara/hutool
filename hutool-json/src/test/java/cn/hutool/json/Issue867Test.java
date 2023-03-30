package cn.hutool.json;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue867Test {

	@Test
	public void toBeanTest(){
		final String json = "{\"abc_1d\":\"123\",\"abc_d\":\"456\",\"abc_de\":\"789\"}";
		final Test02 bean = JSONUtil.toBean(JSONUtil.parseObj(json),Test02.class);
		Assertions.assertEquals("123", bean.getAbc1d());
		Assertions.assertEquals("456", bean.getAbcD());
		Assertions.assertEquals("789", bean.getAbcDe());
	}

	@Data
	static class Test02 {
		@Alias("abc_1d")
		private String abc1d;
		private String abcD;
		private String abcDe;
	}
}
