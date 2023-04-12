/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.bc;

import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BCUtilTest {

	/**
	 * 密钥生成来自：<a href="https://i.goto327.top/CryptTools/SM2.aspx?tdsourcetag=s_pctim_aiomsg">...</a>
	 */
	@Test
	public void createECPublicKeyParametersTest() {
		final String x = "706AD9DAA3E5CEAC3DA59F583429E8043BAFC576BE10092C4EA4D8E19846CA62";
		final String y = "F7E938B02EED7280277493B8556E5B01CB436E018A562DFDC53342BF41FDF728";

		final ECPublicKeyParameters keyParameters = BCUtil.toSm2Params(x, y);
		Assertions.assertNotNull(keyParameters);
	}

	@Test
	public void createECPrivateKeyParametersTest() {
		final String privateKeyHex = "5F6CA5BB044C40ED2355F0372BF72A5B3AE6943712F9FDB7C1FFBAECC06F3829";

		final ECPrivateKeyParameters keyParameters = BCUtil.toSm2Params(privateKeyHex);
		Assertions.assertNotNull(keyParameters);
	}
}
