package cn.hutool.core.tree;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.func.*;
import cn.hutool.core.stream.EasyStream;
import cn.hutool.core.text.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 本类是用于构建树的工具类，特点是采取lambda，以及满足指定类型的Bean进行树操作
 * Bean需要满足三个属性：
 * <ul>
 *     <li>包含不为null的主键(例如id)</li>
 *     <li>包含容许为null的关联外键(例如parentId)</li>
 *     <li>包含自身的子集，例如类型为List的children</li>
 * </ul>
 * 本类的构建方法是通过{@code BeanTree.of} 进行构建，例如：
 * <pre>{@code final BeanTree beanTree = BeanTree.of(JavaBean::getId, JavaBean::getParentId, null, JavaBean::getChildren, JavaBean::setChildren);}</pre>
 * 得到的BeanTree实例可以调用toTree方法，将集合转换为树，例如：
 * <pre>{@code final List<JavaBean> javaBeanTree = beanTree.toTree(originJavaBeanList);}</pre>
 * 也可以将已有的树转换为集合，例如：
 * <pre>{@code final List<JavaBean> javaBeanList = beanTree.flat(originJavaBeanTree);}</pre>
 *
 * @author VampireAchao
 * 最后，引用一句电影经典台词： 无处安放的双手，以及无处安放的灵魂。——《Hello!树先生》
 */
public class BeanTree<T, R extends Comparable<R>> {

	/**
	 * 主键getter
	 */
	private final SerFunction<T, R> idGetter;
	/**
	 * 外键getter
	 */
	private final SerFunction<T, R> pidGetter;
	/**
	 * 外键匹配值(保留此属性主要是性能较外键条件匹配稍微好一点)
	 */
	private final R pidValue;
	/**
	 * 外键匹配条件
	 */
	private final SerPredicate<T> parentPredicate;
	/**
	 * 子集getter
	 */
	private final SerFunction<T, List<T>> childrenGetter;
	/**
	 * 子集setter
	 */
	private final SerBiConsumer<T, List<T>> childrenSetter;

	private BeanTree(final SerFunction<T, R> idGetter,
					 final SerFunction<T, R> pidGetter,
					 final R pidValue,
					 final SerPredicate<T> parentPredicate,
					 final SerFunction<T, List<T>> childrenGetter,
					 final SerBiConsumer<T, List<T>> childrenSetter) {
		this.idGetter = Objects.requireNonNull(idGetter, "idGetter must not be null");
		this.pidGetter = Objects.requireNonNull(pidGetter, "pidGetter must not be null");
		this.pidValue = pidValue;
		this.parentPredicate = parentPredicate;
		this.childrenGetter = Objects.requireNonNull(childrenGetter, "childrenGetter must not be null");
		this.childrenSetter = Objects.requireNonNull(childrenSetter, "childrenSetter must not be null");
	}

	/**
	 * 构建BeanTree
	 *
	 * @param idGetter       主键getter，例如 {@code JavaBean::getId}
	 * @param pidGetter      外键getter，例如 {@code JavaBean::getParentId}
	 * @param pidValue       根节点的外键值，例如 {@code null}
	 * @param childrenGetter 子集getter，例如 {@code JavaBean::getChildren}
	 * @param childrenSetter 子集setter，例如 {@code JavaBean::setChildren}
	 * @param <T>            Bean类型
	 * @param <R>            主键、外键类型
	 * @return BeanTree
	 */
	public static <T, R extends Comparable<R>> BeanTree<T, R> of(final SerFunction<T, R> idGetter,
																 final SerFunction<T, R> pidGetter,
																 final R pidValue,
																 final SerFunction<T, List<T>> childrenGetter,
																 final SerBiConsumer<T, List<T>> childrenSetter) {
		return new BeanTree<>(idGetter, pidGetter, pidValue, null, childrenGetter, childrenSetter);
	}

	/**
	 * 构建BeanTree
	 *
	 * @param idGetter        主键getter，例如 {@code JavaBean::getId}
	 * @param pidGetter       外键getter，例如 {@code JavaBean::getParentId}
	 * @param parentPredicate 根节点判断条件，例如 {@code o -> Objects.isNull(o.getParentId())}
	 * @param childrenGetter  子集getter，例如 {@code JavaBean::getChildren}
	 * @param childrenSetter  子集setter，例如 {@code JavaBean::setChildren}
	 * @param <T>             Bean类型
	 * @param <R>             主键、外键类型
	 * @return BeanTree
	 */
	public static <T, R extends Comparable<R>> BeanTree<T, R> ofMatch(final SerFunction<T, R> idGetter,
																	  final SerFunction<T, R> pidGetter,
																	  final SerPredicate<T> parentPredicate,
																	  final SerFunction<T, List<T>> childrenGetter,
																	  final SerBiConsumer<T, List<T>> childrenSetter) {
		return new BeanTree<>(idGetter, pidGetter, null, Objects.requireNonNull(parentPredicate, "parentPredicate must not be null"), childrenGetter, childrenSetter);
	}

	/**
	 * 将集合转换为树
	 *
	 * @param list 集合
	 * @return 转换后的树
	 */
	public List<T> toTree(final List<T> list) {
		if (Objects.isNull(parentPredicate)) {
			final Map<R, List<T>> pIdValuesMap = EasyStream.of(list)
					.peek(e -> Objects.requireNonNull(idGetter.apply(e),
							() -> StrUtil.format("primary key {} must not null", LambdaUtil.getFieldName(idGetter))
					)).group(pidGetter);
			final List<T> parents = pIdValuesMap.getOrDefault(pidValue, new ArrayList<>());
			findChildren(list, pIdValuesMap);
			return parents;
		}
		final List<T> parents = new ArrayList<>(list.size());
		final Map<R, List<T>> pIdValuesMap = EasyStream.of(list).peek(e -> {
			if (parentPredicate.test(e)) {
				parents.add(e);
			}
			Objects.requireNonNull(idGetter.apply(e));
		}).group(pidGetter);
		findChildren(list, pIdValuesMap);
		return parents;
	}

	/**
	 * 将树扁平化为集合，相当于将树里的所有节点都放到一个集合里
	 *
	 * @param tree 树
	 * @return 集合
	 */
	@SuppressWarnings("unchecked")
	public List<T> flat(final List<T> tree) {
		final AtomicReference<Function<T, EasyStream<T>>> recursiveRef = new AtomicReference<>();
		final Function<T, EasyStream<T>> recursive = e -> EasyStream.of(childrenGetter.apply(e)).flat(recursiveRef.get()).unshift(e);
		recursiveRef.set(recursive);
		return EasyStream.of(tree).flat(recursive).peek(e -> childrenSetter.accept(e, null)).toList();
	}

	/**
	 * 树的过滤操作，本方法一般适用于寻找某人所在部门以及所有上级部门类似的逻辑
	 * 通过{@link SerPredicate}指定的过滤规则，本节点或子节点满足过滤条件，则保留当前节点，否则抛弃节点及其子节点<br>
	 * 即，一条路径上只要有一个节点符合条件，就保留整条路径上的节点
	 *
	 * @param tree      树
	 * @param condition 节点过滤规则函数，只需处理本级节点本身即可，{@link SerPredicate#test(Object)}为{@code true}保留
	 * @return 过滤后的树
	 */
	public List<T> filter(final List<T> tree, final SerPredicate<T> condition) {
		Objects.requireNonNull(condition, "filter condition must be not null");
		final AtomicReference<Predicate<T>> recursiveRef = new AtomicReference<>();
		final Predicate<T> recursive = SerPredicate.multiOr(condition::test,
				e -> Opt.ofEmptyAble(childrenGetter.apply(e))
						.map(children -> EasyStream.of(children).filter(recursiveRef.get()).toList())
						.peek(children -> childrenSetter.accept(e, children))
						.filter(s -> !s.isEmpty()).isPresent());
		recursiveRef.set(recursive);
		return EasyStream.of(tree).filter(recursive).toList();
	}

	/**
	 * 树节点遍历操作
	 *
	 * @param tree   数
	 * @param action 操作
	 * @return 树
	 */
	public List<T> forEach(final List<T> tree, final SerConsumer<T> action) {
		Objects.requireNonNull(action, "action must be not null");
		final AtomicReference<Consumer<T>> recursiveRef = new AtomicReference<>();
		final Consumer<T> recursive = SerConsumer.multi(action::accept,
				e -> Opt.ofEmptyAble(childrenGetter.apply(e))
						.peek(children -> EasyStream.of(children).forEach(recursiveRef.get())));
		recursiveRef.set(recursive);
		EasyStream.of(tree).forEach(recursive);
		return tree;
	}

	/**
	 * 内联函数，获取子集并设置到父节点
	 *
	 * @param list         集合
	 * @param pIdValuesMap 父id与子集的映射
	 */
	private void findChildren(final List<T> list, final Map<R, List<T>> pIdValuesMap) {
		EasyStream.of(list).forEach(value -> {
			final List<T> children = pIdValuesMap.get(idGetter.apply(value));
			if (children != null) {
				childrenSetter.accept(value, children);
			}
		});
	}

}
