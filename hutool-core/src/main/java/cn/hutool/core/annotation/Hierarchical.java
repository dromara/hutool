package cn.hutool.core.annotation;


import java.util.Comparator;

/**
 * <p>描述以一个参照物为对象，存在于该参照物的层级结构中的对象。
 *
 * <p>该对象可通过{@link #getVerticalDistance()}与{@link #getHorizontalDistance()}
 * 描述其在以参照物为基点的坐标坐标系中的位置。<br>
 * 在需要对该接口的实现类进行按优先级排序时，距离{@link #getRoot()}对象越近，则该实现类的优先级越高。
 * 默认提供了{@link #DEFAULT_HIERARCHICAL_COMPARATOR}用于实现该比较规则。<br>
 * 一般情况下，{@link #getRoot()}返回值相同的对象之间的比较才有意义。
 *
 * <p>此外，还提供了{@link Selector}接口用于根据一定的规则从两个{@link Hierarchical}实现类中选择并返回一个最合适的对象，
 * 默认提供了四个实现类：
 * <ul>
 *     <li>{@link Selector#NEAREST_AND_OLDEST_PRIORITY}: 返回距离根对象更近的对象，当距离一样时优先返回旧对象；</li>
 *     <li>{@link Selector#NEAREST_AND_NEWEST_PRIORITY}: 返回距离根对象更近的对象，当距离一样时优先返回新对象；</li>
 *     <li>{@link Selector#FARTHEST_AND_OLDEST_PRIORITY}: 返回距离根对象更远的对象，当距离一样时优先返回旧对象；</li>
 *     <li>{@link Selector#FARTHEST_AND_NEWEST_PRIORITY}: 返回距离根对象更远的对象，当距离一样时优先返回新对象；</li>
 * </ul>
 *
 * @author huangchengxing
 */
public interface Hierarchical extends Comparable<Hierarchical> {

	// ====================== compare  ======================

	/**
	 * 默认{@link #getHorizontalDistance()}与{@link #getVerticalDistance()}排序的比较器
	 */
	Comparator<Hierarchical> DEFAULT_HIERARCHICAL_COMPARATOR = Comparator
		.comparing(Hierarchical::getVerticalDistance)
		.thenComparing(Hierarchical::getHorizontalDistance);

	/**
	 * 按{@link #getVerticalDistance()}和{@link #getHorizontalDistance()}排序
	 *
	 * @param o {@link SynthesizedAnnotation}对象
	 * @return 比较值
	 */
	@Override
	default int compareTo(Hierarchical o) {
		return DEFAULT_HIERARCHICAL_COMPARATOR.compare(this, o);
	}

	// ====================== hierarchical  ======================

	/**
	 * 参照物，即坐标为{@code (0, 0)}的对象。
	 * 当对象本身即为参照物时，该方法应当返回其本身
	 *
	 * @return 参照物
	 */
	Object getRoot();

	/**
	 * 获取该对象与参照物的垂直距离。
	 * 默认情况下，该距离即为当前对象与参照物之间相隔的层级数。
	 *
	 * @return 合成注解与根对象的垂直距离
	 */
	int getVerticalDistance();

	/**
	 * 获取该对象与参照物的水平距离。
	 * 默认情况下，该距离即为当前对象在与参照物{@link #getVerticalDistance()}相同的情况下条，
	 * 该对象被扫描到的顺序。
	 *
	 * @return 合成注解与根对象的水平距离
	 */
	int getHorizontalDistance();

	// ====================== selector  ======================

	/**
	 * {@link Hierarchical}选择器，用于根据一定的规则从两个{@link Hierarchical}实现类中选择并返回一个最合适的对象
	 */
	@FunctionalInterface
	interface Selector {

		/**
		 * 返回距离根对象更近的对象，当距离一样时优先返回旧对象
		 */
		Selector NEAREST_AND_OLDEST_PRIORITY = new NearestAndOldestPrioritySelector();

		/**
		 * 返回距离根对象更近的对象，当距离一样时优先返回新对象
		 */
		Selector NEAREST_AND_NEWEST_PRIORITY = new NearestAndNewestPrioritySelector();

		/**
		 * 返回距离根对象更远的对象，当距离一样时优先返回旧对象
		 */
		Selector FARTHEST_AND_OLDEST_PRIORITY = new FarthestAndOldestPrioritySelector();

		/**
		 * 返回距离根对象更远的对象，当距离一样时优先返回新对象
		 */
		Selector FARTHEST_AND_NEWEST_PRIORITY = new FarthestAndNewestPrioritySelector();

		/**
		 * 比较两个被合成的对象，选择其中的一个并返回
		 *
		 * @param <T>           复合注解类型
		 * @param prev 上一对象，该参数不允许为空
		 * @param next 下一对象，该参数不允许为空
		 * @return 对象
		 */
		<T extends Hierarchical> T choose(T prev, T next);

		/**
		 * 返回距离根对象更近的注解，当距离一样时优先返回旧注解
		 */
		class NearestAndOldestPrioritySelector implements Selector {
			@Override
			public <T extends Hierarchical> T choose(T oldAnnotation, T newAnnotation) {
				return newAnnotation.getVerticalDistance() < oldAnnotation.getVerticalDistance() ? newAnnotation : oldAnnotation;
			}
		}

		/**
		 * 返回距离根对象更近的注解，当距离一样时优先返回新注解
		 */
		class NearestAndNewestPrioritySelector implements Selector {
			@Override
			public <T extends Hierarchical> T choose(T oldAnnotation, T newAnnotation) {
				return newAnnotation.getVerticalDistance() <= oldAnnotation.getVerticalDistance() ? newAnnotation : oldAnnotation;
			}
		}

		/**
		 * 返回距离根对象更远的注解，当距离一样时优先返回旧注解
		 */
		class FarthestAndOldestPrioritySelector implements Selector {
			@Override
			public <T extends Hierarchical> T choose(T oldAnnotation, T newAnnotation) {
				return newAnnotation.getVerticalDistance() > oldAnnotation.getVerticalDistance() ? newAnnotation : oldAnnotation;
			}
		}

		/**
		 * 返回距离根对象更远的注解，当距离一样时优先返回新注解
		 */
		class FarthestAndNewestPrioritySelector implements Selector {
			@Override
			public <T extends Hierarchical> T choose(T oldAnnotation, T newAnnotation) {
				return newAnnotation.getVerticalDistance() >= oldAnnotation.getVerticalDistance() ? newAnnotation : oldAnnotation;
			}
		}

	}

}
