package cn.hutool.poi.excel;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.cell.CellHandler;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
		Assertions.assertEquals("仓库", value);

		Map<String, String> headerAlias = MapUtil.newHashMap();
		headerAlias.put("用户姓名", "userName");
		headerAlias.put("库房", "storageName");
		headerAlias.put("盘点权限", "checkPerm");
		headerAlias.put("领料审批权限", "allotAuditPerm");
		reader.setHeaderAlias(headerAlias);

		// 读取list时默认首个非空行为标题
		List<List<Object>> read = reader.read();
		Assertions.assertEquals("userName", read.get(0).get(0));
		Assertions.assertEquals("storageName", read.get(0).get(1));
		Assertions.assertEquals("checkPerm", read.get(0).get(2));
		Assertions.assertEquals("allotAuditPerm", read.get(0).get(3));

		List<Map<String, Object>> readAll = reader.readAll();
		for (Map<String, Object> map : readAll) {
			Assertions.assertTrue(map.containsKey("userName"));
			Assertions.assertTrue(map.containsKey("storageName"));
			Assertions.assertTrue(map.containsKey("checkPerm"));
			Assertions.assertTrue(map.containsKey("allotAuditPerm"));
		}
	}

	@Test
	public void excelReadTestOfEmptyLine() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("priceIndex.xls"));
		List<Map<String, Object>> readAll = reader.readAll();

		Assertions.assertEquals(4, readAll.size());
	}

	@Test
	public void excelReadTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		List<List<Object>> readAll = reader.read();

		// 标题
		Assertions.assertEquals("姓名", readAll.get(0).get(0));
		Assertions.assertEquals("性别", readAll.get(0).get(1));
		Assertions.assertEquals("年龄", readAll.get(0).get(2));
		Assertions.assertEquals("鞋码", readAll.get(0).get(3));

		// 第一行
		Assertions.assertEquals("张三", readAll.get(1).get(0));
		Assertions.assertEquals("男", readAll.get(1).get(1));
		Assertions.assertEquals(11L, readAll.get(1).get(2));
		Assertions.assertEquals(41.5D, readAll.get(1).get(3));
	}

	@Test
	public void excelReadAsTextTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		Assertions.assertNotNull(reader.readAsText(false));
	}

	@Test
	public void excel03ReadTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"));
		List<List<Object>> readAll = reader.read();

		// for (List<Object> list : readAll) {
		// Console.log(list);
		// }

		// 标题
		Assertions.assertEquals("姓名", readAll.get(0).get(0));
		Assertions.assertEquals("性别", readAll.get(0).get(1));
		Assertions.assertEquals("年龄", readAll.get(0).get(2));
		Assertions.assertEquals("分数", readAll.get(0).get(3));

		// 第一行
		Assertions.assertEquals("张三", readAll.get(1).get(0));
		Assertions.assertEquals("男", readAll.get(1).get(1));
		Assertions.assertEquals(11L, readAll.get(1).get(2));
		Assertions.assertEquals(33.2D, readAll.get(1).get(3));
	}

	@Test
	public void excel03ReadTest2() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"), "校园入学");
		List<List<Object>> readAll = reader.read();

		// 标题
		Assertions.assertEquals("班级", readAll.get(0).get(0));
		Assertions.assertEquals("年级", readAll.get(0).get(1));
		Assertions.assertEquals("学校", readAll.get(0).get(2));
		Assertions.assertEquals("入学时间", readAll.get(0).get(3));
		Assertions.assertEquals("更新时间", readAll.get(0).get(4));
	}

	@Test
	public void excelReadToMapListTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		List<Map<String, Object>> readAll = reader.readAll();

		Assertions.assertEquals("张三", readAll.get(0).get("姓名"));
		Assertions.assertEquals("男", readAll.get(0).get("性别"));
		Assertions.assertEquals(11L, readAll.get(0).get("年龄"));
	}

	@Test
	public void excelReadToBeanListTest() {
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		reader.addHeaderAlias("姓名", "name");
		reader.addHeaderAlias("年龄", "age");
		reader.addHeaderAlias("性别", "gender");
		reader.addHeaderAlias("鞋码", "shoeSize");

		List<Person> all = reader.readAll(Person.class);
		Assertions.assertEquals("张三", all.get(0).getName());
		Assertions.assertEquals("男", all.get(0).getGender());
		Assertions.assertEquals(Integer.valueOf(11), all.get(0).getAge());
		Assertions.assertEquals(new BigDecimal("41.5"), all.get(0).getShoeSize());
	}

	@Test
	@Disabled
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

	@Data
	public static class Person {
		private String name;
		private String gender;
		private Integer age;
		private BigDecimal shoeSize;
	}

	@Test
	@Disabled
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
		Assertions.assertEquals(11L, read.get(1).get(2));
		Assertions.assertEquals(11L, read.get(2).get(2));
	}

	@Test
	@Disabled
	public void readCellsTest() {
		final ExcelReader reader = ExcelUtil.getReader("merge_test.xlsx");
		reader.read((cell, value)-> Console.log("{}, {} {}", cell.getRowIndex(), cell.getColumnIndex(), value));
	}

	@Test
	@Disabled
	public void readTest() {
		// 测试合并单元格是否可以正常读到第一个单元格的值
		final ExcelReader reader = ExcelUtil.getReader("d:/test/人员体检信息表.xlsx");
		final List<List<Object>> read = reader.read();
		for (List<Object> list : read) {
			Console.log(list);
		}
	}

	@Test
	public void nullValueEditTest(){
		final ExcelReader reader = ExcelUtil.getReader("null_cell_test.xlsx");
		reader.setCellEditor((cell, value)-> ObjectUtil.defaultIfNull(value, "#"));
		final List<List<Object>> read = reader.read();

		// 对于任意一个单元格有值的情况下，之前的单元格值按照null处理
		Assertions.assertEquals(1, read.get(1).size());
		Assertions.assertEquals(2, read.get(2).size());
		Assertions.assertEquals(3, read.get(3).size());

		Assertions.assertEquals("#", read.get(2).get(0));
		Assertions.assertEquals("#", read.get(3).get(0));
		Assertions.assertEquals("#", read.get(3).get(1));
	}

	@Test
	@Disabled
	public void readEmptyTest(){
		final ExcelReader reader = ExcelUtil.getReader("d:/test/issue.xlsx");
		final List<Map<String, Object>> maps = reader.readAll();
		Console.log(maps);
	}

	@Test
	@Disabled
	public void readNullRowTest(){
		final ExcelReader reader = ExcelUtil.getReader("d:/test/1.-.xls");
		reader.read((CellHandler) Console::log);
	}

	@Test
	public void readColumnTest(){
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		final List<Object> objects = reader.readColumn(0, 1);

		Assertions.assertEquals(3, objects.size());
		Assertions.assertEquals("张三", objects.get(0));
		Assertions.assertEquals("李四", objects.get(1));
		Assertions.assertEquals("", objects.get(2));
	}
}
