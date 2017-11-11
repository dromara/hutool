/**
 * 
 */
package com.xiaoleilu.hutool.poi.test;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.poi.excel.ExcelWriter;

/**
 * @author looly
 *
 */
public class ExcelWriteTest {
	
	@Test
	@Ignore
	public void writeTest() {
		ArrayList<String> list1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
		ArrayList<String> list2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
		ArrayList<String> list3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
		ArrayList<String> list4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");
		ArrayList<String> list5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");
		ArrayList<ArrayList<String>> list = CollUtil.newArrayList(list1, list2, list3, list4, list5);
		
		ExcelWriter writer = new ExcelWriter("d:/writeTest.xlsx");
		writer.passCurrentRow();
		writer.merge(list1.size() - 1, "测试标题");
		//内容
		writer.write(list);
		writer.close();
	}
}
