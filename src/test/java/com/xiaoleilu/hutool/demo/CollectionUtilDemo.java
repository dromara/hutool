package com.xiaoleilu.hutool.demo;

import java.util.Comparator;
import java.util.List;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 集合工具类示例
 * 
 * @author Looly
 *
 */
public class CollectionUtilDemo {
	public static void main(String[] args) {

		//-------------------------------------------------------------------------------------------------
		// 拼接多个数组示例
		//-------------------------------------------------------------------------------------------------
		// 数组添加元素
		CharSequence[] array1 = { "abc", "123" };
		CharSequence[] array2 = CollectionUtil.append(array1, new String[] { "def", "456" });
		Console.log(array2);
		//-------------------------------------------------------------------------------------------------

		//-------------------------------------------------------------------------------------------------
		// 内存分页实现示例
		//-------------------------------------------------------------------------------------------------
		// 新建三个列表，CollectionUtil.newArrayList方法表示新建ArrayList并填充元素
		List<Integer> list1 = CollectionUtil.newArrayList(1, 2, 3);
		List<Integer> list2 = CollectionUtil.newArrayList(4, 5, 6);
		List<Integer> list3 = CollectionUtil.newArrayList(7, 8, 9);

		// 参数表示把list1,list2,list3合并并按照从小到大排序后，取0~2个（包括第0个，不包括第2个），结果是[1,2]
		Comparator<Integer> comparator = new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		};
		List<Integer> result = CollectionUtil.sortPageAll(6, 2, comparator, list1, list2, list3);
		Console.log(result);
		//-------------------------------------------------------------------------------------------------
	}
}
