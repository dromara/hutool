package cn.hutool.core.bean.copier;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.PropDesc;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.map.MapWrapper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Map属性拷贝到Bean中的拷贝器
 *
 * @param <T> 目标Bean类型
 * @since 5.8.0
 */
public class MapToBeanCopier<T> extends AbsCopier<Map<?, ?>, T> {

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
	public MapToBeanCopier(Map<?, ?> source, T target, Type targetType, CopyOptions copyOptions) {
		super(source, target, copyOptions);

		// 针对MapWrapper特殊处理，提供的Map包装了忽略大小写的Map，则默认转Bean的时候也忽略大小写，如JSONObject
		if(source instanceof MapWrapper){
			final Map<?, ?> raw = ((MapWrapper<?, ?>) source).getRaw();
			if(raw instanceof CaseInsensitiveMap){
				copyOptions.setIgnoreCase(true);
			}
		}

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

		this.source.forEach((sKey, sValue) -> {
			if (null == sKey) {
				return;
			}
			String sKeyStr = copyOptions.editFieldName(sKey.toString());
			// 对key做转换，转换后为null的跳过
			if (null == sKeyStr) {
				return;
			}

			// 忽略不需要拷贝的 key,
			if (false == copyOptions.testKeyFilter(sKeyStr)) {
				return;
			}

			// 检查目标字段可写性
			final PropDesc tDesc = findPropDesc(targetPropDescMap, sKeyStr);
			if (null == tDesc || false == tDesc.isWritable(this.copyOptions.transientSupport)) {
				// 字段不可写，跳过之
				return;
			}
			sKeyStr = tDesc.getFieldName();

			// 检查目标是否过滤属性
			if (false == copyOptions.testPropertyFilter(tDesc.getField(), sValue)) {
				return;
			}

			// 获取目标字段真实类型并转换源值
			final Type fieldType = TypeUtil.getActualType(this.targetType, tDesc.getFieldType());
			//Object newValue = Convert.convertWithCheck(fieldType, sValue, null, this.copyOptions.ignoreError);
			Object newValue = this.copyOptions.convertField(fieldType, sValue);
			newValue = copyOptions.editFieldValue(sKeyStr, newValue);

			// 目标赋值
			tDesc.setValue(this.target, newValue, copyOptions.ignoreNullValue, copyOptions.ignoreError, copyOptions.override);
		});
		return this.target;
	}

	/**
	 * 查找Map对应Bean的名称<br>
	 * 尝试原名称、转驼峰名称、isXxx去掉is的名称
	 *
	 * @param targetPropDescMap 目标bean的属性描述Map
	 * @param sKeyStr 键或字段名
	 * @return {@link PropDesc}
	 */
	private PropDesc findPropDesc(Map<String, PropDesc> targetPropDescMap, String sKeyStr){
		PropDesc propDesc = targetPropDescMap.get(sKeyStr);
		if(null != propDesc){
			return propDesc;
		}

		// 转驼峰尝试查找
		sKeyStr = StrUtil.toCamelCase(sKeyStr);
		propDesc = targetPropDescMap.get(sKeyStr);
		if(null != propDesc){
			return propDesc;
		}

		// boolean类型参数名转换尝试查找
		if(sKeyStr.startsWith("is")){
			sKeyStr = StrUtil.removePreAndLowerFirst(sKeyStr, 2);
			propDesc = targetPropDescMap.get(sKeyStr);
			return propDesc;
		}

		return null;
	}
}
