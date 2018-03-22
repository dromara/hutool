package com.xiaoleilu.hutool.lang;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;

/**
 * 控制台单元测试
 * @author Looly
 *
 */
public class ConsoleTest {
	
	@Test
	public void logTest(){
		Console.log();
		
		String[] a = {"abc", "bcd", "def"};
		Console.log(a);
		
		Console.log("This is Console log for {}.", "test");
	}
	
	@Test
	public void printTest(){
		String[] a = {"abc", "bcd", "def"};
		Console.print(a);
		
		Console.log("This is Console print for {}.", "test");
	}
	
	@Test
	public void errorTest(){
		Console.error();
		
		String[] a = {"abc", "bcd", "def"};
		Console.error(a);
		
		Console.error("This is Console error for {}.", "test");
	}
	
	@Test
	@Ignore
	public void inputTest() {
		Console.log("Please input something: ");
		String input = Console.input();
		Console.log(input);
	}
}
