package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3506Test {

	@Test
	void toBeanTest() {
		final Languages languages = new Languages();
		languages.setLanguageType(Java.class);

		final String hutoolJSONString = JSONUtil.toJsonStr(languages);
		final Languages bean = JSONUtil.toBean(hutoolJSONString, Languages.class);
		Assertions.assertNotNull(bean);
		Assertions.assertEquals(bean.getLanguageType(), Java.class);
	}

	@Data
	public static class Languages {
		private Class<? extends Language> languageType;
	}

	public interface Language {
	}

	public static class Java implements Language {
	}
}
