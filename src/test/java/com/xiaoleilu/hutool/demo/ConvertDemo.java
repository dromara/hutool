package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.convert.Convert;

public class ConvertDemo {
	public static void main(String[] args) {
		System.out.println(Convert.toInt("  120   ", 0));
		System.out.println(Convert.toLong("  120   ", 0L));
		//支持科学计数法
		System.out.println(Convert.toDouble("  12345E-10   ", 0D));

		// 整数
		System.out.println(Convert.digitUppercase(0)); // 零元整
		System.out.println(Convert.digitUppercase(123)); // 壹佰贰拾叁元整
		System.out.println(Convert.digitUppercase(1000000)); // 壹佰万元整
		System.out.println(Convert.digitUppercase(100000001)); // 壹亿零壹元整
		System.out.println(Convert.digitUppercase(1000000000)); // 壹拾亿元整
		System.out.println(Convert.digitUppercase(1234567890)); // 壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元整
		System.out.println(Convert.digitUppercase(1001100101)); // 壹拾亿零壹佰壹拾万零壹佰零壹元整
		System.out.println(Convert.digitUppercase(110101010)); // 壹亿壹仟零壹拾万壹仟零壹拾元整

		// 小数
		System.out.println(Convert.digitUppercase(0.12)); // 壹角贰分
		System.out.println(Convert.digitUppercase(123.34)); // 壹佰贰拾叁元叁角肆分
		System.out.println(Convert.digitUppercase(1000000.56)); // 壹佰万元伍角陆分
		System.out.println(Convert.digitUppercase(100000001.78)); // 壹亿零壹元柒角捌分
		System.out.println(Convert.digitUppercase(1000000000.90)); // 壹拾亿元玖角
		System.out.println(Convert.digitUppercase(1234567890.03)); // 壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁分
		System.out.println(Convert.digitUppercase(1001100101.00)); // 壹拾亿零壹佰壹拾万零壹佰零壹元整
		System.out.println(Convert.digitUppercase(110101010.10)); // 壹亿壹仟零壹拾万壹仟零壹拾元壹角

		// 负数
		System.out.println(Convert.digitUppercase(-0.12)); // 负壹角贰分
		System.out.println(Convert.digitUppercase(-123.34)); // 负壹佰贰拾叁元叁角肆分
		System.out.println(Convert.digitUppercase(-1000000.56)); // 负壹佰万元伍角陆分
		System.out.println(Convert.digitUppercase(-100000001.78)); // 负壹亿零壹元柒角捌分
		System.out.println(Convert.digitUppercase(-1000000000.90)); // 负壹拾亿元玖角
		System.out.println(Convert.digitUppercase(-1234567890.03)); // 负壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁分
		System.out.println(Convert.digitUppercase(-1001100101.00)); // 负壹拾亿零壹佰壹拾万零壹佰零壹元整
		System.out.println(Convert.digitUppercase(-110101010.10)); // 负壹亿壹仟零壹拾万壹仟零壹拾元壹角
	}
}
