package cn.hutool.core.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;

import java.io.Serializable;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 权重随机算法实现<br>
 * <p>
 * 平时，经常会遇到权重随机算法，从不同权重的N个元素中随机选择一个，并使得总体选择结果是按照权重分布的。如广告投放、负载均衡等。
 * </p>
 * <p>
 * 如有4个元素A、B、C、D，权重分别为1、2、3、4，随机结果中A:B:C:D的比例要为1:2:3:4。<br>
 * </p>
 * 总体思路：累加每个元素的权重A(1)-B(3)-C(6)-D(10)，则4个元素的的权重管辖区间分别为[0,1)、[1,3)、[3,6)、[6,10)。<br>
 * 然后随机出一个[0,10)之间的随机数。落在哪个区间，则该区间之后的元素即为按权重命中的元素。<br>
 * 
 * <p>
 * 参考博客：https://www.cnblogs.com/waterystone/p/5708063.html
 * <p>
 * 
 * @param <T> 权重随机获取的对象类型
 * @author looly
 * @since 3.3.0
 */
public class WeightRandom<T> implements Serializable {
	private static final long serialVersionUID = -8244697995702786499L;

	private final TreeMap<Double, T> weightMap;


	/**
	 * 创建权重随机获取器
	 *
	 * @param <T> 权重随机获取的对象类型
	 * @return {@link WeightRandom}
	 */
	public static <T> WeightRandom<T> create() {
		return new WeightRandom<>();
	}

	// ---------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public WeightRandom() {
		weightMap = new TreeMap<>();

	}

	/**
	 * 构造
	 * 
	 * @param weightObj 带有权重的对象
	 */
	public WeightRandom(WeightObj<T> weightObj) {
		this();
		if(null != weightObj) {
			add(weightObj);
		}
	}

	/**
	 * 构造
	 * 
	 * @param weightObjs 带有权重的对象
	 */
	public WeightRandom(Iterable<WeightObj<T>> weightObjs) {
		this();
		if(CollUtil.isNotEmpty(weightObjs)) {
			for (WeightObj<T> weightObj : weightObjs) {
				add(weightObj);
			}
		}
	}

	/**
	 * 构造
	 * 
	 * @param weightObjs 带有权重的对象
	 */
	public WeightRandom(WeightObj<T>[] weightObjs) {
		this();
		for (WeightObj<T> weightObj : weightObjs) {
			add(weightObj);
		}
	}
	// ---------------------------------------------------------------------------------- Constructor end

	/**
	 * 增加对象
	 * 
	 * @param obj 对象
	 * @param weight 权重
	 * @return this
	 */
	public WeightRandom<T> add(T obj, double weight) {
		return add(new WeightObj<>(obj, weight));
	}

	/**
	 * 增加对象权重
	 * 
	 * @param weightObj 权重对象
	 * @return this
	 */
	public WeightRandom<T> add(WeightObj<T> weightObj) {
		if(null != weightObj) {
			final double weight = weightObj.getWeight();
			if(weightObj.getWeight() > 0) {
				double lastWeight = (this.weightMap.size() == 0) ? 0 : this.weightMap.lastKey();
				this.weightMap.put(weight + lastWeight, weightObj.getObj());// 权重累加
			}
		}
		return this;
	}

	/**
	 * 清空权重表
	 * 
	 * @return this
	 */
	public WeightRandom<T> clear() {
		if(null != this.weightMap) {
			this.weightMap.clear();
		}
		return this;
	}

	/**
	 * 下一个随机对象
	 * 
	 * @return 随机对象
	 */
	public T next() {
		if(MapUtil.isEmpty(this.weightMap)) {
			return null;
		}
		final Random random = RandomUtil.getRandom();
		final double randomWeight = this.weightMap.lastKey() * random.nextDouble();
		final SortedMap<Double, T> tailMap = this.weightMap.tailMap(randomWeight, false);
		return this.weightMap.get(tailMap.firstKey());
	}

	/**
	 * 带有权重的对象包装
	 * 
	 * @author looly
	 *
	 * @param <T> 对象类型
	 */
	public static class WeightObj<T> {
		/** 对象 */
		private T obj;
		/** 权重 */
		private final double weight;

		/**
		 * 构造
		 * 
		 * @param obj 对象
		 * @param weight 权重
		 */
		public WeightObj(T obj, double weight) {
			this.obj = obj;
			this.weight = weight;
		}

		/**
		 * 获取对象
		 * 
		 * @return 对象
		 */
		public T getObj() {
			return obj;
		}

		/**
		 * 设置对象
		 * 
		 * @param obj 对象
		 */
		public void setObj(T obj) {
			this.obj = obj;
		}

		/**
		 * 获取权重
		 * 
		 * @return 权重
		 */
		public double getWeight() {
			return weight;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((obj == null) ? 0 : obj.hashCode());
			long temp;
			temp = Double.doubleToLongBits(weight);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			WeightObj<?> other = (WeightObj<?>) obj;
			if (this.obj == null) {
				if (other.obj != null) {
					return false;
				}
			} else if (!this.obj.equals(other.obj)) {
				return false;
			}
			return Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight);
		}
	}
	
}
