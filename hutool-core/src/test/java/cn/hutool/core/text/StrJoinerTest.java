package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StrJoinerTest {

	@Test
	public void joinIntArrayTest(){
		int[] a = {1,2,3,4,5};
		final StrJoiner append = StrJoiner.of(",").append(a);
		Assert.assertEquals("1,2,3,4,5", append.toString());
	}

	@Test
	public void joinEmptyTest(){
		List<String> list = new ArrayList<>();
		final StrJoiner append = StrJoiner.of(",").append(list);
		Assert.assertEquals("", append.toString());
	}

	@Test
	public void noJoinTest(){
		final StrJoiner append = StrJoiner.of(",");
		Assert.assertEquals("", append.toString());
	}
}
