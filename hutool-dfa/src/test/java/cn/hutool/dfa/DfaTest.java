package cn.hutool.dfa;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * DFA单元测试
 *
 * @author Looly
 *
 */
public class DfaTest {

	// 构建被查询的文本，包含停顿词
	String text = "我有一颗$大土^豆，刚出锅的";

	@Test
	public void matchAllTest() {
		// 构建查询树
		WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况一：标准匹配，匹配到最短关键词，并跳过已经匹配的关键词
		// 匹配到【大】，就不再继续匹配了，因此【大土豆】不匹配
		// 匹配到【刚出锅】，就跳过这三个字了，因此【出锅】不匹配（由于刚首先被匹配，因此长的被匹配，最短匹配只针对第一个字相同选最短）
		List<String> matchAll = tree.matchAll(text, -1, false, false);
		Assert.assertEquals(matchAll, CollUtil.newArrayList("大", "土^豆", "刚出锅"));
	}

	/**
	 * 密集匹配原则（最短匹配）测试
	 */
	@Test
	public void densityMatchTest() {
		// 构建查询树
		WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况二：匹配到最短关键词，不跳过已经匹配的关键词
		// 【大】被匹配，最短匹配原则【大土豆】被跳过，【土豆继续被匹配】
		// 【刚出锅】被匹配，由于不跳过已经匹配的词，【出锅】被匹配
		List<String> matchAll = tree.matchAll(text, -1, true, false);
		Assert.assertEquals(matchAll, CollUtil.newArrayList("大", "土^豆", "刚出锅", "出锅"));
	}

	/**
	 * 贪婪匹配原则测试
	 */
	@Test
	public void greedMatchTest() {
		// 构建查询树
		WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况三：匹配到最长关键词，跳过已经匹配的关键词
		// 匹配到【大】，由于到最长匹配，因此【大土豆】接着被匹配
		// 由于【大土豆】被匹配，【土豆】被跳过，由于【刚出锅】被匹配，【出锅】被跳过
		List<String> matchAll = tree.matchAll(text, -1, false, true);
		Assert.assertEquals(matchAll, CollUtil.newArrayList("大", "大土^豆", "刚出锅"));

	}

	/**
	 * 密集匹配原则（最短匹配）和贪婪匹配原则测试
	 */
	@Test
	public void densityAndGreedMatchTest() {
		// 构建查询树
		WordTree tree = buildWordTree();

		// -----------------------------------------------------------------------------------------------------------------------------------
		// 情况四：匹配到最长关键词，不跳过已经匹配的关键词（最全关键词）
		// 匹配到【大】，由于到最长匹配，因此【大土豆】接着被匹配，由于不跳过已经匹配的关键词，土豆继续被匹配
		// 【刚出锅】被匹配，由于不跳过已经匹配的词，【出锅】被匹配
		List<String> matchAll = tree.matchAll(text, -1, true, true);
		Assert.assertEquals(matchAll, CollUtil.newArrayList("大", "大土^豆", "土^豆", "刚出锅", "出锅"));

	}

	/**
	 * 停顿词测试
	 */
	@Test
	public void stopWordTest() {
		WordTree tree = new WordTree();
		tree.addWord("tio");

		List<String> all = tree.matchAll("AAAAAAAt-ioBBBBBBB");
		Assert.assertEquals(all, CollUtil.newArrayList("t-io"));
	}

	@Test
	public void aTest(){
		WordTree tree = new WordTree();
		tree.addWord("women");
		String text = "a WOMEN todo.".toLowerCase();
		List<String> matchAll = tree.matchAll(text, -1, false, false);
		Assert.assertEquals("[women]", matchAll.toString());
	}

	@Test
	public void clearTest(){
		WordTree tree = new WordTree();
		tree.addWord("黑");
		Assert.assertTrue(tree.matchAll("黑大衣").contains("黑"));
		//clear时直接调用Map的clear并没有把endCharacterSet清理掉
		tree.clear();
		tree.addWords("黑大衣","红色大衣");

		//clear() 覆写前 这里想匹配到黑大衣，但是却匹配到了黑
//		Assert.assertFalse(tree.matchAll("黑大衣").contains("黑大衣"));
//		Assert.assertTrue(tree.matchAll("黑大衣").contains("黑"));
		//clear() 覆写后
		Assert.assertTrue(tree.matchAll("黑大衣").contains("黑大衣"));
		Assert.assertFalse(tree.matchAll("黑大衣").contains("黑"));
		Assert.assertTrue(tree.matchAll("红色大衣").contains("红色大衣"));

		//如果不覆写只能通过new出新对象才不会有问题
		tree = new WordTree();
		tree.addWords("黑大衣","红色大衣");
		Assert.assertTrue(tree.matchAll("黑大衣").contains("黑大衣"));
		Assert.assertTrue(tree.matchAll("红色大衣").contains("红色大衣"));
	}

	// ----------------------------------------------------------------------------------------------------------
	/**
	 * 构建查找树
	 *
	 * @return 查找树
	 */
	private WordTree buildWordTree() {
		// 构建查询树
		WordTree tree = new WordTree();
		tree.addWord("大");
		tree.addWord("大土豆");
		tree.addWord("土豆");
		tree.addWord("刚出锅");
		tree.addWord("出锅");
		return tree;
	}
}
