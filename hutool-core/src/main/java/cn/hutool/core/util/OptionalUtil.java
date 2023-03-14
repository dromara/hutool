package cn.hutool.core.util;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * 链式多判断表达式
 *
 * 参考 {@link java.util.Optional}判空类做出的扩展
 * 扩展原 {@link java.util.Optional} 增加函数式表达 可自由根据其它条件判断是否返回null
 *
 *
 * <p>通过 forElse方法 可实现直接写入值或函数式</p>
 * <ul>
 *     <li>{@link OptionalUtil#forElse(E)}</li>
 *     <li>{@link OptionalUtil#forElse(Supplier)}</li>
 * </ul>
 *
 *
 * <p>组合使用</p>
 * <pre>
 * String s = new OptionalUtil<String>()
 *     .forElse(() -> {
 *         // 可写其它逻辑
 *         String a = null;
 *         return a;
 * 	   })
 * 	   .forElse("str02")
 * 	   .get(); // get() 支持传入布尔值 默认false  false表示最终forElse都取到的是null 则抛出{@link NoSuchElementException}异常
 * System.out.println(s); // str02
 * </pre>
 *
 * <p>多函数组合使用</p>
 * <pre>
 * List<Entity> list = new OptionalUtil<List<Entity>>()
 *     .forElse(() -> {
 *         List<Entity> tempList = getList1();
 *         return (Objects.isNull(tempList) || tempList.size() == 0) ? null : tempList; // 满足条数不为空 才用他
 *     })
 *     .forElse(() -> {
 *         List<Entity> tempList = getList2();
 *         if (Objects.isNull(tempList) || tempList.size() == 0) {
 *             return null;
 *         }
 *         return tempList.size() != 10 ? tempList : null; // 满足条数为10条 才用他
 *      })
 *     .get(true); // 允许最终结果为Null值
 * </pre>
 *
 * @author Xiwi
 */
public final class OptionalUtil<E> {

    private final E value;

    OptionalUtil() {
        this.value = null;
    }
    OptionalUtil(E value) {
        this.value = value;
    }

	/**
	 * <pre>
	 * String s = new OptionalUtil<String>()
	 *            .forElse("a")
	 *            .forElse("b")
	 *            .get();
	 * </pre>
	 *
	 * @param other 要取的值
	 * @return 返回链式 最终取值调用 {@link OptionalUtil#get()}
	 */
    public OptionalUtil<E> forElse(E other) {
        return this.value != null ? this : new OptionalUtil<>(other);
    }

	/**
	 * <pre>
	 * String s = new OptionalUtil<String>()
	 *            .forElse(() -> "a")
	 *            .forElse(() -> {
	 *                String a = "b";
	 *                return a;
	 *            }))
	 *            .get();
	 * </pre>
	 *
	 * @param other 要取的值
	 * @return 返回链式 最终取值调用 {@link OptionalUtil#get()}
	 */
    public OptionalUtil<E> forElse(Supplier<E> other) {
        return this.value != null ? this : new OptionalUtil<>(other.get());
    }

	/**
	 * 获取链式结果
	 * 默认为false 值为null则抛出异常
	 * @return E 值
	 */
    public E get() {
        return this.get(false);
    }
	/**
	 * 获取链式结果
	 * @param allowNullValue 是否允许返回空值 默认false
	 * @return E 值
	 */
    public E get(boolean allowNullValue) {
        if (!allowNullValue && this.value == null) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }
}
