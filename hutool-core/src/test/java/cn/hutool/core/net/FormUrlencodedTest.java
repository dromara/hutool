package cn.hutool.core.net;

import cn.hutool.core.net.url.FormUrlencoded;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

public class FormUrlencodedTest {

	@Test
	public void encodeParamTest(){
		String encode = FormUrlencoded.ALL.encode("a+b", CharsetUtil.UTF_8);
		Assert.assertEquals("a%2Bb", encode);

		encode = FormUrlencoded.ALL.encode("a b", CharsetUtil.UTF_8);
		Assert.assertEquals("a+b", encode);
	}
}
