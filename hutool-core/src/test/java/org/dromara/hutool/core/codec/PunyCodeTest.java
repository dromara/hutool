/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PunyCodeTest {

	@Test
	public void encodeDecodeTest() {
		final String text = "Hutool编码器";
		final String strPunyCode = PunyCode.encode(text);
		Assertions.assertEquals("Hutool-ux9js33tgln", strPunyCode);
		String decode = PunyCode.decode("Hutool-ux9js33tgln");
		Assertions.assertEquals(text, decode);
		decode = PunyCode.decode("xn--Hutool-ux9js33tgln");
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeTest2(){
		// 无需编码和解码
		final String text = "Hutool";
		final String strPunyCode = PunyCode.encode(text);
		Assertions.assertEquals("Hutool", strPunyCode);
	}

	@Test
	public void encodeDecodeDomainTest() {
		// 全中文
		final String text = "百度.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assertions.assertEquals("xn--wxtr44c.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeDomainTest2() {
		// 中英文分段
		final String text = "hutool.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assertions.assertEquals("hutool.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeDomainTest3() {
		// 中英文混合
		final String text = "hutool工具.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assertions.assertEquals("xn--hutool-up2j943f.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeEncodeDomainTest2(){
		final String domain = "赵新虎.com";
		final String strPunyCode = PunyCode.encodeDomain(domain);
		Assertions.assertEquals("xn--efvz93e52e.com", strPunyCode);
		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(domain, decode);
	}
}
