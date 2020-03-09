package cn.hutool.core.lang.tree;

/**
 * 树配置属性相关
 *
 * @author liangbaikai
 */
public class TreeNodeConfig {

    /**
     * 默认属性配置对象
     */
    private static TreeNodeConfig defaultConfig = new TreeNodeConfig();

    // 树节点默认属性常量 当然你可以重新设置
    /**
     * 节点 id 名称
     */
    static final String TREE_ID = "id";
    /**
     * 节点 parentId 父id名称
     */
    static final String TREE_PARENT_ID = "parentId";
    /**
     * 节点 name 显示名称
     */
    static final String TREE_NAME = "name";
    /**
     * 节点 weight 顺序名称
     */
    static final String TREE_WEIGHT = "weight";
    /**
     * 节点 name 子节点名称
     */
    static final String TREE_CHILDREN = "children";


    static {
        //init
        defaultConfig.setIdKey(TREE_ID);
        defaultConfig.setWeightKey(TREE_WEIGHT);
        defaultConfig.setNameKey(TREE_NAME);
        defaultConfig.setChildrenKey(TREE_CHILDREN);
        defaultConfig.setParentIdKey(TREE_PARENT_ID);
    }

    // 属性名配置字段
    private String idKey;
    private String parentIdKey;
    private String weightKey;
    private String nameKey;
    private String childrenKey;

    // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
    private Integer deep;


    public String getIdKey() {
        return getOrDefault(idKey, TREE_ID);
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getWeightKey() {
        return getOrDefault(weightKey, TREE_WEIGHT);
    }

    public void setWeightKey(String weightKey) {
        this.weightKey = weightKey;
    }

    public String getNameKey() {
        return getOrDefault(nameKey, TREE_NAME);
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getChildrenKey() {
        return getOrDefault(childrenKey, TREE_CHILDREN);
    }

    public void setChildrenKey(String childrenKey) {
        this.childrenKey = childrenKey;
    }

    public String getParentIdKey() {
        return getOrDefault(parentIdKey, TREE_PARENT_ID);
    }

    public void setParentIdKey(String parentIdKey) {
        this.parentIdKey = parentIdKey;
    }

    public String getOrDefault(String key, String defaultKey) {
        if (key == null) {
            return defaultKey;
        }
        return key;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }

    public static TreeNodeConfig getDefaultConfig() {
        return defaultConfig;
    }

}
