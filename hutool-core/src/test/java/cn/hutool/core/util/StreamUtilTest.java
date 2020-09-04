package cn.hutool.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yiming
 * @date 2020/9/4 13:40
 */
public class StreamUtilTest {

    static List<User> userList = Arrays.asList(
        new User(1L, "A", 3, true, true, true),
        new User(2L, "B", 4, true, true, false),
        new User(3L, "C", 5, true, false, true),
        new User(4L, "E", 6, true, false, false),
        new User(5L, "D", 7, false, true, true),
        new User(6L, "F", 7, false, false, true),
        new User(7L, "G", 8, false, true, false),
        new User(8L, "H", 8, false, false, false)
    );

    @Test
    public void testList() {
        List<Long> userIds = StreamUtil.list(userList, User::getId);
        Assert.assertEquals(userIds.size(), userList.size());
    }

    @Test
    public void testListFilter() {
        // 提取管理员用户名称
        List<String> names = StreamUtil.list(userList, User::getName, x -> Boolean.TRUE.equals(x.isAdmin));
        Assert.assertEquals(names.size(), 4);
    }

    @Test
    public void testToSet() {
        Set<Integer> ages = StreamUtil.toSet(userList, User::getAge);
        Assert.assertEquals(ages.size(), 6);
    }

    @Test
    public void testToSetFilter() {
        // 提取大于6的元素
        Set<Integer> ages = StreamUtil.toSet(userList, User::getAge, x -> x.getAge() > 6);
        Assert.assertEquals(ages.size(), 2);
    }

    @Test
    public void testToMap() {
        Map<Long, User> userMap = StreamUtil.toMap(userList, User::getId);
        Assert.assertEquals(userMap.size(), userList.size());
    }

    @Test
    public void testToMapValue() {
        Map<Long, String> userNameMap = StreamUtil.toMap(userList, User::getId, User::getName);
        Assert.assertEquals(userNameMap.size(), userList.size());
    }

    @Test
    public void testToMapFilter() {
        // 提取年龄>5的用户姓名map
        Map<Long, String> userNameMap = StreamUtil.toMap(userList, User::getId, User::getName, x -> x.getAge() > 5);
        Assert.assertEquals(userNameMap.size(), 5);
    }

    @Test
    public void testToMapMerge() {
        // 提取用户姓名map - 自定义map的value覆盖策略
        User tempUser1 = new User(10L, "X", 10, true, false, false);
        User tempUser2 = new User(10L, "Y", 10, true, false, false);
        List<User> users = new ArrayList<>();
        users.add(tempUser1);
        users.add(tempUser2);
        // 若新的value同原value，取原value
        Map<Long, String> userNameMap = StreamUtil.toMap(users, User::getId, User::getName, (v1, v2) -> v1);
        Assert.assertEquals(userNameMap.get(10L), "X");
        // 若新的value同原value，取新value
        userNameMap = StreamUtil.toMap(users, User::getId, User::getName, (v1, v2) -> v2);
        Assert.assertEquals(userNameMap.get(10L), "Y");
    }

    @Test
    public void groupingBy() {
        // 按年龄分组
        Map<Integer, List<User>> ageGroupMap = StreamUtil.groupingBy(userList, User::getAge);
        Assert.assertEquals(ageGroupMap.size(), 6);
    }

    /**
     * 测试bean
     */
    public static class User {

        private Long id;
        private String name;
        private int age;
        private boolean isAdmin;
        private boolean isSuper;
        private boolean gender;

        public User() {
        }

        public User(Long id, String name, int age, boolean isAdmin, boolean isSuper, boolean gender) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.isAdmin = isAdmin;
            this.isSuper = isSuper;
            this.gender = gender;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public User setAge(int age) {
            this.age = age;
            return this;
        }

        public String testMethod() {
            return "test for " + this.name;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public boolean isIsSuper() {
            return isSuper;
        }

        public void setIsSuper(boolean isSuper) {
            this.isSuper = isSuper;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "User [name=" + name + ", age=" + age + ", isAdmin=" + isAdmin + ", gender=" + gender + "]";
        }
    }
}