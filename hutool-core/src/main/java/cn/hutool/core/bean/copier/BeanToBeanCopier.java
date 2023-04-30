package cn.hutool.core.bean.copier;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.PropDesc;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean属性拷贝到Bean中的拷贝器
 *
 * @param <S> 源Bean类型
 * @param <T> 目标Bean类型
 * @since 5.8.0
 */
public class BeanToBeanCopier<S, T> extends AbsCopier<S, T> {

	/**
	 * 目标的类型（用于泛型类注入）
	 */
	private final Type targetType;

	/**
	 * 构造
	 *
	 * @param source      来源Map
	 * @param target      目标Bean对象
	 * @param targetType  目标泛型类型
	 * @param copyOptions 拷贝选项
	 */
	public BeanToBeanCopier(S source, T target, Type targetType, CopyOptions copyOptions) {
		super(source, target, copyOptions);
		this.targetType = targetType;
	}

	@Override
	public T copy() {
		Class<?> actualEditable = target.getClass();
		if (null != copyOptions.editable) {
			// 检查限制类是否为target的父类或接口
			Assert.isTrue(copyOptions.editable.isInstance(target),
					"Target class [{}] not assignable to Editable class [{}]", actualEditable.getName(), copyOptions.editable.getName());
			actualEditable = copyOptions.editable;
		}
		final Map<String, PropDesc> targetPropDescMap = BeanUtil.getBeanDesc(actualEditable).getPropMap(copyOptions.ignoreCase);

		final Map<String, PropDesc> sourcePropDescMap = BeanUtil.getBeanDesc(source.getClass()).getPropMap(copyOptions.ignoreCase);
		sourcePropDescMap.forEach((sFieldName, sDesc) -> {
			if (null == sFieldName || false == sDesc.isReadable(copyOptions.transientSupport)) {
				// 字段空或不可读，跳过
				return;
			}

			sFieldName = copyOptions.editFieldName(sFieldName);
			// 对key做转换，转换后为null的跳过
			if (null == sFieldName) {
				return;
			}

			// 忽略不需要拷贝的 key,
			if (false == copyOptions.testKeyFilter(sFieldName)) {
				return;
			}

			// 检查目标字段可写性
			final PropDesc tDesc = targetPropDescMap.get(sFieldName);
			if (null == tDesc || false == tDesc.isWritable(this.copyOptions.transientSupport)) {
				// 字段不可写，跳过之
				return;
			}

			// 检查源对象属性是否过滤属性
			Object sValue = sDesc.getValue(this.source);
			if (false == copyOptions.testPropertyFilter(sDesc.getField(), sValue)) {
				return;
			}

			// 获取目标字段真实类型并转换源值
			final Type fieldType = TypeUtil.getActualType(this.targetType, tDesc.getFieldType());
			//sValue = Convert.convertWithCheck(fieldType, sValue, null, this.copyOptions.ignoreError);
			sValue = this.copyOptions.convertField(fieldType, sValue);
			sValue = copyOptions.editFieldValue(sFieldName, sValue);

			// 目标赋值
			tDesc.setValue(this.target, sValue, copyOptions.ignoreNullValue, copyOptions.ignoreError, copyOptions.override);
		});
		return this.target;
	}
}
