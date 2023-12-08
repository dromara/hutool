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

package org.dromara.hutool.core.text.dfa;

import org.dromara.hutool.core.date.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

		Assertions.assertEquals("she,he,her,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assertions.assertEquals(2, ans1.get(0).getBeginIndex().intValue());
		Assertions.assertEquals(4, ans1.get(0).getEndIndex().intValue());
		Assertions.assertEquals(3, ans1.get(1).getBeginIndex().intValue());
		Assertions.assertEquals(4, ans1.get(1).getEndIndex().intValue());
		Assertions.assertEquals(3, ans1.get(2).getBeginIndex().intValue());
		Assertions.assertEquals(5, ans1.get(2).getEndIndex().intValue());
		Assertions.assertEquals(7, ans1.get(3).getBeginIndex().intValue());
		Assertions.assertEquals(9, ans1.get(3).getEndIndex().intValue());

		stopWatch.start("wordtree_char_find");
		final List<String> ans2 = wordTree.matchAll(input, -1, true, true);
		stopWatch.stop();
		Assertions.assertEquals("she,her,say", String.join(",", ans2));

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
		Assertions.assertEquals("she,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assertions.assertEquals(2, ans1.get(0).getBeginIndex().intValue());
		Assertions.assertEquals(4, ans1.get(0).getEndIndex().intValue());
		Assertions.assertEquals(7, ans1.get(1).getBeginIndex().intValue());
		Assertions.assertEquals(9, ans1.get(1).getEndIndex().intValue());

		stopWatch.start("wordtree_char_find_not_density");
		final List<String> ans2 = wordTree.matchAll(input, -1, false, true);
		stopWatch.stop();
		Assertions.assertEquals("she,say", String.join(",", ans2));

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

		Assertions.assertEquals("she,he,her,say", ans1.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assertions.assertEquals(2, ans1.get(0).getBeginIndex().intValue());
		Assertions.assertEquals(4, ans1.get(0).getEndIndex().intValue());
		Assertions.assertEquals(3, ans1.get(1).getBeginIndex().intValue());
		Assertions.assertEquals(4, ans1.get(1).getEndIndex().intValue());
		Assertions.assertEquals(3, ans1.get(2).getBeginIndex().intValue());
		Assertions.assertEquals(5, ans1.get(2).getEndIndex().intValue());
		Assertions.assertEquals(7, ans1.get(3).getBeginIndex().intValue());
		Assertions.assertEquals(9, ans1.get(3).getEndIndex().intValue());

		stopWatch.start("wordtree_char_build_find");
		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("say", "her", "he", "she", "shr");
		final List<String> ans2 = wordTreeLocal.matchAll(input, -1, true, true);
		stopWatch.stop();
		Assertions.assertEquals("she,her,say", String.join(",", ans2));

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

		Assertions.assertEquals(3, result.size());
		Assertions.assertEquals("赵,赵啊,赵啊三", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assertions.assertEquals(Integer.valueOf(0), result.get(0).getBeginIndex());
		Assertions.assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());
		Assertions.assertEquals(Integer.valueOf(0), result.get(1).getBeginIndex());
		Assertions.assertEquals(Integer.valueOf(1), result.get(1).getEndIndex());
		Assertions.assertEquals(Integer.valueOf(0), result.get(2).getBeginIndex());
		Assertions.assertEquals(Integer.valueOf(2), result.get(2).getEndIndex());

		stopWatch.start("wordtree_cn_build_find");
		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		final List<String> result1 = wordTreeLocal.matchAll(input, -1, true, true);
		stopWatch.stop();

		Assertions.assertEquals(1, result1.size());
		Assertions.assertEquals("赵啊三", String.join(",", result1));

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

		Assertions.assertEquals(3, result.size());
		Assertions.assertEquals("赵,赵啊,赵啊三", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assertions.assertEquals(Integer.valueOf(0), result.get(0).getBeginIndex());
		Assertions.assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());
		Assertions.assertEquals(Integer.valueOf(0), result.get(1).getBeginIndex());
		Assertions.assertEquals(Integer.valueOf(1), result.get(1).getEndIndex());
		Assertions.assertEquals(Integer.valueOf(0), result.get(2).getBeginIndex());
		Assertions.assertEquals(Integer.valueOf(2), result.get(2).getEndIndex());

		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		stopWatch.start("wordtree_cn_find");
		final List<String> result1 = wordTreeLocal.matchAllWords(input, -1, true, true).stream().map(FoundWord::getWord)
			.collect(Collectors.toList());
		stopWatch.stop();

		Assertions.assertEquals(1, result1.size());
		Assertions.assertEquals("赵啊三", String.join(",", result1));

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

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("赵", result.stream().map(FoundWord::getWord).collect(Collectors.joining(",")));
		Assertions.assertEquals(Integer.valueOf(0), result.get(0).getBeginIndex());
		Assertions.assertEquals(Integer.valueOf(0), result.get(0).getEndIndex());

		final WordTree wordTreeLocal = new WordTree();
		wordTreeLocal.addWords("赵", "赵啊", "赵啊三");

		stopWatch.start("wordtree_cn_find_not_density");
		final List<String> result1 =
			wordTreeLocal.matchAllWords(input, -1, false, true).stream().map(FoundWord::getWord)
				.collect(Collectors.toList());
		stopWatch.stop();

		Assertions.assertEquals(1, result1.size());
		Assertions.assertEquals("赵啊三", String.join(",", result1));

		//Console.log(stopWatch.prettyPrint());
	}
}
