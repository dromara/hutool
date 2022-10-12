package cn.hutool.core.bean.copier;

import cn.hutool.core.lang.mutable.MutableEntry;
import cn.hutool.core.reflect.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Map属性拷贝到Map中的拷贝器
 *
 * @since 5.8.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MapToMapCopier extends AbsCopier<Map, Map> {

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
	 * @param copyOptions 拷贝选项，{@code null}使用默认配置
	 */
	public MapToMapCopier(final Map source, final Map target, final Type targetType, final CopyOptions copyOptions) {
		super(source, target, copyOptions);
		this.targetType = targetType;
	}

	@Override
	public Map copy() {
		this.source.forEach((sKey, sValue) -> {
			if (null == sKey) {
				return;
			}

			// 编辑键值对
			final MutableEntry<String, Object> entry = copyOptions.editField(sKey.toString(), sValue);
			if(null == entry){
				return;
			}
			sKey = entry.getKey();
			// 对key做转换，转换后为null的跳过
			if (null == sKey) {
				return;
			}
			sValue = entry.getValue();
			// 忽略空值
			if (copyOptions.ignoreNullValue && sValue == null) {
				return;
			}

			final Object targetValue = target.get(sKey);
			// 非覆盖模式下，如果目标值存在，则跳过
			if (false == copyOptions.override && null != targetValue) {
				return;
			}

			// 获取目标值真实类型并转换源值
			final Type[] typeArguments = TypeUtil.getTypeArguments(this.targetType);
			if(null != typeArguments){
				sValue = this.copyOptions.convertField(typeArguments[1], sValue);
			}

			// 目标赋值
			target.put(sKey, sValue);
		});
		return this.target;
	}
}
