package cn.hutool.core.lang;


import cn.hutool.core.lang.hash.CityHash;
import cn.hutool.core.lang.hash.MetroHash;
import org.junit.Test;

import java.util.Random;

public class MetroHashTest {


	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	public void bulkHashing64Test() {
		String[] strArray = getRandomStringArray(10000000);
		long startCity = System.currentTimeMillis();
		for (String s : strArray) {
			CityHash.hash64(s.getBytes());
		}
		long endCity = System.currentTimeMillis();

		long startMetro = System.currentTimeMillis();
		for (String s : strArray) {
			MetroHash.hash64(s);
		}
		long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	public void bulkHashing128Test() {
		String[] strArray = getRandomStringArray(10000000);
		long startCity = System.currentTimeMillis();
		for (String s : strArray) {
			CityHash.hash128(s.getBytes());
		}
		long endCity = System.currentTimeMillis();

		long startMetro = System.currentTimeMillis();
		for (String s : strArray) {
			MetroHash.hash128(s);
		}
		long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	private static String[] getRandomStringArray(int length) {
		String[] result = new String[length];
		Random random = new Random();
		int index = 0;
		while (index < length) {
			result[index++] = getRandomString(random.nextInt(64));
		}
		return result;
	}

	private static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}


}
