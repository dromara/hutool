package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.util.ObjectUtil;

/**
 * Object工具类样例
 * @author Looly
 *
 */
public class ObjectUtilDemo {
	public static class A implements Cloneable{
		private String name = "test";
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	public static void main(String[] args) {
		A a = new A();
		A a2 = ObjectUtil.clone(a);
		System.out.println(a2);
	}
}
