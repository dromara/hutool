package cn.hutool.core.text.dfa;

import cn.hutool.core.date.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class NFATest {

	/**
	 * 密集匹配 测试查找结果，并与WordTree对比效率
	 */
	@Test
	public void testFind() {
		final NFA NFA = new NFA();
		NFA.insert("say", "her", "he", "she", "shr");
//		NFA.buildAc();

		final WordTree wordTree = new WordTree();
		wordTree.addWords("say", "her", "he", "she", "shr");

		final StopWatch stopWatch = new StopWatch();
		final String input = "sasherhsay";

		stopWatch.start("automaton_char_find");
		final List<FoundWord> ans1 = NFA.find(input);
		stopWatch.stop();

		Assert.assertEquals("she,he,her,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assert.assertEquals(2, ans1.get(0).getBeginIndex().intValue());
		Assert.assertEquals(4, ans1.get(0).getEndIndex().intValue());
		Assert.assertEquals(3, ans1.get(1).getBeginIndex().intValue());
		Assert.assertEquals(4, ans1.get(1).getEndIndex().intValue());
		Assert.assertEquals(3, ans1.get(2).getBeginIndex().intValue());
		Assert.assertEquals(5, ans1.get(2).getEndIndex().intValue());
		Assert.assertEquals(7, ans1.get(3).getBeginIndex().intValue());
		Assert.assertEquals(9, ans1.get(3).getEndIndex().intValue());

		stopWatch.start("wordtree_char_find");
		final List<String> ans2 = wordTree.matchAll(input, -1, true, true);
		stopWatch.stop();
		Assert.assertEquals("she,he,her,say", String.join(",", ans2));

		//Console.log(stopWatch.prettyPrint());
	}

	/**
	 * 非密集匹配 测试查找结果，并与WordTree对比效率
	 */
	@Test
	public void testFindNotDensity() {
		final NFA NFA = new NFA();
		NFA.insert("say", "her", "he", "she", "shr");
//		NFA.buildAc();

		final WordTree wordTree = new WordTree();
		wordTree.addWords("say", "her", "he", "she", "shr");

		final StopWatch stopWatch = new StopWatch();
		final String input = "sasherhsay";

		stopWatch.start("automaton_char_find_not_density");
		final List<FoundWord> ans1 = NFA.find(input, false);
		stopWatch.stop();
		Assert.assertEquals("she,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assert.assertEquals(2, ans1.get(0).getBeginIndex().intValue());
		Assert.assertEquals(4, ans1.get(0).getEndIndex().intValue());
		Assert.assertEquals(7, ans1.get(1).getBeginIndex().intValue());
		Assert.assertEquals(9, ans1.get(1).getEndIndex().intValue());

		stopWatch.start("wordtree_char_find_not_density");
		final List<String> ans2 = wordTree.matchAll(input, -1, false, true);
		stopWatch.stop();
		Assert.assertEquals("she,say", String.join(",", ans2));

		//Console.log(stopWatch.prettyPrint());
	}

	/**
	 * 密集匹配 测试建树和查找，并与WordTree对比效率
	 */
	@Test
	public void testBuildAndFind() {
		final StopWatch stopWatch = new StopWatch();
		final String input = "sasherhsay";

		stopWatch.start("automaton_char_buid_find");
		final NFA NFALocal = new NFA();
		NFALocal.insert("say", "her", "he", "she", "shr");
//		NFALocal.buildAc();
		final List<FoundWord> ans1 = NFALocal.find(input);
		stopWatch.stop();

		Assert.assertEquals("she,he,her,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assert.assertEquals(2, ans1.get(0).getBeginIndex().intValue());
		Assert.assertEquals(4, ans1.get(0).getEndIndex().intValue());
		Assert.assertEquals(3, ans1.get(1).getBeginIndex().intValue());
		Assert.assertEquals(4, ans1.get(1).getEndIndex().intValue());
		Assert.assertEquals(3, ans1.get(2).getBeginIndex().intValue());
		Assert.assertEquals(5, ans1.get(2).getEndIndex().intValue());
		Assert.assertEquals(7, ans1.get(3).getBeginIndex().intValue());
		Assert.assertEquals(9, ans1.get(3).getEndIndex().intValue());

		stopWatch.start("wordtree_char_build_find");
		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("say", "her", "he", "she", "shr");
		final List<String> ans2 = wordTreeLocal.matchAll(input, -1, true, true);
		stopWatch.stop();
		Assert.assertEquals("she,he,her,say", String.join(",", ans2));

		//Console.log(stopWatch.prettyPrint());
	}

	/**
	 * 密集匹配 构建树和查找 测试中文字符，并与wordTree对比效率
	 */
	@Test
	public void buildFindCnCharTest() {
		final StopWatch stopWatch = new StopWatch();
		final String input = "赵啊三在做什么";

		stopWatch.start("automaton_cn_build_find");
		final NFA NFALocal = new NFA();
		NFALocal.insert("赵", "赵啊", "赵啊三");
//		NFALocal.buildAc();

		final List<FoundWord> result = NFALocal.find(input);
		stopWatch.stop();

		Assert.assertEquals(3, result.size());
		Assert.assertEquals("赵,赵啊,赵啊三", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assert.assertEquals(Integer.valueOf(0), result.get(0).getBeginIndex());
		Assert.assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());
		Assert.assertEquals(Integer.valueOf(0), result.get(1).getBeginIndex());
		Assert.assertEquals(Integer.valueOf(1), result.get(1).getEndIndex());
		Assert.assertEquals(Integer.valueOf(0), result.get(2).getBeginIndex());
		Assert.assertEquals(Integer.valueOf(2), result.get(2).getEndIndex());

		stopWatch.start("wordtree_cn_build_find");
		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		final List<String> result1 = wordTreeLocal.matchAll(input, -1, true, true);
		stopWatch.stop();

		Assert.assertEquals(3, result1.size());
		Assert.assertEquals("赵,赵啊,赵啊三", String.join(",", result1));

		//Console.log(stopWatch.prettyPrint());
	}

	/**
	 * 密集匹配 测试构建树和查找 中文字符，并与wordTree对比效率
	 */
	@Test
	public void testFindCNChar() {
		final StopWatch stopWatch = new StopWatch();
		final String input = "赵啊三在做什么";

		final NFA NFALocal = new NFA();
		NFALocal.insert("赵", "赵啊", "赵啊三");
//		NFALocal.buildAc();

		stopWatch.start("automaton_cn_find");
		final List<FoundWord> result = NFALocal.find(input);
		stopWatch.stop();

		Assert.assertEquals(3, result.size());
		Assert.assertEquals("赵,赵啊,赵啊三", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assert.assertEquals(Integer.valueOf(0), result.get(0).getBeginIndex());
		Assert.assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());
		Assert.assertEquals(Integer.valueOf(0), result.get(1).getBeginIndex());
		Assert.assertEquals(Integer.valueOf(1), result.get(1).getEndIndex());
		Assert.assertEquals(Integer.valueOf(0), result.get(2).getBeginIndex());
		Assert.assertEquals(Integer.valueOf(2), result.get(2).getEndIndex());

		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		stopWatch.start("wordtree_cn_find");
		final List<String> result1 = wordTreeLocal.matchAllWords(input, -1, true, true).stream().map(FoundWord::getWord)
			.collect(Collectors.toList());
		stopWatch.stop();

		Assert.assertEquals(3, result1.size());
		Assert.assertEquals("赵,赵啊,赵啊三", String.join(",", result1));

		//Console.log(stopWatch.prettyPrint());
	}

	/**
	 * 非密集匹配 测试构建树和查找 中文字符，并与wordTree对比效率，
	 */
	@Test
	public void testFindCNCharNotDensity() {
		final StopWatch stopWatch = new StopWatch();
		final String input = "赵啊三在做什么";

		final NFA NFALocal = new NFA();
		NFALocal.insert("赵", "赵啊", "赵啊三");
//		NFALocal.buildAc();

		stopWatch.start("automaton_cn_find_not_density");
		final List<FoundWord> result = NFALocal.find(input, false);
		stopWatch.stop();

		Assert.assertEquals(1, result.size());
		Assert.assertEquals("赵", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assert.assertEquals(Integer.valueOf(0), result.get(0).getBeginIndex());
		Assert.assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());

		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		stopWatch.start("wordtree_cn_find_not_density");
		final List<String> result1 =
			wordTreeLocal.matchAllWords(input, -1, false, true).stream().map(FoundWord::getWord)
				.collect(Collectors.toList());
		stopWatch.stop();

		Assert.assertEquals(1, result1.size());
		Assert.assertEquals("赵", String.join(",", result1));

		//Console.log(stopWatch.prettyPrint());
	}
}
