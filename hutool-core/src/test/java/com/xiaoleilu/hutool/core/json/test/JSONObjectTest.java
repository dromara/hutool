package com.xiaoleilu.hutool.core.json.test;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;

/**
 * JSONObject单元测试
 * @author Looly
 *
 */
public class JSONObjectTest {
	
	@Test
	public void putAllTest(){
		JSONObject json1 = JSONUtil.createObj();
		json1.put("a", "value1");
		json1.put("b", "value2");
		json1.put("c", "value3");
		
		JSONObject json2 = JSONUtil.createObj();
		json2.put("a", "value21");
		json2.put("b", "value22");
		
		//putAll操作会覆盖相同key的值，因此a,b两个key的值改变，c的值不变
		json1.putAll(json2);
		
		Assert.assertEquals(json1.get("a"), "value21");
		Assert.assertEquals(json1.get("b"), "value22");
		Assert.assertEquals(json1.get("c"), "value3");
	}
}
