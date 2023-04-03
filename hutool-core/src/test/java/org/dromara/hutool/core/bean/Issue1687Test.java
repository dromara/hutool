package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.annotation.Alias;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.map.MapUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

/**
 * https://github.com/dromara/hutool/issues/1687
 */
public class Issue1687Test {

	@Test
	public void toBeanTest(){
		final SysUserFb sysUserFb = new SysUserFb();
		sysUserFb.setDepId("123");
		sysUserFb.setCustomerId("456");

		final SysUser sysUser = BeanUtil.toBean(sysUserFb, SysUser.class);
		// 别名错位导致找不到字段
		Assertions.assertNull(sysUser.getDepart());
		Assertions.assertEquals(new Long(456L), sysUser.getOrgId());
	}

	@Test
	public void toBeanTest2(){
		final SysUserFb sysUserFb = new SysUserFb();
		sysUserFb.setDepId("123");
		sysUserFb.setCustomerId("456");

		// 补救别名错位
		final CopyOptions copyOptions = CopyOptions.of().setFieldMapping(
				MapUtil.builder("depart", "depId").build()
		);
		final SysUser sysUser = BeanUtil.toBean(sysUserFb, SysUser.class, copyOptions);

		Assertions.assertEquals(new Long(123L), sysUser.getDepart());
		Assertions.assertEquals(new Long(456L), sysUser.getOrgId());
	}

	@Data
	static class SysUserFb implements Serializable {

		private static final long serialVersionUID = 1L;

		@Alias("depart")
		private String depId;

		@Alias("orgId")
		private String customerId;
	}

	@Data
	static class SysUser implements Serializable {

		private static final long serialVersionUID = 1L;

		@Alias("depId")
		private Long depart;

		private Long orgId;
	}
}
