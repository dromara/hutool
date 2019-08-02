package cn.hutool.core.bean.compare;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Bean比较器
 * 要求源对象和目标对象必须是同类型
 * 可使用{@link CompareOption}和{@link ComplexCompareOption}进行比较选项的配置
 * @author dawn
 */
public class BeanCompare<T> {

	/**
	 * 源对象
	 */
	private T source;
	/**
	 * 目标对象
	 */
	private T target;
	/**
	 * 比较选项
	 */
	private ComplexCompareOption complexCompareOption;

	/**
	 * 比较器调度,如果传入为list则直接调用list比较器
	 * @return 差异值list
	 */
	public List<ModifyField> compare() {
		if (source instanceof Collection) {
			return this.collCompare();
		}
		return this.fieldCompare();
	}

	/**
	 * 属性比较器，仅能比较属性，不能比较集合
	 *
	 * @return 属性值不同的list
	 */
	public List<ModifyField> fieldCompare() {
		final CompareOption compareOption = this.complexCompareOption.compareOptionMap.get(source.getClass()) != null ?
				this.complexCompareOption.compareOptionMap.get(source.getClass()) : new CompareOption();
		Class<?> actualEditable = source.getClass();
		if (null != compareOption.editable) {
			// 检查限制类是否为source的父类或接口
			if (!compareOption.editable.isInstance(source)) {
				throw new IllegalArgumentException(StrUtil.format("Target class [{}] not assignable to Editable class " +
						"[{}]", source.getClass().getName(), compareOption.editable.getName()));
			}
			actualEditable = compareOption.editable;
		}
		// 从ignoreMap中取出bean对应的忽略字段
		final Set<String> ignoreSet = (compareOption.ignoreProperties != null) ?
				CollUtil.newHashSet(compareOption.ignoreProperties) : null;
		Collection<BeanDesc.PropDesc> propDescList = BeanUtil.getBeanDesc(actualEditable).getProps();
		List<ModifyField> list = compareOfGetter(ignoreSet, propDescList);
		if (CollUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		return list;
	}

	/**
	 * 通过gatter获取属性进行比较
	 * @param ignoreSet 忽略字段set
	 * @param propDescList bean属性字段list
	 * @return 差异值list
	 */
	private List<ModifyField> compareOfGetter(Set<String> ignoreSet, Collection<BeanDesc.PropDesc> propDescList) {
		String fieldName;
		Method getterMethod;
		Class<?> propClass;
		Object sourceValue;
		Object targetValue;
		List<ModifyField> list = new ArrayList<>();
		try {
			for (BeanDesc.PropDesc propDesc : propDescList) {
				fieldName = propDesc.getFieldName();
				propClass = propDesc.getFieldClass();
				// 如果在忽略字段中，则不比较
				if (CollUtil.contains(ignoreSet, fieldName)) {
					continue;
				}
				// getter方法获取值然后比较
				getterMethod = propDesc.getGetter();
				if (getterMethod == null){
					continue;
				}
				sourceValue = getterMethod.invoke(source);
				targetValue = getterMethod.invoke(target);
				// 如果是集合字段, 则调用collCompare
				if (ClassUtil.isAssignable(Collection.class, TypeUtil.getClass(propDesc.getFieldType()))) {
					// list字段比较
					processListResult(list, BeanCompare.create(sourceValue, targetValue, this.complexCompareOption).collCompare());
				} else if (ObjectUtil.isNotNull(getterMethod)) {
					// 属性字段直接进行比较
					processFieldResult(fieldName, propClass, sourceValue, targetValue, list);
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new UtilException(e);
		}
		return list;
	}

	/**
	 * 处理属性字段比较结果
	 * @param fieldName 字段名称
	 * @param propClass 属性类
	 * @param sourceValue 原值
	 * @param targetValue 新值
	 * @param list 差异值list
	 */
	private void processFieldResult(String fieldName, Class<?> propClass, Object sourceValue, Object targetValue, List<ModifyField> list) {
		if (CompareUtil.compare(sourceValue, targetValue, false) != 0) {
			list.add(new ModifyField(source.getClass().getName(), fieldName, propClass, source.hashCode(), sourceValue, targetValue));
		}
	}

	/**
	 * collection集合比较器
	 *
	 * @return 字段不同列表
	 */
	public List<ModifyField> collCompare() {
		List<ModifyField> list = new ArrayList<>();
		Map<Integer, T> targetGroup = new HashMap<>();
		for (Object obj : (Collection) target) {
			targetGroup.put(obj.hashCode(), (T) obj);
		}
		if (ClassUtil.isAssignable(Collection.class, source.getClass()) && CollUtil.isNotEmpty((Collection) source)) {
			for (Object sour : (Collection) source) {
				if (ClassUtil.isAssignable(Collection.class, sour.getClass()) && ObjectUtil.isNotNull(targetGroup.get(sour.hashCode()))) {
					// 如果集合中的类型是collection集合, 则调自己
					processListResult(list, BeanCompare.create(sour, targetGroup.get(sour.hashCode()),
							new ComplexCompareOption()).collCompare());
				} else if (ObjectUtil.isNull(targetGroup.get(sour.hashCode()))) {
					// 如果新数据拿不到老数据的key，证明是数据被删除
					list.add(new ModifyField().setDeleteLine(sour.getClass().getName(), sour.hashCode(), sour));
				} else {
					processListResult(list,
							BeanCompare.create(sour, targetGroup.get(sour.hashCode()), this.complexCompareOption).fieldCompare());
					targetGroup.remove(sour.hashCode());
				}
			}
		}
		if (MapUtil.isNotEmpty(targetGroup)) {
			for (T value : targetGroup.values()) {
				list.add(new ModifyField().setAddLine(value.getClass().getName(), value.hashCode(), value));
			}
		}
		if (CollUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		return list;
	}

	private static void processListResult(List<ModifyField> list, List<ModifyField> modifyFieldList) {
		if (CollUtil.isNotEmpty(modifyFieldList)) {
			list.addAll(modifyFieldList);
		}
	}

	public static <T> BeanCompare<T> create(T source, T target, ComplexCompareOption complexCompareOption) {
		Objects.requireNonNull(source);
		Objects.requireNonNull(target);
		return new BeanCompare<>(source, target, complexCompareOption);
	}

	public static <T> BeanCompare<T> create(T source, T target, CompareOption compareOption) {
		Objects.requireNonNull(source);
		Objects.requireNonNull(target);
		return new BeanCompare<>(source, target, new ComplexCompareOption().setSimpleCompareOption(source.getClass(),
				compareOption));
	}

	public static <T> BeanCompare<T> create(T source, T target) {
		Objects.requireNonNull(source);
		Objects.requireNonNull(target);
		return new BeanCompare<>(source, target, new ComplexCompareOption().setSimpleCompareOption(source.getClass(),
				null));
	}

	private BeanCompare(T source, T target, ComplexCompareOption complexCompareOption) {
		this.source = source;
		this.target = target;
		this.complexCompareOption = complexCompareOption;
	}

	public T getSource() {
		return source;
	}

	public void setSource(T source) {
		this.source = source;
	}

	public T getTarget() {
		return target;
	}

	public void setTarget(T target) {
		this.target = target;
	}

	public ComplexCompareOption getComplexCompareOption() {
		return complexCompareOption;
	}

	public void setComplexCompareOption(ComplexCompareOption complexCompareOption) {
		this.complexCompareOption = complexCompareOption;
	}
}
