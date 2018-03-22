package com.xiaoleilu.hutool.util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import com.xiaoleilu.hutool.bean.BeanUtil;
import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.FastByteArrayOutputStream;
import com.xiaoleilu.hutool.io.IoUtil;

/**
 * 一些通用的函数
 * 
 * @author Looly
 *
 */
public class ObjectUtil {

	/**
	 * 比较两个对象是否相等。<br>
	 * 相同的条件有两个，满足其一即可：<br>
	 * <ol>
	 * <li>obj1 == null &amp;&amp; obj2 == null</li>
	 * <li>obj1.equals(obj2)</li>
	 * </ol>
	 * 1. obj1 == null &amp;&amp; obj2 == null 2. obj1.equals(obj2)
	 * 
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 */
	public static boolean equal(Object obj1, Object obj2) {
		return (obj1 != null) ? (obj1.equals(obj2)) : (obj2 == null);
	}

	/**
	 * 比较两个对象是否不相等。<br>
	 * 
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否不等
	 * @since 3.0.7
	 */
	public static boolean notEqual(Object obj1, Object obj2) {
		return false == equal(obj1, obj2);
	}

	/**
	 * 计算对象长度，如果是字符串调用其length函数，集合类调用其size函数，数组调用其length属性，其他可遍历对象遍历计算长度<br>
	 * 支持的类型包括：
	 * <ul>
	 * <li>CharSequence</li>
	 * <li>Map</li>
	 * <li>Iterator</li>
	 * <li>Enumeration</li>
	 * <li>Array</li>
	 * </ul>
	 * 
	 * @param obj 被计算长度的对象
	 * @return 长度
	 */
	public static int length(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length();
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).size();
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).size();
		}

		int count;
		if (obj instanceof Iterator) {
			Iterator<?> iter = (Iterator<?>) obj;
			count = 0;
			while (iter.hasNext()) {
				count++;
				iter.next();
			}
			return count;
		}
		if (obj instanceof Enumeration) {
			Enumeration<?> enumeration = (Enumeration<?>) obj;
			count = 0;
			while (enumeration.hasMoreElements()) {
				count++;
				enumeration.nextElement();
			}
			return count;
		}
		if (obj.getClass().isArray() == true) {
			return Array.getLength(obj);
		}
		return -1;
	}

	/**
	 * 对象中是否包含元素<br>
	 * 支持的对象类型包括：
	 * <ul>
	 * <li>String</li>
	 * <li>Collection</li>
	 * <li>Map</li>
	 * <li>Iterator</li>
	 * <li>Enumeration</li>
	 * <li>Array</li>
	 * </ul>
	 * 
	 * @param obj 对象
	 * @param element 元素
	 * @return 是否包含
	 */
	public static boolean contains(Object obj, Object element) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			if (element == null) {
				return false;
			}
			return ((String) obj).contains(element.toString());
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).contains(element);
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).values().contains(element);
		}

		if (obj instanceof Iterator) {
			Iterator<?> iter = (Iterator<?>) obj;
			while (iter.hasNext()) {
				Object o = iter.next();
				if (equal(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj instanceof Enumeration) {
			Enumeration<?> enumeration = (Enumeration<?>) obj;
			while (enumeration.hasMoreElements()) {
				Object o = enumeration.nextElement();
				if (equal(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj.getClass().isArray() == true) {
			int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				Object o = Array.get(obj, i);
				if (equal(o, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查对象是否为null<br>
	 * 判断标准为：
	 * <pre>
	 * 1. == null
	 * 2. equals(null)
	 * </pre>
	 * 
	 * @param obj 对象
	 * @return 是否为null
	 */
	public static boolean isNull(Object obj) {
		return null == obj || obj.equals(null);
	}

	/**
	 * 检查对象是否不为null
	 * 
	 * @param obj 对象
	 * @return 是否为null
	 */
	public static boolean isNotNull(Object obj) {
		return null != obj && false == obj.equals(null);
	}

	/**
	 * 如果给定对象为{@code null}返回默认值
	 *
	 * <pre>
	 * ObjectUtil.defaultIfNull(null, null)      = null
	 * ObjectUtil.defaultIfNull(null, "")        = ""
	 * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
	 * ObjectUtil.defaultIfNull("abc", *)        = "abc"
	 * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
	 * </pre>
	 *
	 * @param <T> 对象类型
	 * @param object 被检查对象，可能为{@code null}
	 * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
	 * @return 被检查对象为{@code null}返回默认值，否则返回原值
	 * @since 3.0.7
	 */
	public static <T> T defaultIfNull(final T object, final T defaultValue) {
		return (null != object) ? object : defaultValue;
	}

	/**
	 * 克隆对象<br>
	 * 如果对象实现Cloneable接口，调用其clone方法<br>
	 * 如果实现Serializable接口，执行深度克隆<br>
	 * 否则返回<code>null</code>
	 * 
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 */
	public static <T> T clone(T obj) {
		T result = ArrayUtil.clone(obj);
		if (null == result) {
			if (obj instanceof Cloneable) {
				result = ReflectUtil.invoke(obj, "clone", new Object[] {});
			} else {
				result = cloneByStream(obj);
			}
		}
		return result;
	}

	/**
	 * 返回克隆后的对象，如果克隆失败，返回原对象
	 * 
	 * @param <T> 对象类型
	 * @param obj 对象
	 * @return 克隆后或原对象
	 */
	public static <T> T cloneIfPossible(final T obj) {
		T clone = null;
		try {
			clone = clone(obj);
		} catch (Exception e) {
			// pass
		}
		return clone == null ? obj : clone;
	}

	/**
	 * 序列化后拷贝流的方式克隆<br>
	 * 对象必须实现Serializable接口
	 * 
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 * @throws UtilException IO异常和ClassNotFoundException封装
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneByStream(T obj) {
		if (null == obj || false == (obj instanceof Serializable)) {
			return null;
		}
		final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(obj);
			out.flush();
			final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
			return (T) in.readObject();
		} catch (Exception e) {
			throw new UtilException(e);
		} finally {
			IoUtil.close(out);
		}
	}

	/**
	 * 序列化<br>
	 * 对象必须实现Serializable接口
	 * 
	 * @param <T> 对象类型
	 * @param obj 要被序列化的对象
	 * @return 序列化后的字节码
	 */
	public static <T> byte[] serialize(T obj) {
		if (null == obj || false == (obj instanceof Serializable)) {
			return null;
		}

		FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(byteOut);
			oos.writeObject(obj);
			oos.flush();
		} catch (Exception e) {
			throw new UtilException(e);
		} finally {
			IoUtil.close(oos);
		}
		return byteOut.toByteArray();
	}

	/**
	 * 反序列化<br>
	 * 对象必须实现Serializable接口
	 * 
	 * @param <T> 对象类型
	 * @param bytes 反序列化的字节码
	 * @return 反序列化后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unserialize(byte[] bytes) {
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return (T) ois.readObject();
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 是否为基本类型，包括包装类型和非包装类型
	 * 
	 * @see ClassUtil#isBasicType(Class)
	 * @param object 被检查对象
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Object object) {
		return ClassUtil.isBasicType(object.getClass());
	}

	/**
	 * 检查是否为有效的数字<br>
	 * 检查Double和Float是否为无限大，或者Not a Number<br>
	 * 非数字类型和Null将返回true
	 * 
	 * @param obj 被检查类型
	 * @return 检查结果，非数字类型和Null将返回true
	 */
	public static boolean isValidIfNumber(Object obj) {
		if (obj != null && obj instanceof Number) {
			if (obj instanceof Double) {
				if (((Double) obj).isInfinite() || ((Double) obj).isNaN()) {
					return false;
				}
			} else if (obj instanceof Float) {
				if (((Float) obj).isInfinite() || ((Float) obj).isNaN()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * {@code null}安全的对象比较，{@code null}对象排在末尾
	 * 
	 * @param <T> 被比较对象类型
	 * @param c1 对象1，可以为{@code null}
	 * @param c2 对象2，可以为{@code null}
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @since 3.0.7
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
		return compare(c1, c2, false);
	}

	/**
	 * {@code null}安全的对象比较
	 * 
	 * @param <T> 被比较对象类型
	 * @param c1 对象1，可以为{@code null}
	 * @param c2 对象2，可以为{@code null}
	 * @param nullGreater 当被比较对象为null时是否排在前面
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @since 3.0.7
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
		if (c1 == c2) {
			return 0;
		} else if (c1 == null) {
			return nullGreater ? 1 : -1;
		} else if (c2 == null) {
			return nullGreater ? -1 : 1;
		}
		return c1.compareTo(c2);
	}

	/**
	 * 获得给定类的第一个泛型参数
	 * 
	 * @param obj 被检查的对象
	 * @return {@link Class}
	 * @since 3.0.8
	 */
	public static Class<?> getTypeArgument(Object obj) {
		return getTypeArgument(obj, 0);
	}

	/**
	 * 获得给定类的第一个泛型参数
	 * 
	 * @param obj 被检查的对象
	 * @param index 泛型类型的索引号，既第几个泛型类型
	 * @return {@link Class}
	 * @since 3.0.8
	 */
	public static Class<?> getTypeArgument(Object obj, int index) {
		return ClassUtil.getTypeArgument(obj.getClass(), index);
	}

	/**
	 * 将Object转为String
	 * 
	 * @param obj Bean对象
	 * @return Bean所有字段转为Map后的字符串
	 * @since 3.2.0
	 */
	public static String toString(Object obj) {
		if(obj instanceof Map) {
			return ((Map<?, ?>)obj).toString();
		}
		
		return BeanUtil.beanToMap(obj).toString();
	}
}
