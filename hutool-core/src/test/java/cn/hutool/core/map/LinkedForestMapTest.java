package cn.hutool.core.map;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedForestMapTest {

	private final ForestMap<String, String> treeNodeMap = new LinkedForestMap<>(false);

	@BeforeEach
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
		assertEquals("c", treeEntry.getKey());
		assertEquals("ccc", treeEntry.getValue());

		// 父节点相关方法
		assertEquals(2, treeEntry.getWeight());
		assertEquals(treeNodeMap.get("a"), treeEntry.getRoot());
		assertTrue(treeEntry.hasParent());
		assertEquals(parent, treeEntry.getDeclaredParent());
		assertEquals(treeNodeMap.get("a"), treeEntry.getParent("a"));
		assertTrue(treeEntry.containsParent("a"));

		// 子节点相关方法
		final List<TreeEntry<String, String>> nodes = new ArrayList<>();
		treeEntry.forEachChild(true, nodes::add);
		assertEquals(CollUtil.newArrayList(treeEntry, child), nodes);
		nodes.clear();
		treeEntry.forEachChild(false, nodes::add);
		assertEquals(CollUtil.newArrayList(child), nodes);

		assertEquals(CollUtil.newLinkedHashSet(child), new LinkedHashSet<>(treeEntry.getDeclaredChildren().values()));
		assertEquals(CollUtil.newLinkedHashSet(child), new LinkedHashSet<>(treeEntry.getChildren().values()));
		assertTrue(treeEntry.hasChildren());
		assertEquals(treeNodeMap.get("d"), treeEntry.getChild("d"));
		assertTrue(treeEntry.containsChild("d"));
	}

	@Test
	public void putTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		TreeEntry<String, String> treeEntry = new LinkedForestMap.TreeEntryNode<>(null, "a", "aaa");
		assertNull(map.put("a", treeEntry));
		assertNotEquals(map.get("a"), treeEntry);
		assertEquals(map.get("a").getKey(), treeEntry.getKey());
		assertEquals(map.get("a").getValue(), treeEntry.getValue());

		treeEntry = new LinkedForestMap.TreeEntryNode<>(null, "a", "aaaa");
		assertNotNull(map.put("a", treeEntry));
		assertNotEquals(map.get("a"), treeEntry);
		assertEquals(map.get("a").getKey(), treeEntry.getKey());
		assertEquals(map.get("a").getValue(), treeEntry.getValue());
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
		assertNull(map.get("b"));
		assertFalse(b.hasChildren());
		assertFalse(b.hasParent());
		assertEquals(a, c.getDeclaredParent());
		assertEquals(CollUtil.newArrayList(c), new ArrayList<>(a.getDeclaredChildren().values()));
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

		assertNotNull(a);
		assertEquals("a", a.getKey());
		assertEquals(CollUtil.newArrayList(b, c), new ArrayList<>(a.getChildren().values()));

		assertNotNull(b);
		assertEquals("b", b.getKey());
		assertEquals(CollUtil.newArrayList(c), new ArrayList<>(b.getChildren().values()));

		assertNotNull(c);
		assertEquals("c", c.getKey());
		assertEquals(CollUtil.newArrayList(), new ArrayList<>(c.getChildren().values()));

	}

	@Test
	public void clearTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");
		assertFalse(a.hasParent());
		assertTrue(a.hasChildren());
		assertTrue(b.hasParent());
		assertTrue(b.hasChildren());
		assertTrue(c.hasParent());
		assertFalse(c.hasChildren());

		map.clear();
		assertFalse(a.hasParent());
		assertFalse(a.hasChildren());
		assertFalse(b.hasParent());
		assertFalse(b.hasChildren());
		assertFalse(c.hasParent());
		assertFalse(c.hasChildren());
	}

	@Test
	public void getNodeValueTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.putNode("a", "aaa");
		assertEquals("aaa", map.getNodeValue("a"));
		assertNull(map.getNodeValue("b"));
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
		assertNotNull(a);
		final TreeEntry<String, Map<String, String>> b = map.get("b");
		assertNotNull(b);
		final TreeEntry<String, Map<String, String>> c = map.get("c");
		assertNotNull(c);

		assertNull(a.getDeclaredParent());
		assertEquals(a, b.getDeclaredParent());
		assertEquals(b, c.getDeclaredParent());

		assertEquals(aMap, a.getValue());
		assertEquals(bMap, b.getValue());
		assertEquals(cMap, c.getValue());
	}

	@Test
	public void putNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		assertNull(map.get("a"));

		map.putNode("a", "aaa");
		assertNotNull(map.get("a"));
		assertEquals("aaa", map.get("a").getValue());

		map.putNode("a", "aaaa");
		assertNotNull(map.get("a"));
		assertEquals("aaaa", map.get("a").getValue());
	}

	@Test
	public void putLinkedNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		assertNull(map.get("a"));
		assertNull(map.get("b"));

		map.putLinkedNodes("a", "b", "bbb");
		assertNotNull(map.get("a"));
		assertNull(map.get("a").getValue());
		assertNotNull(map.get("b"));
		assertEquals("bbb", map.get("b").getValue());

		map.putLinkedNodes("a", "b", "bbbb");
		assertNotNull(map.get("a"));
		assertNull(map.get("a").getValue());
		assertNotNull(map.get("b"));
		assertEquals("bbbb", map.get("b").getValue());
	}

	@Test
	public void putLinkedNodesTest2() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);

		assertNull(map.get("a"));
		assertNull(map.get("b"));

		map.putLinkedNodes("a", "aaa", "b", "bbb");
		assertNotNull(map.get("a"));
		assertEquals("aaa", map.get("a").getValue());
		assertNotNull(map.get("b"));
		assertEquals("bbb", map.get("b").getValue());

		map.putLinkedNodes("a", "aaaa", "b", "bbbb");
		assertNotNull(map.get("a"));
		assertEquals("aaaa", map.get("a").getValue());
		assertNotNull(map.get("b"));
		assertEquals("bbbb", map.get("b").getValue());
	}

	@Test
	public void linkNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");

		final TreeEntry<String, String> parent = map.get("a");
		final TreeEntry<String, String> child = map.get("b");

		assertNotNull(parent);
		assertEquals("a", parent.getKey());
		assertEquals(child, parent.getChild("b"));

		assertNotNull(child);
		assertEquals("b", child.getKey());
		assertEquals(parent, child.getDeclaredParent());
	}

	@Test
	public void unlinkNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		final TreeEntry<String, String> parent = map.get("a");
		final TreeEntry<String, String> child = map.get("b");
		map.unlinkNode("a", "b");
		assertFalse(child.hasParent());
		assertFalse(parent.hasChildren());
	}

	@Test
	public void getTreeNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final List<String> expected = CollUtil.newArrayList("a", "b", "c");
		List<String> actual = CollStreamUtil.toList(map.getTreeNodes("a"), TreeEntry::getKey);
		assertEquals(expected, actual);
		actual = CollStreamUtil.toList(map.getTreeNodes("b"), TreeEntry::getKey);
		assertEquals(expected, actual);
		actual = CollStreamUtil.toList(map.getTreeNodes("c"), TreeEntry::getKey);
		assertEquals(expected, actual);
	}

	@Test
	public void getRootNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");

		final TreeEntry<String, String> root = map.get("a");
		assertEquals(root, map.getRootNode("a"));
		assertEquals(root, map.getRootNode("b"));
		assertEquals(root, map.getRootNode("c"));
	}

	@Test
	public void getDeclaredParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");
		assertEquals(a, map.getDeclaredParentNode("b"));
		assertEquals(b, map.getDeclaredParentNode("c"));
	}

	@Test
	public void getParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> a = map.get("a");
		final TreeEntry<String, String> b = map.get("b");

		assertEquals(a, map.getParentNode("c", "a"));
		assertEquals(b, map.getParentNode("c", "b"));
		assertEquals(a, map.getParentNode("b", "a"));
		assertNull(map.getParentNode("a", "a"));
	}

	@Test
	public void containsParentNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		assertTrue(map.containsParentNode("c", "b"));
		assertTrue(map.containsParentNode("c", "a"));
		assertTrue(map.containsParentNode("b", "a"));
		assertFalse(map.containsParentNode("a", "a"));
	}

	@Test
	public void containsChildNodeTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		assertNotNull(b);
		final TreeEntry<String, String> c = map.get("c");
		assertNotNull(c);

		assertTrue(map.containsChildNode("a", "b"));
		assertTrue(map.containsChildNode("a", "c"));
		assertTrue(map.containsChildNode("b", "c"));
		assertFalse(map.containsChildNode("c", "c"));
	}

	@Test
	public void getDeclaredChildNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		assertEquals(CollUtil.newArrayList(b), new ArrayList<>(map.getDeclaredChildNodes("a")));
		assertEquals(CollUtil.newArrayList(c), new ArrayList<>(map.getDeclaredChildNodes("b")));
		assertEquals(CollUtil.newArrayList(), new ArrayList<>(map.getDeclaredChildNodes("c")));
	}

	@Test
	public void getChildNodesTest() {
		final ForestMap<String, String> map = new LinkedForestMap<>(false);
		map.linkNodes("a", "b");
		map.linkNodes("b", "c");
		final TreeEntry<String, String> b = map.get("b");
		final TreeEntry<String, String> c = map.get("c");

		assertEquals(CollUtil.newArrayList(b, c), new ArrayList<>(map.getChildNodes("a")));
		assertEquals(CollUtil.newArrayList(c), new ArrayList<>(map.getChildNodes("b")));
		assertEquals(CollUtil.newArrayList(), new ArrayList<>(map.getChildNodes("c")));
	}

}
