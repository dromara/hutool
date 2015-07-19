package com.xiaoleilu.hutool.bloomFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.xiaoleilu.hutool.FileUtil;
import com.xiaoleilu.hutool.Hashs;

/**
 * BloomFilter实现方式2，此方式使用BitSet存储。<br>
 * Hash算法的使用使用固定顺序，只需指定个数既可
 * @author loolly
 *
 */
public class BloomFilter2 {
	private BitSet bitSet;
	private int bitSetSize;
	private int addedElements;
	private int hashFunctionNumber;

	/**
	 * 构造一个布隆过滤器，过滤器的容量为c * n 个bit.
	 * 
	 * @param c 当前过滤器预先开辟的最大包含记录,通常要比预计存入的记录多一倍.
	 * @param n 当前过滤器预计所要包含的记录.
	 * @param k 哈希函数的个数，等同每条记录要占用的bit数.
	 */
	public BloomFilter2(int c, int n, int k) {
		this.hashFunctionNumber = k;
		this.bitSetSize = (int) Math.ceil(c * k);
		this.addedElements = n;
		this.bitSet = new BitSet(this.bitSetSize);
	}

	/**
	 * 通过文件初始化过滤器.
	 * 
	 * @param path 文件路径
	 * @param charset 字符集
	 * @throws IOException 
	 */
	public void init(String path, String charset) throws IOException {
		BufferedReader reader = FileUtil.getReader(path, charset);
		try {
			String line;
			while(true) {
				line = reader.readLine();
				if(line == null) break;
				this.put(line);
			}
		}finally {
			FileUtil.close(reader);
		}
	}

	/**
	 * 将字符串加入到BloomFilter中
	 * @param str 字符串
	 */
	public void put(String str) {
		int[] positions = createHashes(str, hashFunctionNumber);
		for (int i = 0; i < positions.length; i++) {
			int position = Math.abs(positions[i] % bitSetSize);
			bitSet.set(position, true);
		}
	}

	/**
	 * 判定是否包含指定字符串
	 * @param str 字符串
	 * @return 是否包含，存在误差
	 */
	public boolean contains(String str) {
		int[] positions = createHashes(str, hashFunctionNumber);
		for (int i : positions) {
			int position = Math.abs(i % bitSetSize);
			if (!bitSet.get(position)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return 得到当前过滤器的错误率.
	 */
	public double getFalsePositiveProbability() {
		// (1 - e^(-k * n / m)) ^ k
		return Math.pow((1 - Math.exp(-hashFunctionNumber * (double) addedElements / bitSetSize)), hashFunctionNumber);
	}

	/**
	 * 将字符串的字节表示进行多哈希编码.
	 * 
	 * @param str 待添加进过滤器的字符串字节表示.
	 * @param hashNumber 要经过的哈希个数.
	 * @return 各个哈希的结果数组.
	 */
	public static int[] createHashes(String str, int hashNumber) {
		int[] result = new int[hashNumber];
		for(int i = 0; i < hashNumber; i++) {
			result[i] = hash(str, i);
			
		}
		return result;
	}

	/**
	 * 计算Hash值
	 * @param str 被计算Hash的字符串
	 * @param k Hash算法序号
	 * @return Hash值
	 */
	public static int hash(String str, int k) {
		switch (k) {
			case 0:
				return Hashs.rsHash(str);
			case 1:
				return Hashs.jsHash(str);
			case 2:
				return Hashs.elfHash(str);
			case 3:
				return Hashs.bkdrHash(str);
			case 4:
				return Hashs.apHash(str);
			case 5:
				return Hashs.djbHash(str);
			case 6:
				return Hashs.sdbmHash(str);
			case 7:
				return Hashs.pjwHash(str);
		}
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		BloomFilter2 bloomfilter = new BloomFilter2(30000000, 10000000, 8);
		System.out.println("Bloom Filter Initialize ... ");
		bloomfilter.init("data/base.txt", "utf8");
		System.out.println("Bloom Filter Ready");
		System.out.println("False Positive Probability : " + bloomfilter.getFalsePositiveProbability());
		// 查找新数据
		List<String> result = new ArrayList<String>();
		long t1 = System.currentTimeMillis();
		BufferedReader reader = new BufferedReader(new FileReader("data/input.txt"));
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