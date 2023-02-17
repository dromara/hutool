package cn.hutool.dfa;

import cn.hutool.core.date.StopWatch;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class AutomatonTest extends TestCase {

	/**
	 * 密集匹配 测试查找结果，并与WordTree对比效率
	 */
	public void testFind() {
		Automaton automaton = new Automaton();
		WordTree wordTree = new WordTree();
		automaton.insert("say", "her", "he", "she", "shr");
		automaton.buildAc();
		wordTree.addWords("say", "her", "he", "she", "shr");

		StopWatch stopWatch = new StopWatch();
		String input = "sasherhsay";

		stopWatch.start("automaton_char_find");
		List<FoundWord> ans1 = automaton.find(input);
		stopWatch.stop();
		assertEquals("she,he,her,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		assertEquals(Integer.valueOf(2), ans1.get(0).getStartIndex());
		assertEquals(Integer.valueOf(4), ans1.get(0).getEndIndex());
		assertEquals(Integer.valueOf(3), ans1.get(1).getStartIndex());
		assertEquals(Integer.valueOf(4), ans1.get(1).getEndIndex());
		assertEquals(Integer.valueOf(3), ans1.get(2).getStartIndex());
		assertEquals(Integer.valueOf(5), ans1.get(2).getEndIndex());
		assertEquals(Integer.valueOf(7), ans1.get(3).getStartIndex());
		assertEquals(Integer.valueOf(9), ans1.get(3).getEndIndex());

		stopWatch.start("wordtree_char_find");
		List<String> ans2 = wordTree.matchAll(input, -1, true, true);
		stopWatch.stop();
		assertEquals("she,he,her,say", String.join(",", ans2));

		System.out.println(stopWatch.prettyPrint());
	}

	/**
	 * 非密集匹配 测试查找结果，并与WordTree对比效率
	 */
	public void testFindNotDensity() {
		Automaton automaton = new Automaton();
		WordTree wordTree = new WordTree();
		automaton.insert("say", "her", "he", "she", "shr");
		automaton.buildAc();
		wordTree.addWords("say", "her", "he", "she", "shr");

		StopWatch stopWatch = new StopWatch();
		String input = "sasherhsay";

		stopWatch.start("automaton_char_find_not_density");
		List<FoundWord> ans1 = automaton.find(input, false);
		stopWatch.stop();
		assertEquals("she,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		assertEquals(Integer.valueOf(2), ans1.get(0).getStartIndex());
		assertEquals(Integer.valueOf(4), ans1.get(0).getEndIndex());
		assertEquals(Integer.valueOf(7), ans1.get(1).getStartIndex());
		assertEquals(Integer.valueOf(9), ans1.get(1).getEndIndex());

		stopWatch.start("wordtree_char_find_not_density");
		List<String> ans2 = wordTree.matchAll(input, -1, false, true);
		stopWatch.stop();
		assertEquals("she,say", String.join(",", ans2));

		System.out.println(stopWatch.prettyPrint());
	}

	/**
	 * 密集匹配 测试建树和查找，并与WordTree对比效率
	 */
	public void testBuildAndFind() {
		StopWatch stopWatch = new StopWatch();
		String input = "sasherhsay";

		stopWatch.start("automaton_char_buid_find");
		Automaton automatonLocal = new Automaton();
		automatonLocal.insert("say", "her", "he", "she", "shr");
		automatonLocal.buildAc();
		List<FoundWord> ans1 = automatonLocal.find(input);
		stopWatch.stop();
		assertEquals("she,he,her,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		assertEquals(Integer.valueOf(2), ans1.get(0).getStartIndex());
		assertEquals(Integer.valueOf(4), ans1.get(0).getEndIndex());
		assertEquals(Integer.valueOf(3), ans1.get(1).getStartIndex());
		assertEquals(Integer.valueOf(4), ans1.get(1).getEndIndex());
		assertEquals(Integer.valueOf(3), ans1.get(2).getStartIndex());
		assertEquals(Integer.valueOf(5), ans1.get(2).getEndIndex());
		assertEquals(Integer.valueOf(7), ans1.get(3).getStartIndex());
		assertEquals(Integer.valueOf(9), ans1.get(3).getEndIndex());

		stopWatch.start("wordtree_char_build_find");
		WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("say", "her", "he", "she", "shr");
		List<String> ans2 = wordTreeLocal.matchAll(input, -1, true, true);
		stopWatch.stop();
		assertEquals("she,he,her,say", String.join(",", ans2));

		System.out.println(stopWatch.prettyPrint());
	}

	/**
	 * 密集匹配 构建树和查找 测试中文字符，并与wordTree对比效率
	 */
	@Test
	public void testBuildFindCnChar() {
		StopWatch stopWatch = new StopWatch();
		String input = "赵啊三在做什么";

		stopWatch.start("automaton_cn_build_find");
		Automaton automatonLocal = new Automaton();
		automatonLocal.insert("赵", "赵啊", "赵啊三");
		automatonLocal.buildAc();

		final List<FoundWord> result = automatonLocal.find(input);
		stopWatch.stop();

		Assert.assertEquals(3, result.size());
		Assert.assertEquals("赵,赵啊,赵啊三", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		assertEquals(Integer.valueOf(0), result.get(0).getStartIndex());
		assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());
		assertEquals(Integer.valueOf(0), result.get(1).getStartIndex());
		assertEquals(Integer.valueOf(1), result.get(1).getEndIndex());
		assertEquals(Integer.valueOf(0), result.get(2).getStartIndex());
		assertEquals(Integer.valueOf(2), result.get(2).getEndIndex());

		stopWatch.start("wordtree_cn_build_find");
		WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		final List<String> result1 = wordTreeLocal.matchAll(input, -1, true, true);
		stopWatch.stop();

		Assert.assertEquals(3, result1.size());
		Assert.assertEquals("赵,赵啊,赵啊三", String.join(",", result1));

		System.out.println(stopWatch.prettyPrint());

	}

	/**
	 * 密集匹配 测试构建树和查找 中文字符，并与wordTree对比效率
	 */
	@Test
	public void testFindCNChar() {
		StopWatch stopWatch = new StopWatch();
		String input = "赵啊三在做什么";

		Automaton automatonLocal = new Automaton();
		automatonLocal.insert("赵", "赵啊", "赵啊三");
		automatonLocal.buildAc();

		stopWatch.start("automaton_cn_find");
		final List<FoundWord> result = automatonLocal.find(input);
		stopWatch.stop();

		Assert.assertEquals(3, result.size());
		Assert.assertEquals("赵,赵啊,赵啊三", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		assertEquals(Integer.valueOf(0), result.get(0).getStartIndex());
		assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());
		assertEquals(Integer.valueOf(0), result.get(1).getStartIndex());
		assertEquals(Integer.valueOf(1), result.get(1).getEndIndex());
		assertEquals(Integer.valueOf(0), result.get(2).getStartIndex());
		assertEquals(Integer.valueOf(2), result.get(2).getEndIndex());

		WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		stopWatch.start("wordtree_cn_find");
		final List<String> result1 = wordTreeLocal.matchAllWords(input, -1, true, true).stream().map(FoundWord::getWord)
			.collect(Collectors.toList());
		stopWatch.stop();

		Assert.assertEquals(3, result1.size());
		Assert.assertEquals("赵,赵啊,赵啊三", String.join(",", result1));

		System.out.println(stopWatch.prettyPrint());

	}

	/**
	 * 非密集匹配 测试构建树和查找 中文字符，并与wordTree对比效率，
	 */
	@Test
	public void testFindCNCharNotDensity() {
		StopWatch stopWatch = new StopWatch();
		String input = "赵啊三在做什么";

		Automaton automatonLocal = new Automaton();
		automatonLocal.insert("赵", "赵啊", "赵啊三");
		automatonLocal.buildAc();

		stopWatch.start("automaton_cn_find_not_density");
		final List<FoundWord> result = automatonLocal.find(input, false);
		stopWatch.stop();

		Assert.assertEquals(1, result.size());
		Assert.assertEquals("赵", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		assertEquals(Integer.valueOf(0), result.get(0).getStartIndex());
		assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());

		WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		stopWatch.start("wordtree_cn_find_not_density");
		final List<String> result1 =
			wordTreeLocal.matchAllWords(input, -1, false, true).stream().map(FoundWord::getWord)
				.collect(Collectors.toList());
		stopWatch.stop();

		Assert.assertEquals(1, result1.size());
		Assert.assertEquals("赵", String.join(",", result1));

		System.out.println(stopWatch.prettyPrint());

	}
}
