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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.text.StrUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapUtilTest {

	enum PeopleEnum {GIRL, BOY, CHILD}

	@Data
	@Builder
	static class User {
		private Long id;
		private String name;
	}

	@Data
	@Builder
	static class Group {
		private Long id;
		private List<User> users;
	}

	@Data
	@Builder
	static class UserGroup {
		private Long userId;
		private Long groupId;
	}


	@Test
	void filterTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> Convert.toInt(t.getValue()) % 2 == 0);

		Assertions.assertEquals(2, map2.size());

		Assertions.assertEquals("2", map2.get("b"));
		Assertions.assertEquals("4", map2.get("d"));
	}

	@Test
	void mapTest() {
		// Add test like a foreigner
		final Map<Integer, String> adjectivesMap = MapUtil.<Integer, String>builder()
			.put(0, "lovely")
			.put(1, "friendly")
			.put(2, "happily")
			.build();

		final Map<Integer, String> resultMap = MapUtil.map(adjectivesMap, (k, v) -> v + " " + PeopleEnum.values()[k].name().toLowerCase());

		Assertions.assertEquals("lovely girl", resultMap.get(0));
		Assertions.assertEquals("friendly boy", resultMap.get(1));
		Assertions.assertEquals("happily child", resultMap.get(2));

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
			Assertions.assertEquals("刑部尚书手工耿", users.get(0).getName());
			Assertions.assertEquals("木瓜大盗大漠叔", users.get(1).getName());
			Assertions.assertEquals("竹鼠发烧找华农", users.get(2).getName());
			Assertions.assertEquals("朴实无华朱一旦", users.get(3).getName());
		});
		// 能写代码真开心
	}

	@Test
	void filterMapWrapperTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> camelCaseMap = MapUtil.toCamelCaseMap(map);

		final Map<String, String> map2 = MapUtil.filter(camelCaseMap, t -> Convert.toInt(t.getValue()) % 2 == 0);

		Assertions.assertEquals(2, map2.size());

		Assertions.assertEquals("2", map2.get("b"));
		Assertions.assertEquals("4", map2.get("d"));
	}

	@Test
	void filterContainsTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("abc", "1");
		map.put("bcd", "2");
		map.put("def", "3");
		map.put("fgh", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> StrUtil.contains(t.getKey(), "bc"));
		Assertions.assertEquals(2, map2.size());
		Assertions.assertEquals("1", map2.get("abc"));
		Assertions.assertEquals("2", map2.get("bcd"));
	}

	@Test
	void editTest() {
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

		Assertions.assertEquals(4, map2.size());

		Assertions.assertEquals("10", map2.get("a"));
		Assertions.assertEquals("20", map2.get("b"));
		Assertions.assertEquals("30", map2.get("c"));
		Assertions.assertEquals("40", map2.get("d"));
	}

	@Test
	void reverseTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.reverse(map);

		Assertions.assertEquals("a", map2.get("1"));
		Assertions.assertEquals("b", map2.get("2"));
		Assertions.assertEquals("c", map2.get("3"));
		Assertions.assertEquals("d", map2.get("4"));
	}

	@Test
	void toObjectArrayTest() {
		final Map<String, String> map = MapUtil.newHashMap(true);
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Object[][] objectArray = MapUtil.toObjectArray(map);
		Assertions.assertEquals("a", objectArray[0][0]);
		Assertions.assertEquals("1", objectArray[0][1]);
		Assertions.assertEquals("b", objectArray[1][0]);
		Assertions.assertEquals("2", objectArray[1][1]);
		Assertions.assertEquals("c", objectArray[2][0]);
		Assertions.assertEquals("3", objectArray[2][1]);
		Assertions.assertEquals("d", objectArray[3][0]);
		Assertions.assertEquals("4", objectArray[3][1]);
	}

	@Test
	void sortJoinTest() {
		final Map<String, String> build = MapUtil.builder(new HashMap<String, String>())
			.put("key1", "value1")
			.put("key3", "value3")
			.put("key2", "value2").build();

		final String join1 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false);
		Assertions.assertEquals("key1value1key2value2key3value3", join1);

		final String join2 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123");
		Assertions.assertEquals("key1value1key2value2key3value3123", join2);

		final String join3 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123", "abc");
		Assertions.assertEquals("key1value1key2value2key3value3123abc", join3);
	}

	@Test
	void ofEntriesTest() {
		final Map<String, Integer> map = MapUtil.ofEntries(MapUtil.entry("a", 1), MapUtil.entry("b", 2));
		Assertions.assertEquals(2, map.size());

		Assertions.assertEquals(Integer.valueOf(1), map.get("a"));
		Assertions.assertEquals(Integer.valueOf(2), map.get("b"));
	}

	@Test
	void getIntTest() {
		Assertions.assertThrows(NumberFormatException.class, () -> {
			final Map<String, String> map = MapUtil.ofEntries(MapUtil.entry("a", "d"));
			final Integer a = MapUtil.getInt(map, "a");
			Assertions.assertNotNull(a);
		});
	}

	@Test
	void getIntValueTest() {
		final Map<String, String> map = MapUtil.ofEntries(MapUtil.entry("a", "1"), MapUtil.entry("b", null));
		final int a = MapUtil.get(map, "a", int.class);
		Assertions.assertEquals(1, a);

		final int b = MapUtil.getInt(map, "b", 0);
		Assertions.assertEquals(0, b);
	}

	@Test
	void valuesOfKeysTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final Dict v2 = Dict.of().set("age", 13).set("id", 15).set("name", "李四");

		final String[] keys = v1.keySet().toArray(new String[0]);
		final ArrayList<Object> v1s = MapUtil.valuesOfKeys(v1, keys);
		Assertions.assertTrue(v1s.contains(12));
		Assertions.assertTrue(v1s.contains(23));
		Assertions.assertTrue(v1s.contains("张三"));

		final ArrayList<Object> v2s = MapUtil.valuesOfKeys(v2, keys);
		Assertions.assertTrue(v2s.contains(15));
		Assertions.assertTrue(v2s.contains(13));
		Assertions.assertTrue(v2s.contains("李四"));
	}

	@Test
	void joinIgnoreNullTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", null);
		final String s = MapUtil.joinIgnoreNull(v1, ",", "=");
		Assertions.assertEquals("id=12,name=张三", s);
	}

	@Test
	void computeIfAbsentForJdk8Test() {
		// https://github.com/apache/dubbo/issues/11986
		final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
		// // map.computeIfAbsent("AaAa", key->map.computeIfAbsent("BBBB",key2->42));
		MapUtil.computeIfAbsentForJdk8(map, "AaAa", key -> map.computeIfAbsent("BBBB", key2 -> 42));

		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals(Integer.valueOf(42), map.get("AaAa"));
		Assertions.assertEquals(Integer.valueOf(42), map.get("BBBB"));
	}

	@Test
	void createMapTest() {
		final Map<Object, Object> map = MapUtil.createMap(MapUtil.view(new HashMap<>()).getClass());
		Assertions.assertEquals(HashMap.class, map.getClass());
	}

	@Test
	public void renameKeyTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", null);
		final Map<String, Object> map = MapUtil.renameKey(v1, "name", "newName");
		Assertions.assertEquals("张三", map.get("newName"));
	}

	@Test
	public void removeNullValueTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", null).set("age", null);
		final Map<String, Object> map = MapUtil.removeNullValue(v1);
		Assertions.assertEquals(1, map.size());
	}

	@Test
	public void issue3162Test() {
		final Map<String, Object> map = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
				put("a", "1");
				put("b", "2");
				put("c", "3");
			}
		};
		final Map<String, Object> filtered = MapUtil.filter(map, "a", "b");
		Assertions.assertEquals(2, filtered.size());
		Assertions.assertEquals("1", filtered.get("a"));
		Assertions.assertEquals("2", filtered.get("b"));
	}

	@Test
	public void ofKvsLinkTest() {
		final Map<String, Long> map2 = MapUtil.ofKvs(true,
			"RED", 0xFF0000,
			"GREEN", 0x00FF00,
			"BLUE", 0x0000FF);
		Assertions.assertEquals(3, map2.size());
		Assertions.assertEquals(LinkedHashMap.class, map2.getClass());
	}
}
