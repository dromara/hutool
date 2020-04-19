package cn.hutool.core.comparator;

import org.junit.Assert;
import org.junit.Test;

public class CompareUtilTest {

	@Test
	public void compareTest(){
		int compare = CompareUtil.compare(null, "a", true);
		Assert.assertTrue(compare > 0);

		compare = CompareUtil.compare(null, "a", false);
		Assert.assertTrue(compare < 0);
	}
}
