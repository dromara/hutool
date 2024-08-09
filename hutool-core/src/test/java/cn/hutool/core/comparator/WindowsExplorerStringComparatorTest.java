package cn.hutool.core.comparator;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link WindowsExplorerStringComparator} 单元测试类
 *
 * @author YMNNs
 */
@SuppressWarnings("serial")
public class WindowsExplorerStringComparatorTest {

	List<String> answer1 = new ArrayList<String>() {{
		add("filename");
		add("filename 00");
		add("filename 0");
		add("filename 01");
		add("filename.jpg");
		add("filename.txt");
		add("filename00.jpg");
		add("filename00a.jpg");
		add("filename00a.txt");
		add("filename0");
		add("filename0.jpg");
		add("filename0a.txt");
		add("filename0b.jpg");
		add("filename0b1.jpg");
		add("filename0b02.jpg");
		add("filename0c.jpg");
		add("filename01.0hjh45-test.txt");
		add("filename01.0hjh46");
		add("filename01.1hjh45.txt");
		add("filename01.hjh45.txt");
		add("Filename01.jpg");
		add("Filename1.jpg");
		add("filename2.hjh45.txt");
		add("filename2.jpg");
		add("filename03.jpg");
		add("filename3.jpg");
	}};

	List<String> answer2 = new ArrayList<String>() {{
		add("abc1.doc");
		add("abc2.doc");
		add("abc12.doc");
	}};

	@Test
	public void testCompare1() {
		List<String> toSort = new ArrayList<>(answer1);
		while (toSort.equals(answer1)) {
			Collections.shuffle(toSort);
		}
		toSort.sort(new WindowsExplorerStringComparator());
		Assert.equals(toSort, answer1);
	}

	@Test
	public void testCompare2() {
		List<String> toSort = new ArrayList<>(answer2);
		while (toSort.equals(answer2)) {
			Collections.shuffle(toSort);
		}
		toSort.sort(new WindowsExplorerStringComparator());
		Assert.equals(toSort, answer2);
	}
}
