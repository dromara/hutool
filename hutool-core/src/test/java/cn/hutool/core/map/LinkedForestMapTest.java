package cn.hutool.core.map;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * {@link LinkedForestMap}的测试用例。
 * <ul>
 *     <li>创建一个树节点Map，构建 a -> b -> c -> d 的别名关系，测试调用结果是否符合期望；</li>
 *     <li>移除 b -> c 之间的引用关系，构建 a -> b, c -> d的结构，测试调用结果是否符合期望；</li>
 *     <li>重新构建 b -> c 之间的引用关系，构建 a -> b -> c -> d的结构，测试调用结果是否符合期望；</li>
 *     <li>
 *         为 b 添加 c2、c2 添加 d2 作为子节点，构建
 *         <pre>
 *             a -> b -> c --> d
 *                  | -> c2 -> d2
 *         </pre>
 *         的结构，测试调用结果是否符合期望；
 *     </li>
 * </ul>
 */
public class LinkedForestMapTest {

	private final ForestMap<String, String> treeNodeMap = new LinkedForestMap<>(false);

	@Before
	public void beforeTest() {
		// a -> b -> c -> d
		treeNodeMap.linkNode("a", "b");
		treeNodeMap.linkNode("b", "c");
		treeNodeMap.linkNode("c", "d");
	}

	@Test
	public void testRegisterBeforeRemove() {
		// containsChildNode
		Assert.assertTrue(treeNodeMap.containsChildNode("a", "b"));
		Assert.assertTrue(treeNodeMap.containsChildNode("a", "c"));
		Assert.assertTrue(treeNodeMap.containsChildNode("a", "d"));
		Assert.assertFalse(treeNodeMap.containsChildNode("a", "e"));

		// getChildNodes
		Assert.assertEquals(CollUtil.newLinkedHashSet("b", "c", "d"), transKey(treeNodeMap.getChildNodes("a")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("c", "d"), transKey(treeNodeMap.getChildNodes("b")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("d"), transKey(treeNodeMap.getChildNodes("c")));
		Assert.assertEquals(CollUtil.newLinkedHashSet(), transKey(treeNodeMap.getChildNodes("d")));

		// getRootNode
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("a")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("b")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("c")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("d")));

		// getTreeNodes
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d"), transKey(treeNodeMap.getTreeNodes("a")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d"), transKey(treeNodeMap.getTreeNodes("b")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d"), transKey(treeNodeMap.getTreeNodes("c")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d"), transKey(treeNodeMap.getTreeNodes("d")));

		// 循环依赖
		Assert.assertThrows(IllegalArgumentException.class, () -> treeNodeMap.linkNode("d", "a"));
	}

	@Test
	public void testAfterRemove() {
		// a -> b, c -> d
		treeNodeMap.unlinkNode("b", "c");

		// getDeclaredChildren
		Assert.assertEquals(CollUtil.newLinkedHashSet("b"), transKey(treeNodeMap.getChildNodes("a")));
		Assert.assertEquals(CollUtil.newLinkedHashSet(), transKey(treeNodeMap.getChildNodes("b")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("d"), transKey(treeNodeMap.getChildNodes("c")));
		Assert.assertEquals(CollUtil.newLinkedHashSet(), transKey(treeNodeMap.getChildNodes("d")));

		// getRootNode
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("a")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("b")));
		Assert.assertEquals("c", transKey(treeNodeMap.getRootNode("c")));
		Assert.assertEquals("c", transKey(treeNodeMap.getRootNode("d")));

		// getTreeNodes
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b"), transKey(treeNodeMap.getTreeNodes("a")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b"), transKey(treeNodeMap.getTreeNodes("b")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("c", "d"), transKey(treeNodeMap.getTreeNodes("c")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("c", "d"), transKey(treeNodeMap.getTreeNodes("d")));
	}

	@Test
	public void testReRegisterAfterRemove() {
		// a -> b -> c -> d
		treeNodeMap.linkNode("b", "c");
		testRegisterBeforeRemove();
	}

	@Test
	public void testParallelRegister() {
		// a -> b -> c --> d
		//      | -> c2 -> d2
		treeNodeMap.linkNode("b", "c2");
		treeNodeMap.linkNode("c2", "d2");

		// getDeclaredChildren
		Assert.assertEquals(CollUtil.newLinkedHashSet("b", "c", "d", "c2", "d2"), transKey(treeNodeMap.getChildNodes("a")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("c", "d", "c2", "d2"), transKey(treeNodeMap.getChildNodes("b")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("d"), transKey(treeNodeMap.getChildNodes("c")));
		Assert.assertEquals(CollUtil.newLinkedHashSet(), transKey(treeNodeMap.getChildNodes("d")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("d2"), transKey(treeNodeMap.getChildNodes("c2")));
		Assert.assertEquals(CollUtil.newLinkedHashSet(), transKey(treeNodeMap.getChildNodes("d2")));

		// getRootNode
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("a")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("b")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("c")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("d")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("c2")));
		Assert.assertEquals("a", transKey(treeNodeMap.getRootNode("d2")));

		// getTreeNodes
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d", "c2", "d2"), transKey(treeNodeMap.getTreeNodes("a")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d", "c2", "d2"), transKey(treeNodeMap.getTreeNodes("b")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d", "c2", "d2"), transKey(treeNodeMap.getTreeNodes("c")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d", "c2", "d2"), transKey(treeNodeMap.getTreeNodes("d")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d", "c2", "d2"), transKey(treeNodeMap.getTreeNodes("c2")));
		Assert.assertEquals(CollUtil.newLinkedHashSet("a", "b", "c", "d", "c2", "d2"), transKey(treeNodeMap.getTreeNodes("d2")));
	}

	private static <K, V> Collection<K> transKey(Collection<TreeEntry<K, V>> entryNodes) {
		return CollStreamUtil.toSet(entryNodes, TreeEntry::getKey);
	}

	private static <K, V> K transKey(TreeEntry<K, V> entryNodes) {
		return entryNodes.getKey();
	}

}
