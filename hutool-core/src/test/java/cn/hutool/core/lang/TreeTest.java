package cn.hutool.core.lang;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.*;
import org.junit.Test;

import java.util.List;

/**
 * 通用树测试
 *
 * @author liangbaikai
 */
public class TreeTest {
    // 模拟数据
    static List<Node> nodeList = CollectionUtil.newArrayList();

    static {
        // 模拟数据
        nodeList.add(new Node("1", "0", "系统管理", 5));
        nodeList.add(new Node("11", "1", "用户管理", 222222));
        nodeList.add(new Node("111", "11", "用户添加", 0));
        nodeList.add(new Node("2", "0", "店铺管理", 1));
        nodeList.add(new Node("21", "2", "商品管理", 44));
        nodeList.add(new Node("221", "2", "商品管理2", 2));
    }


    @Test
    public void sampleTree() {
        List<TreeNodeMap> treeNodes = TreeUtil.build(nodeList, "0");

        System.out.println(treeNodes);
    }

    @Test
    public void tree() {

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("order");
        treeNodeConfig.setDeep(3);
        treeNodeConfig.setIdKey("rid");

        //转换器
        List<TreeNodeMap> treeNodes = TreeUtil.build(nodeList, "0", treeNodeConfig,
                new Convert<Node, TreeNodeMap>() {
                    @Override
                    public void convert(Node object, TreeNodeMap treeNode) {
                        treeNode.setId(object.getId());
                        treeNode.setParentId(object.getPid());
                        treeNode.setWeight(object.getWeight());
                        treeNode.setName(object.getName());
                        // 扩展属性 ...
                        treeNode.extra("extraField", 666);
                        treeNode.extra("other", new Object());
                    }
                });
        System.out.println(treeNodes);
    }
}
