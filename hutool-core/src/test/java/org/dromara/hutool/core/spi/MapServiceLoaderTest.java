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
