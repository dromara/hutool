package com.xiaoleilu.hutool.bloomFilter.filter;

public interface Filter {

	/**
	 * 
	 * @param str 字符串
	 * @return 判断一个字符串是否bitMap中存在
	 */
	public boolean contains(String str);

	/**
	 * 在boolean的bitMap中增加一个字符串
	 * @param str 字符串
	 */
	public void add(String str);
	
	/**
	 * @param str 字符串
	 * @return 如果存在就返回true .如果不存在.先增加这个字符串.再返回false
	 */
	public boolean containsAndAdd(String str) ;
	
	/**
	 * 自定义Hash方法
	 * @param str 字符串
	 * @return HashCode
	 */
	public long hash(String str) ;
}