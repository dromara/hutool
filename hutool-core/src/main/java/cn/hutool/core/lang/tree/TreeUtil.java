package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 树工具类 可参考  cn.hutool.core.lang.TreeTest
 *
 * @author liangbaikai
 */
public class TreeUtil {

    /**
     * 树构建
     */
    public static List<TreeNodeMap> build(List<Node> list, Object parentId) {
        return build(list, parentId, TreeNodeConfig.getDefaultConfig(), new DefaultConverter());
    }

    /**
     * 树构建
     */
    public static <T> List<TreeNodeMap> build(List<T> list, Object parentId, Convert<T, TreeNodeMap> convert) {
        return build(list, parentId, TreeNodeConfig.getDefaultConfig(), convert);
    }

    /**
     * 树构建
     *
     * @param list           源数据集合
     * @param parentId       最顶层父id值 一般为 0 之类
     * @param treeNodeConfig 配置
     * @param convert        转换器
     * @param <T>            转换的实体 为数据源里的对象类型
     * @return
     */
    public static <T> List<TreeNodeMap> build(List<T> list, Object parentId, TreeNodeConfig treeNodeConfig, Convert<T, TreeNodeMap> convert) {
        List<TreeNodeMap> treeNodes = CollectionUtil.newArrayList();
        for (T obj : list) {
            TreeNodeMap treeNode = new TreeNodeMap(treeNodeConfig);
            convert.convert(obj, treeNode);
            treeNodes.add(treeNode);
        }

        List<TreeNodeMap> finalTreeNodes = CollectionUtil.newArrayList();
        for (TreeNodeMap treeNode : treeNodes) {
            if (parentId.equals(treeNode.getParentId())) {
                finalTreeNodes.add(treeNode);
                innerBuild(treeNodes, treeNode, 0, treeNodeConfig.getDeep());
            }
        }
        // 内存每层已经排过了 这是最外层排序
        finalTreeNodes = finalTreeNodes.stream().sorted().collect(Collectors.toList());
        return finalTreeNodes;
    }

    /**
     * 递归处理
     *
     * @param treeNodes  数据集合
     * @param parentNode 当前节点
     * @param deep       已递归深度
     * @param maxDeep    最大递归深度 可能为null即不限制
     */
    private static void innerBuild(List<TreeNodeMap> treeNodes, TreeNodeMap parentNode, int deep, Integer maxDeep) {

        if (CollectionUtil.isEmpty(treeNodes)) {
            return;
        }
        //maxDeep 可能为空
        if (maxDeep != null && deep >= maxDeep) {
            return;
        }

        // 每层排序 TreeNodeMap 实现了Comparable接口
        treeNodes = treeNodes.stream().sorted().collect(Collectors.toList());
        for (TreeNodeMap childNode : treeNodes) {
            if (parentNode.getId().equals(childNode.getParentId())) {
                List<TreeNodeMap> children = parentNode.getChildren();
                if (children == null) {
                    children = CollectionUtil.newArrayList();
                    parentNode.setChildren(children);
                }
                children.add(childNode);
                childNode.setParentId(parentNode.getId());
                innerBuild(treeNodes, childNode, deep + 1, maxDeep);
            }
        }
    }

}
