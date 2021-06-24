package cn.hutool.core.text.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
		reader.read(FileUtil.getUtf8Reader("d:/test/test.csv"), Console::log);
	}

	@Test
	@Ignore
	public void writeTest() {
		CsvWriter writer = CsvUtil.getWriter("d:/test/testWrite.csv", CharsetUtil.CHARSET_UTF_8);
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

		CsvWriter writer = CsvUtil.getWriter("d:/test/testWriteBeans.csv", CharsetUtil.CHARSET_UTF_8);
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
		final CsvData read = reader.read(FileUtil.file("d:/test/rw_test.csv"));
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

		final CsvWriter writer = CsvUtil.getWriter("d:/test/csvWrapTest.csv", CharsetUtil.CHARSET_UTF_8);
		writer.write(resultList);
	}
}
