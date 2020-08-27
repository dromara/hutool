package cn.hutool.core.lang.tree;

import cn.hutool.core.annotation.LeafCollection;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 树转换测试
 */
public class TreeConvertTest {

    // 子父级测试数据
    private List<Dept> parentChildMaterials = Arrays.asList(
            new Dept("00000001", "0", "xxx公司"),
            new Dept("00000002", "00000001", "市场部"),
            new Dept("00000003", "00000001", "行政部"),
            new Dept("00000004", "00000001", "IT部"),
            new Dept("00000005", "00000002", "华南"),
            new Dept("00000006", "00000002", "华北"),
            new Dept("00000007", "00000002", "华东")
    );

    // 排序号测试数据
    private List<Dept> sortNoMaterials = Arrays.asList(
            new Dept("00", "xxx公司"),
            new Dept("0010",  "市场部"),
            new Dept("0020",  "行政部"),
            new Dept("0030",  "IT部"),
            new Dept("001010",  "华南"),
            new Dept("001020",  "华北"),
            new Dept("001030",  "华东")
    );

    // 父子结构测试
    @Test
    public void testParentChild() {
        List<Dept> tree = TreeConvert.convert(parentChildMaterials, Dept.class,
                root -> "0".equals(root.getParentId()),
                (root, leaf) -> leaf.getParentId().equals(root.getDeptId())
        );
        Assert.assertEquals("0", tree.get(0).getParentId());
    }

    // 排序号测试
    @Test
    public void testSortNo() {
        List<Dept> tree = TreeConvert.convert(sortNoMaterials, Dept.class,
                root -> "00".equals(root.getSortNo()),
                (root, leaf) ->
                        leaf.getSortNo().startsWith(root.getSortNo()) &&
                        !leaf.getSortNo().equals(root.getSortNo()) &&
                        leaf.getSortNo().length() - root.getSortNo().length() == 2
        );
        Assert.assertEquals("0", tree.get(0).getParentId());
    }

    // 测试实体类
    class Dept {
        private String deptId;
        private String sortNo;
        private String parentId;
        private String deptName;
        @LeafCollection
        private List<Dept> child;
        public Dept() {
        }

        public Dept(String sortNo, String deptName) {
            this.deptName = deptName;
            this.sortNo = sortNo;
        }

        public Dept(String deptId, String parentId, String deptName) {
            this.deptId = deptId;
            this.parentId = parentId;
            this.deptName = deptName;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public List<Dept> getChild() {
            return child;
        }

        public void setChild(List<Dept> child) {
            this.child = child;
        }

        public String getSortNo() {
            return sortNo;
        }

        public void setSortNo(String sortNo) {
            this.sortNo = sortNo;
        }
    }
}
