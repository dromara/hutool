package cn.hutool.core.annotation;

/**
 * 子节点判断函数接口
 *
 * @author shadow
 * @version 5.4.1
 * @since 5.4.1
 */
@FunctionalInterface
public interface LeafDecide<T> {

    /**
     * 是否为子节点
     *
     * @param root root target
     * @param leaf compare target
     * @return 是否从属于 root的子节点
     */
    boolean isLeaf(T root, T leaf);
}
