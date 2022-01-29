package cn.hutool.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>通用Builder</p>
 * 参考: <a href="https://blog.csdn.net/weixin_43935907/article/details/105003719">一看就会的java8通用Builder</a>
 * <p>使用方法如下：</p>
 * <pre>
 * Box box = GenericBuilder
 * 		      .of(Box::new)
 * 		      .with(Box::setId, 1024L)
 * 		      .with(Box::setTitle, "Hello World!")
 * 		      .with(Box::setLength, 9)
 * 		      .with(Box::setWidth, 8)
 * 		      .with(Box::setHeight, 7)
 * 		      .build();
 *
 * </pre>
 *
 * <p> 我们也可以对已创建的对象进行修改：</p>
 * <pre>
 * Box boxModified = GenericBuilder
 * 		      .of(() -&gt; box)
 * 		      .with(Box::setTitle, "Hello Friend!")
 * 		      .with(Box::setLength, 3)
 * 		      .with(Box::setWidth, 4)
 * 		      .with(Box::setHeight, 5)
 * 		      .build();
 * </pre>
 *
 * @author TomXin
 * @since jdk1.8
 */
public class GenericBuilder<T> implements Builder<T> {

	/**
	 * 实例化器
	 */
	private final Supplier<T> instantiator;

	/**
	 * 修改器集合
	 */
	private final List<Consumer<T>> modifiers = new ArrayList<>();

	/**
	 * 构造
	 *
	 * @param instant 实例化器
	 */
	public GenericBuilder(Supplier<T> instant) {
		this.instantiator = instant;
	}

	/**
	 * 通过实例化器创建GenericBuilder
	 *
	 * @param instant 实例化器
	 * @param <T>     目标类型
	 * @return GenericBuilder对象
	 */
	public static <T> GenericBuilder<T> of(Supplier<T> instant) {
		return new GenericBuilder<>(instant);
	}

	/**
	 * 调用1参数方法
	 *
	 * @param consumer 1参数Consumer
	 * @param p1       参数一
	 * @param <P1>     参数一类型
	 * @return GenericBuilder对象
	 */
	public <P1> GenericBuilder<T> with(Consumer1<T, P1> consumer, P1 p1) {
		Consumer<T> c = instance -> consumer.accept(instance, p1);
		modifiers.add(c);
		return this;
	}

	/**
	 * 调用2参数方法
	 *
	 * @param consumer 2参数Consumer
	 * @param p1       参数一
	 * @param p2       参数二
	 * @param <P1>     参数一类型
	 * @param <P2>     参数二类型
	 * @return GenericBuilder对象
	 */
	public <P1, P2> GenericBuilder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
		modifiers.add(c);
		return this;
	}

	/**
	 * 调用3参数方法
	 *
	 * @param consumer 3参数Consumer
	 * @param p1       参数一
	 * @param p2       参数二
	 * @param p3       参数三
	 * @param <P1>     参数一类型
	 * @param <P2>     参数二类型
	 * @param <P3>     参数三类型
	 * @return GenericBuilder对象
	 */
	public <P1, P2, P3> GenericBuilder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
		modifiers.add(c);
		return this;
	}

	/**
	 * 调用4参数方法
	 *
	 * @param consumer 4参数Consumer
	 * @param p1       参数一
	 * @param p2       参数二
	 * @param p3       参数三
	 * @param p4       参数四
	 * @param <P1>     参数一类型
	 * @param <P2>     参数二类型
	 * @param <P3>     参数三类型
	 * @param <P4>     参数四类型
	 * @return GenericBuilder对象
	 */
	public <P1, P2, P3, P4> GenericBuilder<T> with(Consumer4<T, P1, P2, P3, P4> consumer, P1 p1, P2 p2, P3 p3, P4 p4) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3, p4);
		modifiers.add(c);
		return this;
	}

	/**
	 * 调用5参数方法
	 *
	 * @param consumer 5参数Consumer
	 * @param p1       参数一
	 * @param p2       参数二
	 * @param p3       参数三
	 * @param p4       参数四
	 * @param p5       参数五
	 * @param <P1>     参数一类型
	 * @param <P2>     参数二类型
	 * @param <P3>     参数三类型
	 * @param <P4>     参数四类型
	 * @param <P5>     参数五类型
	 * @return GenericBuilder对象
	 */
	public <P1, P2, P3, P4, P5> GenericBuilder<T> with(Consumer5<T, P1, P2, P3, P4, P5> consumer, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3, p4, p5);
		modifiers.add(c);
		return this;
	}

	/**
	 * 构建
	 *
	 * @return 目标对象
	 */
	@Override
	public T build() {
		T value = instantiator.get();
		modifiers.forEach(modifier -> modifier.accept(value));
		modifiers.clear();
		return value;
	}

	/**
	 * 1参数Consumer
	 */
	@FunctionalInterface
	public interface Consumer1<T, P1> {
		/**
		 * 接收参数方法
		 *
		 * @param t  对象
		 * @param p1 参数二
		 */
		void accept(T t, P1 p1);
	}

	/**
	 * 2参数Consumer
	 */
	@FunctionalInterface
	public interface Consumer2<T, P1, P2> {
		/**
		 * 接收参数方法
		 *
		 * @param t  对象
		 * @param p1 参数一
		 * @param p2 参数二
		 */
		void accept(T t, P1 p1, P2 p2);
	}

	/**
	 * 3参数Consumer
	 */
	@FunctionalInterface
	public interface Consumer3<T, P1, P2, P3> {
		/**
		 * 接收参数方法
		 *
		 * @param t  对象
		 * @param p1 参数一
		 * @param p2 参数二
		 * @param p3 参数三
		 */
		void accept(T t, P1 p1, P2 p2, P3 p3);
	}

	/**
	 * 4参数Consumer
	 */
	@FunctionalInterface
	public interface Consumer4<T, P1, P2, P3, P4> {
		/**
		 * 接收参数方法
		 *
		 * @param t  对象
		 * @param p1 参数一
		 * @param p2 参数二
		 * @param p3 参数三
		 * @param p4 参数四
		 */
		void accept(T t, P1 p1, P2 p2, P3 p3, P4 p4);
	}

	/**
	 * 5参数Consumer
	 */
	@FunctionalInterface
	public interface Consumer5<T, P1, P2, P3, P4, P5> {
		/**
		 * 接收参数方法
		 *
		 * @param t  对象
		 * @param p1 参数一
		 * @param p2 参数二
		 * @param p3 参数三
		 * @param p4 参数四
		 * @param p5 参数五
		 */
		void accept(T t, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
	}
}
