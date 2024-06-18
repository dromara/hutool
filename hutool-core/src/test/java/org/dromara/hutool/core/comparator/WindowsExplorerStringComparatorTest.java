package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WindowsExplorerStringComparatorTest {

	final List<String> answer1 = ListUtil.of(
		"filename",
		"filename 00",
		"filename 0",
		"filename 01",
		"filename.jpg",
		"filename.txt",
		"filename00.jpg",
		"filename00a.jpg",
		"filename00a.txt",
		"filename0",
		"filename0.jpg",
		"filename0a.txt",
		"filename0b.jpg",
		"filename0b1.jpg",
		"filename0b02.jpg",
		"filename0c.jpg",
		"filename01.0hjh45-test.txt",
		"filename01.0hjh46",
		"filename01.1hjh45.txt",
		"filename01.hjh45.txt",
		"Filename01.jpg",
		"Filename1.jpg",
		"filename2.hjh45.txt",
		"filename2.jpg",
		"filename03.jpg",
		"filename3.jpg"
	);

	List<String> answer2 = ListUtil.of(
		"abc1.doc",
		"abc2.doc",
		"abc12.doc"
	);

	@Test
	public void testCompare1() {
		final List<String> toSort = new ArrayList<>(answer1);
		while (toSort.equals(answer1)) {
			Collections.shuffle(toSort);
		}
		toSort.sort(WindowsExplorerStringComparator.INSTANCE);
		Assert.equals(toSort, answer1);
	}

	@Test
	public void testCompare2() {
		final List<String> toSort = new ArrayList<>(answer2);
		while (toSort.equals(answer2)) {
			Collections.shuffle(toSort);
		}
		toSort.sort(WindowsExplorerStringComparator.INSTANCE);
		Assert.equals(toSort, answer2);
	}
}
