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

package org.dromara.hutool.core.lang.loader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LazyFunLoaderTest {

	static class BigObject {

		private boolean isDestroy = false;

		public void destroy() {
			this.isDestroy = true;
		}
	}

	@Test
	public void test1() {

		final LazyFunLoader<BigObject> loader = new LazyFunLoader<>(BigObject::new);

		Assertions.assertNotNull(loader.get());
		Assertions.assertTrue(loader.isInitialize());

		// 对于某些对象，在程序关闭时，需要进行销毁操作
		loader.ifInitialized(BigObject::destroy);

		Assertions.assertTrue(loader.get().isDestroy);
	}

	@Test
	public void test2() {

		final LazyFunLoader<BigObject> loader = new LazyFunLoader<>(BigObject::new);

		// 若从未使用，则可以避免不必要的初始化
		loader.ifInitialized(it -> {

			Assertions.fail();
			it.destroy();
		});

		Assertions.assertFalse(loader.isInitialize());
	}

	@Test
	public void testOnLoadStaticFactoryMethod1() {

		final LazyFunLoader<BigObject> loader = LazyFunLoader.on(BigObject::new);

		Assertions.assertNotNull(loader.get());
		Assertions.assertTrue(loader.isInitialize());

		// 对于某些对象，在程序关闭时，需要进行销毁操作
		loader.ifInitialized(BigObject::destroy);

		Assertions.assertTrue(loader.get().isDestroy);
	}

	@Test
	public void testOnLoadStaticFactoryMethod2() {

		final LazyFunLoader<BigObject> loader = LazyFunLoader.on(BigObject::new);

		// 若从未使用，则可以避免不必要的初始化
		loader.ifInitialized(it -> {

			Assertions.fail();
			it.destroy();
		});

	}
}
