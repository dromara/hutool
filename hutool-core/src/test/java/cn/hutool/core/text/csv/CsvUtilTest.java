package cn.hutool.core.text.csv;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvUtilTest {

	@Test
	public void readTest() {
		CsvReader reader = CsvUtil.getReader();
		//从文件中读取CSV数据
		CsvData data = reader.read(FileUtil.file("test.csv"));
		List<CsvRow> rows = data.getRows();
		final CsvRow row0 = rows.get(0);
		Assert.assertEquals("sss,sss", row0.get(0));
		Assert.assertEquals("姓名", row0.get(1));
		Assert.assertEquals("性别", row0.get(2));
		Assert.assertEquals("关注\"对象\"", row0.get(3));
		Assert.assertEquals("年龄", row0.get(4));
		Assert.assertEquals("", row0.get(5));
		Assert.assertEquals("\"", row0.get(6));
	}

	@Test
	public void readTest2() {
		CsvReader reader = CsvUtil.getReader();
		reader.read(FileUtil.getUtf8Reader("test.csv"), (csvRow)-> {
			// 只有一行，所以直接判断
			Assert.assertEquals("sss,sss", csvRow.get(0));
			Assert.assertEquals("姓名", csvRow.get(1));
			Assert.assertEquals("性别", csvRow.get(2));
			Assert.assertEquals("关注\"对象\"", csvRow.get(3));
			Assert.assertEquals("年龄", csvRow.get(4));
			Assert.assertEquals("", csvRow.get(5));
			Assert.assertEquals("\"", csvRow.get(6));
		});
	}

	@Test
	@Ignore
	public void readTest3() {
		CsvReader reader = CsvUtil.getReader();
		String path = FileUtil.isWindows() ? "d:/test/test.csv" : "~/test/test.csv";
		reader.read(FileUtil.getUtf8Reader(path), Console::log);
	}

	@Test
	@Ignore
	public void writeTest() {
		String path = FileUtil.isWindows() ? "d:/test/testWrite.csv" : "~/test/testWrite.csv";
		CsvWriter writer = CsvUtil.getWriter(path, CharsetUtil.CHARSET_UTF_8);
		writer.write(
				new String[] {"a1", "b1", "c1", "123345346456745756756785656"},
				new String[] {"a2", "b2", "c2"},
				new String[] {"a3", "b3", "c3"}
		);
	}

	@Test
	@Ignore
	public void writeBeansTest() {

		@Data
		class Student {
			Integer id;
			String name;
			Integer age;
		}

		String path = FileUtil.isWindows() ? "d:/test/testWriteBeans.csv" : "~/test/testWriteBeans.csv";
		CsvWriter writer = CsvUtil.getWriter(path, CharsetUtil.CHARSET_UTF_8);
		List<Student> students = new ArrayList<>();
		Student student1 = new Student();
		student1.setId(1);
		student1.setName("张三");
		student1.setAge(18);

		Student student2 = new Student();
		student2.setId(2);
		student2.setName("李四");
		student2.setAge(22);

		Student student3 = new Student();
		student3.setId(3);
		student3.setName("王五");
		student3.setAge(31);

		students.add(student1);
		students.add(student2);
		students.add(student3);
		writer.writeBeans(students);
		writer.close();
	}

	@Test
	@Ignore
	public void readLfTest(){
		final CsvReader reader = CsvUtil.getReader();
		String path = FileUtil.isWindows() ? "d:/test/rw_test.csv" : "~/test/rw_test.csv";
		final CsvData read = reader.read(FileUtil.file(path));
		for (CsvRow row : read) {
			Console.log(row);
		}
	}

	@Test
	@Ignore
	public void writeWrapTest(){
		List<List<Object>> resultList=new ArrayList<>();
		List<Object> list =new ArrayList<>();
		list.add("\"name\"");
		list.add("\"code\"");
		resultList.add(list);

		list =new ArrayList<>();
		list.add("\"wang\"");
		list.add(1);
		resultList.add(list);

		String path = FileUtil.isWindows() ? "d:/test/csvWrapTest.csv" : "~/test/csvWrapTest.csv";
		final CsvWriter writer = CsvUtil.getWriter(path, CharsetUtil.CHARSET_UTF_8);
		writer.write(resultList);
	}

	@Test
	@Ignore
	public void writeDataTest(){
		@Data
		@AllArgsConstructor
		class User {
			Integer userId;
			String username;
			String mobile;
		}

		List<String> header = ListUtil.of("用户id", "用户名", "手机号");
		List<CsvRow> row = new ArrayList<>();

		List<User> datas = new ArrayList<>();
		datas.add(new User(1, "张三", "18800001111"));
		datas.add(new User(2, "李四", "18800001112"));
		datas.add(new User(3, "王五", "18800001113"));
		datas.add(new User(4, "赵六", "18800001114"));

		//可以为null
		//Map<String, Integer> headMap = null;
		Map<String, Integer> headMap = new HashMap<>();
		headMap.put("userId", 0);
		headMap.put("username", 1);
		headMap.put("mobile", 2);

		for (User user : datas) {
			// row.size() + 1, 表示从第2行开始，第一行是标题栏
			row.add(new CsvRow(row.size() + 1, headMap,
					BeanUtil.beanToMap(user).values().stream().map(Object::toString).collect(Collectors.toList())));
		}

		CsvData csvData = new CsvData(header, row);
		String path = FileUtil.isWindows() ? "d:/test/csvWriteDataTest.csv" : "~/test/csvWriteDataTest.csv";
		final CsvWriter writer = CsvUtil.getWriter(path, CharsetUtil.CHARSET_UTF_8);
		writer.write(csvData);
	}
}
