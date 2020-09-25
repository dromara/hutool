package cn.hutool.core.comparator;

import org.junit.Assert;
import org.junit.Test;

/**
 * 版本比较单元测试
 * 
 * @author looly
 *
 */
public class VersionComparatorTest {
	
	@Test
	public void versionComparatorTest1() {
		int compare = VersionComparator.INSTANCE.compare("1.2.1", "1.12.1");
		Assert.assertTrue(compare < 0);
	}
	
	@Test
	public void versionComparatorTest2() {
		int compare = VersionComparator.INSTANCE.compare("1.12.1", "1.12.1c");
		Assert.assertTrue(compare < 0);
	}
	
	@Test
	public void versionComparatorTest3() {
		int compare = VersionComparator.INSTANCE.compare(null, "1.12.1c");
		Assert.assertTrue(compare < 0);
	}
	
	@Test
	public void versionComparatorTest4() {
		int compare = VersionComparator.INSTANCE.compare("1.13.0", "1.12.1c");
		Assert.assertTrue(compare > 0);
	}
	
	@Test
	public void versionComparatorTest5() {
		int compare = VersionComparator.INSTANCE.compare("V1.2", "V1.1");
		Assert.assertTrue(compare > 0);
	}
	
	@Test
	public void versionComparatorTes6() {
		int compare = VersionComparator.INSTANCE.compare("V0.0.20170102", "V0.0.20170101");
		Assert.assertTrue(compare > 0);
	}

	@Test
	public void equalsTest(){
		VersionComparator first = new VersionComparator();
		VersionComparator other = new VersionComparator();
		Assert.assertFalse(first.equals(other));
	}
}
