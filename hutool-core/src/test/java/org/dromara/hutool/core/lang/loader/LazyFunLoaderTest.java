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
