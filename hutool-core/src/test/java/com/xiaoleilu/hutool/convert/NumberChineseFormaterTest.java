package com.xiaoleilu.hutool.convert;

import org.junit.Assert;
import org.junit.Test;

public class NumberChineseFormaterTest {
	
	@Test
	public void formatTest() {
		String f1 = NumberChineseFormater.format(10889.72356, false);
		Assert.assertEquals("一万零八百八十九点七二", f1);
		f1 = NumberChineseFormater.format(12653, false);
		Assert.assertEquals("一万二千六百五十三", f1);
		f1 = NumberChineseFormater.format(215.6387, false);
		Assert.assertEquals("二百一十五点六四", f1);
		f1 = NumberChineseFormater.format(1024, false);
		Assert.assertEquals("一千零二十四", f1);
		f1 = NumberChineseFormater.format(100350089, false);
		Assert.assertEquals("一亿三十五万零八十九", f1);
		f1 = NumberChineseFormater.format(1200, false);
		Assert.assertEquals("一千二百", f1);
		f1 = NumberChineseFormater.format(12, false);
		Assert.assertEquals("一十二", f1);
		f1 = NumberChineseFormater.format(0.05, false);
		Assert.assertEquals("零点零五", f1);
	}
	
	@Test
	public void formatTranditionalTest() {
		String f1 = NumberChineseFormater.format(10889.72356, true);
		Assert.assertEquals("壹万零捌百捌十玖点柒贰", f1);
		f1 = NumberChineseFormater.format(12653, true);
		Assert.assertEquals("壹万贰千陆百伍十叁", f1);
		f1 = NumberChineseFormater.format(215.6387, true);
		Assert.assertEquals("贰百壹十伍点陆肆", f1);
		f1 = NumberChineseFormater.format(1024, true);
		Assert.assertEquals("壹千零贰十肆", f1);
		f1 = NumberChineseFormater.format(100350089, true);
		Assert.assertEquals("壹亿叁十伍万零捌十玖", f1);
		f1 = NumberChineseFormater.format(1200, true);
		Assert.assertEquals("壹千贰百", f1);
		f1 = NumberChineseFormater.format(12, true);
		Assert.assertEquals("壹十贰", f1);
		f1 = NumberChineseFormater.format(0.05, true);
		Assert.assertEquals("零点零伍", f1);
	}
}
