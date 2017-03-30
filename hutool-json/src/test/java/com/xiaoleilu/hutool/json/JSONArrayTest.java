package com.xiaoleilu.hutool.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * JSONArray单元测试
 * @author Looly
 *
 */
public class JSONArrayTest {
	
	@Test
	public void addTest(){
		//方法1
		JSONArray array = JSONUtil.createArray();
		//方法2
//		JSONArray array = new JSONArray();
		array.add("value1");
		array.add("value2");
		array.add("value3");
		
		Assert.assertEquals(array.get(0), "value1");
	}
	
	@Test
	public void parseTest(){
		String jsonStr = "[\"value1\", \"value2\", \"value3\"]";
		JSONArray array = JSONUtil.parseArray(jsonStr);
		Assert.assertEquals(array.get(0), "value1");
	}
}
