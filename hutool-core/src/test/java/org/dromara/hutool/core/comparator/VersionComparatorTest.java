/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.comparator;

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
		int compare = VersionComparator.INSTANCE.compare("1.2.1", "1.12.1");
		Assertions.assertTrue(compare < 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1", "1.2.1");
		Assertions.assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTest2() {
		int compare = VersionComparator.INSTANCE.compare("1.12.1", "1.12.1c");
		Assertions.assertTrue(compare < 0);

		compare = VersionComparator.INSTANCE.compare("1.12.1c", "1.12.1");
		Assertions.assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTest3() {
		int compare = VersionComparator.INSTANCE.compare(null, "1.12.1c");
		Assertions.assertTrue(compare < 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1c", null);
		Assertions.assertTrue(compare > 0);
	}

	@Test
	public void versionComparatorTest4() {
		int compare = VersionComparator.INSTANCE.compare("1.13.0", "1.12.1c");
		Assertions.assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1c", "1.13.0");
		Assertions.assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTest5() {
		int compare = VersionComparator.INSTANCE.compare("V1.2", "V1.1");
		Assertions.assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("V1.1", "V1.2");
		Assertions.assertTrue(compare < 0);
	}

	@Test
	public void versionComparatorTes6() {
		int compare = VersionComparator.INSTANCE.compare("V0.0.20170102", "V0.0.20170101");
		Assertions.assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("V0.0.20170101", "V0.0.20170102");
		Assertions.assertTrue(compare < 0);
	}

	@Test
	public void equalsTest(){
		final VersionComparator first = new VersionComparator();
		final VersionComparator other = new VersionComparator();
		Assertions.assertNotEquals(first, other);
	}

	@Test
	public void versionComparatorTest7() {
		int compare = VersionComparator.INSTANCE.compare("1.12.2", "1.12.1c");
		Assertions.assertTrue(compare > 0);

		// 自反测试
		compare = VersionComparator.INSTANCE.compare("1.12.1c", "1.12.2");
		Assertions.assertTrue(compare < 0);
	}

	@Test
	void equalsTest2() {
		final int compare = VersionComparator.INSTANCE.compare("1.12.0", "1.12");
		Assertions.assertEquals(0, compare);
	}

	@Test
	void I8Z3VETest() {
		// 传递性测试
		int compare = VersionComparator.INSTANCE.compare("260", "a-34");
		Assertions.assertTrue(compare > 0);
		compare = VersionComparator.INSTANCE.compare("a-34", "a-3");
		Assertions.assertTrue(compare > 0);
		compare = VersionComparator.INSTANCE.compare("260", "a-3");
		Assertions.assertTrue(compare > 0);
	}

	@Test
	void startWithNoneNumberTest() {
		final int compare = VersionComparator.INSTANCE.compare("V1", "A1");
		Assertions.assertTrue(compare > 0);
	}
}
