package org.dromara.hutool.net;

import org.dromara.hutool.net.url.FormUrlencoded;
import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormUrlencodedTest {

	@Test
	public void encodeParamTest(){
		String encode = FormUrlencoded.ALL.encode("a+b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a%2Bb", encode);

		encode = FormUrlencoded.ALL.encode("a b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a+b", encode);
	}
}
