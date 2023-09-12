package cn.hutool.core.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class IssueI80FP4Test {
	@Test
	public void copyPropertiesTest() {
		final Dest sourceDest = new Dest();
		sourceDest.setCPF(33699);
		sourceDest.setEnderDest("abc");

		final Dest dest = new Dest();
		final CopyOptions copyOptions = CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true).setIgnoreProperties("enderDest");
		BeanUtil.copyProperties(sourceDest, dest, copyOptions);
		Assert.assertNull(dest.getEnderDest());
	}

	@Data
	static class Dest{
		private int cPF;
		private String enderDest;
	}
}
