/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.management.monitor.MonitorSettingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@link Opt}的单元测试
 *
 * @author VampireAchao
 */
public class OptTest {

	@Test
	public void ofTest() {
		Assertions.assertTrue(Opt.of(Optional.empty()).isEmpty());
		Assertions.assertTrue(Opt.of(Optional.of(1)).isPresent());
	}

	@Test
	public void ofBlankAbleTest() {
		// ofBlankAble相对于ofNullable考虑了字符串为空串的情况
		final String hutool = Opt.ofBlankAble("").orElse("hutool");
		Assertions.assertEquals("hutool", hutool);
	}

	@Test
	public void getTest() {
		// 和原版Optional有区别的是，get不会抛出NoSuchElementException
		// 如果想使用原版Optional中的get这样，获取一个一定不为空的值，则应该使用orElseThrow
		final Object opt = Opt.ofNullable(null).get();
		Assertions.assertNull(opt);
	}

	@Test
	public void isEmptyTest() {
		// 这是jdk11 Optional中的新函数，直接照搬了过来
		// 判断包裹内元素是否为空，注意并没有判断空字符串的情况
		final boolean isEmpty = Opt.empty().isEmpty();
		Assertions.assertTrue(isEmpty);
	}

	@Test
	public void peekTest() {
		final User user = new User();
		// 相当于ifPresent的链式调用
		Opt.ofNullable("hutool").peek(user::setUsername).peek(user::setNickname);
		Assertions.assertEquals("hutool", user.getNickname());
		Assertions.assertEquals("hutool", user.getUsername());

		// 注意，传入的lambda中，对包裹内的元素执行赋值操作并不会影响到原来的元素
		final String name = Opt.ofNullable("hutool").peek(username -> username = "123").peek(username -> username = "456").get();
		Assertions.assertEquals("hutool", name);
	}

	@Test
	public void peeksTest() {
		final User user = new User();
		// 相当于上面peek的动态参数调用，更加灵活，你可以像操作数组一样去动态设置中间的步骤，也可以使用这种方式去编写你的代码
		// 可以一行搞定
		Opt.ofNullable("hutool").peeks(user::setUsername, user::setNickname);
		// 也可以在适当的地方换行使得代码的可读性提高
		Opt.of(user).peeks(
			u -> Assertions.assertEquals("hutool", u.getNickname()),
			u -> Assertions.assertEquals("hutool", u.getUsername())
		);
		Assertions.assertEquals("hutool", user.getNickname());
		Assertions.assertEquals("hutool", user.getUsername());

		// 注意，传入的lambda中，对包裹内的元素执行赋值操作并不会影响到原来的元素,这是java语言的特性。。。
		// 这也是为什么我们需要getter和setter而不直接给bean中的属性赋值中的其中一个原因
		final String name = Opt.ofNullable("hutool").peeks(
			username -> username = "123", username -> username = "456",
			n -> Assertions.assertEquals("hutool", n)).get();
		Assertions.assertEquals("hutool", name);

		// 当然，以下情况不会抛出NPE，但也没什么意义
		Opt.ofNullable("hutool").peeks().peeks().peeks();
		Opt.ofNullable(null).peeks(i -> {
		});

	}

	@Test
	public void orTest() {
		// 这是jdk9 Optional中的新函数，直接照搬了过来
		// 给一个替代的Opt
		final String str = Opt.<String>ofNullable(null).or(() -> Opt.ofNullable("Hello hutool!")).map(String::toUpperCase).orElseThrow();
		Assertions.assertEquals("HELLO HUTOOL!", str);

		final User user = User.builder().username("hutool").build();
		final Opt<User> userOpt = Opt.of(user);
		// 获取昵称，获取不到则获取用户名
		final String name = userOpt.map(User::getNickname).or(() -> userOpt.map(User::getUsername)).get();
		Assertions.assertEquals("hutool", name);
	}

	@Test
	public void orElseThrowTest() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			// 获取一个不可能为空的值，否则抛出NoSuchElementException异常
			final Object obj = Opt.ofNullable(null).orElseThrow();
			Assertions.assertNull(obj);
		});
	}

	@Test
	public void orElseThrowTest2() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			// 获取一个不可能为空的值，否则抛出自定义异常
			final Object assignException = Opt.ofNullable(null).orElseThrow(IllegalStateException::new);
			Assertions.assertNull(assignException);
		});
	}

	@Test
	public void orElseRunTest() {
		// 判断一个值是否为空，为空执行一段逻辑,否则执行另一段逻辑
		final Map<String, Integer> map = new HashMap<>();
		final String key = "key";
		map.put(key, 1);
		Opt.ofNullable(map.get(key))
			.ifPresent(v -> map.put(key, v + 1))
			.orElseRun(() -> map.remove(key));
		Assertions.assertEquals((Object) 2, map.get(key));
	}

	@Test
	public void flattedMapTest() {
		// 和Optional兼容的flatMap
		final List<User> userList = new ArrayList<>();
		// 以前，不兼容
//		Opt.ofNullable(userList).map(List::stream).flatMap(Stream::findFirst);
		// 现在，兼容
		final User user = Opt.ofNullable(userList).map(List::stream)
			.flattedMap(Stream::findFirst).orElseGet(User.builder()::build);
		Assertions.assertNull(user.getUsername());
		Assertions.assertNull(user.getNickname());
	}

	@Test
	public void ofEmptyAbleTest() {
		// 以前，输入一个CollectionUtil感觉要命，类似前缀的类一大堆，代码补全形同虚设(在项目中起码要输入完CollectionUtil才能在第一个调出这个函数)
		// 关键它还很常用，判空和判空集合真的太常用了...
		final List<String> past = Opt.ofNullable(Collections.<String>emptyList()).filter(CollUtil::isNotEmpty).orElseGet(() -> Collections.singletonList("hutool"));
		// 现在，一个ofEmptyAble搞定
		final List<String> hutool = Opt.ofEmptyAble(Collections.<String>emptyList()).orElseGet(() -> Collections.singletonList("hutool"));
		Assertions.assertEquals(past, hutool);
		Assertions.assertEquals(Collections.singletonList("hutool"), hutool);
		Assertions.assertFalse(Opt.ofEmptyAble(Arrays.asList(null, null, null)).isEmpty());
	}

	@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantConditions"})
	@Test
	public void execTest() {
		// 有一些资深的程序员跟我说你这个lambda，双冒号语法糖看不懂...
		// 为了尊重资深程序员的意见，并且提升代码可读性，封装了一下 "try catch NPE 和 数组越界"的情况

		// 以前这种写法，简洁但可读性稍低，对资深程序员不太友好
		final List<String> last = null;
		final String npeSituation = Opt.ofEmptyAble(last).flattedMap(l -> l.stream().findFirst()).orElse("hutool");
		final String indexOutSituation = Opt.ofEmptyAble(last).map(l -> l.get(0)).orElse("hutool");

		// 现在代码整洁度降低，但可读性up，如果再人说看不懂这代码...
		final String npe = Opt.ofTry(() -> last.get(0)).exceptionOrElse("hutool");
		final String indexOut = Opt.ofTry(() -> {
			final List<String> list = new ArrayList<>();
			// 你可以在里面写一长串调用链 list.get(0).getUser().getId()
			return list.get(0);
		}).exceptionOrElse("hutool");

		Assertions.assertInstanceOf(AssertionError.class, Opt.ofTry(() -> {
			throw new AssertionError("");
		}).getThrowable());
		Assertions.assertEquals(npe, npeSituation);
		Assertions.assertEquals(indexOut, indexOutSituation);
		Assertions.assertEquals("hutool", npe);
		Assertions.assertEquals("hutool", indexOut);

		// 多线程下情况测试
		Stream.iterate(0, i -> ++i).limit(20000).parallel().forEach(i -> {
			final Opt<Object> opt = Opt.ofTry(() -> {
				if (i % 2 == 0) {
					throw new IllegalStateException(String.valueOf(i));
				} else {
					throw new NullPointerException(String.valueOf(i));
				}
			});
			Assertions.assertTrue(
				(i % 2 == 0 && opt.getThrowable() instanceof IllegalStateException) ||
					(i % 2 != 0 && opt.getThrowable() instanceof NullPointerException)
			);
		});
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static class User {
		private String username;
		private String nickname;
	}

	@SuppressWarnings({"NumericOverflow", "divzero"})
	@Test
	@Disabled
	void testFail() {
		Opt.ofTry(() -> 1 / 0)
			.ifFail(Console::log)
			.ifFail(Console::log, ArithmeticException.class)
			.ifFail(Console::log, NullPointerException.class)
			.ifFail(Console::log, NullPointerException.class, MonitorSettingException.class)
		;
	}


	@SuppressWarnings({"NumericOverflow", "divzero"})
	@Test
	@Disabled
	void testFail1() {
		final Integer i = Opt.ofTry(() -> 1 / 0)
			.map(e -> 666)
			.ifFail(Console::log)
			.orElseGet(() -> 1);
		Assertions.assertEquals(i, 1);
	}

	@SuppressWarnings({"NumericOverflow", "divzero"})
	@Test
	@Disabled
	void testToEasyStream() {
		final List<Integer> list = Opt.ofTry(() -> 1).toEasyStream().toList();
		Assertions.assertArrayEquals(list.toArray(), new Integer[]{1});
	}

}
