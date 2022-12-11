package cn.hutool.core.lang.func;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author nasodaengineer
 */
public class LambdaFactoryTest {

	@Test(expected = RuntimeException.class)
	public void testMethodNotMatch() {
		LambdaFactory.buildLambda(Function.class, Something.class, "setId", Long.class);
	}

	@Test
	public void buildLambdaTest() {
		Something something = new Something();
		something.setId(1L);
		something.setName("name");

		Function<Something, Long> get11 = LambdaFactory.buildLambda(Function.class, Something.class, "getId");
		Function<Something, Long> get12 = LambdaFactory.buildLambda(Function.class, Something.class, "getId");

		Assert.assertEquals(get11, get12);
		Assert.assertEquals(something.getId(), get11.apply(something));

		String name = "sname";
		BiConsumer<Something, String> set = LambdaFactory.buildLambda(BiConsumer.class, Something.class, "setName", String.class);
		set.accept(something, name);

		Assert.assertEquals(something.getName(), name);
	}

	@Data
	private static class Something {
		private Long id;
		private String name;
	}

	/**
	 * 简单的性能测试，大多数情况下直接调用 快于 lambda 快于 反射 快于 代理
	 *
	 * @author nasodaengineer
	 */
	@RunWith(Parameterized.class)
	public static class PerformanceTest {

		@Parameterized.Parameter
		public int count;

		@Parameterized.Parameters
		public static Collection<Integer> parameters() {
			return ListUtil.of(1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000);
		}

		/**
		 * <p>hardCode   运行1次耗时 4600 ns
		 * <p>lambda     运行1次耗时 5400 ns
		 * <p>reflect    运行1次耗时 7100 ns
		 * <p>proxy      运行1次耗时 145400 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行10次耗时 1200 ns
		 * <p>lambda     运行10次耗时 1200 ns
		 * <p>proxy      运行10次耗时 10800 ns
		 * <p>reflect    运行10次耗时 20100 ns
		 * <p>--------------------------------------------
		 * <p>lambda     运行100次耗时 6300 ns
		 * <p>hardCode   运行100次耗时 6400 ns
		 * <p>proxy      运行100次耗时 65100 ns
		 * <p>reflect    运行100次耗时 196800 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行1000次耗时 54100 ns
		 * <p>lambda     运行1000次耗时 82000 ns
		 * <p>reflect    运行1000次耗时 257300 ns
		 * <p>proxy      运行1000次耗时 822700 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行10000次耗时 84400 ns
		 * <p>lambda     运行10000次耗时 209200 ns
		 * <p>reflect    运行10000次耗时 1024300 ns
		 * <p>proxy      运行10000次耗时 1467300 ns
		 * <p>--------------------------------------------
		 * <p>lambda     运行100000次耗时 618700 ns
		 * <p>hardCode   运行100000次耗时 675200 ns
		 * <p>reflect    运行100000次耗时 914100 ns
		 * <p>proxy      运行100000次耗时 2745800 ns
		 * <p>--------------------------------------------
		 * <p>lambda     运行1000000次耗时 5342500 ns
		 * <p>hardCode   运行1000000次耗时 5616400 ns
		 * <p>reflect    运行1000000次耗时 9176700 ns
		 * <p>proxy      运行1000000次耗时 15801800 ns
		 * <p>--------------------------------------------
		 * <p>lambda     运行10000000次耗时 53415200 ns
		 * <p>hardCode   运行10000000次耗时 63714500 ns
		 * <p>proxy      运行10000000次耗时 116420900 ns
		 * <p>reflect    运行10000000次耗时 120817900 ns
		 * <p>--------------------------------------------
		 * <p>lambda     运行100000000次耗时 546706600 ns
		 * <p>hardCode   运行100000000次耗时 557174500 ns
		 * <p>reflect    运行100000000次耗时 924166200 ns
		 * <p>proxy      运行100000000次耗时 1862735900 ns
		 * <p>--------------------------------------------
		 */
		@Test
		@SneakyThrows
		public void lambdaGetPerformanceTest() {
			Something something = new Something();
			something.setId(1L);
			something.setName("name");
			Method getByReflect = Something.class.getMethod("getId");
			Function getByProxy = MethodHandleProxies.asInterfaceInstance(Function.class, MethodHandles.lookup().unreflect(getByReflect));
			Function getByLambda = LambdaFactory.buildLambda(Function.class, getByReflect);
			Task lambdaTask = new Task("lambda", () -> getByLambda.apply(something));
			Task proxyTask = new Task("proxy", () -> getByProxy.apply(something));
			Task reflectTask = new Task("reflect", () -> getByReflect.invoke(something));
			Task hardCodeTask = new Task("hardCode", () -> something.getId());
			Task[] tasks = {hardCodeTask, lambdaTask, proxyTask, reflectTask};
			loop(count, tasks);
		}

		/**
		 * <p>hardCode   运行1次耗时 4800 ns
		 * <p>lambda     运行1次耗时 9100 ns
		 * <p>reflect    运行1次耗时 20600 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行10次耗时 1800 ns
		 * <p>lambda     运行10次耗时 2100 ns
		 * <p>reflect    运行10次耗时 24500 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行100次耗时 15700 ns
		 * <p>lambda     运行100次耗时 17500 ns
		 * <p>reflect    运行100次耗时 418200 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行1000次耗时 101700 ns
		 * <p>lambda     运行1000次耗时 157200 ns
		 * <p>reflect    运行1000次耗时 504900 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行10000次耗时 360800 ns
		 * <p>lambda     运行10000次耗时 371700 ns
		 * <p>reflect    运行10000次耗时 1887600 ns
		 * <p>--------------------------------------------
		 * <p>lambda     运行100000次耗时 581500 ns
		 * <p>hardCode   运行100000次耗时 1629900 ns
		 * <p>reflect    运行100000次耗时 1781700 ns
		 * <p>--------------------------------------------
		 * <p>lambda     运行1000000次耗时 175400 ns
		 * <p>hardCode   运行1000000次耗时 2045400 ns
		 * <p>reflect    运行1000000次耗时 14363200 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行10000000次耗时 60149000 ns
		 * <p>lambda     运行10000000次耗时 60502600 ns
		 * <p>reflect    运行10000000次耗时 187412800 ns
		 * <p>--------------------------------------------
		 * <p>hardCode   运行100000000次耗时 562997300 ns
		 * <p>lambda     运行100000000次耗时 564359700 ns
		 * <p>reflect    运行100000000次耗时 1163617600 ns
		 * --------------------------------------------
		 */
		@Test
		@SneakyThrows
		public void lambdaSetPerformanceTest() {
			Something something = new Something();
			something.setId(1L);
			something.setName("name");
			Method setByReflect = Something.class.getMethod("setName", String.class);
			BiConsumer setByLambda = LambdaFactory.buildLambda(BiConsumer.class, setByReflect);
			String name = "name1";
			Task lambdaTask = new Task("lambda", () -> {
				setByLambda.accept(something, name);
				return null;
			});
			Task reflectTask = new Task("reflect", () -> {
				setByReflect.invoke(something, name);
				return null;
			});
			Task hardCodeTask = new Task("hardCode", () -> {
				something.setName(name);
				return null;
			});
			Task[] tasks = {hardCodeTask, lambdaTask, reflectTask};
			loop(count, tasks);
		}

		@SneakyThrows
		private void loop(int count, Task... tasks) {
			Arrays.stream(tasks)
					.peek(task -> {
						LambdaFactoryTest.SupplierThrowable runnable = task.getRunnable();
						long cost = System.nanoTime();
						for (int i = 0; i < count; i++) {
							runnable.get();
						}
						cost = System.nanoTime() - cost;
						task.setCost(cost);
						task.setCount(count);
					})
					.sorted(Comparator.comparing(Task::getCost))
					.map(Task::format)
					.forEach(System.out::println);
			System.out.println("--------------------------------------------");
		}

		@Getter
		private class Task {
			private String name;
			private LambdaFactoryTest.SupplierThrowable<?> runnable;
			@Setter
			private long cost;
			@Setter
			private Integer count;

			public Task(String name, LambdaFactoryTest.SupplierThrowable<?> runnable) {
				this.name = name;
				this.runnable = runnable;
			}

			public String format() {
				return String.format("%-10s 运行%d次耗时 %d ns", name, count, cost);
			}
		}

	}

	@FunctionalInterface
	interface SupplierThrowable<T> {
		T get0() throws Throwable;

		default T get() {
			try {
				return get0();
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
	}
}
