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

package org.dromara.hutool.core.spi;

import org.dromara.hutool.core.exceptions.HutoolException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListServiceLoaderTest {

	@Test
	void loadFirstAvailableTest() {
		final ListServiceLoader<TestSPI1> serviceLoader = ListServiceLoader.of(TestSPI1.class);
		final TestSPI1 testSPI1 = SpiUtil.loadFirstAvailable(serviceLoader);
		Assertions.assertNotNull(testSPI1);
		Assertions.assertEquals("test service 1", testSPI1.doSth());
	}

	@Test
	void getServiceClassTest() {
		final ListServiceLoader<TestSPI1> serviceLoader = ListServiceLoader.of(TestSPI1.class);
		final Class<TestSPI1> service1 = serviceLoader.getServiceClass(1);
		Assertions.assertEquals(TestService1.class, service1);
	}

	@Test
	void getServiceClassNotExistTest() {
		final ListServiceLoader<TestSPI1> serviceLoader = ListServiceLoader.of(TestSPI1.class);
		Assertions.assertThrows(HutoolException.class, ()->{
			serviceLoader.getServiceClass(0);
		});
	}

	public interface TestSPI1{
		String doSth();
	}

	public static class TestService1 implements TestSPI1 {
		@Override
		public String doSth() {
			return "test service 1";
		}
	}
}
