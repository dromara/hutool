package cn.hutool.core.lang.tree;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 通过转换器将你的实体转化为TreeNodeMap节点实体 属性都存在此处,属性有序，可支持排序
 *
 * @author liangbaikai
 */
public class TreeNodeMap extends LinkedHashMap implements Comparable<TreeNodeMap> {

    private static final long serialVersionUID = 8376668307601977428L;

    private TreeNodeConfig treeNodeConfig;

    public TreeNodeMap() {
        this.treeNodeConfig = TreeNodeConfig.getDefaultConfig();
    }

    public TreeNodeMap(TreeNodeConfig treeNodeConfig) {
        this.treeNodeConfig = treeNodeConfig;
    }

    public <T> T getId() {
        return (T) super.get(treeNodeConfig.getIdKey());
    }

    public void setId(String id) {
        super.put(treeNodeConfig.getIdKey(), id);
    }

    public <T> T getParentId() {
        return (T) super.get(treeNodeConfig.getParentIdKey());
    }

    public void setParentId(String parentId) {
        super.put(treeNodeConfig.getParentIdKey(), parentId);
    }

    public <T> T getName() {
        return (T) super.get(treeNodeConfig.getNameKey());
    }

    public void setName(String name) {
        super.put(treeNodeConfig.getNameKey(), name);
    }

    public Comparable getWeight() {
        return (Comparable) super.get(treeNodeConfig.getWeightKey());
    }

    public TreeNodeMap setWeight(Comparable weight) {
        super.put(treeNodeConfig.getWeightKey(), weight);
        return this;
    }

    public List<TreeNodeMap> getChildren() {
        return (List<TreeNodeMap>) super.get(treeNodeConfig.getChildrenKey());
    }

    public void setChildren(List<TreeNodeMap> children) {
        super.put(treeNodeConfig.getChildrenKey(), children);
    }

    /**
     * 扩展属性
     *
     * @param key
     * @param value
     */
    public void extra(String key, Object value) {
        if (key != null && key != "") {
            super.put(key, value);
        }
    }

    /**
     * 可以支持排序
     *
     * @param treeNodeMap weight值越小 优先级越高 默认为0
     * @return
     */
    @Override
    public int compareTo(TreeNodeMap treeNodeMap) {
        try {
            //可能为空
            int res = this.getWeight().compareTo(treeNodeMap.getWeight());
            return res;
        } catch (NullPointerException e) {
            return 0;
        }
    }
}