package cn.hutool.core.text.csv;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class CsvReaderTest {
	
	@Test
	public void readTest() {
		CsvReader reader = new CsvReader();
		CsvData data = reader.read(ResourceUtil.getReader("test.csv", CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("sss,sss", data.getRow(0).get(0));
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
}
