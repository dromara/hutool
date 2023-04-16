package org.dromara.hutool.core.text.placeholder.template;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrPool;
import org.dromara.hutool.core.text.placeholder.StrTemplate;
import org.dromara.hutool.core.text.placeholder.segment.LiteralSegment;
import org.dromara.hutool.core.text.placeholder.segment.SinglePlaceholderSegment;
import org.dromara.hutool.core.text.placeholder.segment.StrTemplateSegment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 单占位符字符串模板
 * <p>例如，"?", "{}", "$$$"</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class SinglePlaceholderStrTemplate extends StrTemplate {
    /**
     * 默认的占位符
     */
    public static final String DEFAULT_PLACEHOLDER = StrPool.EMPTY_JSON;

    /**
     * 占位符，默认为: {@link StrPool#EMPTY_JSON}
     */
    protected String placeholder;

    protected SinglePlaceholderStrTemplate(final String template, final int features, final String placeholder, final char escape,
                                           final String defaultValue, final UnaryOperator<String> defaultValueHandler) {
        super(template, escape, defaultValue, defaultValueHandler, features);

        Assert.notEmpty(placeholder);
        this.placeholder = placeholder;

        // 初始化Segment列表
        afterInit();
    }

    @Override
    protected List<StrTemplateSegment> parseSegments(final String template) {
        final int placeholderLength = placeholder.length();
        final int strPatternLength = template.length();
        // 记录已经处理到的位置
        int handledPosition = 0;
        // 占位符所在位置
        int delimIndex;
        // 复用的占位符变量
        final SinglePlaceholderSegment singlePlaceholderSegment = SinglePlaceholderSegment.newInstance(placeholder);
        List<StrTemplateSegment> segments = null;
        while (true) {
            delimIndex = template.indexOf(placeholder, handledPosition);
            if (delimIndex == -1) {
                // 整个模板都不带占位符
                if (handledPosition == 0) {
                    return Collections.singletonList(new LiteralSegment(template));
                }
                // 字符串模板剩余部分不再包含占位符
                if (handledPosition < strPatternLength) {
                    segments.add(new LiteralSegment(template.substring(handledPosition, strPatternLength)));
                }
                return segments;
            } else if (segments == null) {
                segments = new ArrayList<>();
            }

            // 存在 转义符
            if (delimIndex > 0 && template.charAt(delimIndex - 1) == escape) {
                // 存在 双转义符
                if (delimIndex > 1 && template.charAt(delimIndex - 2) == escape) {
                    // 转义符之前还有一个转义符，形如："//{"，占位符依旧有效
                    segments.add(new LiteralSegment(template.substring(handledPosition, delimIndex - 1)));
                    segments.add(singlePlaceholderSegment);
                    handledPosition = delimIndex + placeholderLength;
                } else {
                    // 占位符被转义，形如："/{"，当前字符并不是一个真正的占位符，而是普通字符串的一部分
                    segments.add(new LiteralSegment(
                            template.substring(handledPosition, delimIndex - 1) + placeholder.charAt(0)
                    ));
                    handledPosition = delimIndex + 1;
                }
            } else {
                // 正常占位符
                segments.add(new LiteralSegment(template.substring(handledPosition, delimIndex)));
                segments.add(singlePlaceholderSegment);
                handledPosition = delimIndex + placeholderLength;
            }
        }
    }

    // region 格式化方法
    // ################################################## 格式化方法 ##################################################

    /**
     * 按顺序使用 数组元素 替换 占位符
     *
     * @param args 可变参数
     * @return 格式化字符串
     */
    public String format(final Object... args) {
        return formatArray(args);
    }

    /**
     * 按顺序使用 原始数组元素 替换 占位符
     *
     * @param array 原始类型数组，例如: {@code int[]}
     * @return 格式化字符串
     */
    public String formatArray(final Object array) {
        return formatArray(ArrayUtil.wrap(array));
    }

    /**
     * 按顺序使用 数组元素 替换 占位符
     *
     * @param array 数组
     * @return 格式化字符串
     */
    public String formatArray(final Object[] array) {
        if (array == null) {
            return getTemplate();
        }
        return format(Arrays.asList(array));
    }

    /**
     * 按顺序使用 迭代器元素 替换 占位符
     *
     * @param iterable iterable
     * @return 格式化字符串
     */
    public String format(final Iterable<?> iterable) {
        return super.formatSequence(iterable);
    }
    // endregion

    // region 解析方法
    // ################################################## 解析方法 ##################################################

    /**
     * 将 占位符位置的值 按顺序解析为 字符串数组
     *
     * @param str 待解析的字符串，一般是格式化方法的返回值
     * @return 参数值数组
     */
    public String[] matchesToArray(final String str) {
        return matches(str).toArray(new String[0]);
    }

    /**
     * 将 占位符位置的值 按顺序解析为 字符串列表
     *
     * @param str 待解析的字符串，一般是格式化方法的返回值
     * @return 参数值列表
     */
    public List<String> matches(final String str) {
        return super.matchesSequence(str);
    }
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

    public static class Builder extends AbstractBuilder<Builder, SinglePlaceholderStrTemplate> {
        /**
         * 单占位符
         * <p>例如："?"、"{}"</p>
         * <p>默认为 {@link SinglePlaceholderStrTemplate#DEFAULT_PLACEHOLDER}</p>
         */
        protected String placeholder;

        protected Builder(final String template) {
            super(template);
        }

        /**
         * 设置 占位符
         *
         * @param placeholder 占位符，不能为 {@code null} 和 {@code ""}
         * @return builder
         */
        public Builder placeholder(final String placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        @Override
        protected SinglePlaceholderStrTemplate buildInstance() {
            if (this.placeholder == null) {
                this.placeholder = DEFAULT_PLACEHOLDER;
            }
            return new SinglePlaceholderStrTemplate(this.template, this.features, this.placeholder, this.escape,
                    this.defaultValue, this.defaultValueHandler);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
