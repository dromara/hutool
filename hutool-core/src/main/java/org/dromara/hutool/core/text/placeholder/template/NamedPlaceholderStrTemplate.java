/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.text.placeholder.template;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.BeanDesc;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.func.LambdaUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.text.StrPool;
import org.dromara.hutool.core.text.placeholder.StrTemplate;
import org.dromara.hutool.core.text.placeholder.segment.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.*;

/**
 * 有前后缀的字符串模板
 * <p>例如，"{1}", "{name}", "#{id}"</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class NamedPlaceholderStrTemplate extends StrTemplate {
    /**
     * 默认前缀
     */
    public static final String DEFAULT_PREFIX = StrPool.DELIM_START;
    /**
     * 默认后缀
     */
    public static final String DEFAULT_SUFFIX = StrPool.DELIM_END;

    /**
     * 占位符前缀，默认为: {@link #DEFAULT_PREFIX}
     */
    protected String prefix;
    /**
     * 占位符后缀，默认为: {@link #DEFAULT_SUFFIX}
     */
    protected String suffix;
    /**
     * 在 下标占位符中，最大的下标值
     */
    protected int indexedSegmentMaxIdx = 0;

    protected NamedPlaceholderStrTemplate(final String template, final int features, final String prefix,
                                          final String suffix, final char escape, final String defaultValue,
                                          final UnaryOperator<String> defaultValueHandler) {
        super(template, escape, defaultValue, defaultValueHandler, features);

        Assert.notEmpty(prefix);
        Assert.notEmpty(suffix);
        this.prefix = prefix;
        this.suffix = suffix;

        // 一些初始化后续操作
        afterInit();

        // 记录 下标占位符 最大的 下标值
        if (!placeholderSegments.isEmpty()) {
            for (AbstractPlaceholderSegment segment : placeholderSegments) {
                if (segment instanceof IndexedPlaceholderSegment) {
                    this.indexedSegmentMaxIdx = Math.max(this.indexedSegmentMaxIdx, ((IndexedPlaceholderSegment) segment).getIndex());
                }
            }
        }
    }

    @Override
    protected List<StrTemplateSegment> parseSegments(final String template) {
        // 寻找第一个前缀符号
        int openCursor = template.indexOf(prefix);
        // 没有任何占位符
        if (openCursor == -1) {
            return Collections.singletonList(new LiteralSegment(template));
        }

        final int openLength = prefix.length();
        final int closeLength = suffix.length();
        List<StrTemplateSegment> segments = new ArrayList<>();
        int closeCursor = 0;
        // 开始匹配
        final char[] src = template.toCharArray();
        final StringBuilder expression = new StringBuilder(16);
        boolean hasDoubleEscape = false;
        // 占位变量名称
        String variableName;
        // 完整的占位符
        String wholePlaceholder;
        // 上一个解析的segment是否是固定文本，如果是，则需要和当前新的文本部分合并
        boolean isLastLiteralSegment = false;
        while (openCursor > -1) {
            // 开始符号是否被转义，若是则跳过，并寻找下一个开始符号
            if (openCursor > 0 && src[openCursor - 1] == escape) {
                // 存在 双转义符，转义符之前还有一个转义符，形如："\\{"，占位符依旧有效
                if (openCursor > 1 && src[openCursor - 2] == escape) {
                    hasDoubleEscape = true;
                } else {
                    // 开始符号被转义，跳过，寻找下一个开始符号
                    addLiteralSegment(isLastLiteralSegment, segments, template.substring(closeCursor, openCursor - 1) + prefix);
                    isLastLiteralSegment = true;
                    closeCursor = openCursor + openLength;
                    openCursor = template.indexOf(prefix, closeCursor);
                    continue;
                }
            }

            // 没有双转义符
            if (!hasDoubleEscape) {
                if (closeCursor < openCursor) {
                    // 完整记录当前占位符的开始符号与上一占位符的结束符号间的字符串
                    addLiteralSegment(isLastLiteralSegment, segments, template.substring(closeCursor, openCursor));
                }
            } else {
                // 存在双转义符，只能保留一个转义符
                hasDoubleEscape = false;
                addLiteralSegment(isLastLiteralSegment, segments, template.substring(closeCursor, openCursor - 1));
            }

            // 重置结束游标至当前占位符的开始处
            closeCursor = openCursor + openLength;

            // 寻找结束符号下标
            int end = template.indexOf(suffix, closeCursor);
            while (end > -1) {
                // 结束符号被转义，寻找下一个结束符号
                if (end > closeCursor && src[end - 1] == escape) {
                    // 双转义符，保留一个转义符，并且找到了结束符
                    if (end > 1 && src[end - 2] == escape) {
                        expression.append(src, closeCursor, end - closeCursor - 1);
                        break;
                    } else {
                        expression.append(src, closeCursor, end - closeCursor - 1).append(suffix);
                        closeCursor = end + closeLength;
                        end = template.indexOf(suffix, closeCursor);
                    }
                }
                // 找到结束符号
                else {
                    expression.append(src, closeCursor, end - closeCursor);
                    break;
                }
            }

            // 未能找到结束符号，说明匹配异常
            if (end == -1) {
                throw new HutoolException("\"{}\" 中字符下标 {} 处的开始符没有找到对应的结束符", template, openCursor);
            }
            // 找到结束符号，开始到结束符号 之间的字符串 就是占位变量
            else {
                // 占位变量名称
                variableName = expression.toString();
                expression.setLength(0);
                // 完整的占位符
                wholePlaceholder = expression.append(prefix).append(variableName).append(suffix).toString();
                expression.setLength(0);
                // 如果是整数，则当作下标处理
                if (NumberUtil.isInteger(variableName)) {
                    segments.add(new IndexedPlaceholderSegment(variableName, wholePlaceholder));
                } else {
                    // 当作变量名称处理
                    segments.add(new NamedPlaceholderSegment(variableName, wholePlaceholder));
                }
                isLastLiteralSegment = false;
                // 完成当前占位符的处理匹配，寻找下一个
                closeCursor = end + closeLength;
            }

            // 寻找下一个开始符号
            openCursor = template.indexOf(prefix, closeCursor);
        }

        // 若匹配结束后仍有未处理的字符串，则直接将其拼接到表达式上
        if (closeCursor < src.length) {
            addLiteralSegment(isLastLiteralSegment, segments, template.substring(closeCursor));
        }
        return segments;
    }

    // region 格式化方法
    // ################################################## 格式化方法 ##################################################

    // region 基于顺序的格式化方法
    // ############################## 基于顺序的格式化方法 ##############################

    /**
     * 按顺序使用 数组元素 替换 占位符
     *
     * @param args 可变参数
     * @return 格式化字符串
     */
    public String formatSequence(final Object... args) {
        return formatArraySequence(args);
    }

    /**
     * 按顺序使用 原始数组元素 替换 占位符
     *
     * @param array 原始类型数组，例如: {@code int[]}
     * @return 格式化字符串
     */
    public String formatArraySequence(final Object array) {
        return formatArraySequence(ArrayUtil.wrap(array));
    }

    /**
     * 按顺序使用 数组元素 替换 占位符
     *
     * @param array 数组
     * @return 格式化字符串
     */
    public String formatArraySequence(final Object[] array) {
        if (array == null) {
            return getTemplate();
        }
        return formatSequence(Arrays.asList(array));
    }

    /**
     * 按顺序使用 迭代器元素 替换 占位符
     *
     * @param iterable iterable
     * @return 格式化字符串
     */
    @Override
    public String formatSequence(final Iterable<?> iterable) {
        return super.formatSequence(iterable);
    }
    // endregion

    // region 基于下标的格式化方法
    // ############################## 基于下标的格式化方法 ##############################

    /**
     * 按 下标 使用 数组元素 替换 占位符
     *
     * @param args 可变参数
     * @return 格式化字符串
     */
    public String formatIndexed(final Object... args) {
        return formatArrayIndexed(args);
    }

    /**
     * 按 下标 使用 原始数组元素 替换 占位符
     *
     * @param array 原始类型数组
     * @return 格式化字符串
     */
    public String formatArrayIndexed(final Object array) {
        return formatArrayIndexed(ArrayUtil.wrap(array));
    }

    /**
     * 按 下标 使用 数组元素 替换 占位符
     *
     * @param array 数组
     * @return 格式化字符串
     */
    public String formatArrayIndexed(final Object[] array) {
        if (array == null) {
            return getTemplate();
        }
        return formatIndexed(Arrays.asList(array));
    }

    /**
     * 按 下标 使用 集合元素 替换 占位符
     *
     * @param collection 集合元素
     * @return 格式化字符串
     */
    public String formatIndexed(final Collection<?> collection) {
        return formatIndexed(collection, null);
    }

    /**
     * 按 下标 使用 集合元素 替换 占位符
     *
     * @param collection          集合元素
     * @param missingIndexHandler 集合中不存在下标位置时的处理器，根据 下标 返回 代替值
     * @return 格式化字符串
     */
    public String formatIndexed(final Collection<?> collection, IntFunction<String> missingIndexHandler) {
        if (collection == null) {
            return getTemplate();
        }

        final int size = collection.size();
        final boolean isList = collection instanceof List;
        return formatBySegment(segment -> {
            int index = ((IndexedPlaceholderSegment) segment).getIndex();
            if (index < 0) {
                index += size;
            }
            if (index >= 0 && index < size) {
                if (isList) {
                    return ((List<?>) collection).get(index);
                }
                return CollUtil.get(collection, index);
            }
            // 下标越界，代表 占位符 没有对应值，尝试获取代替值
            else if (missingIndexHandler != null) {
                return missingIndexHandler.apply(index);
            } else {
                return formatMissingKey(segment);
            }
        });
    }
    // endregion

    // region 基于键值的格式化方法
    // ############################## 基于键值的格式化方法 ##############################

    /**
     * 使用 占位变量名称 从 Bean 或 Map 中查询值来 替换 占位符
     *
     * @param beanOrMap Bean 或 Map 实例
     * @return 格式化字符串
     */
    @SuppressWarnings("unchecked")
    public String format(final Object beanOrMap) {
        if (beanOrMap == null) {
            return getTemplate();
        }
        if (beanOrMap instanceof Map) {
            return format((Map<String, ?>) beanOrMap);
        } else if (BeanUtil.isReadableBean(beanOrMap.getClass())) {
            final BeanDesc beanDesc = BeanUtil.getBeanDesc(beanOrMap.getClass());
            return format(fieldName -> {
                final Method getterMethod = beanDesc.getGetter(fieldName);
                if (getterMethod == null) {
                    return null;
                }
                return LambdaUtil.buildGetter(getterMethod).apply(beanOrMap);
            });
        }
        return format(fieldName -> BeanUtil.getFieldValue(beanOrMap, fieldName));
    }

    /**
     * 使用 占位变量名称 从 Map 中查询值来 替换 占位符
     *
     * @param map map
     * @return 格式化字符串
     */
    public String format(final Map<String, ?> map) {
        if (map == null) {
            return getTemplate();
        }
        return format(map::get, map::containsKey);
    }

    /**
     * 使用 占位变量名称 从 valueSupplier 中查询值来 替换 占位符
     *
     * @param valueSupplier 根据 占位变量名称 返回 值
     * @return 格式化字符串
     */
    public String format(final Function<String, ?> valueSupplier) {
        if (valueSupplier == null) {
            return getTemplate();
        }
        return formatBySegment(segment -> valueSupplier.apply(segment.getPlaceholder()));
    }

    /**
     * 使用 占位变量名称 从 valueSupplier 中查询值来 替换 占位符
     *
     * @param valueSupplier 根据 占位变量名称 返回 值
     * @param containsKey   占位变量名称 是否存在，例如：{@code map.containsKey(key)}
     * @return 格式化字符串
     */
    public String format(final Function<String, ?> valueSupplier, final Predicate<String> containsKey) {
        if (valueSupplier == null || containsKey == null) {
            return getTemplate();
        }

        return formatBySegment(segment -> {
            final String placeholder = segment.getPlaceholder();
            if (containsKey.test(placeholder)) {
                return valueSupplier.apply(placeholder);
            }
            return formatMissingKey(segment);
        });
    }
    // endregion
    // endregion

    // region 解析方法
    // ################################################## 解析方法 ##################################################

    // region 基于顺序的解析方法
    // ############################## 基于顺序的解析方法 ##############################

    /**
     * 将 占位符位置的值 按顺序解析为 字符串数组
     *
     * @param str 待解析的字符串，一般是格式化方法的返回值
     * @return 字符串数组
     */
    public String[] matchesSequenceToArray(final String str) {
        return matchesSequence(str).toArray(new String[0]);
    }

    /**
     * 将 占位符位置的值 按顺序解析为 字符串列表
     *
     * @param str 待解析的字符串，一般是格式化方法的返回值
     * @return 字符串列表
     */
    @Override
    public List<String> matchesSequence(final String str) {
        return super.matchesSequence(str);
    }
    // endregion

    // region 基于下标的解析方法
    // ############################## 基于下标的解析方法 ##############################

    /**
     * 将 占位符位置的值 按 占位符下标值 解析为 字符串数组
     *
     * @param str 待解析的字符串，一般是格式化方法的返回值
     * @return 字符串数组
     * @see #matchesIndexed(String, IntFunction)
     */
    public String[] matchesIndexedToArray(final String str) {
        return matchesIndexed(str, null).toArray(new String[0]);
    }

    /**
     * 将 占位符位置的值 按 占位符下标值 解析为 字符串数组
     *
     * @param str                 待解析的字符串，一般是格式化方法的返回值
     * @param missingIndexHandler 根据 下标 返回 默认值，该参数可以为 {@code null}，仅在 {@link Feature#MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE} 策略时生效
     * @return 字符串数组
     * @see #matchesIndexed(String, IntFunction)
     */
    public String[] matchesIndexedToArray(final String str, final IntFunction<String> missingIndexHandler) {
        return matchesIndexed(str, missingIndexHandler).toArray(new String[0]);
    }

    /**
     * 将 占位符位置的值 按 占位符下标值 解析为 字符串列表
     *
     * @param str 待解析的字符串，一般是格式化方法的返回值
     * @return 字符串列表
     * @see #matchesIndexed(String, IntFunction)
     */
    public List<String> matchesIndexed(final String str) {
        return matchesIndexed(str, null);
    }

    /**
     * 将 占位符位置的值 按 占位符下标值 解析为 字符串列表
     *
     * <p>例如，模板中为 {@literal "This is between {1} and {2}"}，格式化结果为 {@literal "This is between 666 and 999"},
     * 由于其最大下标为 2, 则解析结果中固定有 3 个元素，解析结果为 {@code [null, "666", "999"]}</p>
     *
     * @param str                 待解析的字符串，一般是格式化方法的返回值
     * @param missingIndexHandler 根据 下标 返回 默认值，该参数可以为 {@code null}，仅在 {@link Feature#MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE} 策略时生效
     * @return 字符串列表
     */
    public List<String> matchesIndexed(final String str, final IntFunction<String> missingIndexHandler) {
        if (str == null || placeholderSegments.isEmpty() || !isMatches(str)) {
            return ListUtil.zero();
        }

        final List<String> params = new ArrayList<>(this.indexedSegmentMaxIdx + 1);
        // 用null值填充所有位置
        ListUtil.setOrPadding(params, this.indexedSegmentMaxIdx, null, null);
        matchesIndexed(str, params::set, missingIndexHandler);
        return params;
    }

    /**
     * 根据 下标 和 下标占位符位置的值，自行提取结果值
     *
     * <p>例如，模板中为 {@literal "This is between {1} and {2}"}，格式化结果为 {@literal "This is between 666 and 999"},
     * 由于其最大下标为 2, 则解析结果中固定有 3 个元素，解析结果为 {@code [null, "666", "999"]}</p>
     *
     * @param str                 待解析的字符串，一般是格式化方法的返回值
     * @param idxValueConsumer    处理 下标 和 下标占位符位置的值 的消费者，例如：<br>{@code (idx, value) -> list.set(idx, value)}
     * @param missingIndexHandler 根据 下标 返回 默认值，该参数可以为 {@code null}，仅在 {@link Feature#MATCH_EMPTY_VALUE_TO_DEFAULT_VALUE} 策略时生效
     */
    public void matchesIndexed(final String str, final BiConsumer<Integer, String> idxValueConsumer,
                               final IntFunction<String> missingIndexHandler) {
        if (str == null || CollUtil.isEmpty(placeholderSegments) || !isMatches(str)) {
            return;
        }

        if (missingIndexHandler == null) {
            matchesByKey(str, (key, value) -> idxValueConsumer.accept(Integer.parseInt(key), value));
        } else {
            matchesByKey(str, (key, value) -> idxValueConsumer.accept(Integer.parseInt(key), value), true, segment -> {
                if ((segment instanceof IndexedPlaceholderSegment)) {
                    return missingIndexHandler.apply(((IndexedPlaceholderSegment) segment).getIndex());
                }
                return getDefaultValue(segment);
            });
        }
    }
    // endregion

    // region 基于键值的解析方法
    // ############################## 基于键值的解析方法 ##############################

    /**
     * 根据 占位变量 和 对应位置解析值 构造 {@link Map}
     *
     * @param str 待解析的字符串，一般是格式化方法的返回值
     * @return {@link Map}
     */
    public Map<String, String> matches(final String str) {
        return matches(str, HashMap::new);
    }

    /**
     * 根据 占位变量 和 对应位置解析值 构造 map 或者 bean 实例
     *
     * @param str               待解析的字符串，一般是格式化方法的返回值
     * @param beanOrMapSupplier 提供一个 bean 或者 map，例如：{@code HashMap::new}
     * @param <T>               返回结果对象类型
     * @return map 或者 bean 实例
     */
    public <T> T matches(final String str, final Supplier<T> beanOrMapSupplier) {
        Assert.notNull(beanOrMapSupplier, "beanOrMapSupplier cannot be null");
        final T obj = beanOrMapSupplier.get();
        if (str == null || obj == null || placeholderSegments.isEmpty() || !isMatches(str)) {
            return obj;
        }

        if (obj instanceof Map) {
            @SuppressWarnings("unchecked") final Map<String, String> map = (Map<String, String>) obj;
            matchesByKey(str, map::put);
        } else if (BeanUtil.isReadableBean(obj.getClass())) {
            final BeanDesc beanDesc = BeanUtil.getBeanDesc(obj.getClass());
            matchesByKey(str, (key, value) -> {
                final Field field = beanDesc.getField(key);
                final Method setterMethod = beanDesc.getSetter(key);
                if (field == null || setterMethod == null) {
                    return;
                }
                final Object convert = Convert.convert(field.getType(), value);
                LambdaUtil.buildSetter(setterMethod).accept(obj, convert);
            });
        }
        return obj;
    }
    // endregion
    // endregion

    /**
     * 创建 builder
     *
     * @param template 字符串模板，不能为 {@code null}
     * @return builder实例
     */
    public static Builder builder(final String template) {
        return new Builder(template);
    }

    public static class Builder extends AbstractBuilder<Builder, NamedPlaceholderStrTemplate> {
        /**
         * 占位符前缀，默认为 {@link NamedPlaceholderStrTemplate#DEFAULT_PREFIX}
         * <p>不能为空字符串</p>
         */
        protected String prefix;
        /**
         * 占位符后缀，默认为 {@link NamedPlaceholderStrTemplate#DEFAULT_SUFFIX}
         * <p>不能为空字符串</p>
         */
        protected String suffix;

        protected Builder(final String template) {
            super(template);
        }

        /**
         * 设置 占位符前缀
         *
         * @param prefix 占位符前缀，不能为空字符串
         * @return builder
         */
        public Builder prefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * 设置 占位符后缀
         *
         * @param suffix 占位符后缀，不能为空字符串
         * @return builder
         */
        public Builder suffix(final String suffix) {
            this.suffix = suffix;
            return this;
        }

        @Override
        protected NamedPlaceholderStrTemplate buildInstance() {
            if (this.prefix == null) {
                this.prefix = DEFAULT_PREFIX;
            }
            if (this.suffix == null) {
                this.suffix = DEFAULT_SUFFIX;
            }
            return new NamedPlaceholderStrTemplate(this.template, this.features, this.prefix, this.suffix, this.escape, this.defaultValue, this.defaultValueHandler);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
