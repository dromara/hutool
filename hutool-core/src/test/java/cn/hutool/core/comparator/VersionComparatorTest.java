package cn.hutool.core.comparator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 版本比较单元测试
 *
 * @author looly
 *
 */
public class VersionComparatorTest {

	@Test
	public void versionComparatorTest1() {
		final int compare = VersionComparator.INSTANCE.compare("1.2.1", "1.12.1");
		Assertions.assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTest2() {
		final int compare = VersionComparator.INSTANCE.compare("1.12.1", "1.12.1c");
		Assertions.assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTest3() {
		final int compare = VersionComparator.INSTANCE.compare(null, "1.12.1c");
		Assertions.assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTest4() {
		final int compare = VersionComparator.INSTANCE.compare("1.13.0", "1.12.1c");
		Assertions.assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTest5() {
		final int compare = VersionComparator.INSTANCE.compare("V1.2", "V1.1");
		Assertions.assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTes6() {
		final int compare = VersionComparator.INSTANCE.compare("V0.0.20170102", "V0.0.20170101");
		Assertions.assertTrue(compare > 0);
	}

	@Test
	public void equalsTest(){
		final VersionComparator first = new VersionComparator();
		final VersionComparator other = new VersionComparator();
		Assertions.assertNotEquals(first, other);
	}
}
