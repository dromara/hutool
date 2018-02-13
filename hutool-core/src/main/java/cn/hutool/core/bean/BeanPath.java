package cn.hutool.core.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Bean路径表达式，用于获取多层嵌套Bean中的字段值或Bean对象<br>
 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
 * <ol>
 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
 * </ol>
 * 
 * 表达式栗子：
 * 
 * <pre>
 * persion
 * persion.name
 * persons[3]
 * person.friends[5].name
 * </pre>
 * 
 * @author Looly
 * @since 4.0.6
 */
public class BeanPath {

	/** 表达式边界符号数组 */
	private static final char[] expChars = { CharUtil.DOT, CharUtil.BRACKET_START, CharUtil.BRACKET_END };

	protected List<Object> patternParts;

	/**
	 * 解析Bean路径表达式为Bean模式<br>
	 * Bean表达式，用于获取多层嵌套Bean中的字段值或Bean对象<br>
	 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * 
	 * 表达式栗子：
	 * 
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * 
	 * @param expression 表达式
	 * @return {@link BeanPath}
	 */
	public static BeanPath create(String expression) {
		return new BeanPath(expression);
	}

	/**
	 * 构造
	 * 
	 * @param expression 表达式
	 */
	public BeanPath(String expression) {
		init(expression);
	}

	/**
	 * 获取Bean中对应表达式的值
	 * 
	 * @param bean Bean对象或Map或List等
	 * @return 值，如果对应值不存在，则返回null
	 */
	public Object get(Object bean) {
		final List<Object> localPatternParts = this.patternParts;
		int length = localPatternParts.size();
		Object subBean = bean;
		boolean isFirst = true;
		Object patternPart;
		for (int i = 0; i < length; i++) {
			patternPart = localPatternParts.get(i);
			subBean = BeanUtil.getFieldValue(subBean, patternPart);
			if (null == subBean) {
				//支持表达式的第一个对象为Bean本身
				if(isFirst && (patternPart instanceof String) && BeanUtil.isMatchName(subBean, (String)patternPart, true)){
					subBean = bean;
					isFirst = false;
				}else{
					return null;
				}
			}
		}
		return subBean;
	}
	
	/**
	 * 设置表达式指定位置（或filed对应）的值<br>
	 * 若表达式指向一个List则设置其坐标对应位置的值，若指向Map则put对应key的值，Bean则设置字段的值<br>
	 * 注意：
	 * <pre>
	 * 1. 如果为List，则设置值得下标不能大于已有List的长度
	 * 2. 如果为数组，不能超过其长度
	 * </pre>
	 * 
	 * @param bean Bean、Map或List
	 * @param value 值
	 */
	public void set(Object bean, Object value) {
		final List<Object> localPatternParts = this.patternParts;
		int lastIndex = localPatternParts.size() - 1;
		Object subBean = bean;
		boolean isFirst = true;
		Object patternPart;
		for (int i = 0; i < lastIndex; i++) {
			patternPart = localPatternParts.get(i);
			subBean = BeanUtil.getFieldValue(subBean, patternPart);
			if (null == subBean) {
				//支持表达式的第一个对象为Bean本身
				if(isFirst && (patternPart instanceof String) && BeanUtil.isMatchName(subBean, (String)patternPart, true)){
					subBean = bean;
					isFirst = false;
				}else{
					throw new NullPointerException(StrUtil.format("No value for field or index: {}", patternPart));
				}
			}
		}
		BeanUtil.setFieldValue(subBean, localPatternParts.get(lastIndex), value);
	}
	
	/**
	 * 初始化
	 * 
	 * @param expression 表达式
	 */
	private void init(String expression) {
		List<Object> localPatternParts = new ArrayList<>();
		int length = expression.length();

		StrBuilder builder = StrUtil.strBuilder();
		char c;
		boolean isNumStart = false;// 下标标识符开始
		for (int i = 0; i < length; i++) {
			c = expression.charAt(i);
			if (ArrayUtil.contains(expChars, c)) {
				// 处理边界符号
				if (CharUtil.BRACKET_END == c) {
					// 中括号（数字下标）结束
					if (false == isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find ']' but no '[' !", expression, i));
					}
					isNumStart = false;
					// 中括号结束加入下标
					if (builder.length() > 0) {
						localPatternParts.add(Integer.parseInt(builder.toString()));
					}
					builder.reset();
				} else {
					if (isNumStart) {
						// 非结束中括号情况下发现起始中括号报错（中括号未关闭）
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, i));
					} else if (CharUtil.BRACKET_START == c) {
						// 数字下标开始
						isNumStart = true;
					}
					// 每一个边界符之前的表达式是一个完整的KEY，开始处理KEY
					if (builder.length() > 0) {
						localPatternParts.add(builder.toString());
					}
					builder.reset();
				}
			} else {
				// 非边界符号，追加字符
				if (isNumStart && (c < '0' || c > '9')) {
					// 中括号之后只能跟数字
					throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, it must number between '[' and ']', but contains '{}' !", expression, i, c));
				}
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
		this.patternParts = Collections.unmodifiableList(localPatternParts);
	}
}
