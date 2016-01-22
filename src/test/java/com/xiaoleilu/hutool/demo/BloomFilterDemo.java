package com.xiaoleilu.hutool.demo;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.bloomFilter.BitSetBloomFilter;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.FileUtil;

public class BloomFilterDemo {
	public static void main(String[] args) throws Exception {
		BitSetBloomFilter bloomfilter = new BitSetBloomFilter(30000000, 10000000, 8);
		System.out.println("Bloom Filter Initialize ... ");
		bloomfilter.init("data/base.txt", "utf8");
		System.out.println("Bloom Filter Ready");
		System.out.println("False Positive Probability : " + bloomfilter.getFalsePositiveProbability());
		// 查找新数据
		List<String> result = new ArrayList<String>();
		long t1 = System.currentTimeMillis();
		BufferedReader reader =FileUtil.getReader("data/input.txt", CharsetUtil.UTF_8);
		String line = reader.readLine();
		while (line != null && line.length() > 0) {
			if (!bloomfilter.contains(line)) {
				result.add(line);
			}
			line = reader.readLine();
		}
		reader.close();
		long t2 = System.currentTimeMillis();
		System.out.println("Parse 9900000 items, Time : " + (t2 - t1) + "ms , find " + result.size() + " new items.");
		System.out.println("Average : " + 9900000 / ((t2 - t1) / 1000) + " items/second");
	}
}
