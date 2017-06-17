package com.xiaoleilu.hutool.core.lang;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.StrSpliter;

public class StrSpliterTest {
	
	@Test
	public void spliteByCharTest(){
		String str1 = "a, ,efedsfs,   ddf";
		List<String> split = StrSpliter.split(str1, ',', 0, true, true);
		Assert.assertEquals("ddf", split.get(2));
		Assert.assertEquals(3, split.size());
	}
}
