package cn.hutool.core.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.math.NumberUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.text.split.SplitUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;

import java.io.Serializable;
import java.util.*;

/**
 * Bean路径表达式，用于获取多层嵌套Bean中的字段值或Bean对象<br>
 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
 * <ol>
 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
 * </ol>
 * <p>
 * 表达式栗子：
 *
 * <pre>
 * persion
 * persion.name
 * persons[3]
 * person.friends[5].name
 * ['person']['friends'][5]['name']
 * </pre>
 *
 * @author Looly
 * @since 4.0.6
 */
public class BeanPath implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 表达式边界符号数组
	 */
	private static final char[] EXP_CHARS = {CharUtil.DOT, CharUtil.BRACKET_START, CharUtil.BRACKET_END};

	private boolean isStartWith = false;
	protected List<String> patternParts;

	/**
	 * 解析Bean路径表达式为Bean模式<br>
	 * Bean表达式，用于获取多层嵌套Bean中的字段值或Bean对象<br>
	 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * ['person']['friends'][5]['name']
	 * </pre>
	 *
	 * @param expression 表达式
	 * @return BeanPath
	 */
	public static BeanPath of(final String expression) {
		return new BeanPath(expression);
	}

	/**
	 * 构造
	 *
	 * @param expression 表达式
	 */
	public BeanPath(final String expression) {
		init(expression);
	}

	/**
	 * 获取表达式解析后的分段列表
	 *
	 * @return 表达式分段列表
	 */
	public List<String> getPatternParts() {
		return this.patternParts;
	}

	/**
	 * 获取Bean中对应表达式的值
	 *
	 * @param bean Bean对象或Map或List等
	 * @return 值，如果对应值不存在，则返回null
	 */
	public Object get(final Object bean) {
		return get(this.patternParts, bean);
	}

	/**
	 * 设置表达式指定位置（或filed对应）的值<br>
	 * 若表达式指向一个List则设置其坐标对应位置的值，若指向Map则put对应key的值，Bean则设置字段的值<br>
	 * 注意：
	 *
	 * <pre>
	 * 1. 如果为List，如果下标不大于List长度，则替换原有值，否则追加值
	 * 2. 如果为数组，如果下标不大于数组长度，则替换原有值，否则追加值
	 * </pre>
	 *
	 * @param bean  Bean、Map或List
	 * @param value 值
	 */
	public void set(final Object bean, final Object value) {
		Objects.requireNonNull(bean);

		Object subBean = bean;
		Object previousBean = null;
		boolean isFirst = true;
		String patternPart;
		// 尝试找到倒数第二个子对象, 最终需要设置它的字段值
		final int length = patternParts.size() - 1;

		// 填充父字段缺失的对象
		for (int i = 0; i < length; i++) {
			patternPart = patternParts.get(i);
			// 保存当前操作的bean, 以便subBean不存在时, 可以用来填充缺失的子对象
			previousBean = subBean;
			// 获取当前对象的子对象
			subBean = getFieldValue(subBean, patternPart);
			if (null == subBean) {
				// 支持表达式的第一个对象为Bean本身（若用户定义表达式$开头，则不做此操作）
				if (isFirst && false == this.isStartWith && BeanUtil.isMatchName(bean, patternPart, true)) {
					subBean = bean;
					isFirst = false;
				} else {
					// 填充缺失的子对象, 根据下一个表达式决定填充的值, 如果是整数(下标)则使用列表, 否则当做Map对象
					subBean = NumberUtil.isInteger(patternParts.get(i + 1)) ? new ArrayList<>() : new HashMap<>();
					BeanUtil.setFieldValue(previousBean, patternPart, subBean);
					// 上面setFieldValue中有可能发生对象转换, 因此此处重新获取子对象
					// 欲知详情请自行阅读FieldUtil.setFieldValue(Object, Field, Object)
					subBean = BeanUtil.getFieldValue(previousBean, patternPart);
				}
			}
		}

		// 设置最终的（当前）字段值
		final Object newSubBean = BeanUtil.setFieldValue(subBean, patternParts.get(length), value);
		if(newSubBean != subBean && null != previousBean){
			// 对象变更，重新加入
			BeanUtil.setFieldValue(previousBean, patternParts.get(length - 1), newSubBean);
		}
	}

	@Override
	public String toString() {
		return this.patternParts.toString();
	}

	//region Private Methods

	/**
	 * 获取Bean中对应表达式的值
	 *
	 * @param patternParts 表达式分段列表
	 * @param bean         Bean对象或Map或List等
	 * @return 值，如果对应值不存在，则返回null
	 */
	private Object get(final List<String> patternParts, final Object bean) {
		Object subBean = bean;
		boolean isFirst = true;
		for (final String patternPart : patternParts) {
			subBean = getFieldValue(subBean, patternPart);
			if (null == subBean) {
				// 支持表达式的第一个对象为Bean本身（若用户定义表达式$开头，则不做此操作）
				if (isFirst && false == this.isStartWith && BeanUtil.isMatchName(bean, patternPart, true)) {
					subBean = bean;
					isFirst = false;
				} else {
					return null;
				}
			}
		}
		return subBean;
	}

	@SuppressWarnings("unchecked")
	private static Object getFieldValue(final Object bean, final String expression) {
		if (StrUtil.isBlank(expression)) {
			return null;
		}

		if (StrUtil.contains(expression, CharUtil.COLON)) {
			// [start:end:step] 模式
			final List<String> parts = SplitUtil.splitTrim(expression, StrUtil.COLON);
			final int start = Integer.parseInt(parts.get(0));
			final int end = Integer.parseInt(parts.get(1));
			int step = 1;
			if (3 == parts.size()) {
				step = Integer.parseInt(parts.get(2));
			}
			if (bean instanceof Collection) {
				return CollUtil.sub((Collection<?>) bean, start, end, step);
			} else if (ArrayUtil.isArray(bean)) {
				return ArrayUtil.sub(bean, start, end, step);
			}
		} else if (StrUtil.contains(expression, ',')) {
			// [num0,num1,num2...]模式或者['key0','key1']模式
			final List<String> keys = SplitUtil.splitTrim(expression, StrUtil.COMMA);
			if (bean instanceof Collection) {
				return CollUtil.getAny((Collection<?>) bean, Convert.convert(int[].class, keys));
			} else if (ArrayUtil.isArray(bean)) {
				return ArrayUtil.getAny(bean, Convert.convert(int[].class, keys));
			} else {
				final String[] unWrappedKeys = new String[keys.size()];
				for (int i = 0; i < unWrappedKeys.length; i++) {
					unWrappedKeys[i] = StrUtil.unWrap(keys.get(i), CharUtil.SINGLE_QUOTE);
				}
				if (bean instanceof Map) {
					// 只支持String为key的Map
					return MapUtil.getAny((Map<String, ?>) bean, unWrappedKeys);
				} else {
					final Map<String, Object> map = BeanUtil.beanToMap(bean);
					return MapUtil.getAny(map, unWrappedKeys);
				}
			}
		} else {
			// 数字或普通字符串
			return BeanUtil.getFieldValue(bean, expression);
		}

		return null;
	}

	/**
	 * 初始化
	 *
	 * @param expression 表达式
	 */
	private void init(final String expression) {
		final List<String> localPatternParts = new ArrayList<>();
		final int length = expression.length();

		final StringBuilder builder = new StringBuilder();
		char c;
		boolean isNumStart = false;// 下标标识符开始
		boolean isInWrap = false; //标识是否在引号内
		for (int i = 0; i < length; i++) {
			c = expression.charAt(i);
			if (0 == i && '$' == c) {
				// 忽略开头的$符，表示当前对象
				isStartWith = true;
				continue;
			}

			if ('\'' == c) {
				// 结束
				isInWrap = (false == isInWrap);
				continue;
			}

			if (false == isInWrap && ArrayUtil.contains(EXP_CHARS, c)) {
				// 处理边界符号
				if (CharUtil.BRACKET_END == c) {
					// 中括号（数字下标）结束
					if (false == isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find ']' but no '[' !", expression, i));
					}
					isNumStart = false;
					// 中括号结束加入下标
				} else {
					if (isNumStart) {
						// 非结束中括号情况下发现起始中括号报错（中括号未关闭）
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, i));
					} else if (CharUtil.BRACKET_START == c) {
						// 数字下标开始
						isNumStart = true;
					}
					// 每一个边界符之前的表达式是一个完整的KEY，开始处理KEY
				}
				if (builder.length() > 0) {
					localPatternParts.add(builder.toString());
				}
				builder.setLength(0);
			} else {
				// 非边界符号，追加字符
				builder.append(c);
			}
		}

		// 末尾边界符检查
		if (isNumStart) {
			throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, length - 1));
		} else {
			if (builder.length() > 0) {
				localPatternParts.add(builder.toString());
			}
		}

		// 不可变List
		this.patternParts = ListUtil.view(localPatternParts);
	}
	//endregion
}
