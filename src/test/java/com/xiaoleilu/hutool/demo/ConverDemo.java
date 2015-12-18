package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.Conver;

public class ConverDemo {
	public static void main(String[] args) {
		System.out.println(Conver.toInt("  120   ", 0));
		System.out.println(Conver.toLong("  120   ", 0L));
	}
}
