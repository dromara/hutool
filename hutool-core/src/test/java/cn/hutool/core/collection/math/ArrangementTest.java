package cn.hutool.core.collection.math;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 排列单元测试
 * @author looly
 *
 */
public class ArrangementTest {
	
	@Test
	public void arrangementTest() {
		long result = Arrangement.count(4, 2);
		Assert.assertEquals(12, result);
	}
	
	@Test
	public void selectTest() {
		Arrangement arrangement = new Arrangement(new String[] { "1", "2", "3", "4" }, 2);
		List<String[]> list = arrangement.select();
		Assert.assertEquals(Arrangement.count(4, 2), list.size());
	}
}
