package cn.hutool.dfa;

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

	public static class TestBean {
		private String str;
		private Integer num;

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

		public Integer getNum() {
			return num;
		}

		public void setNum(Integer num) {
			this.num = num;
		}
	}

}
