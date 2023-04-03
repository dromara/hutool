package org.dromara.hutool.core.codec;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Base64单元测试
 *
 * @author looly
 *
 */
public class Base64Test {

	@Test
	public void isBase64Test(){
		Assertions.assertTrue(Base64.isBase64(Base64.encode(RandomUtil.randomString(1000))));
	}

	@Test
	public void isBase64Test2(){
		String base64 = "dW1kb3MzejR3bmljM2J6djAyZzcwbWk5M213Nnk3cWQ3eDJwOHFuNXJsYmMwaXhxbmg0dmxrcmN0anRkbmd3\n" +
				"ZzcyZWFwanI2NWNneTg2dnp6cmJoMHQ4MHpxY2R6c3pjazZtaQ==";
		Assertions.assertTrue(Base64.isBase64(base64));

		// '=' 不位于末尾
		base64 = "dW1kb3MzejR3bmljM2J6=djAyZzcwbWk5M213Nnk3cWQ3eDJwOHFuNXJsYmMwaXhxbmg0dmxrcmN0anRkbmd3\n" +
				"ZzcyZWFwanI2NWNneTg2dnp6cmJoMHQ4MHpxY2R6c3pjazZtaQ=";
		Assertions.assertFalse(Base64.isBase64(base64));
	}

	@Test
	public void encodeAndDecodeTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base64.encode(a);
		Assertions.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5LiyNjY=", encode);

		final String decodeStr = Base64.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeWithoutPaddingTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base64.encodeWithoutPadding(ByteUtil.toUtf8Bytes(a));
		Assertions.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5LiyNjY", encode);

		final String decodeStr = Base64.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeTest2() {
		final String a = "a61a5db5a67c01445ca2-HZ20181120172058/pdf/中国电信影像云单体网关Docker版-V1.2.pdf";
		final String encode = Base64.encode(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("YTYxYTVkYjVhNjdjMDE0NDVjYTItSFoyMDE4MTEyMDE3MjA1OC9wZGYv5Lit5Zu955S15L+h5b2x5YOP5LqR5Y2V5L2T572R5YWzRG9ja2Vy54mILVYxLjIucGRm", encode);

		final String decodeStr = Base64.decodeStr(encode, CharsetUtil.UTF_8);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeTest3() {
		final String a = ":";
		final String encode = Base64.encode(a);
		Assertions.assertEquals("Og==", encode);

		final String decodeStr = Base64.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeGbkTest(){
		final String orderDescription = "订购成功立即生效，30天内可观看专区中除单独计费影片外的所有内容，到期自动取消。";
		final String result = Base64.encode(orderDescription, CharsetUtil.GBK);

		final String s = Base64.decodeStr(result, CharsetUtil.GBK);
		Assertions.assertEquals(orderDescription, s);
	}

	@Test
	public void decodeEmojiTest(){
		final String str = "😄";
		final String encode = Base64.encode(str);
//		Console.log(encode);

		final String decodeStr = Base64.decodeStr(encode);
		Assertions.assertEquals(str, decodeStr);
	}
}
