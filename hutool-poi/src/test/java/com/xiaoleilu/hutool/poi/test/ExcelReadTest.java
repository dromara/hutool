package com.xiaoleilu.hutool.poi.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.io.resource.ResourceUtil;
import com.xiaoleilu.hutool.poi.excel.ExcelReader;
import com.xiaoleilu.hutool.poi.excel.ExcelUtil;

/**
 * Excel读取单元测试
 * 
 * @author Looly
 *
 */
public class ExcelReadTest {

	@Test
	public void excelReadTestOfEmptyLine() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("priceIndex.xls"));
		List<Map<String, Object>> readAll = reader.readAll();

		Assert.assertEquals(4, readAll.size());
	}

	@Test
	public void excelReadTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		List<List<Object>> readAll = reader.read();

		// 标题
		Assert.assertEquals("姓名", readAll.get(0).get(0));
		Assert.assertEquals("性别", readAll.get(0).get(1));
		Assert.assertEquals("年龄", readAll.get(0).get(2));
		Assert.assertEquals("鞋码", readAll.get(0).get(3));

		// 第一行
		Assert.assertEquals("张三", readAll.get(1).get(0));
		Assert.assertEquals("男", readAll.get(1).get(1));
		Assert.assertEquals(11L, readAll.get(1).get(2));
		Assert.assertEquals(41.5D, readAll.get(1).get(3));
	}

	@Test
	public void excel03ReadTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"));
		List<List<Object>> readAll = reader.read();

		// 标题
		Assert.assertEquals("姓名", readAll.get(0).get(0));
		Assert.assertEquals("性别", readAll.get(0).get(1));
		Assert.assertEquals("年龄", readAll.get(0).get(2));

		// 第一行
		Assert.assertEquals("张三", readAll.get(1).get(0));
		Assert.assertEquals("男", readAll.get(1).get(1));
		Assert.assertEquals(11L, readAll.get(1).get(2));
	}

	@Test
	public void excelReadToMapListTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		List<Map<String, Object>> readAll = reader.readAll();

		Assert.assertEquals("张三", readAll.get(0).get("姓名"));
		Assert.assertEquals("男", readAll.get(0).get("性别"));
		Assert.assertEquals(11L, readAll.get(0).get("年龄"));
	}

	@Test
	public void excelReadToBeanListTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		reader.addHeaderAlias("姓名", "name");
		reader.addHeaderAlias("年龄", "age");
		reader.addHeaderAlias("性别", "gender");

		List<Person> all = reader.readAll(Person.class);
		Assert.assertEquals("张三", all.get(0).getName());
		Assert.assertEquals("男", all.get(0).getGender());
		Assert.assertEquals(Integer.valueOf(11), all.get(0).getAge());
	}

	public static class Person {
		private String name;
		private String gender;
		private Integer age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", gender=" + gender + ", age=" + age + "]";
		}
	}
}
