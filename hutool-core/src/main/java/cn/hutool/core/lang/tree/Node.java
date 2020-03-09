package cn.hutool.core.lang.tree;


/**
 * 树节点 每个属性都可以在{@link TreeNodeConfig}中被重命名,在你的项目里它可以是部门实体 地区实体等任意类树节点实体
 * 类树节点实体: 包含key，父Key.不限于这些属性的可以构造成一颗树的实体对象
 *
 * @author liangbaikai
 */
public class Node {

    // ID
    private String id;

    // 上级ID
    private String pid;

    // 名称
    private String name;

    // 顺序 越小优先级越高 默认0
    private Comparable weight = 0;


    public Node() {
    }

    public Node(String id, String pid, String name, Comparable weight) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        if (weight != null) {
            this.weight = weight;
        }

    }

    public Comparable getWeight() {
        return weight;
    }

    public void setWeight(Comparable weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
