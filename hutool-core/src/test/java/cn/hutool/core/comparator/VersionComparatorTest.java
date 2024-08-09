package cn.hutool.core.comparator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 版本比较单元测试
 *
 * @author looly
 */
public class VersionComparatorTest {

	@Test
	public void compareEmptyTest() {
		int compare = VersionComparator.INSTANCE.compare("", "1.12.1");
		assertTrue(compare < 0);

		compare = VersionComparator.INSTANCE.compare("", null);
		assertTrue(compare > 0);
		compare = VersionComparator.INSTANCE.compare(null, "");
		assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTest1() {
		int compare = VersionComparator.INSTANCE.compare("1.2.1", "1.12.1");
		assertTrue(compare < 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1", "1.2.1");
		assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTest2() {
		int compare = VersionComparator.INSTANCE.compare("1.12.1", "1.12.1c");
		assertTrue(compare < 0);

		compare = VersionComparator.INSTANCE.compare("1.12.1c", "1.12.1");
		assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTest3() {
		int compare = VersionComparator.INSTANCE.compare(null, "1.12.1c");
		assertTrue(compare < 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1c", null);
		assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTest4() {
		int compare = VersionComparator.INSTANCE.compare("1.13.0", "1.12.1c");
		assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1c", "1.13.0");
		assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTest5() {
		int compare = VersionComparator.INSTANCE.compare("V1.2", "V1.1");
		assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("V1.1", "V1.2");
		assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTes6() {
		int compare = VersionComparator.INSTANCE.compare("V0.0.20170102", "V0.0.20170101");
		assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("V0.0.20170101", "V0.0.20170102");
		assertTrue(compare < 0);
	}

	@Test
	public void equalsTest() {
		VersionComparator first = new VersionComparator();
		VersionComparator other = new VersionComparator();
		assertNotEquals(first, other);
	}

	@Test
	public void versionComparatorTest7() {
		int compare = VersionComparator.INSTANCE.compare("1.12.2", "1.12.1c");
		assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1c", "1.12.2");
		assertTrue(compare < 0);
	}

	@Test
	public void equalsTest2() {
		final int compare = VersionComparator.INSTANCE.compare("1.12.0", "1.12");
		assertEquals(0, compare);
	}

	@Test
	public void I8Z3VETest() {
		// 传递性测试
		int compare = VersionComparator.INSTANCE.compare("260", "a-34");
		assertTrue(compare > 0);
		compare = VersionComparator.INSTANCE.compare("a-34", "a-3");
		assertTrue(compare > 0);
		compare = VersionComparator.INSTANCE.compare("260", "a-3");
		assertTrue(compare > 0);
	}

	@Test
	public void startWithNoneNumberTest() {
		final int compare = VersionComparator.INSTANCE.compare("V1", "A1");
		assertTrue(compare > 0);
	}
}
