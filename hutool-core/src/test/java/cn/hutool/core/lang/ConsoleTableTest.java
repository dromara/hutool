package cn.hutool.core.lang;

import org.junit.Test;

public class ConsoleTableTest {

	@Test
//    @Ignore
	public void printTest() {
		ConsoleTable t = new ConsoleTable();
		t.addHeader("姓名", "年龄");
		t.addBody("张三", "15");
		t.addBody("李四", "29");
		t.addBody("王二麻子", "37");
		t.print();

		Console.log();

		t = new ConsoleTable();
		t.addHeader("体温", "占比");
		t.addHeader("℃", "%");
		t.addBody("36.8", "10");
		t.addBody("37", "5");
		t.print();

		Console.log();

		t = new ConsoleTable();
		t.addHeader("标题1", "标题2");
		t.addBody("12345", "混合321654asdfcSDF");
		t.addBody("sd   e3ee  ff22", "ff值");
		t.print();
	}
}
