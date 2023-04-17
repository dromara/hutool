package org.dromara.hutool.core.text.placeholder;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.CharPool;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.placeholder.segment.AbstractPlaceholderSegment;
import org.dromara.hutool.core.text.placeholder.segment.LiteralSegment;
import org.dromara.hutool.core.text.placeholder.segment.StrTemplateSegment;
import org.dromara.hutool.core.text.placeholder.template.NamedPlaceholderStrTemplate;
import org.dromara.hutool.core.text.placeholder.template.SinglePlaceholderStrTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static org.dromara.hutool.core.text.placeholder.StrTemplate.Feature.*;

/**
 * 字符串模板 格式化 和 反解析 抽象父类
 *
 * @author emptypoint
 * @since 6.0.0
 */
public abstract class StrTemplate {
	// region 静态属性和方法
	// ################################################## 静态属性和方法 ##################################################
	/**
	 * 转义符 默认值
	 */
	public static final char DEFAULT_ESCAPE = CharPool.BACKSLASH;

	/**
	 * 全局默认策略，一旦修改，对所有模板对象都生效
	 * <p>该值 是每个模板对象创建时的 策略初始值，因此，修改全局默认策略，不影响已经创建的模板对象</p>
	 */
	protected static int globalFeatures = Feature.of(FORMAT_MISSING_KEY_PRINT_WHOLE_PLACEHOLDER, FORMAT_NULL_VALUE_TO_STR,
		MATCH_KEEP_DEFAULT_VALUE, MATCH_EMPTY_VALUE_TO_NULL, MATCH_NULL_STR_TO_NULL);

	/**
	 * 全局默认值处理器，一旦修改，对所有模板对象都生效
	 * <p>根据 占位符变量 返回 默认值</p>
	 */
	protected static UnaryOperator<String> globalDefaultValueHandler;


	/**
	 * 创建 单占位符模板对象的 Builder
	 * <p>例如，"{}", "?", "$$$"</p>
	 *
	 * @param template 字符串模板
	 * @return 单占位符 模板对象的 Builder
	 */
	public static SinglePlaceholderStrTemplate.Builder of(String template) {
		return SinglePlaceholderStrTemplate.builder(template);
	}

	/**
	 * 创建 有前缀和后缀的占位符模板对象的 Builder
	 * <p>例如，"{0}", "{name}", "#{name}"</p>
	 *
	 * @param template 字符串模板
	 * @return 有前缀和后缀的占位符模板对象的 Builder
	 */
	public static NamedPlaceholderStrTemplate.Builder ofNamed(String template) {
		return NamedPlaceholderStrTemplate.builder(template);
	}

	/**
	 * 设置 全局默认策略，一旦修改，对所有模板对象都生效
	 * <p>该值 是每个模板对象创建时的 策略初始值，因此，修改全局默认策略，不影响已经创建的模板对象</p>
	 *
	 * @param globalFeatures 全局默认策略
	 */
	public static void setGlobalFeatures(final Feature... globalFeatures) {
		StrTemplate.globalFeatures = Feature.of(globalFeatures);
	}

	/**
	 * 设置 全局默认值处理器，一旦修改，对所有模板对象都生效
	 *
	 * @param globalDefaultValueHandler 全局默认处理器，根据 占位符变量 返回 默认值
	 */
	public static void setGlobalDefaultValue(final UnaryOperator<String> globalDefaultValueHandler) {
		StrTemplate.globalDefaultValueHandler = Objects.requireNonNull(globalDefaultValueHandler);
	}
	// endregion

	// region 普通属性
	// ################################################## 普通属性 ##################################################

	/**
	 * 字符串模板
	 */
	private final String template;
	/**
	 * 转义符，默认为: {@link CharPool#BACKSLASH}
	 *
	 * <p>转义符如果标记在 占位符的开始或者结束 之前，则该占位符无效，属于普通字符串的一部分<br>
	 * 例如，转义符为 {@literal '/'}，占位符为 "{}"：<br>
	 * 当字符串模板为 {@literal "I am /{}"} 时，该模板中没有任何需要替换的占位符，格式化结果为 {@literal "I am {}"}
	 * </p>
	 *
	 * <p>如果要打印转义符，使用双转义符即可，例如，转义符为 {@literal '/'}，占位符为 "{}"：<br>
	 * 当字符串模板为 {@literal "I am //{}"} ，格式化参数为 {@literal "student"}, 格式化结果为 {@literal "I am /student"}
	 * </p>
	 */
	protected final char escape;
	/**
	 * 占位符 没有找到 对应的填充值时 使用的默认值，如果没有，则使用 {@link #defaultValueHandler} 提供默认值,
	 * 如果也没有，使用 {@link #globalDefaultValueHandler}，还是没有，则抛出异常
	 */
	protected final String defaultValue;
	/**
	 * 当前模板的默认值处理器，根据 占位变量 返回 默认值
	 */
	protected final UnaryOperator<String> defaultValueHandler;
	/**
	 * 当前模板的策略值
	 */
	private final int features;
	/**
	 * 模板中的所有固定文本和占位符
	 */
	protected List<StrTemplateSegment> segments;
	/**
	 * 所有占位符
	 */
	protected List<AbstractPlaceholderSegment> placeholderSegments;
	/**
	 * 模板中的固定文本长度，序列化时用于计算最终文本长度
	 */
	protected int fixedTextTotalLength;
	// endregion

	protected StrTemplate(final String template, final char escape, final String defaultValue,
						  final UnaryOperator<String> defaultValueHandler, final int features) {
		Assert.notNull(template, "String template cannot be null");
		this.template = template;
		this.escape = escape;
		this.defaultValue = defaultValue;
		this.defaultValueHandler = defaultValueHandler;
		this.features = features;
	}

	/**
	 * 获取 模板字符串
	 *
	 * @return 模板字符串
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * 获取 当前模板的 策略值
	 *
	 * @return 策略值
	 */
	public int getFeatures() {
		return features;
	}

	/**
	 * 校验 传入的字符串 是否和模板匹配
	 *
	 * @param str 校验字符串，应该是由格式化方法生成的字符串
	 * @return 是否和模板匹配
	 */
	public boolean isMatches(final String str) {
		if (StrUtil.isEmpty(str)) {
			return false;
		}
		int startIdx = 0, findIdx;
		boolean hasPlaceholder = false;
		String text;
		for (StrTemplateSegment segment : segments) {
			if (segment instanceof LiteralSegment) {
				text = segment.getText();
				findIdx = str.indexOf(text, startIdx);
				// 没有找到固定文本，匹配失败
				if (findIdx == -1) {
					return false;
				}
				// 出现 未匹配 的文本，但是这里却没有占位符，匹配失败
				else if (findIdx != startIdx && !hasPlaceholder) {
					return false;
				}
				startIdx = findIdx + text.length();
				hasPlaceholder = false;
			} else {
				// 有两个紧密相连的占位符，无法正确地拆分变量值
				if (hasPlaceholder) {
					throw new UtilException("There are two closely related placeholders that cannot be split properly!");
				}
				hasPlaceholder = true;
			}
		}

		return true;
	}

	/**
	 * 获取 所有占位变量名称列表
	 * <p>例如，{@literal "{}"->"{}"、"{name}"->"name"}</p>
	 *
	 * @return 所有占位变量名称列表
	 */
	public List<String> getPlaceholderVariableNames() {
		return this.placeholderSegments.stream()
			.map(AbstractPlaceholderSegment::getPlaceholder)
			.collect(Collectors.toList());
	}

	/**
	 * 获取 所有占位符的完整文本列表
	 * <p>例如，{@literal "{}"->"{}"、"{name}"->"{name}"}</p>
	 *
	 * @return 所有占位符的完整文本列表
	 */
	public List<String> getPlaceholderTexts() {
		return this.placeholderSegments.stream()
			.map(AbstractPlaceholderSegment::getText)
			.collect(Collectors.toList());
	}

	// region 格式化方法
	// ################################################## 格式化方法 ##################################################

	/**
	 * 根据 原始数据 生成 格式化字符串
	 * <p>依次遍历模板中的 占位符，根据 占位符 返回 需要序列化的值</p>
	 * <p>不对 占位符 和 参数值 做任何处理，由用户抉择</p>
	 *
	 * @param valueSupplier 根据 占位符 返回 需要序列化的值的字符串形式，例如：<br>
	 *                      {@code key -> map.get(key)}
	 * @return 模板格式化之后的结果
	 */
	public String formatRawByKey(final Function<String, String> valueSupplier) {
		return formatRawBySegment(segment -> valueSupplier.apply(segment.getPlaceholder()));
	}

	/**
	 * 根据 原始数据 生成 格式化字符串
	 * <p>依次遍历模板中的 占位符，根据 占位符 返回 需要序列化的值</p>
	 * <p>不对 占位符 和 参数值 做任何处理，由用户抉择</p>
	 *
	 * @param valueSupplier 根据 占位符 返回 需要序列化的值的字符串形式，例如：<br>
	 *                      {@code segment -> map.get(segment.getPlaceholder())}
	 * @return 模板格式化之后的结果
	 */
	public String formatRawBySegment(final Function<AbstractPlaceholderSegment, String> valueSupplier) {
		// 保存 参数转为字符串的结果
		final List<String> values = new ArrayList<>(placeholderSegments.size());
		// 先统计 固定文本 + 需要格式化的参数的字符串 的总字符数量
		int totalTextLength = this.fixedTextTotalLength;

		String valueStr;
		for (AbstractPlaceholderSegment segment : placeholderSegments) {
			// 根据 占位符 返回 需要序列化的值
			valueStr = valueSupplier.apply(segment);
			if (valueStr == null) {
				valueStr = "null";
			}
			totalTextLength += valueStr.length();
			values.add(valueStr);
		}

		final StringBuilder sb = new StringBuilder(totalTextLength);
		final Iterator<String> valueIterator = values.iterator();
		// 构造格式化结果字符串
		for (StrTemplateSegment segment : segments) {
			segment.format(sb, valueIterator);
		}
		return sb.toString();
	}

	/**
	 * 按顺序使用 迭代器元素 替换 占位符
	 *
	 * @param iterable iterable
	 * @return 格式化字符串
	 */
	protected String formatSequence(final Iterable<?> iterable) {
		if (iterable == null) {
			return getTemplate();
		}

		final Iterator<?> iterator = iterable.iterator();
		return formatBySegment(segment -> {
			if (iterator.hasNext()) {
				return iterator.next();
			} else {
				return formatMissingKey(segment);
			}
		});
	}

	/**
	 * 根据 策略 和 默认值 处理需要序列化的值, 生成 格式化字符串
	 * <p>依次遍历模板中的 占位符，根据 占位符 返回 需要序列化的值</p>
	 *
	 * @param valueSupplier 根据 占位符 返回 需要序列化的值，如果返回值不是 {@link String}，则使用 {@link StrUtil#utf8Str(Object)}
	 *                      方法转为字符串
	 * @return 模板格式化之后的结果
	 */
	protected String formatBySegment(final Function<AbstractPlaceholderSegment, ?> valueSupplier) {
		return formatRawBySegment(segment -> {
			// 根据 占位符 返回 需要序列化的值
			Object value = valueSupplier.apply(segment);
			if (value != null) {
				if (value instanceof String) {
					return (String) value;
				} else {
					return StrUtil.utf8Str(value);
				}
			} else {
				// 处理null值
				return formatNullValue(segment);
			}
		});
	}

	/**
	 * 根据 策略 返回 格式化参数中 找不到 占位符 时的默认值
	 * <p>例如，map中没有 占位符变量 这个key；基于下标的参数中，找不到 占位符下标 对应的 列表元素</p>
	 *
	 * @param segment 占位符
	 * @return 参数中找不到占位符时的默认值
	 */
	protected String formatMissingKey(final AbstractPlaceholderSegment segment) {
		final int features = getFeatures();
		if (FORMAT_MISSING_KEY_PRINT_WHOLE_PLACEHOLDER.contains(features)) {
			return segment.getText();
		} else if (FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE.contains(features)) {
			return getDefaultValue(segment);
		} else if (FORMAT_MISSING_KEY_PRINT_NULL.contains(features)) {
			return "null";
		} else if (FORMAT_MISSING_KEY_PRINT_EMPTY.contains(features)) {
			return "";
		} else if (FORMAT_MISSING_KEY_PRINT_VARIABLE_NAME.contains(features)) {
			return segment.getPlaceholder();
		} else if (FORMAT_MISSING_KEY_THROWS.contains(features)) {
			throw new UtilException("There is no value associated with key: '" + segment.getPlaceholder() + "'");
		}
		throw new UtilException("There is no value associated with key: '" + segment.getPlaceholder() +
			"'. You should define some Feature for missing key when building.");
	}

	/**
	 * 根据 策略 返回 占位符 对应的值为 {@code null} 时的返回值
	 *
	 * @param segment 占位符
	 * @return 占位符对应的值为 {@code null} 时的返回值
	 */
	protected String formatNullValue(final AbstractPlaceholderSegment segment) {
		final int features = getFeatures();
		if (FORMAT_NULL_VALUE_TO_STR.contains(features)) {
			return "null";
		} else if (FORMAT_NULL_VALUE_TO_EMPTY.contains(features)) {
			return "";
		} else if (FORMAT_NULL_VALUE_TO_WHOLE_PLACEHOLDER.contains(features)) {
			return segment.getText();
		} else if (FORMAT_NULL_VALUE_TO_DEFAULT_VALUE.contains(features)) {
			return getDefaultValue(segment);
		}
		throw new UtilException("There is a NULL value cannot resolve. You should define a Feature for null value when building or filter null value.");
	}
	// endregion

	// region 解析方法
	// ################################################## 解析方法 ##################################################

	// region 原始数据的解析方法
	// 不对 占位符 和 解析得到的值 做任何处理，由用户抉择
	// ############################# 原始数据的解析方法 #############################

	/**
	 * 原始数据的解析方法
	 * <p>不对 占位符 和 解析得到的值 做任何处理，由用户抉择</p>
	 *
	 * @param str              待解析的字符串
	 * @param keyValueConsumer 消费 占位符变量名称 和 占位符对应的解析得到的字符串值，例如：<br>{@code (key, value) -> map.put(key, value)}
	 */
	public void matchesRawByKey(final String str, final BiConsumer<String, String> keyValueConsumer) {
		if (str == null || keyValueConsumer == null || CollUtil.isEmpty(placeholderSegments)) {
			return;
		}
		matchesRawBySegment(str, (segment, value) -> keyValueConsumer.accept(segment.getPlaceholder(), value));
	}

	/**
	 * 原始数据的解析方法
	 * <p>不对 占位符 和 解析得到的值 做任何处理，由用户抉择</p>
	 *
	 * @param str              待解析的字符串
	 * @param keyValueConsumer 消费 占位符 和 占位符对应的解析得到的字符串值，例如：<br>{@code (key, value) -> map.put(key, value)}
	 */
	public void matchesRawBySegment(final String str, final BiConsumer<AbstractPlaceholderSegment, String> keyValueConsumer) {
		if (str == null || keyValueConsumer == null || CollUtil.isEmpty(placeholderSegments)) {
			return;
		}

		int startIdx = 0, findIdx;
		AbstractPlaceholderSegment placeholderSegment = null;
		String text;
		for (StrTemplateSegment segment : segments) {
			if (segment instanceof LiteralSegment) {
				text = segment.getText();
				// 查找固定文本
				findIdx = str.indexOf(text, startIdx);
				// 没有找到固定文本，匹配失败
				if (findIdx == -1) {
					return;
				} else if (placeholderSegment != null) {
					// 处理 占位符 和 解析得到的字符串值原始值
					keyValueConsumer.accept(placeholderSegment, str.substring(startIdx, findIdx));
				}
				// 中间出现 未匹配 的文本，同时还没有占位变量，匹配失败
				else if (findIdx != startIdx) {
					return;
				}
				startIdx = findIdx + text.length();
				placeholderSegment = null;
			} else {
				// 有两个紧密相连的占位符，无法正确地拆分变量值
				if (placeholderSegment != null) {
					throw new UtilException("There are two closely related placeholders that cannot be split properly!");
				}
				placeholderSegment = (AbstractPlaceholderSegment) segment;
			}
		}

		// 结尾有未匹配的 占位变量
		if (placeholderSegment != null) {
			keyValueConsumer.accept(placeholderSegment, str.substring(startIdx));
		}
	}
	// endregion

	// region 普通解析方法
	// 根据 策略 和 默认值 进行解析处理
	// ############################# 普通解析方法 #############################

	/**
	 * 将 占位符位置的值 按顺序解析为 字符串列表
	 *
	 * @param str 待解析的字符串，一般是格式化方法的返回值
	 * @return 字符串列表
	 */
	protected List<String> matchesSequence(final String str) {
		if (str == null || placeholderSegments.isEmpty() || !isMatches(str)) {
			return ListUtil.zero();
		}

		final List<String> list = new ArrayList<>(placeholderSegments.size());
		matchesByKey(str, (segment, value) -> list.add(value));
		return list;
	}

	/**
	 * 根据 策略 和 默认值 获得最终的 value，由消费者处理该 value
	 *
	 * @param str              待解析的字符串
	 * @param keyValueConsumer 按占位符顺序 消费 占位符变量 和 最终的value，例如：<br>{@code (key, value) -> map.put(key, value)}
	 */
	public void matchesByKey(final String str, final BiConsumer<String, String> keyValueConsumer) {
		if (hasDefaultValue()) {
			matchesByKey(str, keyValueConsumer, true, this::getDefaultValue);
		} else {
			matchesByKey(str, keyValueConsumer, false, null);
		}
	}

	/**
	 * 根据 策略 和 默认值 获得最终的 value，由消费者处理该 value
	 *
	 * @param str                  待解析的字符串
	 * @param keyValueConsumer     按占位符顺序 消费 占位符变量 和 最终的value，例如：<br>{@code (key, value) -> map.put(key, value)}
	 * @param hasDefaultValue      是否有默认值
	 * @param defaultValueSupplier 默认值提供者，根据 占位符 返回 默认值
	 */
	protected void matchesByKey(final String str, final BiConsumer<String, String> keyValueConsumer, final boolean hasDefaultValue,
								final Function<AbstractPlaceholderSegment, String> defaultValueSupplier) {
		if (str == null || keyValueConsumer == null || CollUtil.isEmpty(placeholderSegments)) {
			return;
		}
		matchesRawBySegment(str, (segment, value) -> matchByKey(
			keyValueConsumer, segment.getPlaceholder(), value, hasDefaultValue,
			// 默认值
			() -> hasDefaultValue ? StrUtil.utf8Str(defaultValueSupplier.apply(segment)) : null
		));
	}

	/**
	 * 根据 策略 和 默认值 获得最终的 value，由消费者处理该 value
	 *
	 * @param keyValueConsumer     按占位符顺序 消费 占位符变量 和 最终的value，例如：<br>{@code (key, value) -> map.put(key, value)}
	 * @param key                  占位符变量
	 * @param value                解析得到的值，原始值
	 * @param hasDefaultValue      是否有默认值
	 * @param defaultValueSupplier 默认值提供者
	 */
	private void matchByKey(final BiConsumer<String, String> keyValueConsumer, final String key, final String value,
							final boolean hasDefaultValue, final Supplier<String> defaultValueSupplier) {
		final int features = getFeatures();

		// 存在默认值
		if (hasDefaultValue) {
			// 保留默认值，则跳过默认值策略处理，由后续策略决定 最终的值
			if (!MATCH_KEEP_DEFAULT_VALUE.contains(features)) {
				// 解析到的参数值 是 默认值
				if (value.equals(defaultValueSupplier.get())) {
					// 校验 默认值策略
					if (MATCH_IGNORE_DEFAULT_VALUE.contains(features)) {
						return;
					} else if (MATCH_DEFAULT_VALUE_TO_NULL.contains(features)) {
						keyValueConsumer.accept(key, null);
						return;
					}
				}
			}
		}

		// 解析到的参数值 是 空字符串
		if ("".equals(value)) {
			if (MATCH_EMPTY_VALUE_TO_NULL.contains(features)) {
				keyValueConsumer.accept(key, null);
			} else if (MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE.contains(features)) {
				keyValueConsumer.accept(key, defaultValueSupplier.get());
			} else if (MATCH_IGNORE_EMPTY_VALUE.contains(features)) {
				return;
			} else if (MATCH_KEEP_VALUE_EMPTY.contains(features)) {
				keyValueConsumer.accept(key, value);
			}
			return;
		}

		// 解析到的参数值 是 null字符串
		if ("null".equals(value)) {
			if (MATCH_NULL_STR_TO_NULL.contains(features)) {
				keyValueConsumer.accept(key, null);
			} else if (MATCH_KEEP_NULL_STR.contains(features)) {
				keyValueConsumer.accept(key, value);
			} else if (MATCH_IGNORE_NULL_STR.contains(features)) {
				return;
			}
			return;
		}

		// 普通参数值
		keyValueConsumer.accept(key, value);
	}
	// endregion
	// endregion

	/**
	 * 是否有默认值
	 *
	 * @return 是否有默认值
	 */
	protected boolean hasDefaultValue() {
		return defaultValue != null || defaultValueHandler != null || globalDefaultValueHandler != null;
	}

	/**
	 * 根据 占位符 返回默认值
	 * <p>根据定义的默认值、默认值提供者、全局默认值提供者，返回默认值</p>
	 *
	 * @param segment 占位符
	 * @return 默认值
	 */
	protected String getDefaultValue(final AbstractPlaceholderSegment segment) {
		if (defaultValue != null) {
			return defaultValue;
		} else if (defaultValueHandler != null) {
			return StrUtil.utf8Str(defaultValueHandler.apply(segment.getPlaceholder()));
		} else if (globalDefaultValueHandler != null) {
			return StrUtil.utf8Str(globalDefaultValueHandler.apply(segment.getPlaceholder()));
		}
		throw new UtilException("There is no default value for key: '" + segment.getPlaceholder() +
			"'. You should define a 'defaultValue' or 'defaultValueHandler' or 'globalDefaultValueHandler' when building.");
	}

	/**
	 * 一些公共的初始化代码
	 * <p>由于此时子类还没构造完成，所以只能由子类构造方法调用</p>
	 */
	protected void afterInit() {
		// 解析 并 优化 segment 列表
		this.segments = optimizeSegments(parseSegments(template));

		// 计算 固定文本segment 的 数量 和 文本总长度
		int literalSegmentSize = 0, fixedTextTotalLength = 0;
		for (StrTemplateSegment segment : this.segments) {
			if (segment instanceof LiteralSegment) {
				++literalSegmentSize;
				fixedTextTotalLength += segment.getText().length();
			}
		}
		this.fixedTextTotalLength = fixedTextTotalLength;

		// 获取 占位符segment 列表
		final int placeholderSegmentsSize = segments.size() - literalSegmentSize;
		if (placeholderSegmentsSize == 0) {
			this.placeholderSegments = ListUtil.zero();
		} else {
			List<AbstractPlaceholderSegment> placeholderSegments = new ArrayList<>(placeholderSegmentsSize);
			for (StrTemplateSegment segment : segments) {
				if (segment instanceof AbstractPlaceholderSegment) {
					placeholderSegments.add((AbstractPlaceholderSegment) segment);
				}
			}
			this.placeholderSegments = placeholderSegments;
		}
	}


	/**
	 * 将 模板 解析为 Segment 列表
	 *
	 * @param template 字符串模板
	 * @return Segment列表
	 */
	protected abstract List<StrTemplateSegment> parseSegments(String template);

	/**
	 * 获取 模板中 所有segment
	 *
	 * @return segment列表
	 */
	protected List<StrTemplateSegment> getSegments() {
		return segments;
	}

	/**
	 * 获取 模板中的 占位符 segment
	 *
	 * @return 占位符列表
	 */
	protected List<AbstractPlaceholderSegment> getPlaceholderSegments() {
		return placeholderSegments;
	}

	/**
	 * 优化节点列表
	 * <p>移除空文本节点，合并连续的文本节点</p>
	 *
	 * @param segments 节点列表
	 * @return 不占用多余空间的节点列表
	 */
	private List<StrTemplateSegment> optimizeSegments(final List<StrTemplateSegment> segments) {
		if (CollUtil.isEmpty(segments)) {
			return segments;
		}

		final List<StrTemplateSegment> list = new ArrayList<>(segments.size());
		StrTemplateSegment last;
		for (StrTemplateSegment segment : segments) {
			if (segment instanceof LiteralSegment) {
				// 空的文本节点，没有任何意义
				if (segment.getText().isEmpty()) {
					continue;
				}
				if (list.isEmpty()) {
					list.add(segment);
					continue;
				}
				last = list.get(list.size() - 1);
				// 如果是两个连续的文本节点，需要合并
				if (last instanceof LiteralSegment) {
					list.set(list.size() - 1, new LiteralSegment(last.getText() + segment.getText()));
				} else {
					list.add(segment);
				}
			} else {
				list.add(segment);
			}
		}
		// 释放空闲的列表元素
		return list.size() == segments.size() ? list : new ArrayList<>(list);
	}

	/**
	 * 抽象Builder
	 *
	 * @param <BuilderChild>  Builder子类
	 * @param <TemplateChild> 模板子类
	 */
	protected static abstract class AbstractBuilder<BuilderChild extends AbstractBuilder<BuilderChild, TemplateChild>, TemplateChild extends StrTemplate> {
		/**
		 * 字符串模板
		 */
		protected final String template;
		/**
		 * 默认值
		 */
		protected String defaultValue;
		/**
		 * 默认值处理器
		 */
		protected UnaryOperator<String> defaultValueHandler;
		/**
		 * 用户是否设置了 转义符
		 */
		protected boolean escape$set;
		/**
		 * 转义符
		 */
		protected char escape;
		/**
		 * 策略值
		 */
		protected int features;

		protected AbstractBuilder(final String template) {
			this.template = Objects.requireNonNull(template);
			// 策略值 初始为 全局默认策略
			this.features = StrTemplate.globalFeatures;
		}

		/**
		 * 设置 转义符
		 *
		 * @param escape 转义符
		 * @return builder子对象
		 */
		public BuilderChild escape(final char escape) {
			this.escape = escape;
			this.escape$set = true;
			return self();
		}

		/**
		 * 设置 新的策略值，完全覆盖旧的策略值
		 *
		 * @param newFeatures 新策略枚举
		 * @return builder子对象
		 */
		public BuilderChild features(final Feature... newFeatures) {
			this.features = Feature.of(newFeatures);
			return self();
		}

		/**
		 * 向 策略值 中 添加策略
		 * <p>同组内的策略是互斥的，一但设置为组内的某个新策略，就会清除之前的同组策略，仅保留新策略</p>
		 *
		 * @param appendFeatures 需要新增的策略
		 * @return builder子对象
		 */
		public BuilderChild addFeatures(final Feature... appendFeatures) {
			if (ArrayUtil.isNotEmpty(appendFeatures)) {
				for (Feature feature : appendFeatures) {
					this.features = feature.set(this.features);
				}
			}
			return self();
		}

		/**
		 * 从 策略值 中 删除策略
		 * <p>删除的策略 可以 不存在</p>
		 *
		 * @param removeFeatures 需要删除的策略
		 * @return builder子对象
		 */
		public BuilderChild removeFeatures(final Feature... removeFeatures) {
			if (ArrayUtil.isNotEmpty(removeFeatures)) {
				for (Feature feature : removeFeatures) {
					this.features = feature.clear(this.features);
				}
			}
			return self();
		}

		/**
		 * 设置 默认值
		 * <p>不可能为 {@code null}，可以为 {@code "null"}</p>
		 *
		 * @param defaultValue 默认值
		 * @return builder子对象
		 */
		public BuilderChild defaultValue(final String defaultValue) {
			this.defaultValue = Objects.requireNonNull(defaultValue);
			return self();
		}

		/**
		 * 设置 默认值处理器
		 *
		 * @param defaultValueHandler 默认值处理器，根据 占位变量 返回 默认值
		 * @return builder子对象
		 */
		public BuilderChild defaultValue(final UnaryOperator<String> defaultValueHandler) {
			this.defaultValueHandler = Objects.requireNonNull(defaultValueHandler);
			return self();
		}

		/**
		 * 创建 模板对象
		 *
		 * @return 模板对象
		 */
		public TemplateChild build() {
			if (!this.escape$set) {
				this.escape = DEFAULT_ESCAPE;
			}
			return buildInstance();
		}

		/**
		 * 设置 转义符
		 *
		 * @return builder子对象
		 */
		protected abstract BuilderChild self();

		/**
		 * 子类Builder 返回 创建的 模板对象
		 *
		 * @return 模板对象
		 */
		protected abstract TemplateChild buildInstance();
	}

	/**
	 * 格式化 和 解析 策略
	 * <p>同组内的策略是互斥的，一但设置为组内的某个新策略，就会清除之前的同组策略，仅保留新策略</p>
	 */
	public enum Feature {
		// region 格式化策略
		// ======================================== 格式化策略 ========================================

		// region 占位符没有对应值策略组
		// 传递的格式化参数中找不到 占位变量，例如：占位符有三个，格式化时仅传入两个值；map中不包含占位符变量这个key；按下标格式化时，传入的列表不包含这个下标时；
		// ==================== 占位符没有对应值策略组 ====================
		/**
		 * 格式化时，如果 占位符 没有 对应的值，则打印完整占位符<br>
		 * 对于 变量占位符，例如"${name}"，原样打印"${name}"<br>
		 * <p>默认策略</p>
		 */
		FORMAT_MISSING_KEY_PRINT_WHOLE_PLACEHOLDER(0, 0, 6),
		/**
		 * 格式化时，如果 占位符 没有 对应的值，则打印 默认值，如果 没有默认值，则抛出异常<br>
		 */
		FORMAT_MISSING_KEY_PRINT_DEFAULT_VALUE(1, 0, 6),
		/**
		 * 格式化时，如果 占位符 没有 对应的值，且没有默认值，则打印 {@code "null"}字符串<br>
		 */
		FORMAT_MISSING_KEY_PRINT_NULL(2, 0, 6),
		/**
		 * 格式化时，如果 占位符 没有 对应的值，则打印 空字符串<br>
		 * <p>该策略意味着 模板存在默认值，且为 空字符串</p>
		 */
		FORMAT_MISSING_KEY_PRINT_EMPTY(3, 0, 6),
		/**
		 * 格式化时，如果 占位符 没有 对应的值：<br>
		 * 对于 单个占位符，例如"?"，打印完整占位符"?";<br>
		 * 对于 变量占位符，则只打印占位变量，例如"${name}"，只打印"name";<br>
		 */
		FORMAT_MISSING_KEY_PRINT_VARIABLE_NAME(4, 0, 6),
		/**
		 * 格式化时，如果 占位符 没有 对应的值，则抛出异常<br>
		 */
		FORMAT_MISSING_KEY_THROWS(5, 0, 6),
		//endregion

		// region null值策略组
		// ==================== null值策略组 ====================
		/**
		 * 格式化时，如果 占位符 对应的值为 {@code null}，则打印 {@code "null"} 字符串
		 * <p>默认策略</p>
		 */
		FORMAT_NULL_VALUE_TO_STR(6, 6, 4),
		/**
		 * 格式化时，如果 占位符 对应的值为 {@code null}，则打印 {@code ""} 空字符串
		 */
		FORMAT_NULL_VALUE_TO_EMPTY(7, 6, 4),
		/**
		 * 格式化时，如果 占位符 对应的值为 {@code null}，则原样打印占位符<br>
		 * 对于 变量占位符，输出完整占位符，例如"${name}"，打印"${name}"<br>
		 */
		FORMAT_NULL_VALUE_TO_WHOLE_PLACEHOLDER(8, 6, 4),
		/**
		 * 格式化时，如果 占位符 对应的值为 {@code null}，则使用 默认值，如果 没有默认值，则抛出异常<br>
		 */
		FORMAT_NULL_VALUE_TO_DEFAULT_VALUE(9, 6, 4),
		//endregion
		//endregion

		// region 解析策略
		// 解析策略校验顺序： 默认值策略、空字符串策略、null字符串策略
		// ======================================== 解析策略 ========================================

		// region 默认值策略组
		// ==================== 默认值策略组 ====================
		/**
		 * 解析时，结果中 包含 默认值，原样返回
		 * <p>默认策略</p>
		 */
		MATCH_KEEP_DEFAULT_VALUE(16, 16, 3),
		/**
		 * 解析时，结果中 不包含 默认值，只要等于默认值，都忽略
		 * <p>即，返回的结果 map 中 不会包含 这个key</p>
		 * <p>在 基于下标的解析方法中 不生效，基于下标的解析结果只区分是否为 {@code null}，元素数量是固定的</p>
		 */
		MATCH_IGNORE_DEFAULT_VALUE(17, 16, 3),
		/**
		 * 解析时，在 结果中 将 默认值 转为 {@code null}
		 * <p>返回的结果 map 中 包含 这个key</p>
		 */
		MATCH_DEFAULT_VALUE_TO_NULL(18, 16, 3),
		// endregion

		// region 空字符串策略组
		// 占位符 位置的值是 空字符串
		// ==================== 空字符串策略组 ====================
		/**
		 * 解析时，占位符 对应的值为 空字符串，将 这个空字符串 转为 {@code null}
		 * <p>默认策略</p>
		 */
		MATCH_EMPTY_VALUE_TO_NULL(19, 19, 4),
		/**
		 * 解析时，占位符 对应的值为 空字符串，将 这个空字符串 转为 默认值，如果 没有默认值，则转为 {@code null}
		 */
		MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE(20, 19, 4),
		/**
		 * 解析时，占位符 对应的值为 空字符串，结果中 不包含 这个空字符串
		 * <p>即，返回的结果 map 中 不会包含 这个key</p>
		 * <p>在 基于下标的解析方法中 不生效，基于下标的解析结果只区分是否为 {@code null}，元素数量是固定的</p>
		 */
		MATCH_IGNORE_EMPTY_VALUE(21, 19, 4),
		/**
		 * 解析时，占位符 对应的值为 空字符串，结果中 依然保留 这个空字符串
		 */
		MATCH_KEEP_VALUE_EMPTY(22, 19, 4),
		// endregion

		// region null值策略组
		// ==================== null值策略组 ====================
		/**
		 * 解析时，占位符 对应的值为 {@code "null"} 字符串，在 结果中 转为 {@code null}
		 * <p>默认策略</p>
		 */
		MATCH_NULL_STR_TO_NULL(23, 23, 3),
		/**
		 * 解析时，占位符 对应的值为 {@code "null"} 字符串，在 结果中 保留字符串形式 {@code "null"}
		 */
		MATCH_KEEP_NULL_STR(24, 23, 3),
		/**
		 * 解析时，占位符 对应的值为 {@code "null"} 字符串，结果中 不包含 这个值
		 * <p>即，返回的结果 map 中 不会包含 这个key</p>
		 * <p>在 基于下标的解析方法中 不生效，基于下标的解析结果只区分是否为 {@code null}，元素数量是固定的</p>
		 */
		MATCH_IGNORE_NULL_STR(25, 23, 3),
		// endregion
		// endregion
		;
		/**
		 * 掩码
		 */
		private final int mask;
		/**
		 * 清除掩码的二进制值
		 */
		private final int clearMask;

		/**
		 * 策略构造方法
		 *
		 * @param bitPos   位数，掩码中哪一位需要置为1，从0开始
		 * @param bitStart 同组第一个策略的掩码位数
		 * @param bitLen   同组策略数量
		 */
		Feature(int bitPos, int bitStart, int bitLen) {
			this.mask = 1 << bitPos;
			this.clearMask = (-1 << (bitStart + bitLen)) | ((1 << bitStart) - 1);
		}

		/**
		 * 是否为当前策略
		 *
		 * @param features 外部的策略值
		 * @return 是否为当前策略
		 */
		public boolean contains(int features) {
			return (features & mask) != 0;
		}

		/**
		 * 在 策略值 中添加 当前策略
		 *
		 * @param features 外部的策略值
		 * @return 添加后的策略值
		 */
		public int set(int features) {
			return (features & clearMask) | mask;
		}

		/**
		 * 在 策略值 中移除 当前策略
		 *
		 * @param features 外部的策略值
		 * @return 移除后的策略值
		 */
		public int clear(int features) {
			return (features & clearMask);
		}

		/**
		 * 计算 总的策略值
		 *
		 * @param features 策略枚举数组
		 * @return 总的策略值
		 */
		public static int of(Feature... features) {
			if (features == null) {
				return 0;
			}

			int value = 0;
			for (Feature feature : features) {
				value = feature.set(value);
			}

			return value;
		}
	}
}
