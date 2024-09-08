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

package org.dromara.hutool.crypto.bc;

import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.dromara.hutool.crypto.KeyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;

public class ECKeyUtilTest {

	/**
	 * 密钥生成来自：<a href="https://i.goto327.top/CryptTools/SM2.aspx?tdsourcetag=s_pctim_aiomsg">...</a>
	 */
	@Test
	public void createECPublicKeyParametersTest() {
		final String x = "706AD9DAA3E5CEAC3DA59F583429E8043BAFC576BE10092C4EA4D8E19846CA62";
		final String y = "F7E938B02EED7280277493B8556E5B01CB436E018A562DFDC53342BF41FDF728";

		final ECPublicKeyParameters keyParameters = ECKeyUtil.toSm2PublicParams(x, y);
		Assertions.assertNotNull(keyParameters);
	}

	@Test
	public void createECPrivateKeyParametersTest() {
		final String privateKeyHex = "5F6CA5BB044C40ED2355F0372BF72A5B3AE6943712F9FDB7C1FFBAECC06F3829";

		final ECPrivateKeyParameters keyParameters = ECKeyUtil.toSm2PrivateParams(privateKeyHex);
		Assertions.assertNotNull(keyParameters);
	}

	@Test
	void getECPublicKeyTest() {
		final KeyPair sm2 = KeyUtil.generateKeyPair("sm2");
		final PublicKey ecPublicKey = ECKeyUtil.getECPublicKey((ECPrivateKey) sm2.getPrivate(), SM2Constant.SM2_EC_SPEC);
		Assertions.assertEquals(sm2.getPublic(), ecPublicKey);
	}
}
