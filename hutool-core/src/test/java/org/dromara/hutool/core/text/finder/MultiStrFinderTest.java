package org.dromara.hutool.core.text.finder;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cmm
 * @date 2024/8/12 11:16
 */
public class MultiStrFinderTest {

	@Test
	public void test1(){
		ArrayList<String> strings = new ArrayList<>();
		strings.add("sha");
		strings.add("asa");
		strings.add("ha");
		strings.add("hex");
		MultiStrFinder finder = MultiStrFinder.of(strings);
		String text = "asdasahhxxeshaaahexaaasa";
		Map<String, List<Integer>> match = finder.findMatch(text);
		System.out.println(text);
		match.forEach((k,v) -> {
			System.out.println(k + ":" + v);
		});

	}


	@Test
	public void test2(){
		ArrayList<String> strings = new ArrayList<>();
		strings.add("沙漠");
		strings.add("撒");
		strings.add("哈");
		strings.add("害克斯");
		MultiStrFinder finder = MultiStrFinder.of(strings);
		String text = "撒哈拉大沙漠，你看哈哈哈。hex码中文写成海克斯科技";
		Map<String, List<Integer>> match = finder.findMatch(text);
		System.out.println(text);
		match.forEach((k,v) -> {
			System.out.println(k + ":" + v);
		});

	}

}
