package cn.hutool.core.io.file;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;

public class TailerTest {
	
	@Test
	@Ignore
	public void tailTest() {
		FileUtil.tail(FileUtil.file("e:/tail.txt"), new LineHandler() {
			
			@Override
			public void handle(String line) {
				if(StrUtil.isEmpty(line)) {
					Console.log();
				}
				Console.print("{}", line);
			}
		});
	}
}
