package cn.hutool.core.map;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class LinkedForestMapTest {

	private final ForestMap<String, String> treeNodeMap = new LinkedForestMap<>(false);

	@Before
	public void beforeTest() {
		// a -> b -> c -> d
		treeNodeMap.putLinkedNodes("a", "b", "bbb");
		treeNodeMap.putLinkedNodes("b", "c", "ccc");
		treeNodeMap.putLinkedNodes("c", "d", "ddd");
		treeNodeMap.get("a").setValue("aaa");
	}

	@Test
	public void testTreeEntry() {
		final TreeEntry<String, String> parent = treeNodeMap.get("b");
		final TreeEntry<String, String> treeEntry = treeNodeMap.get("c");
		final TreeEntry<String, String> child = treeNodeMap.get("d");

		// Entry相关
		Assert.assertEquals("c", treeEntry.getKey());
		Assert.assertEquals("ccc", treeEntry.getValue());

		// 父节点相关方法
		Assert.assertEquals(2, treeEntry.getWeight());
		Assert.assertEquals(treeNodeMap.get("a"), treeEntry.getRoot());
		Assert.assertTrue(treeEntry.hasParent());
		Assert.assertEquals(parent, treeEntry.getDeclaredParent());
		Assert.assertEquals(treeNodeMap.get("a"), treeEntry.getParent("a"));
		Assert.assertTrue(treeEntry.containsParent("a"));

		// 子节点相关方法
		final List<TreeEntry<String, String>> nodes = new ArrayList<>();
		treeEntry.forEachChild(true, nodes::add);
		Assert.assertEquals(CollUtil.newArrayList(treeEntry, child), nodes);
		nodes.clear();
		treeEntry.forEachChild(false, nodes::add);
		Assert.assertEquals(CollUtil.newArrayList(child), nodes);

		Assert.assertEquals(CollUtil.newLinkedHashSet(child), new LinkedHashSet<>(treeEntry.getDeclaredChildren().values()));
		Assert.assertEquals(CollUtil.newLinkedHashSet(child), new LinkedHashSet<>(treeEntry.getChildren().values()));
		Assert.assertTrue(treeEntry.hasChildren());
		Assert.assertEquals(treeNodeMap.get("d"), treeEntry.getChild("d"));
		Assert.assertTrue(treeEntry.containsChild("d"));
	}

	@Test
	public void putTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		TreeEntry<String, String> treeEntry = new LinkedForestMap.TreeEntryNode<>(null, "a", "aaa");
		Assert.assertNull(map.put("a", treeEntry));
		Assert.assertNotEquals(map.get("a"), treeEntry);
		Assert.assertEquals(map.get("a").getKey(), treeEntry.getKey());
		Assert.assertEquals(map.get("a").getValue(), treeEntry.getValue());

		treeEntry = new LinkedForestMap.TreeEntryNode<>(null, "a", "aaaa");
		Assert.assertNotNull(map.put("a", treeEntry));
		Assert.assertNotEquals(map.get("a"), treeEntry);
		Assert.assertEquals(map.get("a").getKey(), treeEntry.getKey());
		Assert.assertEquals(map.get("a").getValue(), treeEntry.getValue());
	}

	@Test
	public void removeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		map.remove("b");
		Assert.assertNull(map.get("b"));
		Assert.assertFalse(b.hasChildren());
		Assert.assertFalse(b.hasParent());
		Assert.assertEquals(a, c.getDeclaredParent());
		Assert.assertEquals(CollUtil.newArrayList(c), new ArrayList<>(a.getDeclaredChildren().values()));
	}

	@Test
	public void putAllTest() {
		final ForestMap<String, String> source = new LinkedForestMap<>(false);
		source.linkNodes("a", "b");
		source.linkNodes("b", "c");

		final ForestMap<String, String> target = new LinkedForestMap<>(false);
		target.putAll(source);

		final TreeEntry<String, String> a = target.get("a");
		final TreeEntry<String, String> b = target.get("b");
		final TreeEntry<String, String> c = target.get("c");

		Assert.assertNotNull(a);
		Assert.assertEquals("a", a.getKey());
		Assert.assertEquals(CollUtil.newArrayList(b, c), new ArrayList<>(a.getChildren().values()));

		Assert.assertNotNull(b);
		Assert.assertEquals("b", b.getKey());
		Assert.assertEquals(CollUtil.newArrayList(c), new ArrayList<>(b.getChildren().values()));

		Assert.assertNotNull(c);
		Assert.assertEquals("c", c.getKey());
		Assert.assertEquals(CollUtil.newArrayList(), new ArrayList<>(c.getChildren().values()));

	}

	@Test
	public void clearTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");
		Assert.assertFalse(a.hasParent());
		Assert.assertTrue(a.hasChildren());
		Assert.assertTrue(b.hasParent());
		Assert.assertTrue(b.hasChildren());
		Assert.assertTrue(c.hasParent());
		Assert.assertFalse(c.hasChildren());

		map.clear();
		Assert.assertTrue(map.isEmpty());
		Assert.assertFalse(a.hasParent());
		Assert.assertFalse(a.hasChildren());
		Assert.assertFalse(b.hasParent());
		Assert.assertFalse(b.hasChildren());
		Assert.assertFalse(c.hasParent());
		Assert.assertFalse(c.hasChildren());
	}

	@Test
	public void getNodeValueTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.putNode("a", "aaa");
		Assert.assertEquals("aaa", map.getNodeValue("a"));
		Assert.assertNull(map.getNodeValue("b"));
	}

	@Test
	public void putAllNodeTest() {
		final ForestMap<String, Map<String, String>> map = new LinkedForestMap<>(false);

		final Map<String, String> aMap = MapBuilder.<String, String> create()
			.put("pid", null)
			.put("id", "a")
			.build();
		final Map<String, String> bMap = MapBuilder.<String, String> create()
			.put("pid", "a")
			.put("id", "b")
			.build();
		final Map<String, String> cMap = MapBuilder.<String, String> create()
			.put("pid", "b")
			.put("id", "c")
			.build();
		map.putAllNode(Arrays.asList(aMap, bMap, cMap), m -> m.get("id"), m -> m.get("pid"), true);

		final TreeEntry<String, Map<String, String>> a = map.get("a");
		Assert.assertNotNull(a);
		final TreeEntry<String, Map<String, String>> b = map.get("b");
		Assert.assertNotNull(b);
		final TreeEntry<String, Map<String, String>> c = map.get("c");
		Assert.assertNotNull(c);

		Assert.assertNull(a.getDeclaredParent());
		Assert.assertEquals(a, b.getDeclaredParent());
		Assert.assertEquals(b, c.getDeclaredParent());

		Assert.assertEquals(aMap, a.getValue());
		Assert.assertEquals(bMap, b.getValue());
		Assert.assertEquals(cMap, c.getValue());
	}

	@Test
	public void putNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		Assert.assertNull(map.get("a"));

		map.putNode("a", "aaa");
		Assert.assertNotNull(map.get("a"));
		Assert.assertEquals("aaa", map.get("a").getValue());

		map.putNode("a", "aaaa");
		Assert.assertNotNull(map.get("a"));
		Assert.assertEquals("aaaa", map.get("a").getValue());
	}

	@Test
	public void putLinkedNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		Assert.assertNull(map.get("a"));
		Assert.assertNull(map.get("b"));

		map.putLinkedNodes("a", "b", "bbb");
		Assert.assertNotNull(map.get("a"));
		Assert.assertNull(map.get("a").getValue());
		Assert.assertNotNull(map.get("b"));
		Assert.assertEquals("bbb", map.get("b").getValue());

		map.putLinkedNodes("a", "b", "bbbb");
		Assert.assertNotNull(map.get("a"));
		Assert.assertNull(map.get("a").getValue());
		Assert.assertNotNull(map.get("b"));
		Assert.assertEquals("bbbb", map.get("b").getValue());
	}

	@Test
	public void putLinkedNodesTest2() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		Assert.assertNull(map.get("a"));
		Assert.assertNull(map.get("b"));

		map.putLinkedNodes("a", "aaa", "b", "bbb");
		Assert.assertNotNull(map.get("a"));
		Assert.assertEquals("aaa", map.get("a").getValue());
		Assert.assertNotNull(map.get("b"));
		Assert.assertEquals("bbb", map.get("b").getValue());

		map.putLinkedNodes("a", "aaaa", "b", "bbbb");
		Assert.assertNotNull(map.get("a"));
		Assert.assertEquals("aaaa", map.get("a").getValue());
		Assert.assertNotNull(map.get("b"));
		Assert.assertEquals("bbbb", map.get("b").getValue());
	}

	@Test
	public void linkNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");

		final TreeEntry<String, String> parent = map.get("a");
		final TreeEntry<String, String> child = map.get("b");

		Assert.assertNotNull(parent);
		Assert.assertEquals("a", parent.getKey());
		Assert.assertEquals(child, parent.getChild("b"));

		Assert.assertNotNull(child);
		Assert.assertEquals("b", child.getKey());
		Assert.assertEquals(parent, child.getDeclaredParent());
	}

	@Test
	public void unlinkNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		final TreeEntry<String, String> parent = map.get("a");
		final TreeEntry<String, String> child = map.get("b");
		map.unlinkNode("a", "b");
		Assert.assertFalse(child.hasParent());
		Assert.assertFalse(parent.hasChildren());
	}

	@Test
	public void getTreeNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final List<String> expected = CollUtil.newArrayList("a", "b", "c");
		List<String> actual = CollStreamUtil.toList(map.getTreeNodes("a"), TreeEntry::getKey);
		Assert.assertEquals(expected, actual);
		actual = CollStreamUtil.toList(map.getTreeNodes("b"), TreeEntry::getKey);
		Assert.assertEquals(expected, actual);
		actual = CollStreamUtil.toList(map.getTreeNodes("c"), TreeEntry::getKey);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void getRootNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> root = map.get("a");
		Assert.assertEquals(root, map.getRootNode("a"));
		Assert.assertEquals(root, map.getRootNode("b"));
		Assert.assertEquals(root, map.getRootNode("c"));
	}

	@Test
	public void getDeclaredParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		Assert.assertEquals(a, map.getDeclaredParentNode("b"));
		Assert.assertEquals(b, map.getDeclaredParentNode("c"));
	}

	@Test
	public void getParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");

		Assert.assertEquals(a, map.getParentNode("c", "a"));
		Assert.assertEquals(b, map.getParentNode("c", "b"));
		Assert.assertEquals(a, map.getParentNode("b", "a"));
		Assert.assertNull(map.getParentNode("a", "a"));
	}

	@Test
	public void containsParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		Assert.assertTrue(map.containsParentNode("c", "b"));
		Assert.assertTrue(map.containsParentNode("c", "a"));
		Assert.assertTrue(map.containsParentNode("b", "a"));
		Assert.assertFalse(map.containsParentNode("a", "a"));
	}

	@Test
	public void containsChildNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		Assert.assertTrue(map.containsChildNode("a", "b"));
		Assert.assertTrue(map.containsChildNode("a", "c"));
		Assert.assertTrue(map.containsChildNode("b", "c"));
		Assert.assertFalse(map.containsChildNode("c", "c"));
	}

	@Test
	public void getDeclaredChildNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		Assert.assertEquals(CollUtil.newArrayList(b), new ArrayList<>(map.getDeclaredChildNodes("a")));
		Assert.assertEquals(CollUtil.newArrayList(c), new ArrayList<>(map.getDeclaredChildNodes("b")));
		Assert.assertEquals(CollUtil.newArrayList(), new ArrayList<>(map.getDeclaredChildNodes("c")));
	}

	@Test
	public void getChildNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		Assert.assertEquals(CollUtil.newArrayList(b, c), new ArrayList<>(map.getChildNodes("a")));
		Assert.assertEquals(CollUtil.newArrayList(c), new ArrayList<>(map.getChildNodes("b")));
		Assert.assertEquals(CollUtil.newArrayList(), new ArrayList<>(map.getChildNodes("c")));
	}

}
