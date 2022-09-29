package cn.hutool.poi.excel;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.func.SerBiConsumer;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("alias.xlsx"));

		//读取单个单元格内容测试
		final Object value = reader.readCellValue(1, 2);
		Assert.assertEquals("仓库", value);

		final Map<String, String> headerAlias = MapUtil.newHashMap();
		headerAlias.put("用户姓名", "userName");
		headerAlias.put("库房", "storageName");
		headerAlias.put("盘点权限", "checkPerm");
		headerAlias.put("领料审批权限", "allotAuditPerm");
		reader.setHeaderAlias(headerAlias);

		// 读取list时默认首个非空行为标题
		final List<List<Object>> read = reader.read();
		Assert.assertEquals("userName", read.get(0).get(0));
		Assert.assertEquals("storageName", read.get(0).get(1));
		Assert.assertEquals("checkPerm", read.get(0).get(2));
		Assert.assertEquals("allotAuditPerm", read.get(0).get(3));

		final List<Map<String, Object>> readAll = reader.readAll();
		for (final Map<String, Object> map : readAll) {
			Assert.assertTrue(map.containsKey("userName"));
			Assert.assertTrue(map.containsKey("storageName"));
			Assert.assertTrue(map.containsKey("checkPerm"));
			Assert.assertTrue(map.containsKey("allotAuditPerm"));
		}
	}

	@Test
	public void excelReadTestOfEmptyLine() {
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("priceIndex.xls"));
		final List<Map<String, Object>> readAll = reader.readAll();

		Assert.assertEquals(4, readAll.size());
	}

	@Test
	public void excelReadTest() {
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		final List<List<Object>> readAll = reader.read();

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
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		Assert.assertNotNull(reader.readAsText(false));
	}

	@Test
	public void excel03ReadTest() {
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"));
		final List<List<Object>> readAll = reader.read();

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
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xls"), "校园入学");
		final List<List<Object>> readAll = reader.read();

		// 标题
		Assert.assertEquals("班级", readAll.get(0).get(0));
		Assert.assertEquals("年级", readAll.get(0).get(1));
		Assert.assertEquals("学校", readAll.get(0).get(2));
		Assert.assertEquals("入学时间", readAll.get(0).get(3));
		Assert.assertEquals("更新时间", readAll.get(0).get(4));
	}

	@Test
	public void excelReadToMapListTest() {
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		final List<Map<String, Object>> readAll = reader.readAll();

		Assert.assertEquals("张三", readAll.get(0).get("姓名"));
		Assert.assertEquals("男", readAll.get(0).get("性别"));
		Assert.assertEquals(11L, readAll.get(0).get("年龄"));
	}

	@Test
	public void excelReadToBeanListTest() {
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		reader.addHeaderAlias("姓名", "name");
		reader.addHeaderAlias("年龄", "age");
		reader.addHeaderAlias("性别", "gender");
		reader.addHeaderAlias("鞋码", "shoeSize");

		final List<Person> all = reader.readAll(Person.class);
		Assert.assertEquals("张三", all.get(0).getName());
		Assert.assertEquals("男", all.get(0).getGender());
		Assert.assertEquals(Integer.valueOf(11), all.get(0).getAge());
		Assert.assertEquals(new BigDecimal("41.5"), all.get(0).getShoeSize());
	}

	@Test
	@Ignore
	public void excelReadToBeanListTest2() {
		final ExcelReader reader = ExcelUtil.getReader("f:/test/toBean.xlsx");
		reader.addHeaderAlias("姓名", "name");
		reader.addHeaderAlias("年龄", "age");
		reader.addHeaderAlias("性别", "gender");

		final List<Person> all = reader.read(0, 2, Person.class);
		for (final Person person : all) {
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
	@Ignore
	public void readDoubleTest() {
		final ExcelReader reader = ExcelUtil.getReader("f:/test/doubleTest.xls");
		final List<List<Object>> read = reader.read();
		for (final List<Object> list : read) {
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

	@Test
	@Ignore
	public void readCellsTest() {
		final ExcelReader reader = ExcelUtil.getReader("merge_test.xlsx");
		reader.read((cell, value)-> Console.log("{}, {} {}", cell.getRowIndex(), cell.getColumnIndex(), value));
	}

	@Test
	@Ignore
	public void readTest() {
		// 测试合并单元格是否可以正常读到第一个单元格的值
		final ExcelReader reader = ExcelUtil.getReader("d:/test/人员体检信息表.xlsx");
		final List<List<Object>> read = reader.read();
		for (final List<Object> list : read) {
			Console.log(list);
		}
	}

	@Test
	public void nullValueEditTest(){
		final ExcelReader reader = ExcelUtil.getReader("null_cell_test.xlsx");
		reader.setCellEditor((cell, value)-> ObjUtil.defaultIfNull(value, "#"));
		final List<List<Object>> read = reader.read();

		// 对于任意一个单元格有值的情况下，之前的单元格值按照null处理
		Assert.assertEquals(1, read.get(1).size());
		Assert.assertEquals(2, read.get(2).size());
		Assert.assertEquals(3, read.get(3).size());

		Assert.assertEquals("#", read.get(2).get(0));
		Assert.assertEquals("#", read.get(3).get(0));
		Assert.assertEquals("#", read.get(3).get(1));
	}

	@Test
	@Ignore
	public void readEmptyTest(){
		final ExcelReader reader = ExcelUtil.getReader("d:/test/issue.xlsx");
		final List<Map<String, Object>> maps = reader.readAll();
		Console.log(maps);
	}

	@Test
	@Ignore
	public void readNullRowTest(){
		final ExcelReader reader = ExcelUtil.getReader("d:/test/1.-.xls");
		reader.read((SerBiConsumer<Cell, Object>) Console::log);
	}

	@Test
	public void readColumnTest(){
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
		final List<Object> objects = reader.readColumn(0, 1);

		Assert.assertEquals(3, objects.size());
		Assert.assertEquals("张三", objects.get(0));
		Assert.assertEquals("李四", objects.get(1));
		Assert.assertEquals("", objects.get(2));
	}

	@Test
	public void readColumnNPETest() {
		// https://github.com/dromara/hutool/pull/2234
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("read_row_npe.xlsx"));
		reader.readColumn(0, 1);
	}

	@Test
	public void readIssueTest() {
		//https://gitee.com/dromara/hutool/issues/I5OSFC
		final ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("read.xlsx"));
		final List<Map<String, Object>> read = reader.read(1,2,2);
		for (Map<String, Object> map : read) {
			Console.log(map);
		}
		//超出lastIndex 抛出相应提示：startRowIndex row index 4 is greater than last row index 2.
		//而非:Illegal Capacity: -1
		try {
			final List<Map<String, Object>> readGreaterIndex = reader.read(1,4,4);
		} catch (Exception e) {
			Console.log(e.toString());
		}
	}
}
