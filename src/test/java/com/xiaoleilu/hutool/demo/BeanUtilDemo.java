package com.xiaoleilu.hutool.demo;

import java.util.HashMap;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.BeanUtil;

/**
 * BeanUtil类Demo
 * @author Looly
 *
 */
public class BeanUtilDemo {
	public static void main(String[] args) {
		Console.log(BeanUtil.isBean(HashMap.class));
		
		A a = new A();
		B b = new B();
		BeanUtil.copyProperties(a, b);
		Console.log(b.field1);
		Console.log(b.field2);
	}
	
	public static class A{
		private String field1 = "A_Value";
		private int field2 = 12;
		public String getField1() {
			return field1;
		}
		public void setField1(String field1) {
			this.field1 = field1;
		}
		public int getField2() {
			return field2;
		}
		public void setField2(int field2) {
			this.field2 = field2;
		}
	}
	
	public static class B{
		private String field1;
		private int field2;
		public String getField1() {
			return field1;
		}
		public void setField1(String field1) {
			this.field1 = field1;
		}
		public int getField2() {
			return field2;
		}
		public void setField2(int field2) {
			this.field2 = field2;
		}
	}
}
