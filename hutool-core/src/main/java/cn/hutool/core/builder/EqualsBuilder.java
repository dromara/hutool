package cn.hutool.core.builder;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ArrayUtil;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>{@link Object#equals(Object)} 方法的构建器</p>
 *
 * <p>两个对象equals必须保证hashCode值相等，hashCode值相等不能保证一定equals</p>
 *
 * <p>使用方法如下：</p>
 * <pre>
 * public boolean equals(Object obj) {
 *   if (obj == null) { return false; }
 *   if (obj == this) { return true; }
 *   if (obj.getClass() != getClass()) {
 *     return false;
 *   }
 *   MyClass rhs = (MyClass) obj;
 *   return new EqualsBuilder()
 *                 .appendSuper(super.equals(obj))
 *                 .append(field1, rhs.field1)
 *                 .append(field2, rhs.field2)
 *                 .append(field3, rhs.field3)
 *                 .isEquals();
 *  }
 * </pre>
 *
 * <p> 我们也可以通过反射判断所有字段是否equals：</p>
 * <pre>
 * public boolean equals(Object obj) {
 *   return EqualsBuilder.reflectionEquals(this, obj);
 * }
 * </pre>
 *
 * 来自Apache Commons Lang改造
 */
public class EqualsBuilder implements Builder<Boolean> {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * A registry of objects used by reflection methods to detect cyclical object references and avoid infinite loops.
	 * </p>
	 */
	private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal<>();

	/**
	 * <p>
	 * Returns the registry of object pairs being traversed by the reflection
	 * methods in the current thread.
	 * </p>
	 *
	 * @return Set the registry of objects being traversed
	 * @since 3.0
	 */
	static Set<Pair<IDKey, IDKey>> getRegistry() {
		return REGISTRY.get();
	}

	/**
	 * <p>
	 * Converters value pair into a register pair.
	 * </p>
	 *
	 * @param lhs <code>this</code> object
	 * @param rhs the other object
	 * @return the pair
	 */
	static Pair<IDKey, IDKey> getRegisterPair(final Object lhs, final Object rhs) {
		final IDKey left = new IDKey(lhs);
		final IDKey right = new IDKey(rhs);
		return new Pair<>(left, right);
	}

	/**
	 * <p>
	 * Returns <code>true</code> if the registry contains the given object pair.
	 * Used by the reflection methods to avoid infinite loops.
	 * Objects might be swapped therefore a check is needed if the object pair
	 * is registered in given or swapped order.
	 * </p>
	 *
	 * @param lhs <code>this</code> object to lookup in registry
	 * @param rhs the other object to lookup on registry
	 * @return boolean <code>true</code> if the registry contains the given object.
	 * @since 3.0
	 */
	static boolean isRegistered(final Object lhs, final Object rhs) {
		final Set<Pair<IDKey, IDKey>> registry = getRegistry();
		final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
		final Pair<IDKey, IDKey> swappedPair = new Pair<>(pair.getKey(), pair.getValue());

		return registry != null
				&& (registry.contains(pair) || registry.contains(swappedPair));
	}

	/**
	 * <p>
	 * Registers the given object pair.
	 * Used by the reflection methods to avoid infinite loops.
	 * </p>
	 *
	 * @param lhs <code>this</code> object to register
	 * @param rhs the other object to register
	 */
	static void register(final Object lhs, final Object rhs) {
		synchronized (EqualsBuilder.class) {
			if (getRegistry() == null) {
				REGISTRY.set(new HashSet<>());
			}
		}

		final Set<Pair<IDKey, IDKey>> registry = getRegistry();
		final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
		registry.add(pair);
	}

	/**
	 * <p>
	 * Unregisters the given object pair.
	 * </p>
	 *
	 * <p>
	 * Used by the reflection methods to avoid infinite loops.
	 *
	 * @param lhs <code>this</code> object to unregister
	 * @param rhs the other object to unregister
	 * @since 3.0
	 */
	static void unregister(final Object lhs, final Object rhs) {
		Set<Pair<IDKey, IDKey>> registry = getRegistry();
		if (registry != null) {
			final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
			registry.remove(pair);
			synchronized (EqualsBuilder.class) {
				//read again
				registry = getRegistry();
				if (registry != null && registry.isEmpty()) {
					REGISTRY.remove();
				}
			}
		}
	}

	/**
	 * 是否equals，此值随着构建会变更，默认true
	 */
	private boolean isEquals = true;

	/**
	 * 构造，初始状态值为true
	 */
	public EqualsBuilder() {
		// do nothing for now.
	}

	//-------------------------------------------------------------------------

	/**
	 * <p>反射检查两个对象是否equals，此方法检查对象及其父对象的属性（包括私有属性）是否equals</p>
	 *
	 * @param lhs           此对象
	 * @param rhs           另一个对象
	 * @param excludeFields 排除的字段集合，如果有不参与计算equals的字段加入此集合即可
	 * @return 两个对象是否equals，是返回<code>true</code>
	 */
	public static boolean reflectionEquals(final Object lhs, final Object rhs, final Collection<String> excludeFields) {
		return reflectionEquals(lhs, rhs, ArrayUtil.toArray(excludeFields, String.class));
	}

	/**
	 * <p>反射检查两个对象是否equals，此方法检查对象及其父对象的属性（包括私有属性）是否equals</p>
	 *
	 * @param lhs           此对象
	 * @param rhs           另一个对象
	 * @param excludeFields 排除的字段集合，如果有不参与计算equals的字段加入此集合即可
	 * @return 两个对象是否equals，是返回<code>true</code>
	 */
	public static boolean reflectionEquals(final Object lhs, final Object rhs, final String... excludeFields) {
		return reflectionEquals(lhs, rhs, false, null, excludeFields);
	}

	/**
	 * <p>This method uses reflection to determine if the two <code>Object</code>s
	 * are equal.</p>
	 *
	 * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
	 * fields. This means that it will throw a security exception if run under
	 * a security manager, if the permissions are not set up correctly. It is also
	 * not as efficient as testing explicitly. Non-primitive fields are compared using
	 * <code>equals()</code>.</p>
	 *
	 * <p>If the TestTransients parameter is set to <code>true</code>, transient
	 * members will be tested, otherwise they are ignored, as they are likely
	 * derived fields, and not part of the value of the <code>Object</code>.</p>
	 *
	 * <p>Static fields will not be tested. Superclass fields will be included.</p>
	 *
	 * @param lhs            <code>this</code> object
	 * @param rhs            the other object
	 * @param testTransients whether to include transient fields
	 * @return <code>true</code> if the two Objects have tested equals.
	 */
	public static boolean reflectionEquals(final Object lhs, final Object rhs, final boolean testTransients) {
		return reflectionEquals(lhs, rhs, testTransients, null);
	}

	/**
	 * <p>This method uses reflection to determine if the two <code>Object</code>s
	 * are equal.</p>
	 *
	 * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
	 * fields. This means that it will throw a security exception if run under
	 * a security manager, if the permissions are not set up correctly. It is also
	 * not as efficient as testing explicitly. Non-primitive fields are compared using
	 * <code>equals()</code>.</p>
	 *
	 * <p>If the testTransients parameter is set to <code>true</code>, transient
	 * members will be tested, otherwise they are ignored, as they are likely
	 * derived fields, and not part of the value of the <code>Object</code>.</p>
	 *
	 * <p>Static fields will not be included. Superclass fields will be appended
	 * up to and including the specified superclass. A null superclass is treated
	 * as java.lang.Object.</p>
	 *
	 * @param lhs              <code>this</code> object
	 * @param rhs              the other object
	 * @param testTransients   whether to include transient fields
	 * @param reflectUpToClass the superclass to reflect up to (inclusive),
	 *                         may be <code>null</code>
	 * @param excludeFields    array of field names to exclude from testing
	 * @return <code>true</code> if the two Objects have tested equals.
	 * @since 2.0
	 */
	public static boolean reflectionEquals(final Object lhs, final Object rhs, final boolean testTransients, final Class<?> reflectUpToClass,
	                                       final String... excludeFields) {
		if (lhs == rhs) {
			return true;
		}
		if (lhs == null || rhs == null) {
			return false;
		}
		// Find the leaf class since there may be transients in the leaf
		// class or in classes between the leaf and root.
		// If we are not testing transients or a subclass has no ivars,
		// then a subclass can test equals to a superclass.
		final Class<?> lhsClass = lhs.getClass();
		final Class<?> rhsClass = rhs.getClass();
		Class<?> testClass;
		if (lhsClass.isInstance(rhs)) {
			testClass = lhsClass;
			if (!rhsClass.isInstance(lhs)) {
				// rhsClass is a subclass of lhsClass
				testClass = rhsClass;
			}
		} else if (rhsClass.isInstance(lhs)) {
			testClass = rhsClass;
			if (!lhsClass.isInstance(rhs)) {
				// lhsClass is a subclass of rhsClass
				testClass = lhsClass;
			}
		} else {
			// The two classes are not related.
			return false;
		}
		final EqualsBuilder equalsBuilder = new EqualsBuilder();
		try {
			if (testClass.isArray()) {
				equalsBuilder.append(lhs, rhs);
			} else {
				reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
				while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
					testClass = testClass.getSuperclass();
					reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
				}
			}
		} catch (final IllegalArgumentException e) {
			// In this case, we tried to test a subclass vs. a superclass and
			// the subclass has ivars or the ivars are transient and
			// we are testing transients.
			// If a subclass has ivars that we are trying to test them, we get an
			// exception and we know that the objects are not equal.
			return false;
		}
		return equalsBuilder.isEquals();
	}

	/**
	 * <p>Appends the fields and values defined by the given object of the
	 * given Class.</p>
	 *
	 * @param lhs           the left hand object
	 * @param rhs           the right hand object
	 * @param clazz         the class to append details of
	 * @param builder       the builder to append to
	 * @param useTransients whether to test transient fields
	 * @param excludeFields array of field names to exclude from testing
	 */
	private static void reflectionAppend(
			final Object lhs,
			final Object rhs,
			final Class<?> clazz,
			final EqualsBuilder builder,
			final boolean useTransients,
			final String[] excludeFields) {

		if (isRegistered(lhs, rhs)) {
			return;
		}

		try {
			register(lhs, rhs);
			final Field[] fields = clazz.getDeclaredFields();
			AccessibleObject.setAccessible(fields, true);
			for (int i = 0; i < fields.length && builder.isEquals; i++) {
				final Field f = fields[i];
				if (false == ArrayUtil.contains(excludeFields, f.getName())
						&& (f.getName().indexOf('$') == -1)
						&& (useTransients || !Modifier.isTransient(f.getModifiers()))
						&& (!Modifier.isStatic(f.getModifiers()))) {
					try {
						builder.append(f.get(lhs), f.get(rhs));
					} catch (final IllegalAccessException e) {
						//this can't happen. Would get a Security exception instead
						//throw a runtime exception in case the impossible happens.
						throw new InternalError("Unexpected IllegalAccessException");
					}
				}
			}
		} finally {
			unregister(lhs, rhs);
		}
	}

	//-------------------------------------------------------------------------

	/**
	 * <p>Adds the result of <code>super.equals()</code> to this builder.</p>
	 *
	 * @param superEquals the result of calling <code>super.equals()</code>
	 * @return EqualsBuilder - used to chain calls.
	 * @since 2.0
	 */
	public EqualsBuilder appendSuper(final boolean superEquals) {
		if (isEquals == false) {
			return this;
		}
		isEquals = superEquals;
		return this;
	}

	//-------------------------------------------------------------------------

	/**
	 * <p>Test if two <code>Object</code>s are equal using their
	 * <code>equals</code> method.</p>
	 *
	 * @param lhs the left hand object
	 * @param rhs the right hand object
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final Object lhs, final Object rhs) {
		if (isEquals == false) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null || rhs == null) {
			this.setEquals(false);
			return this;
		}
		final Class<?> lhsClass = lhs.getClass();
		if (false == lhsClass.isArray()) {
			// The simple case, not an array, just test the element
			isEquals = lhs.equals(rhs);
		}

		// 判断数组的equals
		this.setEquals(ArrayUtil.equals(lhs, rhs));
		return this;
	}

	/**
	 * <p>
	 * Test if two <code>long</code> s are equal.
	 * </p>
	 *
	 * @param lhs the left hand <code>long</code>
	 * @param rhs the right hand <code>long</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final long lhs, final long rhs) {
		if (isEquals == false) {
			return this;
		}
		isEquals = (lhs == rhs);
		return this;
	}

	/**
	 * <p>Test if two <code>int</code>s are equal.</p>
	 *
	 * @param lhs the left hand <code>int</code>
	 * @param rhs the right hand <code>int</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final int lhs, final int rhs) {
		if (isEquals == false) {
			return this;
		}
		isEquals = (lhs == rhs);
		return this;
	}

	/**
	 * <p>Test if two <code>short</code>s are equal.</p>
	 *
	 * @param lhs the left hand <code>short</code>
	 * @param rhs the right hand <code>short</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final short lhs, final short rhs) {
		if (isEquals == false) {
			return this;
		}
		isEquals = (lhs == rhs);
		return this;
	}

	/**
	 * <p>Test if two <code>char</code>s are equal.</p>
	 *
	 * @param lhs the left hand <code>char</code>
	 * @param rhs the right hand <code>char</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final char lhs, final char rhs) {
		if (isEquals == false) {
			return this;
		}
		isEquals = (lhs == rhs);
		return this;
	}

	/**
	 * <p>Test if two <code>byte</code>s are equal.</p>
	 *
	 * @param lhs the left hand <code>byte</code>
	 * @param rhs the right hand <code>byte</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final byte lhs, final byte rhs) {
		if (isEquals == false) {
			return this;
		}
		isEquals = (lhs == rhs);
		return this;
	}

	/**
	 * <p>Test if two <code>double</code>s are equal by testing that the
	 * pattern of bits returned by <code>doubleToLong</code> are equal.</p>
	 *
	 * <p>This handles NaNs, Infinities, and <code>-0.0</code>.</p>
	 *
	 * <p>It is compatible with the hash code generated by
	 * <code>HashCodeBuilder</code>.</p>
	 *
	 * @param lhs the left hand <code>double</code>
	 * @param rhs the right hand <code>double</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final double lhs, final double rhs) {
		if (isEquals == false) {
			return this;
		}
		return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
	}

	/**
	 * <p>Test if two <code>float</code>s are equal byt testing that the
	 * pattern of bits returned by doubleToLong are equal.</p>
	 *
	 * <p>This handles NaNs, Infinities, and <code>-0.0</code>.</p>
	 *
	 * <p>It is compatible with the hash code generated by
	 * <code>HashCodeBuilder</code>.</p>
	 *
	 * @param lhs the left hand <code>float</code>
	 * @param rhs the right hand <code>float</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final float lhs, final float rhs) {
		if (isEquals == false) {
			return this;
		}
		return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
	}

	/**
	 * <p>Test if two <code>booleans</code>s are equal.</p>
	 *
	 * @param lhs the left hand <code>boolean</code>
	 * @param rhs the right hand <code>boolean</code>
	 * @return EqualsBuilder - used to chain calls.
	 */
	public EqualsBuilder append(final boolean lhs, final boolean rhs) {
		if (isEquals == false) {
			return this;
		}
		isEquals = (lhs == rhs);
		return this;
	}

	/**
	 * <p>Returns <code>true</code> if the fields that have been checked
	 * are all equal.</p>
	 *
	 * @return boolean
	 */
	public boolean isEquals() {
		return this.isEquals;
	}

	/**
	 * <p>Returns <code>true</code> if the fields that have been checked
	 * are all equal.</p>
	 *
	 * @return <code>true</code> if all of the fields that have been checked
	 * are equal, <code>false</code> otherwise.
	 * @since 3.0
	 */
	@Override
	public Boolean build() {
		return isEquals();
	}

	/**
	 * Sets the <code>isEquals</code> value.
	 *
	 * @param isEquals The value to set.
	 * @since 2.1
	 */
	protected void setEquals(boolean isEquals) {
		this.isEquals = isEquals;
	}

	/**
	 * Reset the EqualsBuilder so you can use the same object again
	 *
	 * @since 2.5
	 */
	public void reset() {
		this.isEquals = true;
	}
}
