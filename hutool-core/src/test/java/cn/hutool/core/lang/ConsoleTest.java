package cn.hutool.core.lang;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.thread.ThreadUtil;

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
	public void logTest2(){
		Console.log("a", "b", "c");
		Console.log((Object) "a", "b", "c");
	}
	
	@Test
	public void printTest(){
		String[] a = {"abc", "bcd", "def"};
		Console.print(a);
		
		Console.log("This is Console print for {}.", "test");
	}

	@Test
	public void printTest2(){
		Console.print("a", "b", "c");
		Console.print((Object) "a", "b", "c");
	}
	
	@Test
	public void errorTest(){
		Console.error();
		
		String[] a = {"abc", "bcd", "def"};
		Console.error(a);
		
		Console.error("This is Console error for {}.", "test");
	}

	@Test
	public void errorTest2(){
		Console.error("a", "b", "c");
		Console.error((Object) "a", "b", "c");
	}
	
	@Test
	@Ignore
	public void inputTest() {
		Console.log("Please input something: ");
		String input = Console.input();
		Console.log(input);
	}
	
	@Test
	@Ignore
	public void printProgressTest() {
		for(int i = 0; i < 100; i++) {
			Console.printProgress('#', 100, i / 100D);
			ThreadUtil.sleep(200);
		}
	}

}
