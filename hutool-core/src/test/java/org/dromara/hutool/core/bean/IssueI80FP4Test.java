package org.dromara.hutool.core.bean;

import lombok.Data;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI80FP4Test {
	@Test
	public void copyPropertiesTest() {
		final Dest sourceDest = new Dest();
		sourceDest.setCPF(33699);
		sourceDest.setEnderDest("abc");

		final Dest dest = new Dest();
		final CopyOptions copyOptions = CopyOptions.of()
			.setIgnoreNullValue(true)
			.setIgnoreCase(true)
			.setIgnoreProperties("enderDest");

		BeanUtil.copyProperties(sourceDest, dest, copyOptions);
		Assertions.assertNull(dest.getEnderDest());
	}

	@Test
	public void copyPropertiesTest2() {
		final Dest sourceDest = new Dest();
		sourceDest.setCPF(33699);
		sourceDest.setEnderDest("abc");

		final Dest dest = new Dest();
		final CopyOptions copyOptions = CopyOptions.of()
			.setIgnoreNullValue(true)
			.setIgnoreCase(true)
			.setIgnoreProperties("enderdest");

		BeanUtil.copyProperties(sourceDest, dest, copyOptions);
		Assertions.assertNull(dest.getEnderDest());
	}

	@Data
	static class Dest{
		private int cPF;
		private String enderDest;
	}
}
