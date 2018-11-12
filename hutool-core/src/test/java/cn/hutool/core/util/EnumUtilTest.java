package cn.hutool.core.util;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.collection.CollUtil;

/**
 * EnumUtil单元测试
 * 
 * @author looly
 *
 */
public class EnumUtilTest {

	@Test
	public void getNamesTest() {
		List<String> names = EnumUtil.getNames(TestEnum.class);
		Assert.assertEquals(CollUtil.newArrayList("TEST1", "TEST2", "TEST3"), names);
	}
	
	@Test
	public void getFieldValuesTest() {
		List<Object> types = EnumUtil.getFieldValues(TestEnum.class, "type");
		Assert.assertEquals(CollUtil.newArrayList("type1", "type2", "type3"), types);
	}
	
	@Test
	public void getFieldNamesTest() {
		List<String> names = EnumUtil.getFieldNames(TestEnum.class);
		Assert.assertEquals(CollUtil.newArrayList("type", "name"), names);
	}
	
	@Test
	public void likeValueOfTest() {
		TestEnum value = EnumUtil.likeValueOf(TestEnum.class, "type2");
		Assert.assertEquals(TestEnum.TEST2, value);
	}
	
	@Test
	public void getEnumMapTest() {
		Map<String,TestEnum> enumMap = EnumUtil.getEnumMap(TestEnum.class);
		Assert.assertEquals(TestEnum.TEST1, enumMap.get("TEST1"));
	}
	
	@Test
	public void getNameFieldMapTest() {
		Map<String, Object> enumMap = EnumUtil.getNameFieldMap(TestEnum.class, "type");
		Assert.assertEquals("type1", enumMap.get("TEST1"));
	}
	
	public static enum TestEnum{
		TEST1("type1"), TEST2("type2"), TEST3("type3");
		
		private TestEnum(String type) {
			this.type = type;
		}
		
		private String type;
		
		public String getType() {
			return this.type;
		}
	}
}
