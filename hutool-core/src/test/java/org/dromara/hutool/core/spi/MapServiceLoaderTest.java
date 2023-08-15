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

package org.dromara.hutool.core.spi;

import org.dromara.hutool.core.exception.HutoolException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapServiceLoaderTest {

	@Test
	void loadFirstAvailableTest() {
		final MapServiceLoader<TestSPI1> serviceLoader = MapServiceLoader.of(TestSPI1.class);
		final TestSPI1 testSPI1 = SpiUtil.loadFirstAvailable(serviceLoader);
		Assertions.assertNotNull(testSPI1);
		Assertions.assertEquals("test service 1", testSPI1.doSth());
	}

	@Test
	void getServiceClassTest() {
		final MapServiceLoader<TestSPI1> serviceLoader = MapServiceLoader.of(TestSPI1.class);
		final Class<TestSPI1> service1 = serviceLoader.getServiceClass("service1");
		Assertions.assertEquals(TestService1.class, service1);
	}

	@Test
	void getServiceClassNotExistTest() {
		final MapServiceLoader<TestSPI1> serviceLoader = MapServiceLoader.of(TestSPI1.class);
		Assertions.assertThrows(HutoolException.class, ()->{
			serviceLoader.getServiceClass("service2");
		});
	}

	@Test
	void getServiceClassEmptyTest() {
		final MapServiceLoader<TestSPI1> serviceLoader = MapServiceLoader.of(TestSPI1.class);
		final Class<TestSPI1> serviceEmpty = serviceLoader.getServiceClass("serviceEmpty");
		Assertions.assertNull(serviceEmpty);
	}

	@Test
	void getServiceNotDefineTest() {
		final MapServiceLoader<TestSPI1> serviceLoader = MapServiceLoader.of(TestSPI1.class);
		final Class<TestSPI1> service1 = serviceLoader.getServiceClass("serviceNotDefine");
		Assertions.assertNull(service1);
	}

	public interface TestSPI1{
		String doSth();
	}

	public static class TestService1 implements TestSPI1{

		@Override
		public String doSth() {
			return "test service 1";
		}
	}
}
