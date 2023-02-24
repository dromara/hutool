package cn.hutool.core.bean.filler;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 填充工具类
 * 多服务调用或者多库查询时候，聚合数据使用
 * 比如订单服务查询时，订单的用户相关参数需要填充，通过工具类可简化中间步骤
 *
 * @author pursue-wind
 */
public class Filler {

	/**
	 * @param collectionSupplier    原集合
	 * @param collectionIdQueryData 根据 getter1 getter2 的值查询其他属性的函数
	 * @param getter                原集合属性取出函数
	 * @param setter                原集合属性赋值函数
	 * @param <IN>                  原集合泛型
	 * @param <ID>                  原集合属性取出类型
	 * @param <R>                   collectionIdQueryData 根据取出类型查询返回类型
	 */
	public static <IN, ID, R> void fill(Supplier<Collection<IN>> collectionSupplier,
										Function<Collection<ID>, Map<ID, R>> collectionIdQueryData,
										Function<IN, ID> getter,
										BiConsumer<IN, R> setter) {
		Collection<IN> collection = collectionSupplier.get();
		Collection<ID> ids = collection.stream().map(getter).filter(Objects::nonNull).collect(Collectors.toSet());
		Map<ID, R> map = collectionIdQueryData.apply(ids);
		collection.forEach(in ->
				Optional.ofNullable(map.get(getter.apply(in)))
						.ifPresent(r -> setter.accept(in, r)));
	}


	/**
	 * @param collectionSupplier    原集合
	 * @param collectionIdQueryData 根据 getter1 getter2 的值查询其他属性的函数
	 * @param getter1               原集合属性取出函数1
	 * @param setter1               原集合属性赋值函数1
	 * @param getter2               原集合属性取出函数2
	 * @param setter2               原集合属性赋值函数2
	 * @param <IN>                  原集合泛型
	 * @param <ID>                  原集合属性取出类型
	 * @param <R>                   collectionIdQueryData 根据取出类型查询返回类型
	 */
	public static <IN, ID, R> void fill(
			Supplier<Collection<IN>> collectionSupplier,
			Function<Collection<ID>, Map<ID, R>> collectionIdQueryData,
			Function<IN, ID> getter1, BiConsumer<IN, R> setter1,
			Function<IN, ID> getter2, BiConsumer<IN, R> setter2
	) {
		Map<Function<IN, ID>, BiConsumer<IN, R>> getterSetterMap = new HashMap<Function<IN, ID>, BiConsumer<IN, R>>() {{
			put(getter1, setter1);
			put(getter2, setter2);
		}};
		fill(collectionSupplier, getterSetterMap, collectionIdQueryData);
	}

	/**
	 * @param collectionSupplier    原集合
	 * @param collectionIdQueryData 根据 getter1 getter2 的值查询其他属性的函数
	 * @param getter1               原集合属性取出函数1
	 * @param setter1               原集合属性赋值函数1
	 * @param getter2               原集合属性取出函数2
	 * @param setter2               原集合属性赋值函数2
	 * @param getter2               原集合属性取出函数3
	 * @param setter2               原集合属性赋值函数3
	 * @param <IN>                  原集合泛型
	 * @param <ID>                  原集合属性取出类型
	 * @param <R>                   collectionIdQueryData 根据取出类型查询返回类型
	 */
	public static <IN, ID, R> void fill(Supplier<Collection<IN>> collectionSupplier,
										Function<Collection<ID>, Map<ID, R>> collectionIdQueryData,
										Function<IN, ID> getter1, BiConsumer<IN, R> setter1,
										Function<IN, ID> getter2, BiConsumer<IN, R> setter2,
										Function<IN, ID> getter3, BiConsumer<IN, R> setter3
	) {
		Map<Function<IN, ID>, BiConsumer<IN, R>> getterSetterMap = new HashMap<Function<IN, ID>, BiConsumer<IN, R>>() {{
			put(getter1, setter1);
			put(getter2, setter2);
			put(getter3, setter3);
		}};

		fill(collectionSupplier, getterSetterMap, collectionIdQueryData);
	}

	/**
	 * @param collectionSupplier    原集合
	 * @param collectionIdQueryData 根据 getter1 getter2 的值查询其他属性的函数
	 * @param getterSetterMap       {原集合属性取出函数:原集合属性赋值函数}
	 * @param <IN>                  原集合泛型
	 * @param <ID>                  原集合属性取出类型
	 * @param <R>                   collectionIdQueryData 根据取出类型查询返回类型
	 */
	public static <IN, ID, R> void fill(Supplier<Collection<IN>> collectionSupplier,
										Map<Function<IN, ID>, BiConsumer<IN, R>> getterSetterMap,
										Function<Collection<ID>, Map<ID, R>> collectionIdQueryData) {
		Collection<IN> collection = collectionSupplier.get();
		if (null == collection || collection.isEmpty()) {
			return;
		}
		Collection<ID> ids = getterSetterMap.keySet().stream()
				.map(getter -> collection.stream().map(getter).filter(Objects::nonNull))
				.flatMap(Stream::distinct)
				.collect(Collectors.toSet());
		Map<ID, R> map = collectionIdQueryData.apply(ids);
		getterSetterMap.forEach((getter, setter) ->
				collection.forEach(in ->
						Optional.ofNullable(map.get(getter.apply(in)))
								.ifPresent(r -> setter.accept(in, r))));
	}

	public static <IN, ID, R> void fillMultiValueString(Supplier<Collection<IN>> collectionSupplier,
														Function<IN, ID> collectionIdGetter,
														Function<Collection<ID>, Map<ID, R>> collectionIdQueryData,
														Map<Function<R, String>, BiConsumer<IN, String>> queryDataGetterAndCollectionSetter
	) {
		fillMultiValue(collectionSupplier, collectionIdGetter, collectionIdQueryData, queryDataGetterAndCollectionSetter);
	}

	/**
	 * 填充多个值
	 *
	 * @param collectionSupplier                 原集合
	 * @param collectionIdGetter                 集合映射原属性
	 * @param collectionIdQueryData              根据 collectionIdGetter 的值查询其他属性的函数
	 * @param queryDataGetterAndCollectionSetter collectionIdQueryData为对象或需要转换时，取值和设值函数映射
	 * @param <IN>                               原集合泛型
	 * @param <ID>                               原集合属性取出类型
	 * @param <R>                                collectionIdQueryData 根据取出类型查询返回类型
	 * @param <S>                                将 collectionIdQueryData 的 R 类型进行转换为赋值给原集合的类型
	 */
	public static <IN, ID, R, S> void fillMultiValue(Supplier<Collection<IN>> collectionSupplier,
													 Function<IN, ID> collectionIdGetter,
													 Function<Collection<ID>, Map<ID, R>> collectionIdQueryData,
													 Map<Function<R, S>, BiConsumer<IN, S>> queryDataGetterAndCollectionSetter
	) {
		Collection<IN> collection = collectionSupplier.get();
		if (null == collection || collection.isEmpty()) {
			return;
		}

		Collection<ID> ids = collection.stream().map(collectionIdGetter).filter(Objects::nonNull).collect(Collectors.toSet());

		Map<ID, R> map = collectionIdQueryData.apply(ids);
		queryDataGetterAndCollectionSetter
				.forEach((queryDataGetter, collectionSetter) ->
						collection.forEach(in ->
								Optional.ofNullable(map.get(collectionIdGetter.apply(in)))
										.ifPresent(r -> collectionSetter.accept(in, queryDataGetter.apply(r)))));
	}
}
