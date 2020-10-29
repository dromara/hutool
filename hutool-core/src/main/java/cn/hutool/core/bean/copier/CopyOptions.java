package cn.hutool.core.bean.copier;

import cn.hutool.core.lang.Editor;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.Map;

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
	 * 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 */
	protected Class<?> editable;
	/**
	 * 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 */
	protected boolean ignoreNullValue;
	/**
	 * 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 */
	protected String[] ignoreProperties;
	/**
	 * 是否忽略字段注入错误
	 */
	protected boolean ignoreError;
	/**
	 * 是否忽略字段大小写
	 */
	protected boolean ignoreCase;
	/**
	 * 拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用
	 */
	protected Map<String, String> fieldMapping;
	/**
	 * 反向映射表，自动生成用于反向查找
	 */
	private Map<String, String> reversedFieldMapping;
	/**
	 * 字段属性编辑器，用于自定义属性转换规则，例如驼峰转下划线等
	 */
	protected Editor<String> fieldNameEditor;
	/**
	 * 是否支持transient关键字修饰和@Transient注解，如果支持，被修饰的字段或方法对应的字段将被忽略。
	 */
	private boolean transientSupport = true;

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
		this.editable = editable;
		this.ignoreNullValue = ignoreNullValue;
		this.ignoreProperties = ignoreProperties;
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
	 * 设置忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 *
	 * @param ignoreProperties 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreProperties(String... ignoreProperties) {
		this.ignoreProperties = ignoreProperties;
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
	 * 设置拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用
	 *
	 * @param fieldMapping 拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用
	 * @return CopyOptions
	 */
	public CopyOptions setFieldMapping(Map<String, String> fieldMapping) {
		this.fieldMapping = fieldMapping;
		return this;
	}

	/**
	 * 设置字段属性编辑器，用于自定义属性转换规则，例如驼峰转下划线等<br>
	 * 此转换器只针对源端的字段做转换，请确认转换后与目标端字段一致<br>
	 * 当转换后的字段名为null时忽略这个字段
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
	 * 是否支持transient关键字修饰和@Transient注解，如果支持，被修饰的字段或方法对应的字段将被忽略。
	 *
	 * @return 是否支持
	 * @since 5.4.2
	 */
	public boolean isTransientSupport() {
		return this.transientSupport;
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
	 * 获得映射后的字段名<br>
	 * 当非反向，则根据源字段名获取目标字段名，反之根据目标字段名获取源字段名。
	 *
	 * @param fieldName 字段名
	 * @param reversed 是否反向映射
	 * @return 映射后的字段名
	 */
	protected String getMappedFieldName(String fieldName, boolean reversed){
		Map<String, String> mapping = reversed ? getReversedMapping() : this.fieldMapping;
		if(MapUtil.isEmpty(mapping)){
			return fieldName;
		}
		return ObjectUtil.defaultIfNull(mapping.get(fieldName), fieldName);
	}

	/**
	 * 转换字段名为编辑后的字段名
	 * @param fieldName 字段名
	 * @return 编辑后的字段名
	 * @since 5.4.2
	 */
	protected String editFieldName(String fieldName){
		return (null != this.fieldNameEditor) ? this.fieldNameEditor.edit(fieldName) : fieldName;
	}

	/**
	 * 获取反转之后的映射
	 *
	 * @return 反转映射
	 * @since 4.1.10
	 */
	private Map<String, String> getReversedMapping() {
		if(null == this.fieldMapping){
			return null;
		}
		if(null == this.reversedFieldMapping){
			reversedFieldMapping = MapUtil.reverse(this.fieldMapping);
		}
		return reversedFieldMapping;
	}
}
