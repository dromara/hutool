package com.xiaoleilu.hutool.demo;

import java.util.Arrays;

import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 集合工具类示例
 * @author Looly
 *
 */
public class CollectionUtilDemo {
	public static void main(String[] args) {
		
		//数组添加元素
		CharSequence[] array1 = {"abc", "123"};
		CharSequence[] array2 = CollectionUtil.append(array1, new String[]{"def", "456"});
		System.out.println(Arrays.toString(array2));
	}
}
