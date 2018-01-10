package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

/**
 * StrBuilder单元测试
 * @author looly
 *
 */
public class StrBuilderTest {
	
	@Test
	public void appendTest() {
		StrBuilder builder = StrBuilder.create();
		builder.append("aaa").append("你好").append('r');
		Assert.assertEquals("aaa你好r", builder.toString());
	}
	
	@Test
	public void insertTest() {
		StrBuilder builder = StrBuilder.create(1);
		builder.append("aaa").append("你好").append('r');
		builder.insert(3, "数据插入");
		Assert.assertEquals("aaa数据插入你好r", builder.toString());
	}
	
	@Test
	public void insertTest2() {
		StrBuilder builder = StrBuilder.create(1);
		builder.append("aaa").append("你好").append('r');
		builder.insert(8, "数据插入");
		Assert.assertEquals("aaa你好r  数据插入", builder.toString());
	}
	
	@Test
	public void resetTest() {
		StrBuilder builder = StrBuilder.create(1);
		builder.append("aaa").append("你好").append('r');
		builder.insert(3, "数据插入");
		builder.reset();
		Assert.assertEquals("", builder.toString());
	}
	
	@Test
	public void resetTest2() {
		StrBuilder builder = StrBuilder.create(1);
		builder.append("aaa").append("你好").append('r');
		builder.insert(3, "数据插入");
		builder.reset();
		builder.append("bbb".toCharArray());
		Assert.assertEquals("bbb", builder.toString());
	}
	
	@Test
	public void appendObjectTest() {
		StrBuilder builder = StrBuilder.create(1);
		builder.append(123).append(456.123D).append(true).append('\n');
		Assert.assertEquals("123456.123true\n", builder.toString());
	}
}
