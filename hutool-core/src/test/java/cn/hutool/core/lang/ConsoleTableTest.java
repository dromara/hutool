package cn.hutool.core.lang;

import org.junit.Ignore;
import org.junit.Test;

public class ConsoleTableTest {

	@Test
	@Ignore
	public void printSBCTest() {
		ConsoleTable t = ConsoleTable.create();
		t.addHeader("姓名", "年龄");
		t.addBody("张三", "15");
		t.addBody("李四", "29");
		t.addBody("王二麻子", "37");
		t.print();

		Console.log();

		t = ConsoleTable.create();
		t.addHeader("体温", "占比");
		t.addHeader("℃", "%");
		t.addBody("36.8", "10");
		t.addBody("37", "5");
		t.print();

		Console.log();

		t = ConsoleTable.create();
		t.addHeader("标题1", "标题2");
		t.addBody("12345", "混合321654asdfcSDF");
		t.addBody("sd   e3ee  ff22", "ff值");
		t.print();
	}

	@Test
	@Ignore
	public void printDBCTest() {
		ConsoleTable t = ConsoleTable.create().setSBCMode(false);
		t.addHeader("姓名", "年龄");
		t.addBody("张三", "15");
		t.addBody("李四", "29");
		t.addBody("王二麻子", "37");
		t.print();

		Console.log();

		t = ConsoleTable.create().setSBCMode(false);
		t.addHeader("体温", "占比");
		t.addHeader("℃", "%");
		t.addBody("36.8", "10");
		t.addBody("37", "5");
		t.print();
	}
}
