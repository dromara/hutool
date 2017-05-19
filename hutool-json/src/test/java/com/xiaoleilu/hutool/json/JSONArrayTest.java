package com.xiaoleilu.hutool.json;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.json.test.bean.Exam;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CharsetUtil;

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
	
	@Test
	public void parseTest2(){
		String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.CHARSET_UTF_8);
		
		JSONArray array = JSONUtil.parseArray(jsonStr);
//		for (Object obj : array) {
//			Exam exam = JSONUtil.toBean((JSONObject)obj, Exam.class);
//			Console.log(exam);
//		}
		
		JSONObject obj0 = array.getJSONObject(0);
		Exam exam = JSONUtil.toBean(obj0, Exam.class);
		Console.log(exam);
	}
}
