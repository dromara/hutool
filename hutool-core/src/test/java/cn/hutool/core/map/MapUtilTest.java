package cn.hutool.core.map;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.text.StrUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapUtilTest {

	enum PeopleEnum {GIRL, BOY, CHILD}

	@Data
	@Builder
	public static class User {
		private Long id;
		private String name;
	}

	@Data
	@Builder
	public static class Group {
		private Long id;
		private List<User> users;
	}

	@Data
	@Builder
	public static class UserGroup {
		private Long userId;
		private Long groupId;
	}


	@Test
	public void filterTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> Convert.toInt(t.getValue()) % 2 == 0);

		Assert.assertEquals(2, map2.size());

		Assert.assertEquals("2", map2.get("b"));
		Assert.assertEquals("4", map2.get("d"));
	}

	@Test
	public void mapTest() {
		// Add test like a foreigner
		final Map<Integer, String> adjectivesMap = MapUtil.<Integer, String>builder()
				.put(0, "lovely")
				.put(1, "friendly")
				.put(2, "happily")
				.build();

		final Map<Integer, String> resultMap = MapUtil.map(adjectivesMap, (k, v) -> v + " " + PeopleEnum.values()[k].name().toLowerCase());

		Assert.assertEquals("lovely girl", resultMap.get(0));
		Assert.assertEquals("friendly boy", resultMap.get(1));
		Assert.assertEquals("happily child", resultMap.get(2));

		// 下单用户，Queue表示正在 .排队. 抢我抢不到的二次元周边！
		final Queue<String> customers = new ArrayDeque<>(Arrays.asList("刑部尚书手工耿", "木瓜大盗大漠叔", "竹鼠发烧找华农", "朴实无华朱一旦"));
		// 分组
		final List<Group> groups = Stream.iterate(0L, i -> ++i).limit(4).map(i -> Group.builder().id(i).build()).collect(Collectors.toList());
		// 如你所见，它是一个map，key由用户id，value由用户组成
		final Map<Long, User> idUserMap = Stream.iterate(0L, i -> ++i).limit(4).map(i -> User.builder().id(i).name(customers.poll()).build()).collect(Collectors.toMap(User::getId, Function.identity()));
		// 如你所见，它是一个map，key由分组id，value由用户ids组成，典型的多对多关系
		final Map<Long, List<Long>> groupIdUserIdsMap = groups.stream().flatMap(group -> idUserMap.keySet().stream().map(userId -> UserGroup.builder().groupId(group.getId()).userId(userId).build())).collect(Collectors.groupingBy(UserGroup::getUserId, Collectors.mapping(UserGroup::getGroupId, Collectors.toList())));

		// 神奇的魔法发生了， 分组id和用户ids组成的map，竟然变成了订单编号和用户实体集合组成的map
		final Map<Long, List<User>> groupIdUserMap = MapUtil.map(groupIdUserIdsMap, (groupId, userIds) -> userIds.stream().map(idUserMap::get).collect(Collectors.toList()));

		// 然后你就可以拿着这个map，去封装groups，使其能够在订单数据带出客户信息啦
		groups.forEach(group -> Opt.ofNullable(group.getId()).map(groupIdUserMap::get).ifPresent(group::setUsers));

		// 下面是测试报告
		groups.forEach(group -> {
			final List<User> users = group.getUsers();
			Assert.assertEquals("刑部尚书手工耿", users.get(0).getName());
			Assert.assertEquals("木瓜大盗大漠叔", users.get(1).getName());
			Assert.assertEquals("竹鼠发烧找华农", users.get(2).getName());
			Assert.assertEquals("朴实无华朱一旦", users.get(3).getName());
		});
		// 能写代码真开心
	}

	@Test
	public void filterMapWrapperTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> camelCaseMap = MapUtil.toCamelCaseMap(map);

		final Map<String, String> map2 = MapUtil.filter(camelCaseMap, t -> Convert.toInt(t.getValue()) % 2 == 0);

		Assert.assertEquals(2, map2.size());

		Assert.assertEquals("2", map2.get("b"));
		Assert.assertEquals("4", map2.get("d"));
	}

	@Test
	public void filterContainsTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("abc", "1");
		map.put("bcd", "2");
		map.put("def", "3");
		map.put("fgh", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> StrUtil.contains(t.getKey(), "bc"));
		Assert.assertEquals(2, map2.size());
		Assert.assertEquals("1", map2.get("abc"));
		Assert.assertEquals("2", map2.get("bcd"));
	}

	@Test
	public void editTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.edit(map, t -> {
			// 修改每个值使之*10
			t.setValue(t.getValue() + "0");
			return t;
		});

		Assert.assertEquals(4, map2.size());

		Assert.assertEquals("10", map2.get("a"));
		Assert.assertEquals("20", map2.get("b"));
		Assert.assertEquals("30", map2.get("c"));
		Assert.assertEquals("40", map2.get("d"));
	}

	@Test
	public void reverseTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.reverse(map);

		Assert.assertEquals("a", map2.get("1"));
		Assert.assertEquals("b", map2.get("2"));
		Assert.assertEquals("c", map2.get("3"));
		Assert.assertEquals("d", map2.get("4"));
	}

	@Test
	public void toObjectArrayTest() {
		final Map<String, String> map = MapUtil.newHashMap(true);
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Object[][] objectArray = MapUtil.toObjectArray(map);
		Assert.assertEquals("a", objectArray[0][0]);
		Assert.assertEquals("1", objectArray[0][1]);
		Assert.assertEquals("b", objectArray[1][0]);
		Assert.assertEquals("2", objectArray[1][1]);
		Assert.assertEquals("c", objectArray[2][0]);
		Assert.assertEquals("3", objectArray[2][1]);
		Assert.assertEquals("d", objectArray[3][0]);
		Assert.assertEquals("4", objectArray[3][1]);
	}

	@Test
	public void sortJoinTest(){
		final Map<String, String> build = MapUtil.builder(new HashMap<String, String>())
				.put("key1", "value1")
				.put("key3", "value3")
				.put("key2", "value2").build();

		final String join1 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false);
		Assert.assertEquals("key1value1key2value2key3value3", join1);

		final String join2 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123");
		Assert.assertEquals("key1value1key2value2key3value3123", join2);

		final String join3 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123", "abc");
		Assert.assertEquals("key1value1key2value2key3value3123abc", join3);
	}

	@Test
	public void ofEntriesTest(){
		final Map<String, Integer> map = MapUtil.ofEntries(MapUtil.entry("a", 1), MapUtil.entry("b", 2));
		Assert.assertEquals(2, map.size());

		Assert.assertEquals(Integer.valueOf(1), map.get("a"));
		Assert.assertEquals(Integer.valueOf(2), map.get("b"));
	}

	@Test(expected = NumberFormatException.class)
	public void getIntTest(){
		final Map<String, String> map = MapUtil.ofEntries(MapUtil.entry("a", "d"));
		final Integer a = MapUtil.getInt(map, "a");
		Assert.assertNotNull(a);
	}

	@Test
	public void getIntValueTest(){
		final Map<String, String> map = MapUtil.ofEntries(MapUtil.entry("a", "1"), MapUtil.entry("b", null));
		final int a = MapUtil.get(map, "a", int.class);
		Assert.assertEquals(1, a);

		final int b = MapUtil.getInt(map, "b", 0);
		Assert.assertEquals(0, b);
	}

	@Test
	public void valuesOfKeysTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final Dict v2 = Dict.of().set("age", 13).set("id", 15).set("name", "李四");

		final String[] keys = v1.keySet().toArray(new String[0]);
		final ArrayList<Object> v1s = MapUtil.valuesOfKeys(v1, keys);
		Assert.assertTrue(v1s.contains(12));
		Assert.assertTrue(v1s.contains(23));
		Assert.assertTrue(v1s.contains("张三"));

		final ArrayList<Object> v2s = MapUtil.valuesOfKeys(v2, keys);
		Assert.assertTrue(v2s.contains(15));
		Assert.assertTrue(v2s.contains(13));
		Assert.assertTrue(v2s.contains("李四"));
	}

	@Test
	public void joinIgnoreNullTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", null);
		final String s = MapUtil.joinIgnoreNull(v1, ",", "=");
		Assert.assertEquals("id=12,name=张三", s);
	}
}
