package cn.hutool.json;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.test.bean.Exam;

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
	public void parseFileTest(){
		JSONArray array = JSONUtil.readJSONArray(FileUtil.file("exam_test.json"), CharsetUtil.CHARSET_UTF_8);
		
		JSONObject obj0 = array.getJSONObject(0);
		Exam exam = JSONUtil.toBean(obj0, Exam.class);
		Assert.assertEquals("0", exam.getAnswerArray()[0].getSeq());
	}
	
	@Test
	@Ignore
	public void parseBeanListTest() {
		KeyBean b1 = new KeyBean();
		b1.setAkey("aValue1");
		b1.setBkey("bValue1");
		KeyBean b2 = new KeyBean();
		b2.setAkey("aValue2");
		b2.setBkey("bValue2");
		
		ArrayList<KeyBean> list = CollUtil.newArrayList(b1, b2);
		
		JSONArray jsonArray = JSONUtil.parseArray(list);
		Console.log(jsonArray);
	}
	
	@Test
	public void toListTest(){
		String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.CHARSET_UTF_8);
		JSONArray array = JSONUtil.parseArray(jsonStr);
		
		ArrayList<Exam> list = array.toList(Exam.class);
		Assert.assertFalse(list.isEmpty());
	}
	
	/**
	 * 单元测试用于测试在列表元素中有null时的情况下是否出错
	 */
	@Test
	public void toListWithNullTest() {
		String json = "[null,{'akey':'avalue','bkey':'bvalue'}]";
		JSONArray ja = JSONUtil.parseArray(json);
		
		ArrayList<KeyBean> list = ja.toList(KeyBean.class);
		Assert.assertTrue(null == list.get(0));
		Assert.assertEquals("avalue", list.get(1).getAkey());
		Assert.assertEquals("bvalue", list.get(1).getBkey());
	}
	
	public static class KeyBean{
		private String akey;
		private String bkey;
		
		public String getAkey() {
			return akey;
		}
		public void setAkey(String akey) {
			this.akey = akey;
		}
		public String getBkey() {
			return bkey;
		}
		public void setBkey(String bkey) {
			this.bkey = bkey;
		}
		
		@Override
		public String toString() {
			return "KeyBean [akey=" + akey + ", bkey=" + bkey + "]";
		}
	}
}
