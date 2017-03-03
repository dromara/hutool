package com.xiaoleilu.hutool.core.date;

import org.junit.Test;

import com.xiaoleilu.hutool.date.DateTime;
import com.xiaoleilu.hutool.lang.Console;

/**
 * DateTime单元测试
 * @author Looly
 *
 */
public class DateTimeTest {
	
	@Test
	public void datetimeTest(){
		DateTime time = new DateTime();
		Console.log(time);
	}
}
