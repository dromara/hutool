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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.collection.CollUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.EnumSet;

/**
 * EnumSet创建时无法自动获取其元素类型，通过传入方式获取
 */
public class Issue2683Test {

	enum Version {
		dev,
		prod
	}

	@Data
	public static class Vto {
		EnumSet<Version> versions;
	}


	@Test
	public void beanWithEnumSetTest() {
		final Vto v1 = new Vto();
		v1.setVersions(EnumSet.allOf(Version.class));
		final Vto v2 = BeanUtil.copyProperties(v1, Vto.class);
		Assertions.assertNotNull(v2);
		Assertions.assertNotNull(v2.getVersions());
	}

	@Test
	public void enumSetTest() {
		final Collection<Version> objects = CollUtil.create(EnumSet.class, Version.class);
		Assertions.assertNotNull(objects);
		Assertions.assertTrue(EnumSet.class.isAssignableFrom(objects.getClass()));
	}
}
