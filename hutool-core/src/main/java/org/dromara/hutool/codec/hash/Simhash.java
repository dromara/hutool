/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.codec.hash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * <p>
 * Simhash是一种局部敏感hash，用于海量文本去重。<br>
 * 算法实现来自：<a href="https://github.com/xlturing/Simhash4J">https://github.com/xlturing/Simhash4J</a>
 * </p>
 *
 * <p>
 * 局部敏感hash定义：假定两个字符串具有一定的相似性，在hash之后，仍然能保持这种相似性，就称之为局部敏感hash。
 * </p>
 *
 * @author Looly, litaoxiao
 * @since 4.3.3
 */
public class Simhash implements Hash64<Collection<? extends CharSequence>> {

	private final int bitNum = 64;
	/**
	 * 存储段数，默认按照4段进行simhash存储
	 */
	private final int fracCount;
	private final int fracBitNum;
	/**
	 * 汉明距离的衡量标准，小于此距离标准表示相似
	 */
	private final int hammingThresh;

	/**
	 * 按照分段存储simhash，查找更快速
	 */
	private final List<Map<String, List<Long>>> storage;
	private final StampedLock lock = new StampedLock();

	/**
	 * 构造
	 */
	public Simhash() {
		this(4, 3);
	}

	/**
	 * 构造
	 *
	 * @param fracCount     存储段数
	 * @param hammingThresh 汉明距离的衡量标准
	 */
	public Simhash(final int fracCount, final int hammingThresh) {
		this.fracCount = fracCount;
		this.fracBitNum = bitNum / fracCount;
		this.hammingThresh = hammingThresh;
		this.storage = new ArrayList<>(fracCount);
		for (int i = 0; i < fracCount; i++) {
			storage.add(new HashMap<>());
		}
	}

	/**
	 * 指定文本计算simhash值
	 *
	 * @param segList 分词的词列表
	 * @return Hash值
	 */
	@Override
	public long hash64(final Collection<? extends CharSequence> segList) {
		final int bitNum = this.bitNum;
		// 按照词语的hash值，计算simHashWeight(低位对齐)
		final int[] weight = new int[bitNum];
		long wordHash;
		for (final CharSequence seg : segList) {
			wordHash = MurmurHash.INSTANCE.hash64(seg);
			for (int i = 0; i < bitNum; i++) {
				if (((wordHash >> i) & 1) == 1)
					weight[i] += 1;
				else
					weight[i] -= 1;
			}
		}

		// 计算得到Simhash值
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitNum; i++) {
			sb.append((weight[i] > 0) ? 1 : 0);
		}

		return new BigInteger(sb.toString(), 2).longValue();
	}

	/**
	 * 判断文本是否与已存储的数据重复
	 *
	 * @param segList 文本分词后的结果
	 * @return 是否重复
	 */
	public boolean equals(final Collection<? extends CharSequence> segList) {
		final long simhash = hash64(segList);
		final List<String> fracList = splitSimhash(simhash);
		final int hammingThresh = this.hammingThresh;

		String frac;
		Map<String, List<Long>> fracMap;
		final long stamp = this.lock.readLock();
		try {
			for (int i = 0; i < fracCount; i++) {
				frac = fracList.get(i);
				fracMap = storage.get(i);
				if (fracMap.containsKey(frac)) {
					for (final Long simhash2 : fracMap.get(frac)) {
						// 当汉明距离小于标准时相似
						if (hamming(simhash, simhash2) < hammingThresh) {
							return true;
						}
					}
				}
			}
		} finally {
			this.lock.unlockRead(stamp);
		}
		return false;
	}

	/**
	 * 按照(frac, 《simhash, content》)索引进行存储
	 *
	 * @param simhash Simhash值
	 */
	public void store(final Long simhash) {
		final int fracCount = this.fracCount;
		final List<Map<String, List<Long>>> storage = this.storage;
		final List<String> lFrac = splitSimhash(simhash);

		String frac;
		Map<String, List<Long>> fracMap;
		final long stamp = this.lock.writeLock();
		try {
			for (int i = 0; i < fracCount; i++) {
				frac = lFrac.get(i);
				fracMap = storage.get(i);
				if (fracMap.containsKey(frac)) {
					fracMap.get(frac).add(simhash);
				} else {
					final List<Long> ls = new ArrayList<>();
					ls.add(simhash);
					fracMap.put(frac, ls);
				}
			}
		} finally {
			this.lock.unlockWrite(stamp);
		}
	}

	//------------------------------------------------------------------------------------------------------ Private method start

	/**
	 * 计算汉明距离
	 *
	 * @param s1 值1
	 * @param s2 值2
	 * @return 汉明距离
	 */
	private int hamming(final Long s1, final Long s2) {
		final int bitNum = this.bitNum;
		int dis = 0;
		for (int i = 0; i < bitNum; i++) {
			if ((s1 >> i & 1) != (s2 >> i & 1))
				dis++;
		}
		return dis;
	}

	/**
	 * 将simhash分成n段
	 *
	 * @param simhash Simhash值
	 * @return N段Simhash
	 */
	private List<String> splitSimhash(final Long simhash) {
		final int bitNum = this.bitNum;
		final int fracBitNum = this.fracBitNum;

		final List<String> ls = new ArrayList<>();
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitNum; i++) {
			sb.append(simhash >> i & 1);
			if ((i + 1) % fracBitNum == 0) {
				ls.add(sb.toString());
				sb.setLength(0);
			}
		}
		return ls;
	}
	//------------------------------------------------------------------------------------------------------ Private method end
}
