package cn.hutool.core.text;

import cn.hutool.core.text.split.SplitUtil;
import org.junit.Assert;
import org.junit.Test;

public class SplitUtilTest {

	@Test
	public void issueI6FKSITest(){
		// issue:I6FKSI
		Assert.assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitByLength("test length 0", 0));
	}

}
