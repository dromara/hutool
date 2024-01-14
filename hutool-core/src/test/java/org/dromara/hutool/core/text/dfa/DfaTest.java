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

import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * DFA单元测试
 *
 * @author Looly
 */
public class DfaTest {

	// 构建被查询的文本，包含停顿词
	String text = "我有一颗$大土^豆，刚出锅的";

	@Test
	public void matchAllTest() {
		// 构建查询树
		final WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况一：标准匹配，匹配到最短关键词，并跳过已经匹配的关键词
		// 匹配到【大】，就不再继续匹配了，因此【大土豆】不匹配
		// 匹配到【刚出锅】，就跳过这三个字了，因此【出锅】不匹配（由于刚首先被匹配，因此长的被匹配，最短匹配只针对第一个字相同选最短）
		final List<String> matchAll = tree.matchAll(text, -1, false, false);
		Assertions.assertEquals(matchAll, ListUtil.of("大", "土^豆", "刚出锅"));
	}

	/**
	 * 密集匹配原则（最短匹配）测试
	 */
	@Test
	public void densityMatchTest() {
		// 构建查询树
		final WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况二：匹配到最短关键词，不跳过已经匹配的关键词
		// 【大】被匹配，最短匹配原则【大土豆】被跳过，【土豆继续被匹配】
		// 【刚出锅】被匹配，由于不跳过已经匹配的词，【出锅】被匹配
		final List<String> matchAll = tree.matchAll(text, -1, true, false);
		Assertions.assertEquals(matchAll, ListUtil.of("大", "土^豆", "刚出锅", "出锅"));
	}

	/**
	 * 贪婪非密集匹配原则测试<br>
	 * 贪婪：最长匹配
	 * 非密集：跳过匹配到的
	 */
	@Test
	public void greedMatchTest() {
		// 构建查询树
		final WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况三：匹配到最长关键词，跳过已经匹配的关键词
		// 匹配到【大】和【大土豆】，最长匹配则保留【大土豆】，非密集匹配，【土豆】跳过。
		// 由于【刚出锅】被匹配，由于非密集匹配，【出锅】被跳过
		final List<String> matchAll = tree.matchAll(text, -1, false, true);
		Assertions.assertEquals(ListUtil.of("大土^豆", "刚出锅"), matchAll);
	}

	/**
	 * 密集匹配原则（最长匹配）和贪婪匹配原则测试
	 * 贪婪：最长匹配
	 * 密集：不跳过匹配到的
	 */
	@Test
	public void densityAndGreedMatchTest() {
		// 构建查询树
		final WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况四：匹配到最长关键词，不跳过已经匹配的关键词（最全关键词）
		// 匹配到【大】和【大土豆】，由于到最长匹配，因此【大土豆】保留，由于不跳过已经匹配的关键词，【土豆】继续被匹配
		// 【刚出锅】被匹配，由于不跳过已经匹配的词，【出锅】被匹配
		final List<String> matchAll = tree.matchAll(text, -1, true, true);
		Assertions.assertEquals(ListUtil.of("大土^豆", "土^豆", "刚出锅", "出锅"), matchAll);

	}

	/**
	 * 由于贪婪匹配，因此【赵】、【赵阿】都被跳过，只保留最长的【赵阿三】
	 */
	@Test
	public void densityAndGreedMatchTest2() {
		final WordTree tree = new WordTree();
		tree.addWord("赵");
		tree.addWord("赵阿");
		tree.addWord("赵阿三");

		final List<FoundWord> result = tree.matchAllWords("赵阿三在做什么", -1, true, true);
		Assertions.assertEquals(1, result.size());

		Assertions.assertEquals("赵阿三", result.get(0).getWord());
		Assertions.assertEquals(0, result.get(0).getBeginIndex().intValue());
		Assertions.assertEquals(2, result.get(0).getEndIndex().intValue());
	}

	/**
	 * 停顿词测试
	 */
	@Test
	public void stopWordTest() {
		final WordTree tree = new WordTree();
		tree.addWord("tio");

		final List<String> all = tree.matchAll("AAAAAAAt-ioBBBBBBB");
		Assertions.assertEquals(all, ListUtil.of("t-io"));
	}

	@Test
	public void aTest() {
		final WordTree tree = new WordTree();
		tree.addWord("women");
		final String text = "a WOMEN todo.".toLowerCase();
		final List<String> matchAll = tree.matchAll(text, -1, false, false);
		Assertions.assertEquals("[women]", matchAll.toString());
	}

	@Test
	public void clearTest() {
		WordTree tree = new WordTree();
		tree.addWord("黑");
		Assertions.assertTrue(tree.matchAll("黑大衣").contains("黑"));
		//clear时直接调用Map的clear并没有把endCharacterSet清理掉
		tree.clear();
		tree.addWords("黑大衣", "红色大衣");

		//clear() 覆写前 这里想匹配到黑大衣，但是却匹配到了黑
//		Assertions.assertFalse(tree.matchAll("黑大衣").contains("黑大衣"));
//		Assertions.assertTrue(tree.matchAll("黑大衣").contains("黑"));
		//clear() 覆写后
		Assertions.assertTrue(tree.matchAll("黑大衣").contains("黑大衣"));
		Assertions.assertFalse(tree.matchAll("黑大衣").contains("黑"));
		Assertions.assertTrue(tree.matchAll("红色大衣").contains("红色大衣"));

		//如果不覆写只能通过new出新对象才不会有问题
		tree = new WordTree();
		tree.addWords("黑大衣", "红色大衣");
		Assertions.assertTrue(tree.matchAll("黑大衣").contains("黑大衣"));
		Assertions.assertTrue(tree.matchAll("红色大衣").contains("红色大衣"));
	}

	// ----------------------------------------------------------------------------------------------------------

	/**
	 * 构建查找树
	 *
	 * @return 查找树
	 */
	private WordTree buildWordTree() {
		// 构建查询树
		final WordTree tree = new WordTree();
		tree.addWord("大");
		tree.addWord("大土豆");
		tree.addWord("土豆");
		tree.addWord("刚出锅");
		tree.addWord("出锅");
		return tree;
	}

	@Test
	void issueI8LAEWTest() {
		final WordTree wordTree = new WordTree();
		wordTree.addWords("UserServiceImpl", "UserService");

		final String text = "This is test Service: UserServiceImpl UserServiceTest...";
		final List<String> strings = wordTree.matchAll(text, -1, false, true);
		Assertions.assertEquals("[UserServiceImpl, UserService]", strings.toString());
	}

	/**
	 * 此测试验证边界问题，当最后一个字符匹配时的问题
	 */
	@Test
	void matchAbTest() {
		final WordTree wordTree = new WordTree();
		wordTree.addWords("ab", "b");

		// 非密集，非贪婪
		List<String> strings = wordTree.matchAll("abab", -1, false, false);
		Assertions.assertEquals("[ab, ab]", strings.toString());

		// 密集，非贪婪
		strings = wordTree.matchAll("abab", -1, true, false);
		Assertions.assertEquals("[ab, b, ab, b]", strings.toString());

		// 非密集，贪婪
		strings = wordTree.matchAll("abab", -1, false, true);
		Assertions.assertEquals("[ab, ab]", strings.toString());

		// 密集，贪婪
		strings = wordTree.matchAll("abab", -1, true, true);
		Assertions.assertEquals("[ab, b, ab, b]", strings.toString());
	}

	@Test
	void flattenTest() {
		final WordTree wordTree = new WordTree();
		final List<String> list = Arrays.asList("阿帕奇", "阿超", "HuTool", "HuTao");
		wordTree.addWords(list);
		final List<String> flattened = wordTree.flatten();
		flattened.sort(Comparator.comparingInt(list::indexOf));
		Assertions.assertEquals(list, flattened);
	}
}
