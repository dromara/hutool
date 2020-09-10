package cn.hutool.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.collection.CollUtil;

public class ObjectUtilTest {

    @Test
    public void cloneTest() {
        Obj obj = new Obj();
        Obj obj2 = ObjectUtil.clone(obj);
        Assert.assertEquals("OK", obj2.doSomeThing());
    }

    static class Obj extends CloneSupport<Obj> {
        public String doSomeThing() {
            return "OK";
        }
    }

    @Test
    public void toStringTest() {
        ArrayList<String> strings = CollUtil.newArrayList("1", "2");
        String result = ObjectUtil.toString(strings);
        Assert.assertEquals("[1, 2]", result);
    }

    /**
     * 测试pojo
     */
    @Test
    public void testPojo() {
        Course course = new Course();
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(course));
        course.setName("化学");
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(course));
    }

    /**
     * 测试基本数据类型的默认值
     */
    @Test
    public void testPrimitive() {
        int integer1 = 0;
        int integer2 = 1;
        byte byte1 = 0;
        byte byte2 = 1;
        long long1 = 0L;
        long long2 = 1L;
        double double1 = 0.0d;
        double double2 = 1.1d;
        float float1 = 0.0f;
        float float2 = 1.1f;
        char char1 = '\u0000';
        char char2 = '\u0006';
        short short1 = 0;
        short short2 = 1;
        boolean boolean1 = false;
        boolean boolean2 = true;

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(integer1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(integer2));

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(byte1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(byte2));

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(long1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(long2));

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(double1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(double2));

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(float1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(float2));

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(char1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(char2));

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(short1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(short2));

        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(boolean1));
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(boolean2));
    }

    /**
     * 测试是否忽略boolean类型
     */
    @Test
    public void testIgnoreBoolean() {
        Boolean flag = false;
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(flag));
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(flag, false));


        Student student = new Student();
        student.setPretty(false);
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(flag));

        student.setPretty(true);
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(flag));

    }

    /**
     * 测试继承父类属性
     */
    @Test
    public void testExtend() {
        Student student = new Student();
        student.setName("李雷");
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(student));
    }

    /**
     * 测试pojo嵌套
     */
    @Test
    public void testNestPojo() {
        Student student = new Student();
        Course course = new Course();
        student.setCourse(course);
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(student));
    }

    /**
     * 测试入参为数组
     */
    @Test
    public void testArray() {
        Student[] emptyStudentArray = {new Student(), new Student(), new Student()};
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(emptyStudentArray));

        Student[] notEmptyStudentArray = {new Student("李华"), new Student(), new Student()};
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(notEmptyStudentArray));
    }

    /**
     * 测试迭代器
     */
    @Test
    public void testIterator() {
        List<Student> emptyList = new ArrayList<>();
        emptyList.add(new Student());
        emptyList.add(new Student());
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(emptyList));

        List<Student> notEmptyList = new ArrayList<>();
        notEmptyList.add(new Student("小华"));
        notEmptyList.add(new Student());
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(notEmptyList));
    }

    /**
     * 测试入参字符串
     */
    @Test
    public void testString() {
        String empty = "";
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(empty));

        String notEmpty = "aaa";
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(notEmpty));
    }

    /**
     * 测试入参为Map接口的实例
     */
    @Test
    public void testMap() {
        Map<Object, Object> map = new HashMap<>();
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(map));
        map.put("name", "hutool");
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(map));
    }

    /**
     * 测试无效的属性名是否会被跳过 如：
     * <ul>
     * <li>以$开头的属性<li/>
     * <li>序列化属性serialVersionUID<li/>
     * <li>名字为class的属性<li/>
     * <ul/>
     */
    @Test
    public void testInvalidPropertyName() {
        SerialObject serialObject = new SerialObject();
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(serialObject));
        serialObject.setName("hutool");
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(serialObject));
    }

    /**
     * 测试pojo嵌套集合的情况
     */
    @Test
    public void testPojoNextList() {
        Course course = new Course();
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(course));
        List<Student> students = new ArrayList<>();
        course.setStudents(students);
        Assert.assertEquals(true, ObjectUtil.isDeepEmpty(course));
        Student student = new Student();
        student.setName("李明");
        students.add(student);
        Assert.assertEquals(false, ObjectUtil.isDeepEmpty(course));
    }

    /**
     * 课程类
     */
    class Course {
        private String name;
        private Integer score;
        private List<Student> students;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }
    }

    class People {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    class SerialObject implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    class Student extends People {
        private String nickname;
        private Course course;
        private Boolean pretty;

        public Student() {
        }

        public Student(String nickname) {
            this.nickname = nickname;
        }

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public Boolean getPretty() {
            return pretty;
        }

        public void setPretty(Boolean pretty) {
            this.pretty = pretty;
        }
    }
}
