package cn.hutool.core.text.csv;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.List;
import java.util.Map;

public class CsvReaderTest {

	@Test
	public void readTest() {
		CsvReader reader = new CsvReader();
		CsvData data = reader.read(ResourceUtil.getReader("test.csv", CharsetUtil.CHARSET_UTF_8));
		assertEquals("sss,sss", data.getRow(0).get(0));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals("性别", data.getRow(0).get(2));
		assertEquals("关注\"对象\"", data.getRow(0).get(3));
	}

	@Test
	public void readMapListTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"));

		assertEquals("张三", result.get(0).get("姓名"));
		assertEquals("男", result.get(0).get("gender"));
		assertEquals("无", result.get(0).get("focus"));
		assertEquals("33", result.get(0).get("age"));

		assertEquals("李四", result.get(1).get("姓名"));
		assertEquals("男", result.get(1).get("gender"));
		assertEquals("好对象", result.get(1).get("focus"));
		assertEquals("23", result.get(1).get("age"));

		assertEquals("王妹妹", result.get(2).get("姓名"));
		assertEquals("女", result.get(2).get("gender"));
		assertEquals("特别关注", result.get(2).get("focus"));
		assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readAliasMapListTest() {
		final CsvReadConfig csvReadConfig = CsvReadConfig.defaultConfig();
		csvReadConfig.addHeaderAlias("姓名", "name");

		final CsvReader reader = CsvUtil.getReader(csvReadConfig);
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"));

		assertEquals("张三", result.get(0).get("name"));
		assertEquals("男", result.get(0).get("gender"));
		assertEquals("无", result.get(0).get("focus"));
		assertEquals("33", result.get(0).get("age"));

		assertEquals("李四", result.get(1).get("name"));
		assertEquals("男", result.get(1).get("gender"));
		assertEquals("好对象", result.get(1).get("focus"));
		assertEquals("23", result.get(1).get("age"));

		assertEquals("王妹妹", result.get(2).get("name"));
		assertEquals("女", result.get(2).get("gender"));
		assertEquals("特别关注", result.get(2).get("focus"));
		assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readBeanListTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<TestBean> result = reader.read(
				ResourceUtil.getUtf8Reader("test_bean.csv"), TestBean.class);

		assertEquals("张三", result.get(0).getName());
		assertEquals("男", result.get(0).getGender());
		assertEquals("无", result.get(0).getFocus());
		assertEquals(Integer.valueOf(33), result.get(0).getAge());

		assertEquals("李四", result.get(1).getName());
		assertEquals("男", result.get(1).getGender());
		assertEquals("好对象", result.get(1).getFocus());
		assertEquals(Integer.valueOf(23), result.get(1).getAge());

		assertEquals("王妹妹", result.get(2).getName());
		assertEquals("女", result.get(2).getGender());
		assertEquals("特别关注", result.get(2).getFocus());
		assertEquals(Integer.valueOf(22), result.get(2).getAge());
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
		for (CsvRow strings : read) {
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
		for (CsvRow row : read) {
			Console.log(row.getByName("案件ID"));
		}
	}

	@Test
	public void lineNoTest() {
		CsvReader reader = new CsvReader();
		CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals("a,b,c,d", CollUtil.join(data.getRow(0), ","));

		assertEquals(4, data.getRow(2).getOriginalLineNumber());
		assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(2), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		assertEquals(6, data.getRow(3).getOriginalLineNumber());
		assertEquals("a,s,d,f", CollUtil.join(data.getRow(3), ","));
	}

	@Test
	public void lineLimitTest() {
		// 从原始第2行开始读取
		CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2));
		CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));

		assertEquals(2, data.getRow(0).getOriginalLineNumber());
		assertEquals("1,2,3,4", CollUtil.join(data.getRow(0), ","));

		assertEquals(4, data.getRow(1).getOriginalLineNumber());
		assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(1), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		assertEquals(6, data.getRow(2).getOriginalLineNumber());
		assertEquals("a,s,d,f", CollUtil.join(data.getRow(2), ","));
	}

	@Test
	public void lineLimitWithHeaderTest() {
		// 从原始第2行开始读取
		CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2).setContainsHeader(true));
		CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));

		assertEquals(4, data.getRow(0).getOriginalLineNumber());
		assertEquals("q,w,e,r,我是一段\n带换行的内容",
				CollUtil.join(data.getRow(0), ",").replace("\r", ""));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		assertEquals(6, data.getRow(1).getOriginalLineNumber());
		assertEquals("a,s,d,f", CollUtil.join(data.getRow(1), ","));
	}

	@Test
	public void customConfigTest() {
		final CsvReader reader = CsvUtil.getReader(
				CsvReadConfig.defaultConfig()
						.setTextDelimiter('\'')
						.setFieldSeparator(';'));
		final CsvData csvRows = reader.readFromStr("123;456;'789;0'abc;");
		final CsvRow row = csvRows.getRow(0);
		assertEquals("123", row.get(0));
		assertEquals("456", row.get(1));
		assertEquals("'789;0'abc", row.get(2));
	}

	@Test
	public void readDisableCommentTest() {
		final CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().disableComment());
		final CsvData read = reader.read(ResourceUtil.getUtf8Reader("test.csv"));
		final CsvRow row = read.getRow(0);
		assertEquals("# 这是一行注释，读取时应忽略", row.get(0));
	}

	@Test
	@Disabled
	public void streamTest() {
		final CsvReader reader = CsvUtil.getReader(ResourceUtil.getUtf8Reader("test_bean.csv"));
		reader.stream().limit(2).forEach(Console::log);
	}
}
