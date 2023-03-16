package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Issue2924Test {

	@Test
	public void toListTest(){
		final String idsJsonString = "[1174,137,1172,210,1173,627,628]";
		final List<Integer> idList = JSONUtil.toList(idsJsonString,Integer.class);
		Assert.assertEquals("[1174, 137, 1172, 210, 1173, 627, 628]", idList.toString());
	}
}
