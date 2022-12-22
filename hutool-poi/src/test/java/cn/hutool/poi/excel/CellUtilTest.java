package cn.hutool.poi.excel;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;

public class CellUtilTest {
	
	@Test
	@Ignore
	public void isDateTest() {
		String[] all = BuiltinFormats.getAll();
		for(int i = 0 ; i < all.length; i++) {
			Console.log("{} {}", i, all[i]);
		}
	}
}
