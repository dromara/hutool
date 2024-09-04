package cn.hutool.core.lang;

import cn.hutool.core.util.RandomUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 动态按权重随机的随机池，底层是list实现。
 *
 * @param <E> 元素类型
 * @author 王叶峰
 * @since 5.8.33
 */
public class WeightListRandom<E> implements Serializable {
	private static final long serialVersionUID = 6902006276975764032L;

	/**
	 * 随机元素池
	 */
	private final ArrayList<EWeight<E>> randomPool;

	/**
	 * 构造
	 */
	public WeightListRandom() {
		randomPool = new ArrayList<>();
	}

	/**
	 * 构造
	 *
	 * @param poolSize 容量
	 */
	public WeightListRandom(int poolSize) {
		randomPool = new ArrayList<>(poolSize);
	}

	/**
	 * 添加元素
	 *
	 * @param e      元素
	 * @param weight 权重
	 */
	public void add(E e, double weight) {
		Assert.isTrue(weight > 0, "权重必须大于0！");
		randomPool.add(new EWeight<>(e, sumWeight() + weight));
	}

	/**
	 * 移除元素
	 *
	 * @param e 元素
	 * @return 是否移除成功
	 */
	public boolean remove(E e) {
		boolean removed = false;
		double weight = 0;
		int i = 0;
		Iterator<EWeight<E>> iterator = randomPool.iterator();
		while (iterator.hasNext()) {
			EWeight<E> ew = iterator.next();
			if (!removed && ew.e.equals(e)) {
				iterator.remove();
				weight = ew.sumWeight - (i == 0 ? 0 : randomPool.get(i - 1).sumWeight);// 权重=当前权重-上一个权重
				removed = true;
			}
			if (removed) {
				// 重新计算后续权重
				ew.sumWeight -= weight;
			}
			i++;
		}
		return removed;
	}

	/**
	 * 随机出一个元素
	 *
	 * @return 随机池的一个元素
	 */
	public E next() {
		checkEmptyPool();

		if (randomPool.size() == 1) {
			return randomPool.get(0).e;
		}
		ThreadLocalRandom random = RandomUtil.getRandom();
		double randVal = random.nextDouble() * sumWeight();
		return binarySearch(randVal);
	}

	/**
	 * 按照给定的总权重随机出一个元素
	 *
	 * @param weight 总权重
	 * @return 随机池的一个元素或者null
	 */
	public E randomByWeight(double weight) {
		Assert.isTrue(weight >= sumWeight(), "权重必须大于当前总权重！");
		ThreadLocalRandom random = RandomUtil.getRandom();
		double randVal = random.nextDouble() * sumWeight();
		if (randVal > sumWeight()) {
			return null;
		}
		return binarySearch(randVal);
	}

	/**
	 * 判断随机池是否为空
	 *
	 * @return 是否为空
	 */
	public boolean isEmpty() {
		return randomPool.isEmpty();
	}

	private static class EWeight<E> {
		final E e;
		double sumWeight;

		public EWeight(E e, double sumWeight) {
			this.e = e;
			this.sumWeight = sumWeight;
		}
	}

	/**
	 * 二分查找小于等于key的最大值的元素
	 *
	 * @param key 目标值
	 * @return 随机池的一个元素或者null 当key大于所有元素的总权重时，返回null
	 */
	private E binarySearch(double key) {
		int low = 0;
		int high = randomPool.size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			double midVal = randomPool.get(mid).sumWeight;

			if (midVal < key) {
				low = mid + 1;
			} else if (midVal > key) {
				high = mid - 1;
			} else {
				return randomPool.get(mid).e;
			}
		}
		return randomPool.get(low).e;
	}

	private double sumWeight() {
		if (randomPool.isEmpty()) {
			return 0;
		}
		return randomPool.get(randomPool.size() - 1).sumWeight;
	}

	private void checkEmptyPool() {
		if (isEmpty()) {
			throw new IllegalArgumentException("随机池为空！");
		}
	}
}
