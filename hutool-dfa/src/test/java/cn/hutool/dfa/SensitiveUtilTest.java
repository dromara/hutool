package cn.hutool.dfa;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SensitiveUtilTest {

	@Test
	public void testSensitiveFilter() {
		List<String> wordList = new ArrayList<>();
		wordList.add("大");
		wordList.add("大土豆");
		wordList.add("土豆");
		wordList.add("刚出锅");
		wordList.add("出锅");
		TestBean bean = new TestBean();
		bean.setStr("我有一颗$大土^豆，刚出锅的");
		bean.setNum(100);
		SensitiveUtil.init(wordList);
		bean = SensitiveUtil.sensitiveFilter(bean, true, null);
		Assert.assertEquals(bean.getStr(), "我有一颗$****，***的");
	}

	@Data
	public static class TestBean {
		private String str;
		private Integer num;
	}

	@Test
	public void issue2126(){
		SensitiveUtil.init(ListUtil.of("赵", "赵阿", "赵阿三"));

		String result = SensitiveUtil.sensitiveFilter("赵阿三在做什么。", true, null);
		Assert.assertEquals("***在做什么。", result);
	}
}
