package cn.hutool.core.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;

import cn.hutool.core.util.ArrayUtil;

/** 
 * 用于构建 {@link java.lang.Comparable#compareTo(Object)} 方法的辅助工具
 *
 * <p>
 * 在Bean对象中，所有相关字段都参与比对，继承的字段不参与。使用方法如下：
 *
 * <pre>
 * public class MyClass {
 *   String field1;
 *   int field2;
 *   boolean field3;
 *
 *   ...
 *
 *   public int compareTo(Object o) {
 *     MyClass myClass = (MyClass) o;
 *     return new CompareToBuilder()
 *       .appendSuper(super.compareTo(o)
 *       .append(this.field1, myClass.field1)
 *       .append(this.field2, myClass.field2)
 *       .append(this.field3, myClass.field3)
 *       .toComparison();
 *   }
 * }
 * </pre>
 * 
 * 字段值按照顺序比较，如果某个字段返回非0结果，比较终止，使用{@code toComparison()}返回结果，后续比较忽略。
 *
 * <p>
 * 也可以使用{@link #reflectionCompare(Object, Object) reflectionCompare} 方法通过反射比较字段，使用方法如下：
 *
 * <pre>
 * public int compareTo(Object o) {
 *   return CompareToBuilder.reflectionCompare(this, o);
 * }
 * </pre>
 *
 *TODO 待整理
 * 来自于Apache-Commons-Lang3
 * @author looly，Apache-Commons
 * @since 4.2.2
 */
public class CompareToBuilder implements Builder<Integer> {
    
	/** 当前比较状态 */
    private int comparison;

    /**
     * 构造，构造后调用append方法增加比较项，然后调用{@link #toComparison()}获取结果
     */
    public CompareToBuilder() {
        super();
        comparison = 0;
    }

    //-----------------------------------------------------------------------
    /** 
     * 通过反射比较两个Bean对象，对象字段可以为private。比较规则如下：
     *
     * <ul>
     * <li>static字段不比较</li>
     * <li>Transient字段不参与比较</li>
     * <li>父类字段参与比较</li>
     * </ul>
     *
     *<p>
     *如果被比较的两个对象都为<code>null</code>，被认为相同。
     *
     * @param lhs  第一个对象
     * @param rhs  第二个对象
     * @return a negative integer, zero, or a positive integer as <code>lhs</code>
     *  is less than, equal to, or greater than <code>rhs</code>
     * @throws NullPointerException  if either (but not both) parameters are
     *  <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     */
    public static int reflectionCompare(final Object lhs, final Object rhs) {
        return reflectionCompare(lhs, rhs, false, null);
    }

    /**
     * <p>Compares two <code>Object</code>s via reflection.</p>
     *
     * <p>Fields can be private, thus <code>AccessibleObject.setAccessible</code>
     * is used to bypass normal access control checks. This will fail under a 
     * security manager unless the appropriate permissions are set.</p>
     *
     * <ul>
     * <li>Static fields will not be compared</li>
     * <li>If <code>compareTransients</code> is <code>true</code>,
     *     compares transient members.  Otherwise ignores them, as they
     *     are likely derived fields.</li>
     * <li>Superclass fields will be compared</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * they are considered equal.</p>
     *
     * @param lhs  left-hand object
     * @param rhs  right-hand object
     * @param compareTransients  whether to compare transient fields
     * @return a negative integer, zero, or a positive integer as <code>lhs</code>
     *  is less than, equal to, or greater than <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  (but not both) is <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     */
    public static int reflectionCompare(final Object lhs, final Object rhs, final boolean compareTransients) {
        return reflectionCompare(lhs, rhs, compareTransients, null);
    }

    /**
     * <p>Compares two <code>Object</code>s via reflection.</p>
     *
     * <p>Fields can be private, thus <code>AccessibleObject.setAccessible</code>
     * is used to bypass normal access control checks. This will fail under a 
     * security manager unless the appropriate permissions are set.</p>
     *
     * <ul>
     * <li>Static fields will not be compared</li>
     * <li>If <code>compareTransients</code> is <code>true</code>,
     *     compares transient members.  Otherwise ignores them, as they
     *     are likely derived fields.</li>
     * <li>Superclass fields will be compared</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * they are considered equal.</p>
     *
     * @param lhs  left-hand object
     * @param rhs  right-hand object
     * @param excludeFields  Collection of String fields to exclude
     * @return a negative integer, zero, or a positive integer as <code>lhs</code>
     *  is less than, equal to, or greater than <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  (but not both) is <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     * @since 2.2
     */
    public static int reflectionCompare(final Object lhs, final Object rhs, final Collection<String> excludeFields) {
        return reflectionCompare(lhs, rhs, ArrayUtil.toArray(excludeFields, String.class));
    }

    /**
     * <p>Compares two <code>Object</code>s via reflection.</p>
     *
     * <p>Fields can be private, thus <code>AccessibleObject.setAccessible</code>
     * is used to bypass normal access control checks. This will fail under a 
     * security manager unless the appropriate permissions are set.</p>
     *
     * <ul>
     * <li>Static fields will not be compared</li>
     * <li>If <code>compareTransients</code> is <code>true</code>,
     *     compares transient members.  Otherwise ignores them, as they
     *     are likely derived fields.</li>
     * <li>Superclass fields will be compared</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * they are considered equal.</p>
     *
     * @param lhs  left-hand object
     * @param rhs  right-hand object
     * @param excludeFields  array of fields to exclude
     * @return a negative integer, zero, or a positive integer as <code>lhs</code>
     *  is less than, equal to, or greater than <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  (but not both) is <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     * @since 2.2
     */
    public static int reflectionCompare(final Object lhs, final Object rhs, final String... excludeFields) {
        return reflectionCompare(lhs, rhs, false, null, excludeFields);
    }

    /**
     * <p>Compares two <code>Object</code>s via reflection.</p>
     *
     * <p>Fields can be private, thus <code>AccessibleObject.setAccessible</code>
     * is used to bypass normal access control checks. This will fail under a 
     * security manager unless the appropriate permissions are set.</p>
     *
     * <ul>
     * <li>Static fields will not be compared</li>
     * <li>If the <code>compareTransients</code> is <code>true</code>,
     *     compares transient members.  Otherwise ignores them, as they
     *     are likely derived fields.</li>
     * <li>Compares superclass fields up to and including <code>reflectUpToClass</code>.
     *     If <code>reflectUpToClass</code> is <code>null</code>, compares all superclass fields.</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * they are considered equal.</p>
     *
     * @param lhs  left-hand object
     * @param rhs  right-hand object
     * @param compareTransients  whether to compare transient fields
     * @param reflectUpToClass  last superclass for which fields are compared
     * @param excludeFields  fields to exclude
     * @return a negative integer, zero, or a positive integer as <code>lhs</code>
     *  is less than, equal to, or greater than <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  (but not both) is <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     * @since 2.2 (2.0 as <code>reflectionCompare(Object, Object, boolean, Class)</code>)
     */
    public static int reflectionCompare(
        final Object lhs, 
        final Object rhs, 
        final boolean compareTransients, 
        final Class<?> reflectUpToClass, 
        final String... excludeFields) {

        if (lhs == rhs) {
            return 0;
        }
        if (lhs == null || rhs == null) {
            throw new NullPointerException();
        }
        Class<?> lhsClazz = lhs.getClass();
        if (!lhsClazz.isInstance(rhs)) {
            throw new ClassCastException();
        }
        final CompareToBuilder compareToBuilder = new CompareToBuilder();
        reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
        while (lhsClazz.getSuperclass() != null && lhsClazz != reflectUpToClass) {
            lhsClazz = lhsClazz.getSuperclass();
            reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
        }
        return compareToBuilder.toComparison();
    }

    /**
     * <p>Appends to <code>builder</code> the comparison of <code>lhs</code>
     * to <code>rhs</code> using the fields defined in <code>clazz</code>.</p>
     * 
     * @param lhs  left-hand object
     * @param rhs  right-hand object
     * @param clazz  <code>Class</code> that defines fields to be compared
     * @param builder  <code>CompareToBuilder</code> to append to
     * @param useTransients  whether to compare transient fields
     * @param excludeFields  fields to exclude
     */
    private static void reflectionAppend(
        final Object lhs,
        final Object rhs,
        final Class<?> clazz,
        final CompareToBuilder builder,
        final boolean useTransients,
        final String[] excludeFields) {
        
        final Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (int i = 0; i < fields.length && builder.comparison == 0; i++) {
            final Field f = fields[i];
            if (false == ArrayUtil.contains(excludeFields, f.getName())
                && (f.getName().indexOf('$') == -1)
                && (useTransients || !Modifier.isTransient(f.getModifiers()))
                && (!Modifier.isStatic(f.getModifiers()))) {
                try {
                    builder.append(f.get(lhs), f.get(rhs));
                } catch (final IllegalAccessException e) {
                    // This can't happen. Would get a Security exception instead.
                    // Throw a runtime exception in case the impossible happens.
                    throw new InternalError("Unexpected IllegalAccessException");
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Appends to the <code>builder</code> the <code>compareTo(Object)</code>
     * result of the superclass.</p>
     *
     * @param superCompareTo  result of calling <code>super.compareTo(Object)</code>
     * @return this - used to chain append calls
     * @since 2.0
     */
    public CompareToBuilder appendSuper(final int superCompareTo) {
        if (comparison != 0) {
            return this;
        }
        comparison = superCompareTo;
        return this;
    }
    
    //-----------------------------------------------------------------------
    /**
     * <p>Appends to the <code>builder</code> the comparison of
     * two <code>Object</code>s.</p>
     *
     * <ol>
     * <li>Check if <code>lhs == rhs</code></li>
     * <li>Check if either <code>lhs</code> or <code>rhs</code> is <code>null</code>,
     *     a <code>null</code> object is less than a non-<code>null</code> object</li>
     * <li>Check the object contents</li>
     * </ol>
     * 
     * <p><code>lhs</code> must either be an array or implement {@link Comparable}.</p>
     *
     * @param lhs  left-hand object
     * @param rhs  right-hand object
     * @return this - used to chain append calls
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     */
    public CompareToBuilder append(final Object lhs, final Object rhs) {
        return append(lhs, rhs, null);
    }

    /**
     * <p>Appends to the <code>builder</code> the comparison of
     * two <code>Object</code>s.</p>
     *
     * <ol>
     * <li>Check if <code>lhs == rhs</code></li>
     * <li>Check if either <code>lhs</code> or <code>rhs</code> is <code>null</code>,
     *     a <code>null</code> object is less than a non-<code>null</code> object</li>
     * <li>Check the object contents</li>
     * </ol>
     *
     * <p>If <code>lhs</code> is an array, array comparison methods will be used.
     * Otherwise <code>comparator</code> will be used to compare the objects.
     * If <code>comparator</code> is <code>null</code>, <code>lhs</code> must
     * implement {@link Comparable} instead.</p>
     *
     * @param lhs  left-hand object
     * @param rhs  right-hand object
     * @param comparator  <code>Comparator</code> used to compare the objects,
     *  <code>null</code> means treat lhs as <code>Comparable</code>
     * @return this - used to chain append calls
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     * @since 2.0
     */
    public CompareToBuilder append(final Object lhs, final Object rhs, final Comparator<?> comparator) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.getClass().isArray()) {
            // switch on type of array, to dispatch to the correct handler
            // handles multi dimensional arrays
            // throws a ClassCastException if rhs is not the correct array type
            if (lhs instanceof long[]) {
                append((long[]) lhs, (long[]) rhs);
            } else if (lhs instanceof int[]) {
                append((int[]) lhs, (int[]) rhs);
            } else if (lhs instanceof short[]) {
                append((short[]) lhs, (short[]) rhs);
            } else if (lhs instanceof char[]) {
                append((char[]) lhs, (char[]) rhs);
            } else if (lhs instanceof byte[]) {
                append((byte[]) lhs, (byte[]) rhs);
            } else if (lhs instanceof double[]) {
                append((double[]) lhs, (double[]) rhs);
            } else if (lhs instanceof float[]) {
                append((float[]) lhs, (float[]) rhs);
            } else if (lhs instanceof boolean[]) {
                append((boolean[]) lhs, (boolean[]) rhs);
            } else {
                // not an array of primitives
                // throws a ClassCastException if rhs is not an array
                append((Object[]) lhs, (Object[]) rhs, comparator);
            }
        } else {
            // the simple case, not an array, just test the element
            if (comparator == null) {
                @SuppressWarnings("unchecked") // assume this can be done; if not throw CCE as per Javadoc
                final Comparable<Object> comparable = (Comparable<Object>) lhs;
                comparison = comparable.compareTo(rhs);
            } else {
                @SuppressWarnings("unchecked") // assume this can be done; if not throw CCE as per Javadoc
                final Comparator<Object> comparator2 = (Comparator<Object>) comparator;
                comparison = comparator2.compare(lhs, rhs);
            }
        }
        return this;
    }

    //-------------------------------------------------------------------------
    /**
     * Appends to the <code>builder</code> the comparison of
     * two <code>long</code>s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final long lhs, final long rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
        return this;
    }

    /**
     * Appends to the <code>builder</code> the comparison of
     * two <code>int</code>s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final int lhs, final int rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
        return this;
    }

    /**
     * Appends to the <code>builder</code> the comparison of
     * two <code>short</code>s.
     * 
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final short lhs, final short rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
        return this;
    }

    /**
     * Appends to the <code>builder</code> the comparison of
     * two <code>char</code>s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final char lhs, final char rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
        return this;
    }

    /**
     * Appends to the <code>builder</code> the comparison of
     * two <code>byte</code>s.
     * 
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final byte lhs, final byte rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the comparison of
     * two <code>double</code>s.</p>
     *
     * <p>This handles NaNs, Infinities, and <code>-0.0</code>.</p>
     *
     * <p>It is compatible with the hash code generated by
     * <code>HashCodeBuilder</code>.</p>
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final double lhs, final double rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Double.compare(lhs, rhs);
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the comparison of
     * two <code>float</code>s.</p>
     *
     * <p>This handles NaNs, Infinities, and <code>-0.0</code>.</p>
     *
     * <p>It is compatible with the hash code generated by
     * <code>HashCodeBuilder</code>.</p>
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final float lhs, final float rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Float.compare(lhs, rhs);
        return this;
    }

    /**
     * Appends to the <code>builder</code> the comparison of
     * two <code>booleans</code>s.
     *
     * @param lhs  left-hand value
     * @param rhs  right-hand value
     * @return this - used to chain append calls
      */
    public CompareToBuilder append(final boolean lhs, final boolean rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == false) {
            comparison = -1;
        } else {
            comparison = +1;
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>Object</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a short length array is less than a long length array</li>
     *  <li>Check array contents element by element using {@link #append(Object, Object, Comparator)}</li>
     * </ol>
     *
     * <p>This method will also will be called for the top level of multi-dimensional,
     * ragged, and multi-typed arrays.</p>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     */
    public CompareToBuilder append(final Object[] lhs, final Object[] rhs) {
        return append(lhs, rhs, null);
    }
    
    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>Object</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a short length array is less than a long length array</li>
     *  <li>Check array contents element by element using {@link #append(Object, Object, Comparator)}</li>
     * </ol>
     *
     * <p>This method will also will be called for the top level of multi-dimensional,
     * ragged, and multi-typed arrays.</p>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @param comparator  <code>Comparator</code> to use to compare the array elements,
     *  <code>null</code> means to treat <code>lhs</code> elements as <code>Comparable</code>.
     * @return this - used to chain append calls
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  with <code>lhs</code>
     * @since 2.0
     */
    public CompareToBuilder append(final Object[] lhs, final Object[] rhs, final Comparator<?> comparator) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i], comparator);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>long</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(long, long)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final long[] lhs, final long[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>int</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(int, int)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final int[] lhs, final int[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>short</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(short, short)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final short[] lhs, final short[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>char</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(char, char)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final char[] lhs, final char[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>byte</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(byte, byte)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final byte[] lhs, final byte[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>double</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(double, double)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final double[] lhs, final double[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>float</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(float, float)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final float[] lhs, final float[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>Appends to the <code>builder</code> the deep comparison of
     * two <code>boolean</code> arrays.</p>
     *
     * <ol>
     *  <li>Check if arrays are the same using <code>==</code></li>
     *  <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
     *  <li>Check array length, a shorter length array is less than a longer length array</li>
     *  <li>Check array contents element by element using {@link #append(boolean, boolean)}</li>
     * </ol>
     *
     * @param lhs  left-hand array
     * @param rhs  right-hand array
     * @return this - used to chain append calls
     */
    public CompareToBuilder append(final boolean[] lhs, final boolean[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a negative integer, a positive integer, or zero as
     * the <code>builder</code> has judged the "left-hand" side
     * as less than, greater than, or equal to the "right-hand"
     * side.
     * 
     * @return final comparison result
     * @see #build()
     */
    public int toComparison() {
        return comparison;
    }

    /**
     * Returns a negative Integer, a positive Integer, or zero as
     * the <code>builder</code> has judged the "left-hand" side
     * as less than, greater than, or equal to the "right-hand"
     * side.
     * 
     * @return final comparison result as an Integer
     * @see #toComparison()
     * @since 3.0
     */
    @Override
    public Integer build() {
        return toComparison();
    }
}

