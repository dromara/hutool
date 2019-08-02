package cn.hutool.core.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

import cn.hutool.core.bean.compare.ComplexCompareOption;
import cn.hutool.core.bean.compare.ModifyField;
import cn.hutool.core.bean.compare.CompareOption;
import cn.hutool.core.date.DateUtil;
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

	@Test
	public void comparePropertiesTest() {
		// 测试compareProperties
		OrderHeader order1 = new OrderHeader();
		OrderHeader order2 = new OrderHeader();
		OrderLine line1 = new OrderLine();
		OrderLine line2 = new OrderLine();
		OrderLine line3 = new OrderLine();
		OrderLine line4 = new OrderLine();
		OrderLine line5 = new OrderLine();
		order1.setAmount(new BigDecimal("100.0"));
		order1.setId(1L);
		order1.setLongId(2L);
		order1.setNum("123456");
		order1.setPer(100);
		order1.setSubmitFlag(true);
		order1.setCreatedBy(3L);
		order1.setCreatedDate(DateUtil.date());
		order2.setAmount(new BigDecimal("100.000"));
		order2.setId(2L);
		order2.setLongId(3L);
		order2.setNum("123456789");
		order2.setPer(200);
		order2.setSubmitFlag(false);
		order2.setCreatedBy(4L);
		order2.setCreatedDate(DateUtil.yesterday());
		line1.setLineId(1l);
		line1.setCreatedBy(10l);
		line1.setCreatedDate(DateUtil.tomorrow());
		line1.setItemCode(cn.hutool.core.lang.UUID.randomUUID());
		line1.setLineAmount(100.00000);
		line2.setLineId(2l);
		line2.setItemCode(cn.hutool.core.lang.UUID.randomUUID());
		line2.setLineAmount(100.00010);
		line3.setLineId(3l);
		line3.setItemCode(cn.hutool.core.lang.UUID.randomUUID());
		line3.setLineAmount(100.000);
		line4.setLineId(1l);
		line4.setItemCode(cn.hutool.core.lang.UUID.randomUUID());
		line4.setLineAmount(101.000);
		line5.setLineId(2l);
		line5.setItemCode(cn.hutool.core.lang.UUID.randomUUID());
		line5.setLineAmount(100.003);
		Set<OrderLine> orderLineSet1 = new HashSet<>();
		Set<OrderLine> orderLineSet2 = new HashSet<>();
		List<OrderLine> orderLineList1 = new ArrayList<>();
		List<OrderLine> orderLineList2 = new ArrayList<>();
		orderLineSet1.add(line1);
		orderLineSet1.add(line2);
		orderLineSet1.add(line3);
		orderLineSet2.add(line4);
		orderLineSet2.add(line5);
		orderLineList1.add(line1);
		orderLineList1.add(line2);
		orderLineList1.add(line3);
		orderLineList2.add(line4);
		orderLineList2.add(line5);
		orderLineSet1.add(line1);
		order1.setOrderLineSet(orderLineSet1);
		order1.setOrderLineList(orderLineList1);
		order2.setOrderLineSet(orderLineSet2);
		order2.setOrderLineList(orderLineList2);
		// 直接比较
		List<ModifyField> modifyFieldList1 = BeanUtil.compareProperties(order1, order2);
		// 配置比较选项
		Assert.assertTrue(modifyFieldList1.size() == 21);
		List<ModifyField> modifyFieldList2 = BeanUtil.compareProperties(
				order1,
				order2,
				new CompareOption(Audit.class, new String[]{Audit.FIELD_CREATED_BY}));
		// 配置复杂比较选项
		Assert.assertTrue(modifyFieldList2.size() == 1);
		List<ModifyField> modifyFieldList3 = BeanUtil.compareProperties(
				order1,
				order2,
				new ComplexCompareOption().setSimpleCompareOption(
						OrderHeader.class, new CompareOption(null, new String[]{OrderHeader.FIELD_ORDER_LINE_LIST}))
						.setSimpleCompareOption(OrderLine.class, new CompareOption(Audit.class, new String[]{Audit.FIELD_CREATED_BY})));
		Assert.assertTrue(modifyFieldList3.size() == 9);
		// 比较list
		List<ModifyField> modifyFieldList4 = BeanUtil.compareProperties(orderLineList1, orderLineList2);
		Assert.assertTrue(modifyFieldList4.size() == 7);
	}

	public class OrderHeader extends Audit{
		public static final String FIELD_ORDER_LINE_LIST = "orderLineList";
		private Long id;
		private String num;
		private BigDecimal amount;
		private boolean submitFlag;
		private int per;
		private long longId;
		private List<OrderLine> orderLineList;
		private Set<OrderLine> orderLineSet;

		public List<OrderLine> getOrderLineList() {
			return orderLineList;
		}

		public void setOrderLineList(List<OrderLine> orderLineList) {
			this.orderLineList = orderLineList;
		}

		public Set<OrderLine> getOrderLineSet() {
			return orderLineSet;
		}

		public void setOrderLineSet(Set<OrderLine> orderLineSet) {
			this.orderLineSet = orderLineSet;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public boolean isSubmitFlag() {
			return submitFlag;
		}

		public void setSubmitFlag(boolean submitFlag) {
			this.submitFlag = submitFlag;
		}

		public int getPer() {
			return per;
		}

		public void setPer(int per) {
			this.per = per;
		}

		public long getLongId() {
			return longId;
		}

		public void setLongId(long longId) {
			this.longId = longId;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("{");
			sb.append("\"id\":")
					.append(id);
			sb.append(",\"num\":\"")
					.append(num).append('\"');
			sb.append(",\"amount\":")
					.append(amount);
			sb.append(",\"submitFlag\":")
					.append(submitFlag);
			sb.append(",\"per\":")
					.append(per);
			sb.append(",\"longId\":")
					.append(longId);
			sb.append(",\"orderLineList\":")
					.append(orderLineList);
			sb.append(",\"orderLineSet\":")
					.append(orderLineSet);
			sb.append(",\"createdDate\":\"")
					.append(getCreatedDate()).append('\"');
			sb.append(",\"createdBy\":")
					.append(getCreatedBy());
			sb.append('}');
			return sb.toString();
		}
	}

	public class OrderLine extends Audit {
		private Long lineId;
		private cn.hutool.core.lang.UUID itemCode;
		private double lineAmount;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			OrderLine orderLine = (OrderLine) o;
			return Objects.equals(lineId, orderLine.lineId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(lineId);
		}

		public Long getLineId() {
			return lineId;
		}

		public void setLineId(Long lineId) {
			this.lineId = lineId;
		}

		public cn.hutool.core.lang.UUID getItemCode() {
			return itemCode;
		}

		public void setItemCode(cn.hutool.core.lang.UUID itemCode) {
			this.itemCode = itemCode;
		}

		public double getLineAmount() {
			return lineAmount;
		}

		public void setLineAmount(double lineAmount) {
			this.lineAmount = lineAmount;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("{");
			sb.append("\"lineId\":")
					.append(lineId);
			sb.append(",\"itemCode\":")
					.append(itemCode);
			sb.append(",\"lineAmount\":")
					.append(lineAmount);
			sb.append(",\"createdDate\":\"")
					.append(getCreatedDate()).append('\"');
			sb.append(",\"createdBy\":")
					.append(getCreatedBy());
			sb.append('}');
			return sb.toString();
		}
	}

	public class Audit{

		public static final String FIELD_CREATED_BY = "createdBy";
		private Date createdDate;
		private Long createdBy;

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public Long getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Long createdBy) {
			this.createdBy = createdBy;
		}
	}
}
