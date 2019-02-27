package cn.hutool.core.comparator;

public class CompareUtil {
	
	/**
	 * {@code null}安全的对象比较，{@code null}对象排在末尾
	 * 
	 * @param <T> 被比较对象类型
	 * @param c1 对象1，可以为{@code null}
	 * @param c2 对象2，可以为{@code null}
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
		return compare(c1, c2, false);
	}

	/**
	 * {@code null}安全的对象比较
	 * 
	 * @param <T> 被比较对象类型（必须实现Comparable接口）
	 * @param c1 对象1，可以为{@code null}
	 * @param c2 对象2，可以为{@code null}
	 * @param isNullGreater 当被比较对象为null时是否排在前面
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean isNullGreater) {
		if (c1 == c2) {
			return 0;
		} else if (c1 == null) {
			return isNullGreater ? 1 : -1;
		} else if (c2 == null) {
			return isNullGreater ? -1 : 1;
		}
		return c1.compareTo(c2);
	}
	
	/**
	 * 自然比较两个对象的大小，比较规则如下：
	 * 
	 * <pre>
	 * 1、如果实现Comparable调用compareTo比较
	 * 2、o1.equals(o2)返回0
	 * 3、比较hashCode值
	 * 4、比较toString值
	 * </pre>
	 * 
	 * @param o1 对象1
	 * @param o2 对象2
	 * @param isNullGreater null值是否做为最大值
	 * @return 比较结果，如果o1 &lt; o2，返回数小于0，o1==o2返回0，o1 &gt; o2 大于0
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> int compare(T o1, T o2, boolean isNullGreater) {
		if (o1 == o2) {
			return 0;
		} else if (null == o1) {// null 排在后面
			return isNullGreater ? 1 : -1;
		} else if (null == o2) {
			return isNullGreater ? -1 : 1;
		}
		
		if(o1 instanceof Comparable && o2 instanceof Comparable) {
			//如果bean可比较，直接比较bean
			return ((Comparable)o1).compareTo(o2);
		}
		
		if(o1.equals(o2)) {
			return 0;
		}
		
		int result = Integer.compare(o1.hashCode(), o2.hashCode());
		if(0 == result) {
			result = compare(o1.toString(), o2.toString());
		}
		
		return result;
	}
}
