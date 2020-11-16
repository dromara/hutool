package cn.hutool.core.bean;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Bean工具单元测试
 *
 * @author Looly
 */
public class BeanUtilTest {

	@Test
	public void isBeanTest() {

		// HashMap不包含setXXX方法，不是bean
		boolean isBean = BeanUtil.isBean(HashMap.class);
		Assert.assertFalse(isBean);
	}

	@Test
	public void fillBeanTest() {
		Person person = BeanUtil.fillBean(new Person(), new ValueProvider<String>() {

			@Override
			public Object value(String key, Type valueType) {
				switch (key) {
					case "name":
						return "张三";
					case "age":
						return 18;
				}
				return null;
			}

			@Override
			public boolean containsKey(String key) {
				// 总是存在key
				return true;
			}

		}, CopyOptions.create());

		Assert.assertEquals(person.getName(), "张三");
		Assert.assertEquals(person.getAge(), 18);
	}

	@Test
	public void fillBeanWithMapIgnoreCaseTest() {
		HashMap<String, Object> map = CollUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);
		map.put("openId", "DFDFSDFWERWER");
		SubPerson person = BeanUtil.fillBeanWithMapIgnoreCase(map, new SubPerson(), false);
		Assert.assertEquals(person.getName(), "Joe");
		Assert.assertEquals(person.getAge(), 12);
		Assert.assertEquals(person.getOpenid(), "DFDFSDFWERWER");
	}

	@Test
	public void toBeanTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		final Map<?, ?> map = BeanUtil.toBean(person, Map.class);
		Assert.assertEquals("测试A11", map.get("name"));
		Assert.assertEquals(14, map.get("age"));
		Assert.assertEquals("11213232", map.get("openid"));
		// static属性应被忽略
		Assert.assertFalse(map.containsKey("SUBNAME"));
	}

	/**
	 * 忽略转换错误测试
	 */
	@Test
	public void toBeanIgnoreErrorTest() {
		HashMap<String, Object> map = CollUtil.newHashMap();
		map.put("name", "Joe");
		// 错误的类型，此处忽略
		map.put("age", "aaaaaa");

		Person person = BeanUtil.toBeanIgnoreError(map, Person.class);
		Assert.assertEquals("Joe", person.getName());
		// 错误的类型，不copy这个字段，使用对象创建的默认值
		Assert.assertEquals(0, person.getAge());
	}

	@Test
	public void mapToBeanIgnoreCaseTest() {
		HashMap<String, Object> map = CollUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);

		Person person = BeanUtil.toBeanIgnoreCase(map, Person.class, false);
		Assert.assertEquals("Joe", person.getName());
		Assert.assertEquals(12, person.getAge());
	}

	@Test
	public void mapToBeanTest() {
		HashMap<String, Object> map = CollUtil.newHashMap();
		map.put("a_name", "Joe");
		map.put("b_age", 12);

		// 别名，用于对应bean的字段名
		HashMap<String, String> mapping = CollUtil.newHashMap();
		mapping.put("a_name", "name");
		mapping.put("b_age", "age");

		Person person = BeanUtil.toBean(map, Person.class, CopyOptions.create().setFieldMapping(mapping));
		Assert.assertEquals("Joe", person.getName());
		Assert.assertEquals(12, person.getAge());
	}

	/**
	 * 测试public类型的字段注入是否成功
	 */
	@Test
	public void mapToBeanTest2() {
		HashMap<String, Object> map = CollUtil.newHashMap();
		map.put("name", "Joe");
		map.put("age", 12);

		// 非空构造也可以实例化成功
		Person2 person = BeanUtil.toBean(map, Person2.class, CopyOptions.create());
		Assert.assertEquals("Joe", person.name);
		Assert.assertEquals(12, person.age);
	}

	/**
	 * 测试在不忽略错误情况下，转换失败需要报错。
	 */
	@Test(expected = NumberFormatException.class)
	public void mapToBeanWinErrorTest() {
		Map<String, String> map = new HashMap<>();
		map.put("age", "哈哈");
		Person user = BeanUtil.toBean(map, Person.class);
	}

	@Test
	public void beanToMapTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		Map<String, Object> map = BeanUtil.beanToMap(person);

		Assert.assertEquals("测试A11", map.get("name"));
		Assert.assertEquals(14, map.get("age"));
		Assert.assertEquals("11213232", map.get("openid"));
		// static属性应被忽略
		Assert.assertFalse(map.containsKey("SUBNAME"));
	}

	@Test
	public void beanToMapTest2() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		Map<String, Object> map = BeanUtil.beanToMap(person, true, true);
		Assert.assertEquals("sub名字", map.get("sub_name"));
	}

	@Test
	public void beanToMapWithAliasTest() {
		SubPersonWithAlias person = new SubPersonWithAlias();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");
		person.setSlow(true);

		Map<String, Object> map = BeanUtil.beanToMap(person);
		Assert.assertEquals("sub名字", map.get("aliasSubName"));
	}

	@Test
	public void mapToBeanWithAliasTest() {
		Map<String, Object> map = MapUtil.newHashMap();
		map.put("aliasSubName", "sub名字");
		map.put("slow", true);

		final SubPersonWithAlias subPersonWithAlias = BeanUtil.toBean(map, SubPersonWithAlias.class);
		Assert.assertEquals("sub名字", subPersonWithAlias.getSubName());
	}

	@Test
	public void beanToMapWithLocalDateTimeTest() {
		final LocalDateTime now = LocalDateTime.now();

		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");
		person.setDate(now);
		person.setDate2(now.toLocalDate());

		Map<String, Object> map = BeanUtil.beanToMap(person, false, true);
		Assert.assertEquals(now, map.get("date"));
		Assert.assertEquals(now.toLocalDate(), map.get("date2"));
	}

	@Test
	public void getPropertyTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		Object name = BeanUtil.getProperty(person, "name");
		Assert.assertEquals("测试A11", name);
		Object subName = BeanUtil.getProperty(person, "subName");
		Assert.assertEquals("sub名字", subName);
	}

	@Test
	public void getPropertyDescriptorsTest() {
		HashSet<Object> set = CollUtil.newHashSet();
		PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(SubPerson.class);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			set.add(propertyDescriptor.getName());
		}
		Assert.assertTrue(set.contains("age"));
		Assert.assertTrue(set.contains("id"));
		Assert.assertTrue(set.contains("name"));
		Assert.assertTrue(set.contains("openid"));
		Assert.assertTrue(set.contains("slow"));
		Assert.assertTrue(set.contains("subName"));
	}

	@Test
	public void copyPropertiesTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		SubPerson person1 = BeanUtil.copyProperties(person, SubPerson.class);
		Assert.assertEquals(14, person1.getAge());
		Assert.assertEquals("11213232", person1.getOpenid());
		Assert.assertEquals("测试A11", person1.getName());
		Assert.assertEquals("sub名字", person1.getSubName());
	}

	@Test
	public void copyPropertiesHasBooleanTest() {
		SubPerson p1 = new SubPerson();
		p1.setSlow(true);

		// 测试boolean参数值isXXX形式
		SubPerson p2 = new SubPerson();
		BeanUtil.copyProperties(p1, p2);
		Assert.assertTrue(p2.getSlow());

		// 测试boolean参数值非isXXX形式
		SubPerson2 p3 = new SubPerson2();
		BeanUtil.copyProperties(p1, p3);
		Assert.assertTrue(p3.getSlow());
	}

	@Test
	public void copyPropertiesBeanToMapTest() {
		// 测试BeanToMap
		SubPerson p1 = new SubPerson();
		p1.setSlow(true);
		p1.setName("测试");
		p1.setSubName("sub测试");

		Map<String, Object> map = MapUtil.newHashMap();
		BeanUtil.copyProperties(p1, map);
		Assert.assertTrue((Boolean) map.get("slow"));
		Assert.assertEquals("测试", map.get("name"));
		Assert.assertEquals("sub测试", map.get("subName"));
	}

	@Test
	public void copyPropertiesMapToMapTest() {
		// 测试MapToMap
		Map<String, Object> p1 = new HashMap<>();
		p1.put("isSlow", true);
		p1.put("name", "测试");
		p1.put("subName", "sub测试");

		Map<String, Object> map = MapUtil.newHashMap();
		BeanUtil.copyProperties(p1, map);
		Assert.assertTrue((Boolean) map.get("isSlow"));
		Assert.assertEquals("测试", map.get("name"));
		Assert.assertEquals("sub测试", map.get("subName"));
	}

	@Test
	public void trimBeanStrFieldsTest() {
		Person person = new Person();
		person.setAge(1);
		person.setName("  张三 ");
		person.setOpenid(null);
		Person person2 = BeanUtil.trimStrFields(person);

		// 是否改变原对象
		Assert.assertEquals("张三", person.getName());
		Assert.assertEquals("张三", person2.getName());
	}

	// -----------------------------------------------------------------------------------------------------------------
	@Getter
	@Setter
	public static class SubPerson extends Person {

		public static final String SUBNAME = "TEST";

		private UUID id;
		private String subName;
		private Boolean slow;
		private LocalDateTime date;
		private LocalDate date2;
	}

	@Getter
	@Setter
	public static class SubPerson2 extends Person {
		private String subName;
		// boolean参数值非isXXX形式
		private Boolean slow;
	}

	@Getter
	@Setter
	public static class SubPersonWithAlias extends Person {
		// boolean参数值非isXXX形式
		@Alias("aliasSubName")
		private String subName;
		private Boolean slow;
	}

	@Getter
	@Setter
	public static class SubPersonWithOverlayTransientField extends PersonWithTransientField {
		// 覆盖父类中 transient 属性
		private String name;
	}

	@Getter
	@Setter
	public static class Person {
		private String name;
		private int age;
		private String openid;
	}

	@Getter
	@Setter
	public static class PersonWithTransientField {
		private transient String name;
		private int age;
		private String openid;
	}

	public static class Person2 {

		public Person2(String name, int age, String openid) {
			this.name = name;
			this.age = age;
			this.openid = openid;
		}

		public String name;
		public int age;
		public String openid;
	}

	/**
	 * <a href="https://github.com/looly/hutool/issues/1173">#1173</a>
	 */
	@Test
	public void beanToBeanOverlayFieldTest() {
		SubPersonWithOverlayTransientField source = new SubPersonWithOverlayTransientField();
		source.setName("zhangsan");
		source.setAge(20);
		source.setOpenid("1");
		SubPersonWithOverlayTransientField dest = new SubPersonWithOverlayTransientField();
		BeanUtil.copyProperties(source, dest);

		Assert.assertEquals(source.getName(), dest.getName());
		Assert.assertEquals(source.getAge(), dest.getAge());
		Assert.assertEquals(source.getOpenid(), dest.getOpenid());
	}

	@Test
	public void beanToBeanTest() {
		// 修复对象无getter方法导致报错的问题
		Page page1 = new Page();
		BeanUtil.toBean(page1, Page.class);
	}

	public static class Page {
		private boolean optimizeCountSql = true;

		public boolean optimizeCountSql() {
			return optimizeCountSql;
		}

		public Page setOptimizeCountSql(boolean optimizeCountSql) {
			this.optimizeCountSql = optimizeCountSql;
			return this;
		}
	}

	@Test
	public void copyBeanToBeanTest() {
		// 测试在copyProperties方法中alias是否有效
		Food info = new Food();
		info.setBookID("0");
		info.setCode("123");
		HllFoodEntity entity = new HllFoodEntity();
		BeanUtil.copyProperties(info, entity);
		Assert.assertEquals(info.getBookID(), entity.getBookId());
		Assert.assertEquals(info.getCode(), entity.getCode2());
	}

	@Test
	public void copyBeanTest() {
		Food info = new Food();
		info.setBookID("0");
		info.setCode("123");
		Food newFood = BeanUtil.copyProperties(info, Food.class, "code");
		Assert.assertEquals(info.getBookID(), newFood.getBookID());
		Assert.assertNull(newFood.getCode());
	}

	@Data
	public static class Food {
		@Alias("bookId")
		private String bookID;
		private String code;
	}

	@Data
	public static class HllFoodEntity implements Serializable {
		private static final long serialVersionUID = 1L;

		private String bookId;
		@Alias("code")
		private String code2;
	}

	@Test
	public void setPropertiesTest() {
		Map<String, Object> resultMap = MapUtil.newHashMap();
		BeanUtil.setProperty(resultMap, "codeList[0].name", "张三");
		Assert.assertEquals("{codeList={0={name=张三}}}", resultMap.toString());
	}

	@Test
	public void beanCopyTest() {
		final Station station = new Station();
		station.setId(123456L);

		final Station station2 = new Station();

		BeanUtil.copyProperties(station, station2);
		Assert.assertEquals(new Long(123456L), station2.getId());
	}

	public static class Station extends Tree<Station, Long> {

	}

	public static class Tree<E, T> extends Entity<T> {

	}

	@Data
	public static class Entity<T> {
		private T id;
	}

	@Test
	public void toMapTest() {
		// 测试转map的时候返回key
		String name = null;
		PrivilegeIClassification a = new PrivilegeIClassification();
		a.setId("1");
		a.setName("2");
		a.setCode("3");
		 a.setCreateTime(new Date());
		a.setSortOrder(9L);

		Map<String, Object> f = BeanUtil.beanToMap(
				a,
				new LinkedHashMap<>(),
				false,
				key -> Arrays.asList("id", "name", "code", "sortOrder").contains(key) ? key : null);
		Assert.assertFalse(f.containsKey(null));
	}

	@Data
	public static class PrivilegeIClassification implements Serializable {
		private static final long serialVersionUID = 1L;

		private String id;
		private String name;
		private String code;
		private Long rowStatus;
		private Long sortOrder;
		private Date createTime;
	}
}
