package cn.hutool.web.gen;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 支持序列化的 Function
 *
 * <p> 参考 MyBatis-Plus </p>
 */
@FunctionalInterface
public interface SFunc<T, R> extends Function<T, R>,  Serializable {
}
