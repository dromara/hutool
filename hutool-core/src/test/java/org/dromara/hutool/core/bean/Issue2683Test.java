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
