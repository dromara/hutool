package cn.hutool.core.text.csv;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvReaderTest {

	@Test
	public void readTest() {
		CsvReader reader = new CsvReader();
		CsvData data = reader.read(ResourceUtil.getReader("test.csv", CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("sss,sss", data.getRow(0).get(0));
		Assert.assertEquals(1, data.getRow(0).getOriginalLineNumber());
		Assert.assertEquals("性别", data.getRow(0).get(2));
		Assert.assertEquals("关注\"对象\"", data.getRow(0).get(3));
	}

	@Test
	public void readMapListTest(){
		final CsvReader reader = CsvUtil.getReader();
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"));

		Assert.assertEquals("张三", result.get(0).get("姓名"));
		Assert.assertEquals("男", result.get(0).get("gender"));
		Assert.assertEquals("无", result.get(0).get("focus"));
		Assert.assertEquals("33", result.get(0).get("age"));

		Assert.assertEquals("李四", result.get(1).get("姓名"));
		Assert.assertEquals("男", result.get(1).get("gender"));
		Assert.assertEquals("好对象", result.get(1).get("focus"));
		Assert.assertEquals("23", result.get(1).get("age"));

		Assert.assertEquals("王妹妹", result.get(2).get("姓名"));
		Assert.assertEquals("女", result.get(2).get("gender"));
		Assert.assertEquals("特别关注", result.get(2).get("focus"));
		Assert.assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readAliasMapListTest(){
		final CsvReadConfig csvReadConfig = CsvReadConfig.defaultConfig();
		csvReadConfig.addHeaderAlias("姓名", "name");

		final CsvReader reader = CsvUtil.getReader(csvReadConfig);
		final List<Map<String, String>> result = reader.readMapList(
				ResourceUtil.getUtf8Reader("test_bean.csv"));

		Assert.assertEquals("张三", result.get(0).get("name"));
		Assert.assertEquals("男", result.get(0).get("gender"));
		Assert.assertEquals("无", result.get(0).get("focus"));
		Assert.assertEquals("33", result.get(0).get("age"));

		Assert.assertEquals("李四", result.get(1).get("name"));
		Assert.assertEquals("男", result.get(1).get("gender"));
		Assert.assertEquals("好对象", result.get(1).get("focus"));
		Assert.assertEquals("23", result.get(1).get("age"));

		Assert.assertEquals("王妹妹", result.get(2).get("name"));
		Assert.assertEquals("女", result.get(2).get("gender"));
		Assert.assertEquals("特别关注", result.get(2).get("focus"));
		Assert.assertEquals("22", result.get(2).get("age"));
	}

	@Test
	public void readBeanListTest(){
		final CsvReader reader = CsvUtil.getReader();
		final List<TestBean> result = reader.read(
				ResourceUtil.getUtf8Reader("test_bean.csv"), TestBean.class);

		Assert.assertEquals("张三", result.get(0).getName());
		Assert.assertEquals("男", result.get(0).getGender());
		Assert.assertEquals("无", result.get(0).getFocus());
		Assert.assertEquals(Integer.valueOf(33), result.get(0).getAge());

		Assert.assertEquals("李四", result.get(1).getName());
		Assert.assertEquals("男", result.get(1).getGender());
		Assert.assertEquals("好对象", result.get(1).getFocus());
		Assert.assertEquals(Integer.valueOf(23), result.get(1).getAge());

		Assert.assertEquals("王妹妹", result.get(2).getName());
		Assert.assertEquals("女", result.get(2).getGender());
		Assert.assertEquals("特别关注", result.get(2).getFocus());
		Assert.assertEquals(Integer.valueOf(22), result.get(2).getAge());
	}

	@Data
	private static class TestBean{
		@Alias("姓名")
		private String name;
		private String gender;
		private String focus;
		private Integer age;
	}

	@Test
	@Ignore
	public void readTest2(){
		final CsvReader reader = CsvUtil.getReader();
		final CsvData read = reader.read(FileUtil.file("d:/test/test.csv"));
		for (CsvRow strings : read) {
			Console.log(strings);
		}
	}

	@Test
	@Ignore
	public void readTest3(){
		final CsvReadConfig csvReadConfig = CsvReadConfig.defaultConfig();
		csvReadConfig.setContainsHeader(true);
		final CsvReader reader = CsvUtil.getReader(csvReadConfig);
		final CsvData read = reader.read(FileUtil.file("d:/test/ceshi.csv"));
		for (CsvRow row : read) {
			Console.log(row.getByName("案件ID"));
		}
	}

	@Test
	public void lineNoTest(){
		CsvReader reader = new CsvReader();
		CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals(1, data.getRow(0).getOriginalLineNumber());
		Assert.assertEquals("a,b,c,d", CollUtil.join(data.getRow(0), ","));

		Assert.assertEquals(4, data.getRow(2).getOriginalLineNumber());
		Assert.assertEquals("q,w,e,r,我是一段\n带换行的内容", CollUtil.join(data.getRow(2), ","));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		Assert.assertEquals(6, data.getRow(3).getOriginalLineNumber());
		Assert.assertEquals("a,s,d,f", CollUtil.join(data.getRow(3), ","));
	}

	@Test
	public void lineLimitTest(){
		// 从原始第2行开始读取
		CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2));
		CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));

		Assert.assertEquals(2, data.getRow(0).getOriginalLineNumber());
		Assert.assertEquals("1,2,3,4", CollUtil.join(data.getRow(0), ","));

		Assert.assertEquals(4, data.getRow(1).getOriginalLineNumber());
		Assert.assertEquals("q,w,e,r,我是一段\n带换行的内容", CollUtil.join(data.getRow(1), ","));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		Assert.assertEquals(6, data.getRow(2).getOriginalLineNumber());
		Assert.assertEquals("a,s,d,f", CollUtil.join(data.getRow(2), ","));
	}

	@Test
	public void lineLimitWithHeaderTest(){
		// 从原始第2行开始读取
		CsvReader reader = new CsvReader(CsvReadConfig.defaultConfig().setBeginLineNo(2).setContainsHeader(true));
		CsvData data = reader.read(ResourceUtil.getReader("test_lines.csv", CharsetUtil.CHARSET_UTF_8));

		Assert.assertEquals(4, data.getRow(0).getOriginalLineNumber());
		Assert.assertEquals("q,w,e,r,我是一段\n带换行的内容", CollUtil.join(data.getRow(0), ","));

		// 文件中第3行数据，对应原始行号是6（从0开始）
		Assert.assertEquals(6, data.getRow(1).getOriginalLineNumber());
		Assert.assertEquals("a,s,d,f", CollUtil.join(data.getRow(1), ","));
	}

	@Test
	public void customConfigTest(){
		final CsvReader reader = CsvUtil.getReader(
				CsvReadConfig.defaultConfig()
						.setTextDelimiter('\'')
						.setFieldSeparator(';'));
		final CsvData csvRows = reader.readFromStr("123;456;'789;0'abc;");
		final CsvRow row = csvRows.getRow(0);
		Assert.assertEquals("123", row.get(0));
		Assert.assertEquals("456", row.get(1));
		Assert.assertEquals("'789;0'abc", row.get(2));
	}

	@Test
	public void readDisableCommentTest(){
		final CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().disableComment());
		final CsvData read = reader.read(ResourceUtil.getUtf8Reader("test.csv"));
		final CsvRow row = read.getRow(0);
		Assert.assertEquals("# 这是一行注释，读取时应忽略", row.get(0));
	}

	/**
	 * 从 InputStream 流式读取 csv - 成功
	 */
	@Test
	public void streamingReadSuccessTest() throws IOException {
		// mock - 指定列数、行数的 csv 测试数据到内存中
		int columnCount = 16;
		int lineCount = 100000;
		byte[] csvMockData = mockCsvFileData(columnCount, lineCount);

		// 原始数据
		CsvData originalRows = CsvUtil.getReader().read(IoUtil.getReader(IoUtil.toStream(csvMockData), CharsetUtil.CHARSET_UTF_8));

		// 测试数据
		try (CsvParser parser = CsvUtil.getReader().streamingRead(IoUtil.toStream(csvMockData))){
			CsvRow testRow;
			int originalPointer = 0;
			while ((testRow = parser.nextRow()) != null) {
				// 断言 - 流式读取，断言每行每列与原始数据相等
				CsvRow originalRow = originalRows.getRow(originalPointer++);
				Assert.assertEquals(originalRow.size(), testRow.size());
				for (int i = 0; i < originalRow.size(); i++) {
					Assert.assertEquals(originalRow.get(i), testRow.get(i));
				}
			}

			// 断言 - 流式读取的数据行数和原始数据行数相等（将 pointer 矫正前一位）
			Assert.assertEquals(lineCount, originalPointer - 1);
		}

	}

	private byte[] mockCsvFileData(int columnCount, int lineCount) {
		// output stream in memory
		try (
				FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
				CsvWriter writer = CsvUtil.getWriter(IoUtil.getWriter(outputStream, CharsetUtil.CHARSET_UTF_8));
		) {
			// csv header
			String[] headers = ArrayUtil.newArray(String.class, columnCount);
			for (int i = 0; i < columnCount; i++) {
				headers[i] = "header_" + (i + 1);
			}
			writer.write(headers);

			// csv line data
			String[] lineValues = ArrayUtil.newArray(String.class, columnCount);
			for (int i = 0; i < lineCount; i++) {
				for (int j = 0; j < columnCount; j++) {
					lineValues[j] = "col_" + (j + 1) + "_line" + (i + 1);
				}
				writer.write(lineValues);
			}
			return outputStream.toByteArray();
		}
	}

}
