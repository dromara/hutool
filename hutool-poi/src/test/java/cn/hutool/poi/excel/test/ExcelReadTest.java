package cn.hutool.poi.excel.test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Excel读取单元测试
 *
 * @author Looly
 */
public class ExcelReadTest {

	@Test
	public void aliasTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("alias.xlsx"));

		//读取单个单元格内容测试
		Object value = reader.readCellValue(1, 2);
		Assert.assertEquals("仓库", value);

		Map<String, String> headerAlias = MapUtil.newHashMap();
		headerAlias.put("用户姓名", "userName");
		headerAlias.put("库房", "storageName");
		headerAlias.put("盘点权限", "checkPerm");
		headerAlias.put("领料审批权限", "allotAuditPerm");
		reader.setHeaderAlias(headerAlias);

		// 读取list时默认首个非空行为标题
		List<List<Object>> read = reader.read();
		Assert.assertEquals("userName", read.get(0).get(0));
		Assert.assertEquals("storageName", read.get(0).get(1));
		Assert.assertEquals("checkPerm", read.get(0).get(2));
		Assert.assertEquals("allotAuditPerm", read.get(0).get(3));

		List<Map<String, Object>> readAll = reader.readAll();
		for (Map<String, Object> map : readAll) {
			Assert.assertTrue(map.containsKey("userName"));
			Assert.assertTrue(map.containsKey("storageName"));
			Assert.assertTrue(map.containsKey("checkPerm"));
			Assert.assertTrue(map.containsKey("allotAuditPerm"));
		}
	}

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
	public void excelReadAsTextTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		Assert.assertNotNull(reader.readAsText(false));
	}

	@Test
	public void excel03ReadTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"));
		List<List<Object>> readAll = reader.read();

		// for (List<Object> list : readAll) {
		// Console.log(list);
		// }

		// 标题
		Assert.assertEquals("姓名", readAll.get(0).get(0));
		Assert.assertEquals("性别", readAll.get(0).get(1));
		Assert.assertEquals("年龄", readAll.get(0).get(2));
		Assert.assertEquals("分数", readAll.get(0).get(3));

		// 第一行
		Assert.assertEquals("张三", readAll.get(1).get(0));
		Assert.assertEquals("男", readAll.get(1).get(1));
		Assert.assertEquals(11L, readAll.get(1).get(2));
		Assert.assertEquals(33.2D, readAll.get(1).get(3));
	}

	@Test
	public void excel03ReadTest2() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"), "校园入学");
		List<List<Object>> readAll = reader.read();

		// 标题
		Assert.assertEquals("班级", readAll.get(0).get(0));
		Assert.assertEquals("年级", readAll.get(0).get(1));
		Assert.assertEquals("学校", readAll.get(0).get(2));
		Assert.assertEquals("入学时间", readAll.get(0).get(3));
		Assert.assertEquals("更新时间", readAll.get(0).get(4));
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

	@Test
	@Ignore
	public void excelReadToBeanListTest2() {
		ExcelReader reader = ExcelUtil.getReader("f:/test/toBean.xlsx");
		reader.addHeaderAlias("姓名", "name");
		reader.addHeaderAlias("年龄", "age");
		reader.addHeaderAlias("性别", "gender");

		List<Person> all = reader.read(0, 2, Person.class);
		for (Person person : all) {
			Console.log(person);
		}
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

	@Test
	@Ignore
	public void readDoubleTest() {
		ExcelReader reader = ExcelUtil.getReader("f:/test/doubleTest.xls");
		final List<List<Object>> read = reader.read();
		for (List<Object> list : read) {
			Console.log(list.get(8));
		}
	}

	@Test
	public void mergeReadTest() {
		final ExcelReader reader = ExcelUtil.getReader("merge_test.xlsx");
		final List<List<Object>> read = reader.read();
		// 验证合并单元格在两行中都可以取到值
		Assert.assertEquals(11L, read.get(1).get(2));
		Assert.assertEquals(11L, read.get(2).get(2));
	}

}
