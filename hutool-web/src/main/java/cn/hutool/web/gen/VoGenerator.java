package cn.hutool.web.gen;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.web.anno.Domain;
import cn.hutool.web.anno.VO;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * VO 建造者
 *
 * @author 码小瑞
 */
public class VoGenerator<T> {

	/**
	 * 在此对象上构建 VO
	 */
	private final T src;

	/**
	 * 要对哪些 pageId 生成 VO
	 */
	private final List<String> pageIds;

	/**
	 * 要对哪些属性生成 VO
	 */
	private final List<SFunc<T, ?>> attrs;

	/**
	 * 自由设置的 VO 生成属性
	 */
	private final Map<String, Object> freeParameter;

	/**
	 * Json 对象生成配置
	 */
	private final static JSONConfig CONFIG = JSONConfig.create().setIgnoreNullValue(false);

	public VoGenerator(T src) {
		this.src = src;
		this.pageIds = new ArrayList<>();
		this.attrs = new ArrayList<>();
		freeParameter = new HashMap<>();
	}

	/**
	 * pageIds 属性建造方法
	 */
	public VoGenerator<T> withPageIds(String... pageIds) {
		Collections.addAll(this.pageIds, pageIds);
		return this;
	}

	/**
	 * 直接为生成的 VO 设置键值
	 */
	public VoGenerator<T> put(String k, Object v) {
		freeParameter.put(k, v);
		return this;
	}

	/**
	 * attrs 属性建造方法
	 */
	@SafeVarargs
	public final VoGenerator<T> withAttrs(SFunc<T, ?>... attr) {
		Collections.addAll(this.attrs, attr);
		return this;
	}

	/**
	 * 生成 VO 对象
	 */
	public JSONObject genVoObj() {
		JsonObjVisitor visitor = new JsonObjVisitor();
		if (src != null) {
			final Class<?> cl = src.getClass();
			final Class<?> superCl = cl.getSuperclass();
			genJsonObj(visitor, cl, src);
			genJsonObj(visitor, superCl, src);
		}
		return visitor.get(freeParameter);
	}

	/**
	 * 生成 VO 数组
	 */
	public JSONArray genVoArr() {
		return genJsonArr((List<?>) src).get(freeParameter);
	}

	/**
	 * 递归将 cl 构建成 VO，包含 cl 中的内嵌 @Domain 对象与 List 数组
	 */
	private JsonObjVisitor genJsonObj(JsonObjVisitor visitor, Class<?> cl, Object data) {
		// 获取 cl 中所有的字段
		final List<Field> fields = new ArrayList<>();
		CollUtil.addAll(fields, getFieldsByPageIds(cl));
		CollUtil.addAll(fields, getFieldsByAttrs(cl));

		try {
			for (Field field : fields) {
				field.setAccessible(true);
				final Class<?> fieldType = field.getType();
				// 字段类型被 @Domain 标识需要递归深入
				if (fieldType.getAnnotation(Domain.class) != null) {
					visitor.put(field, genJsonObj(new JsonObjVisitor(), field.getType(), field.get(data)));
					continue;
				}
				// List 类型的数据需要对每个列表项都做 Json 化处理
				if (fieldType.equals(List.class)) {
					visitor.put(field, genJsonArr((List<?>) field.get(data)));
					continue;
				}
				visitor.put(field, field.get(data));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return visitor;
	}

	/**
	 * 递归将 cl 构建成 VO，对每个列表项都按照 VO 生成规则去生成 对应 JsonObject
	 */
	private JsonArrVisitor genJsonArr(List<?> list) {
		JsonArrVisitor visitor = new JsonArrVisitor();
		for (Object listItem : list) {
			visitor.add(genJsonObj(new JsonObjVisitor(), listItem.getClass(), listItem));
		}
		return visitor;
	}

	/**
	 * 获取 pageId 与 @VO 注解中指定 pageId 匹配的字段
	 *
	 * @param cl 要获取哪个 Class 的被 @VO 注解标注的字段
	 */
	private List<Field> getFieldsByPageIds(Class<?> cl) {
		final Field[] fields = cl.getDeclaredFields();

		List<Field> result = new ArrayList<>(fields.length);
		for (Field field : fields) {
			final VO voAnno = field.getAnnotation(VO.class);
			if (voAnno == null) continue;

			for (String id : voAnno.pageIds()) {
				if (this.pageIds.contains(id)) {
					result.add(field);
				}
			}
		}
		return result;
	}

	/**
	 * 获取与所传入 Lambda 表达式相匹配的字段
	 *
	 * @param cl 要获取哪个 Class 中的字段
	 */
	private List<Field> getFieldsByAttrs(Class<?> cl) {
		final List<Field> result = new ArrayList<>(this.attrs.size());
		if (CollUtil.isEmpty(attrs)) {
			return result;
		}

		for (SFunc<T, ?> attr : attrs) {
			final SerializedLambda sLambda;
			try {
				sLambda = getLambdaInfo(attr);
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			final String implClassName = sLambda.getImplClass().replace("/", ".");
			if (cl.getName().equals(implClassName)) {
				final String methodName = sLambda.getImplMethodName();
				final String fieldName = StrUtil.removePreAndLowerFirst(methodName, "get");
				final Field field;
				try {
					field = cl.getDeclaredField(fieldName);
					result.add(field);
				} catch (NoSuchFieldException e) {
					throw new IllegalArgumentException("找不到" + methodName + "对应属性");
				}
			}
		}
		return result;
	}

	/**
	 * 反射获取 Lambda 表达式运行时信息
	 *
	 * @param func Lambda 表达式
	 */
	private SerializedLambda getLambdaInfo(SFunc<T, ?> func)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final Method method = func.getClass().getDeclaredMethod("writeReplace");
		method.setAccessible(true);
		return (SerializedLambda) method.invoke(func);
	}

	/**
	 * VO 对象访问者
	 * <p> 包装 VO 对象的生成过程，用于控制生成行为 </p>
	 */
	private static class JsonObjVisitor {
		private final JSONObject json;

		public JsonObjVisitor() {
			this.json = new JSONObject(CONFIG);
		}

		/**
		 * 设置 VO 中的属性名
		 * <p> 后续可以通过此方法对生成 VO 中的属性名做扩展 </p>
		 *
		 * @param field Java 属性
		 * @return VO 中的属性名
		 */
		private static String fieldName(Field field) {
			final VO anno = field.getAnnotation(VO.class);
			if (anno != null) {
				final String alias = anno.alias();
				if (StrUtil.isNotBlank(alias)) {
					return alias;
				}
			}
			return field.getName();
		}

		public void put(String k, Object v) {
			this.json.set(k, v);
		}

		public void put(Field k, Object v) {
			this.json.set(fieldName(k), v);
		}

		public void put(Field k, JsonObjVisitor v) {
			this.json.set(fieldName(k), v.get(null));
		}

		public void put(Field k, JsonArrVisitor v) {
			this.json.set(fieldName(k), v.get(null));
		}

		public JSONObject get(Map<String, Object> map) {
			if (CollUtil.isNotEmpty(map)) {
				map.forEach(json::set);
			}
			return json;
		}
	}

	/**
	 * VO 数组访问者
	 * <p> 包装 VO 数组的生成过程，用于控制生成行为 </p>
	 */
	private static class JsonArrVisitor {
		private final JSONArray array;

		public JsonArrVisitor() {
			this.array = new JSONArray(CONFIG);
		}

		public void add(JsonObjVisitor json) {
			this.array.add(json.get(null));
		}

		public JSONArray get(Map<String, Object> map) {
			if (CollUtil.isNotEmpty(map)) {
				for (Object item : array) {
					map.forEach(((JSONObject) item)::set);
				}
			}
			return array;
		}
	}
}
