package cn.hutool.core.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;

/**
 * Bean工具单元测试
 *
 * @author Looly
 *
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
		HashMap<String, Object> map = CollectionUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);
		map.put("openId", "DFDFSDFWERWER");
		SubPerson person = BeanUtil.fillBeanWithMapIgnoreCase(map, new SubPerson(), false);
		Assert.assertEquals(person.getName(), "Joe");
		Assert.assertEquals(person.getAge(), 12);
		Assert.assertEquals(person.getOpenid(), "DFDFSDFWERWER");
	}

	@Test
	public void mapToBeanIgnoreCaseTest() {
		HashMap<String, Object> map = CollectionUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);

		Person person = BeanUtil.mapToBeanIgnoreCase(map, Person.class, false);
		Assert.assertEquals("Joe", person.getName());
		Assert.assertEquals(12, person.getAge());
	}

	@Test
	public void mapToBeanTest() {
		HashMap<String, Object> map = CollectionUtil.newHashMap();
		map.put("a_name", "Joe");
		map.put("b_age", 12);

		// 别名
		HashMap<String, String> mapping = CollUtil.newHashMap();
		mapping.put("a_name", "name");
		mapping.put("b_age", "age");

		Person person = BeanUtil.mapToBean(map, Person.class, CopyOptions.create().setFieldMapping(mapping));
		Assert.assertEquals("Joe", person.getName());
		Assert.assertEquals(12, person.getAge());
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
	public void copyPropertiesHasBooleanTest() {
		SubPerson p1 = new SubPerson();
		p1.setSlow(true);

		// 测试boolean参数值isXXX形式
		SubPerson p2 = new SubPerson();
		BeanUtil.copyProperties(p1, p2);
		Assert.assertTrue(p2.isSlow());

		// 测试boolean参数值非isXXX形式
		SubPerson2 p3 = new SubPerson2();
		BeanUtil.copyProperties(p1, p3);
		Assert.assertTrue(p3.isSlow());
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
		Assert.assertTrue((Boolean) map.get("isSlow"));
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
	public static class SubPerson extends Person {

		public static final String SUBNAME = "TEST";

		private UUID id;
		private String subName;
		private Boolean isSlow;

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getSubName() {
			return subName;
		}

		public void setSubName(String subName) {
			this.subName = subName;
		}

		public Boolean isSlow() {
			return isSlow;
		}

		public void setSlow(Boolean isSlow) {
			this.isSlow = isSlow;
		}
	}

	public static class SubPerson2 extends Person {
		private String subName;
		// boolean参数值非isXXX形式
		private Boolean slow;

		public String getSubName() {
			return subName;
		}

		public void setSubName(String subName) {
			this.subName = subName;
		}

		public Boolean isSlow() {
			return slow;
		}

		public void setSlow(Boolean isSlow) {
			this.slow = isSlow;
		}
	}

	public static class Person {
		private String name;
		private int age;
		private String openid;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}
	}
}
