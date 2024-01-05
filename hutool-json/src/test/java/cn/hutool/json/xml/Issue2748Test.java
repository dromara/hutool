package cn.hutool.json.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.XML;
import org.junit.Assert;
import org.junit.Test;

public class Issue2748Test {

	@Test
	public void toJSONObjectTest() {
		final String s = StrUtil.repeat("<a>", 600);

		Assert.assertThrows(JSONException.class, () -> {
			XML.toJSONObject(s, ParseConfig.of().setMaxNestingDepth(512));
		});
	}
}
