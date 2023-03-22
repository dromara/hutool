package cn.hutool.core.bean.copier;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.TypeConverter;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * 属性拷贝选项<br>
 * 包括：<br>
 * 1、限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类<br>
 * 2、是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null<br>
 * 3、忽略的属性列表，设置一个属性列表，不拷贝这些属性值<br>
 *
 * @author Looly
 */
public class CopyOptions implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类<br>
	 * 如果目标对象是Map，源对象是Bean，则作用于源对象上
	 */
	protected Class<?> editable;
	/**
	 * 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 */
	protected boolean ignoreNullValue;
	/**
	 * 属性过滤器，断言通过的属性才会被复制<br>
	 * 断言参数中Field为源对象的字段对象,如果源对象为Map，使用目标对象，Object为源对象的对应值
	 */
	private BiPredicate<Field, Object> propertiesFilter;
	/**
	 * 是否忽略字段注入错误
	 */
	protected boolean ignoreError;
	/**
	 * 是否忽略字段大小写
	 */
	protected boolean ignoreCase;
	/**
	 * 字段属性编辑器，用于自定义属性转换规则，例如驼峰转下划线等<br>
	 * 规则为，{@link Editor#edit(Object)}属性为源对象的字段名称或key，返回值为目标对象的字段名称或key
	 */
	private Editor<String> fieldNameEditor;
	/**
	 * 字段属性值编辑器，用于自定义属性值转换规则，例如null转""等
	 */
	protected BiFunction<String, Object, Object> fieldValueEditor;
	/**
	 * 是否支持transient关键字修饰和@Transient注解，如果支持，被修饰的字段或方法对应的字段将被忽略。
	 */
	protected boolean transientSupport = true;
	/**
	 * 是否覆盖目标值，如果不覆盖，会先读取目标对象的值，非{@code null}则写，否则忽略。如果覆盖，则不判断直接写
	 */
	protected boolean override = true;

	/**
	 * 源对象和目标对象都是 {@code Map} 时, 需要忽略的源对象 {@code Map} key
	 */
	private Set<String> ignoreKeySet;

	/**
	 * 自定义类型转换器，默认使用全局万能转换器转换
	 */
	protected TypeConverter converter = (type, value) -> {
		if(null == value){
			return null;
		}

		final String name = value.getClass().getName();
		if(ArrayUtil.contains(new String[]{"cn.hutool.json.JSONObject", "cn.hutool.json.JSONArray"}, name)){
			// 由于设计缺陷导致JSON转Bean时无法使用自定义的反序列化器，此处采用反射方式修复bug，此类问题会在6.x解决
			return ReflectUtil.invoke(value, "toBean", ObjectUtil.defaultIfNull(type, Object.class));
		}

		return Convert.convertWithCheck(type, value, null, ignoreError);
	};

	//region create

	/**
	 * 创建拷贝选项
	 *
	 * @return 拷贝选项
	 */
	public static CopyOptions create() {
		return new CopyOptions();
	}

	/**
	 * 创建拷贝选项
	 *
	 * @param editable         限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
	 * @param ignoreNullValue  是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 * @param ignoreProperties 忽略的属性列表，设置一个属性列表，不拷贝这些属性值
	 * @return 拷贝选项
	 */
	public static CopyOptions create(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
		return new CopyOptions(editable, ignoreNullValue, ignoreProperties);
	}
	//endregion

	/**
	 * 构造拷贝选项
	 */
	public CopyOptions() {
	}

	/**
	 * 构造拷贝选项
	 *
	 * @param editable         限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
	 * @param ignoreNullValue  是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 * @param ignoreProperties 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 */
	public CopyOptions(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
		this.propertiesFilter = (f, v) -> true;
		this.editable = editable;
		this.ignoreNullValue = ignoreNullValue;
		this.setIgnoreProperties(ignoreProperties);
	}

	/**
	 * 设置限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
	 *
	 * @param editable 限制的类或接口
	 * @return CopyOptions
	 */
	public CopyOptions setEditable(Class<?> editable) {
		this.editable = editable;
		return this;
	}

	/**
	 * 设置是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 *
	 * @param ignoreNullVall 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreNullValue(boolean ignoreNullVall) {
		this.ignoreNullValue = ignoreNullVall;
		return this;
	}

	/**
	 * 设置忽略空值，当源对象的值为null时，忽略而不注入此值
	 *
	 * @return CopyOptions
	 * @since 4.5.7
	 */
	public CopyOptions ignoreNullValue() {
		return setIgnoreNullValue(true);
	}

	/**
	 * 属性过滤器，断言通过的属性才会被复制<br>
	 * {@link BiPredicate#test(Object, Object)}返回{@code true}则属性通过，{@code false}不通过，抛弃之
	 *
	 * @param propertiesFilter 属性过滤器
	 * @return CopyOptions
	 */
	public CopyOptions setPropertiesFilter(BiPredicate<Field, Object> propertiesFilter) {
		this.propertiesFilter = propertiesFilter;
		return this;
	}

	/**
	 * 设置忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 *
	 * @param ignoreProperties 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreProperties(String... ignoreProperties) {
		this.ignoreKeySet = CollUtil.newHashSet(ignoreProperties);
		return this;
	}

	/**
	 * 设置忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值，Lambda方式
	 *
	 * @param <P>   参数类型
	 * @param <R>   返回值类型
	 * @param funcs 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 * @return CopyOptions
	 * @since 5.8.0
	 */
	@SuppressWarnings("unchecked")
	public <P, R> CopyOptions setIgnoreProperties(Func1<P, R>... funcs) {
		this.ignoreKeySet = ArrayUtil.mapToSet(funcs, LambdaUtil::getFieldName);
		return this;
	}

	/**
	 * 设置是否忽略字段的注入错误
	 *
	 * @param ignoreError 是否忽略注入错误
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
		return this;
	}

	/**
	 * 设置忽略字段的注入错误
	 *
	 * @return CopyOptions
	 * @since 4.5.7
	 */
	public CopyOptions ignoreError() {
		return setIgnoreError(true);
	}

	/**
	 * 设置是否忽略字段的大小写
	 *
	 * @param ignoreCase 是否忽略大小写
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	/**
	 * 设置忽略字段的大小写
	 *
	 * @return CopyOptions
	 * @since 4.5.7
	 */
	public CopyOptions ignoreCase() {
		return setIgnoreCase(true);
	}

	/**
	 * 设置拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用<br>
	 * 需要注意的是，当使用ValueProvider作为数据提供者时，这个映射是相反的，即fieldMapping中key为目标Bean的名称，而value是提供者中的key
	 *
	 * @param fieldMapping 拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用
	 * @return CopyOptions
	 */
	public CopyOptions setFieldMapping(Map<String, String> fieldMapping) {
		return setFieldNameEditor((key -> fieldMapping.getOrDefault(key, key)));
	}

	/**
	 * 设置字段属性编辑器，用于自定义属性转换规则，例如驼峰转下划线等<br>
	 * 此转换器只针对源端的字段做转换，请确认转换后与目标端字段一致<br>
	 * 当转换后的字段名为null时忽略这个字段<br>
	 * 需要注意的是，当使用ValueProvider作为数据提供者时，这个映射是相反的，即fieldMapping中key为目标Bean的名称，而value是提供者中的key
	 *
	 * @param fieldNameEditor 字段属性编辑器，用于自定义属性转换规则，例如驼峰转下划线等
	 * @return CopyOptions
	 * @since 5.4.2
	 */
	public CopyOptions setFieldNameEditor(Editor<String> fieldNameEditor) {
		this.fieldNameEditor = fieldNameEditor;
		return this;
	}

	/**
	 * 设置字段属性值编辑器，用于自定义属性值转换规则，例如null转""等<br>
	 *
	 * @param fieldValueEditor 字段属性值编辑器，用于自定义属性值转换规则，例如null转""等
	 * @return CopyOptions
	 * @since 5.7.15
	 */
	public CopyOptions setFieldValueEditor(BiFunction<String, Object, Object> fieldValueEditor) {
		this.fieldValueEditor = fieldValueEditor;
		return this;
	}

	/**
	 * 编辑字段值
	 *
	 * @param fieldName  字段名
	 * @param fieldValue 字段值
	 * @return 编辑后的字段值
	 * @since 5.7.15
	 */
	protected Object editFieldValue(String fieldName, Object fieldValue) {
		return (null != this.fieldValueEditor) ?
				this.fieldValueEditor.apply(fieldName, fieldValue) : fieldValue;
	}

	/**
	 * 设置是否支持transient关键字修饰和@Transient注解，如果支持，被修饰的字段或方法对应的字段将被忽略。
	 *
	 * @param transientSupport 是否支持
	 * @return this
	 * @since 5.4.2
	 */
	public CopyOptions setTransientSupport(boolean transientSupport) {
		this.transientSupport = transientSupport;
		return this;
	}

	/**
	 * 设置是否覆盖目标值，如果不覆盖，会先读取目标对象的值，为{@code null}则写，否则忽略。如果覆盖，则不判断直接写
	 *
	 * @param override 是否覆盖目标值
	 * @return this
	 * @since 5.7.17
	 */
	public CopyOptions setOverride(boolean override) {
		this.override = override;
		return this;
	}

	/**
	 * 设置自定义类型转换器，默认使用全局万能转换器转换。
	 *
	 * @param converter 转换器
	 * @return this
	 * @since 5.8.0
	 */
	public CopyOptions setConverter(TypeConverter converter) {
		this.converter = converter;
		return this;
	}

	/**
	 * 使用自定义转换器转换字段值<br>
	 * 如果自定义转换器为{@code null}，则返回原值。
	 *
	 * @param targetType 目标类型
	 * @param fieldValue 字段值
	 * @return 编辑后的字段值
	 * @since 5.8.0
	 */
	protected Object convertField(Type targetType, Object fieldValue) {
		return (null != this.converter) ?
				this.converter.convert(targetType, fieldValue) : fieldValue;
	}

	/**
	 * 转换字段名为编辑后的字段名
	 *
	 * @param fieldName 字段名
	 * @return 编辑后的字段名
	 * @since 5.4.2
	 */
	protected String editFieldName(String fieldName) {
		return (null != this.fieldNameEditor) ? this.fieldNameEditor.edit(fieldName) : fieldName;
	}

	/**
	 * 测试是否保留字段，{@code true}保留，{@code false}不保留
	 *
	 * @param field 字段
	 * @param value 值
	 * @return 是否保留
	 */
	protected boolean testPropertyFilter(Field field, Object value) {
		return null == this.propertiesFilter || this.propertiesFilter.test(field, value);
	}

	/**
	 * 测试是否保留key, {@code true} 不保留， {@code false} 保留
	 *
	 * @param key {@link Map} key
	 * @return 是否保留
	 */
	protected boolean testKeyFilter(Object key) {
		return CollUtil.isEmpty(this.ignoreKeySet) || false == this.ignoreKeySet.contains(key);
	}
}
