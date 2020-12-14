package cn.hutool.bloomfilter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.HashUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.BitSet;

/**
 * BloomFilter实现方式2，此方式使用BitSet存储。<br>
 * Hash算法的使用使用固定顺序，只需指定个数即可
 * @author loolly
 *
 */
public class BitSetBloomFilter implements BloomFilter{
	private static final long serialVersionUID = 1L;
	
	private final BitSet bitSet;
	private final int bitSetSize;
	private final int addedElements;
	private final int hashFunctionNumber;

	/**
	 * 构造一个布隆过滤器，过滤器的容量为c * n 个bit.
	 * 
	 * @param c 当前过滤器预先开辟的最大包含记录,通常要比预计存入的记录多一倍.
	 * @param n 当前过滤器预计所要包含的记录.
	 * @param k 哈希函数的个数，等同每条记录要占用的bit数.
	 */
	public BitSetBloomFilter(int c, int n, int k) {
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
	 * @throws IOException IO异常
	 */
	public void init(String path, String charset) throws IOException {
		BufferedReader reader = FileUtil.getReader(path, charset);
		try {
			String line;
			while(true) {
				line = reader.readLine();
				if(line == null) {
					break;
				}
				this.add(line);
			}
		}finally {
			IoUtil.close(reader);
		}
	}
	
	@Override
	public boolean add(String str) {
		if (contains(str)) {
			return false;
		}

		int[] positions = createHashes(str, hashFunctionNumber);
		for (int value : positions) {
			int position = Math.abs(value % bitSetSize);
			bitSet.set(position, true);
		}
		return true;
	}
	
	/**
	 * 判定是否包含指定字符串
	 * @param str 字符串
	 * @return 是否包含，存在误差
	 */
	@Override
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
				return HashUtil.rsHash(str);
			case 1:
				return HashUtil.jsHash(str);
			case 2:
				return HashUtil.elfHash(str);
			case 3:
				return HashUtil.bkdrHash(str);
			case 4:
				return HashUtil.apHash(str);
			case 5:
				return HashUtil.djbHash(str);
			case 6:
				return HashUtil.sdbmHash(str);
			case 7:
				return HashUtil.pjwHash(str);
			default:
				return 0;
		}
	}
}