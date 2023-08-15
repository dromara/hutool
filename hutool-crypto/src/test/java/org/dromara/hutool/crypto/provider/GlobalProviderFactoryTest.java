/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.Provider;

/**
 * 单元测试，只针对bouncycastle引入有效
 */
public class GlobalProviderFactoryTest {

	@Test
	void getProviderTest() {
		final Provider provider = GlobalProviderFactory.getProvider();
		Assertions.assertNotNull(provider);
		Assertions.assertEquals(
			"org.bouncycastle.jce.provider.BouncyCastleProvider",
			provider.getClass().getName());
	}

}
