package cn.hutool.core.lang.tree;

/**
 * 默认的简单转换器
 *
 * @author liangbaikai
 */
public class DefaultConverter implements Convert<Node, TreeNodeMap> {
    @Override
    public void convert(Node object, TreeNodeMap treeNode) {
        treeNode.setId(object.getId());
        treeNode.setParentId(object.getPid());
        treeNode.setWeight(object.getWeight());
        treeNode.setName(object.getName());

        //扩展字段
        // treeNode.extra("other",11);
        // treeNode.extra("other2",object.getXXX);
    }
}
