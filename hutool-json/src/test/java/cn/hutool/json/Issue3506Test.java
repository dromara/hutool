package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/dromara/hutool/issues/3506
 */
public class Issue3506Test {

	@Test
	public void test3506() {
		Languages languages = new Languages();
		languages.setLanguageType(Java.class);
		String hutoolJSONString = JSONUtil.toJsonStr(languages);
		final Languages bean = JSONUtil.toBean(hutoolJSONString, Languages.class);
		Assert.assertNotNull(bean);
		Assert.assertEquals(bean.getLanguageType(), Java.class);
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
