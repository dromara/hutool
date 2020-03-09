package cn.hutool.core.lang.tree;

/**
 * 树节点转换器 可以参考{{@link DefaultConverter}}
 *
 * @author liangbaikai
 */
public interface Convert<T, TreeNodeMap> {
    /**
     * @param object   源数据实体
     * @param treeNode 树节点实体
     */
    void convert(T object, TreeNodeMap treeNode);
}

