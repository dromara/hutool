package cn.hutool.core.text.csv;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
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
}
