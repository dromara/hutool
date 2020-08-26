package cn.hutool.core.annotation;

/**
 * 根判定函数接口
 *
 * @author shadow
 * @version 5.4.1
 * @since 5.4.1
 */
@FunctionalInterface
public interface RootDecide<T> {

    /**
     * 判断是否为根
     *
     * @param root 根节点
     * @return 是否是根节点
     */
    boolean isRoot(T root);
}
