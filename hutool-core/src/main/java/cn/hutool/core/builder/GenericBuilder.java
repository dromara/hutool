package cn.hutool.core.builder;

import cn.hutool.core.lang.func.Supplier1;
import cn.hutool.core.lang.func.Supplier2;
import cn.hutool.core.lang.func.Supplier3;
import cn.hutool.core.lang.func.Supplier4;
import cn.hutool.core.lang.func.Supplier5;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>通用Builder</p>
 * 参考: <a href="https://blog.csdn.net/weixin_43935907/article/details/105003719">一看就会的java8通用Builder</a>
 * <p>使用方法如下：</p>
 * <pre>
 * Box box = GenericBuilder
 * 		.of(Box::new)
 * 		.with(Box::setId, 1024L)
 * 		.with(Box::setTitle, "Hello World!")
 * 		.with(Box::setLength, 9)
 * 		.with(Box::setWidth, 8)
 * 		.with(Box::setHeight, 7)
 * 		.build();
 *
 * </pre>
 *
 * <p> 我们也可以对已创建的对象进行修改：</p>
 * <pre>
 * Box boxModified = GenericBuilder
 * 		.of(() -&gt; box)
 * 		.with(Box::setTitle, "Hello Friend!")
 * 		.with(Box::setLength, 3)
 * 		.with(Box::setWidth, 4)
 * 		.with(Box::setHeight, 5)
 * 		.build();
 * </pre>
 * <p> 我们还可以对这样调用有参构造，这对于创建一些在有参构造中包含初始化函数的对象是有意义的：</p>
 * <pre>
 * Box box1 = GenericBuilder
 * 		.of(Box::new, 2048L, "Hello Partner!", 222, 333, 444)
 * 		.with(Box::alis)
 * 		.build();
 * </pre>
 * <p>注意：本工具类支持调用的方法的参数数量不超过1个，更多的参数不利于阅读和维护。</p>
 *
 * @author TomXin
 * @since 5.7.21
 */
public class GenericBuilder<T> implements Builder<T> {

	/**
	 * 实例化器
	 */
	private final Supplier<T> instant;

	/**
	 * 修改器列表
	 */
	private final List<Consumer<T>> modifiers = new ArrayList<>();

	/**
	 * 构造
	 *
	 * @param instant 实例化器
	 */
	public GenericBuilder(Supplier<T> instant) {
		this.instant = instant;
	}

	/**
	 * 通过无参数实例化器创建GenericBuilder
	 *
	 * @param instant 实例化器
	 * @param <T>     目标类型
	 * @return GenericBuilder对象
	 */
	public static <T> GenericBuilder<T> of(Supplier<T> instant) {
		return new GenericBuilder<>(instant);
	}

	/**
	 * 通过1参数实例化器创建GenericBuilder
	 *
	 * @param instant 实例化器
	 * @param p1      参数一
	 * @param <T>     目标类型
	 * @param <P1>    参数一类型
	 * @return GenericBuilder对象
	 */
	public static <T, P1> GenericBuilder<T> of(Supplier1<T, P1> instant, P1 p1) {
		return of(instant.toSupplier(p1));
	}

	/**
	 * 通过2参数实例化器创建GenericBuilder
	 *
	 * @param instant 实例化器
	 * @param p1      参数一
	 * @param p2      参数二
	 * @param <T>     目标类型
	 * @param <P1>    参数一类型
	 * @param <P2>    参数二类型
	 * @return GenericBuilder对象
	 */
	public static <T, P1, P2> GenericBuilder<T> of(Supplier2<T, P1, P2> instant, P1 p1, P2 p2) {
		return of(instant.toSupplier(p1, p2));
	}

	/**
	 * 通过3参数实例化器创建GenericBuilder
	 *
	 * @param instant 实例化器
	 * @param p1      参数一
	 * @param p2      参数二
	 * @param p3      参数三
	 * @param <T>     目标类型
	 * @param <P1>    参数一类型
	 * @param <P2>    参数二类型
	 * @param <P3>    参数三类型
	 * @return GenericBuilder对象
	 */
	public static <T, P1, P2, P3> GenericBuilder<T> of(Supplier3<T, P1, P2, P3> instant, P1 p1, P2 p2, P3 p3) {
		return of(instant.toSupplier(p1, p2, p3));
	}

	/**
	 * 通过4参数实例化器创建GenericBuilder
	 *
	 * @param instant 实例化器
	 * @param p1      参数一
	 * @param p2      参数二
	 * @param p3      参数三
	 * @param p4      参数四
	 * @param <T>     目标类型
	 * @param <P1>    参数一类型
	 * @param <P2>    参数二类型
	 * @param <P3>    参数三类型
	 * @param <P4>    参数四类型
	 * @return GenericBuilder对象
	 */
	public static <T, P1, P2, P3, P4> GenericBuilder<T> of(Supplier4<T, P1, P2, P3, P4> instant, P1 p1, P2 p2, P3 p3, P4 p4) {
		return of(instant.toSupplier(p1, p2, p3, p4));
	}

	/**
	 * 通过5参数实例化器创建GenericBuilder
	 *
	 * @param instant 实例化器
	 * @param p1      参数一
	 * @param p2      参数二
	 * @param p3      参数三
	 * @param p4      参数四
	 * @param p5      参数五
	 * @param <T>     目标类型
	 * @param <P1>    参数一类型
	 * @param <P2>    参数二类型
	 * @param <P3>    参数三类型
	 * @param <P4>    参数四类型
	 * @param <P5>    参数五类型
	 * @return GenericBuilder对象
	 */
	public static <T, P1, P2, P3, P4, P5> GenericBuilder<T> of(Supplier5<T, P1, P2, P3, P4, P5> instant, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
		return of(instant.toSupplier(p1, p2, p3, p4, p5));
	}


	/**
	 * 调用无参数方法
	 *
	 * @param consumer 无参数Consumer
	 * @return GenericBuilder对象
	 */
	public GenericBuilder<T> with(Consumer<T> consumer) {
		modifiers.add(consumer);
		return this;
	}


	/**
	 * 调用1参数方法
	 *
	 * @param <P1>     参数一类型
	 * @param consumer 1参数Consumer，一般为Setter方法引用
	 * @param p1       参数一
	 * @return GenericBuilder对象
	 */
	public <P1> GenericBuilder<T> with(BiConsumer<T, P1> consumer, P1 p1) {
		modifiers.add(instant -> consumer.accept(instant, p1));
		return this;
	}

	/**
	 * 构建
	 *
	 * @return 目标对象
	 */
	@Override
	public T build() {
		T value = instant.get();
		modifiers.forEach(modifier -> modifier.accept(value));
		modifiers.clear();
		return value;
	}
}
