package org.dromara.hutool.csv;

import org.dromara.hutool.annotation.Alias;
import org.dromara.hutool.collection.CollUtil;
import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.io.resource.ResourceUtil;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.util.CharsetUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@SuppressWarnings("resource")
public class CsvReaderTest {

	@Test
	public void readTest() {
		final CsvReader reader = new CsvReader();
		final CsvData data = reader.read(ResourceUtil.getReader("test.csv", CharsetUtil.UTF_8));
		Assertions.assertEquals("sss,sss", data.getRow(0).get(0));
		Assertions.assertEquals(1, data.getRow(0).getOriginalLineNumber());
		Assertions.assertEquals("性别", data.getRow(0).get(2));
		Assertions.assertEquals("关注\"对象\"", data.getRow(0).get(3));
	}

	@Test
	public void readMapListTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"));

		Assertions.assertEquals("张三", result.get(0).get("姓名"));
		Assertions.assertEquals("男", result.get(0).get("gender"));
		Assertions.assertEquals("无", result.get(0).get("focus"));
		Assertions.assertEquals("33", result.get(0).get("age"));

		Assertions.assertEquals("李四", result.get(1).get("姓名"));
		Assertions.assertEquals("男", result.get(1).get("gender"));
		Assertions.assertEquals("好对象", result.get(1).get("focus"));
		Assertions.assertEquals("23", result.get(1).get("age"));

		Assertions.assertEquals("王妹妹", result.get(2).get("姓名"));
		Assertions.assertEquals("女", result.get(2).get("gender"));
		Assertions.assertEquals("特别关注", result.get(2).get("focus"));
		Assertions.assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readAliasMapListTest() {
		final CsvReadConfig csvReadConfig = CsvReadConfig.defaultConfig();
		csvReadConfig.addHeaderAlias("姓名", "name");

		final CsvReader reader = CsvUtil.getReader(csvReadConfig);
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"));

		Assertions.assertEquals("张三", result.get(0).get("name"));
		Assertions.assertEquals("男", result.get(0).get("gender"));
		Assertions.assertEquals("无", result.get(0).get("focus"));
		Assertions.assertEquals("33", result.get(0).get("age"));

		Assertions.assertEquals("李四", result.get(1).get("name"));
		Assertions.assertEquals("男", result.get(1).get("gender"));
		Assertions.assertEquals("好对象", result.get(1).get("focus"));
		Assertions.assertEquals("23", result.get(1).get("age"));

		Assertions.assertEquals("王妹妹", result.get(2).get("name"));
		Assertions.assertEquals("女", result.get(2).get("gender"));
		Assertions.assertEquals("特别关注", result.get(2).get("focus"));
		Assertions.assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readBeanListTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<TestBean> result = reader.read(
				ResourceUtil.getUtf8Reader("test_bean.csv"), TestBean.class);

		Assertions.assertEquals("张三", result.get(0).getName());
		Assertions.assertEquals("男", result.get(0).getGender());
		Assertions.assertEquals("无", result.get(0).getFocus());
		Assertions.assertEquals(Integer.valueOf(33), result.get(0).getAge());

		Assertions.assertEquals("李四", result.get(1).getName());
		Assertions.assertEquals("男", result.get(1).getGender());
		Assertions.assertEquals("好对象", result.get(1).getFocus());
		Assertions.assertEquals(Integer.valueOf(23), result.get(1).getAge());

		Assertions.assertEquals("王妹妹", result.get(2).getName());
		Assertions.assertEquals("女", result.get(2).getGender());
		Assertions.assertEquals("特别关注", result.get(2).getFocus());
		Assertions.assertEquals(Integer.valueOf(22), result.get(2).getAge());
	}

	@Data
	private static class TestBean {
		@Alias("姓名")
		private String name;
		private String gender;
		private String focus;
		private Integer age;
	}

	@Test
	@Disabled
	public void readTest2() {
		final CsvReader reader = CsvUtil.getReader();
		final CsvData read = reader.read(FileUtil.file("d:/test/test.csv"));
		for (final CsvRow strings : read) {
			Console.log(strings);
		}
	}

	@Test
	@Disabled
	public void readTest3() {
		final CsvReadConfig csvReadConfig = CsvReadConfig.defaultConfig();
		csvReadConfig.setContainsHeader(true);
		final CsvReader reader = CsvUtil.getReader(csvReadConfig);
		final CsvData read = reader.read(FileUtil.file("d:/test/ceshi.csv"));
		for (final CsvRow row : read) {
			Console.log(row.getByName("案件ID"));
		}
	}

	@Test
	public void lineNoTest() {
		final CsvReader reader = new CsvReader();
		final CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.UTF_8));
		Assertions.assertEquals(1, data.getRow(0).getOriginalLineNumber());
		Assertions.assertEquals("a,b,c,d", CollUtil.join(data.getRow(0), ","));

		Assertions.assertEquals(4, data.getRow(2).getOriginalLineNumber());
		Assertions.assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(2), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		Assertions.assertEquals(6, data.getRow(3).getOriginalLineNumber());
		Assertions.assertEquals("a,s,d,f", CollUtil.join(data.getRow(3), ","));
	}

	@Test
	public void lineLimitTest() {
		// 从原始第2行开始读取
		final CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2));
		final CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.UTF_8));

		Assertions.assertEquals(2, data.getRow(0).getOriginalLineNumber());
		Assertions.assertEquals("1,2,3,4", CollUtil.join(data.getRow(0), ","));

		Assertions.assertEquals(4, data.getRow(1).getOriginalLineNumber());
		Assertions.assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(1), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		Assertions.assertEquals(6, data.getRow(2).getOriginalLineNumber());
		Assertions.assertEquals("a,s,d,f", CollUtil.join(data.getRow(2), ","));
	}

	@Test
	public void lineLimitWithHeaderTest() {
		// 从原始第2行开始读取
		final CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2).setContainsHeader(true));
		final CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.UTF_8));

		Assertions.assertEquals(4, data.getRow(0).getOriginalLineNumber());
		Assertions.assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(0), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		Assertions.assertEquals(6, data.getRow(1).getOriginalLineNumber());
		Assertions.assertEquals("a,s,d,f", CollUtil.join(data.getRow(1), ","));
	}

	@Test
	public void customConfigTest() {
		final CsvReader reader = CsvUtil.getReader(
				CsvReadConfig.defaultConfig()
						.setTextDelimiter('\'')
						.setFieldSeparator(';'));
		final CsvData csvRows = reader.readFromStr("123;456;'789;0'abc;");
		final CsvRow row = csvRows.getRow(0);
		Assertions.assertEquals("123", row.get(0));
		Assertions.assertEquals("456", row.get(1));
		Assertions.assertEquals("'789;0'abc", row.get(2));
	}

	@Test
	public void readDisableCommentTest() {
		final CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().disableComment());
		final CsvData read = reader.read(ResourceUtil.getUtf8Reader("test.csv"));
		final CsvRow row = read.getRow(0);
		Assertions.assertEquals("# 这是一行注释，读取时应忽略", row.get(0));
	}

	@Test
	@Disabled
	public void streamTest() {
		final CsvReader reader = CsvUtil.getReader(ResourceUtil.getUtf8Reader("test_bean.csv"));
		reader.stream().limit(2).forEach(Console::log);
	}

	@Test
	@Disabled
	public void issue2306Test(){
		final CsvReader reader = CsvUtil.getReader(ResourceUtil.getUtf8Reader("d:/test/issue2306.csv"));
		final CsvData csvData = reader.read();
		for (CsvRow csvRow : csvData) {
			Console.log(csvRow);
		}
	}
}
