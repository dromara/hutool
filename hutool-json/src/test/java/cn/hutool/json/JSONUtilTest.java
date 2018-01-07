package cn.hutool.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.test.bean.Price;
import cn.hutool.json.test.bean.UserA;

public class JSONUtilTest {
	
	@Test
	public void toJsonStrTest() {
		UserA a1 = new UserA();
		a1.setA("aaaa");
		a1.setDate(DateUtil.date());
		a1.setName("AAAAName");
		UserA a2 = new UserA();
		a2.setA("aaaa222");
		a2.setDate(DateUtil.date());
		a2.setName("AAAA222Name");
		
		ArrayList<UserA> list = CollectionUtil.newArrayList(a1, a2);
		HashMap<String, Object> map = CollectionUtil.newHashMap();
		map.put("total", 13);
		map.put("rows", list);
		
		
		String str = JSONUtil.toJsonStr(map);
		Assert.assertNotNull(str);
	}
	
	@Test
	public void toBeanTest() {
		String json = "{\"ADT\":[[{\"BookingCode\":[\"N\",\"N\"]}]]}";
		
		Price price = JSONUtil.toBean(json, Price.class);
		Assert.assertEquals("N", price.getADT().get(0).get(0).getBookingCode().get(0));
	}
	
}
