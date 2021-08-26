package cn.hutool.core.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CollectionStream测试方法
 */
public class CollStreamUtilTest {
	@Test
	public void testToIdentityMap() {
		Map<Long, Student> map = CollStreamUtil.toIdentityMap(null, Student::getStudentId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		List<Student> list = new ArrayList<>();
		map = CollStreamUtil.toIdentityMap(list, Student::getStudentId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 1, 2, "李四"));
		list.add(new Student(1, 1, 3, "王五"));
		map = CollStreamUtil.toIdentityMap(list, Student::getStudentId);
		Assert.assertEquals(map.get(1L).getName(), "张三");
		Assert.assertEquals(map.get(2L).getName(), "李四");
		Assert.assertEquals(map.get(3L).getName(), "王五");
		Assert.assertNull(map.get(4L));
	}

	@Test
	public void testToMap() {
		Map<Long, String> map = CollStreamUtil.toMap(null, Student::getStudentId, Student::getName);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		List<Student> list = new ArrayList<>();
		map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 1, 2, "李四"));
		list.add(new Student(1, 1, 3, "王五"));
		map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName);
		Assert.assertEquals(map.get(1L), "张三");
		Assert.assertEquals(map.get(2L), "李四");
		Assert.assertEquals(map.get(3L), "王五");
		Assert.assertNull(map.get(4L));
	}

	@Test
	public void testGroupByKey() {
		Map<Long, List<Student>> map = CollStreamUtil.groupByKey(null, Student::getClassId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		List<Student> list = new ArrayList<>();
		map = CollStreamUtil.groupByKey(list, Student::getClassId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 2, "李四"));
		list.add(new Student(2, 1, 1, "擎天柱"));
		list.add(new Student(2, 2, 2, "威震天"));
		list.add(new Student(2, 3, 2, "霸天虎"));
		map = CollStreamUtil.groupByKey(list, Student::getClassId);
		Map<Long, List<Student>> compare = new HashMap<>();
		List<Student> class1 = new ArrayList<>();
		class1.add(new Student(1, 1, 1, "张三"));
		class1.add(new Student(2, 1, 1, "擎天柱"));
		compare.put(1L, class1);
		List<Student> class2 = new ArrayList<>();
		class2.add(new Student(1, 2, 2, "李四"));
		class2.add(new Student(2, 2, 2, "威震天"));

		compare.put(2L, class2);
		List<Student> class3 = new ArrayList<>();
		class3.add(new Student(2, 3, 2, "霸天虎"));
		compare.put(3L, class3);
		Assert.assertEquals(map, compare);
	}

	@Test
	public void testGroupBy2Key() {
		Map<Long, Map<Long, List<Student>>> map = CollStreamUtil.groupBy2Key(null, Student::getTermId, Student::getClassId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		List<Student> list = new ArrayList<>();
		map = CollStreamUtil.groupBy2Key(list, Student::getTermId, Student::getClassId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 2, "李四"));
		list.add(new Student(1, 2, 3, "王五"));
		list.add(new Student(2, 1, 1, "擎天柱"));
		list.add(new Student(2, 2, 2, "威震天"));
		list.add(new Student(2, 2, 3, "霸天虎"));
		map = CollStreamUtil.groupBy2Key(list, Student::getTermId, Student::getClassId);
		Map<Long, Map<Long, List<Student>>> compare = new HashMap<>();
		Map<Long, List<Student>> map1 = new HashMap<>();
		List<Student> list11 = new ArrayList<>();
		list11.add(new Student(1, 1, 1, "张三"));
		map1.put(1L, list11);
		compare.put(1L, map1);
		List<Student> list12 = new ArrayList<>();
		list12.add(new Student(1, 2, 2, "李四"));
		list12.add(new Student(1, 2, 3, "王五"));
		map1.put(2L, list12);
		compare.put(2L, map1);
		Map<Long, List<Student>> map2 = new HashMap<>();
		List<Student> list21 = new ArrayList<>();
		list21.add(new Student(2, 1, 1, "擎天柱"));
		map2.put(1L, list21);
		compare.put(2L, map2);

		List<Student> list22 = new ArrayList<>();
		list22.add(new Student(2, 2, 2, "威震天"));
		list22.add(new Student(2, 2, 3, "霸天虎"));
		map2.put(2L, list22);
		compare.put(2L, map2);
		Assert.assertEquals(map, compare);
	}

	@Test
	public void testGroup2Map() {
		Map<Long, Map<Long, Student>> map = CollStreamUtil.group2Map(null, Student::getTermId, Student::getClassId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);

		List<Student> list = new ArrayList<>();
		map = CollStreamUtil.group2Map(list, Student::getTermId, Student::getClassId);
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 1, "李四"));
		list.add(new Student(2, 2, 1, "王五"));
		map = CollStreamUtil.group2Map(list, Student::getTermId, Student::getClassId);
		Map<Long, Map<Long, Student>> compare = new HashMap<>();
		Map<Long, Student> map1 = new HashMap<>();
		map1.put(1L, new Student(1, 1, 1, "张三"));
		map1.put(2L, new Student(1, 2, 1, "李四"));
		compare.put(1L, map1);
		Map<Long, Student> map2 = new HashMap<>();
		map2.put(2L, new Student(2, 2, 1, "王五"));
		compare.put(2L, map2);
		Assert.assertEquals(compare, map);

	}

	@Test
	public void testTranslate2List() {
		List<String> list = CollStreamUtil.toList(null, Student::getName);
		Assert.assertEquals(list, Collections.EMPTY_LIST);
		List<Student> students = new ArrayList<>();
		list = CollStreamUtil.toList(students, Student::getName);
		Assert.assertEquals(list, Collections.EMPTY_LIST);
		students.add(new Student(1, 1, 1, "张三"));
		students.add(new Student(1, 2, 2, "李四"));
		students.add(new Student(2, 1, 1, "李四"));
		students.add(new Student(2, 2, 2, "李四"));
		students.add(new Student(2, 3, 2, "霸天虎"));
		list = CollStreamUtil.toList(students, Student::getName);
		List<String> compare = new ArrayList<>();
		compare.add("张三");
		compare.add("李四");
		compare.add("李四");
		compare.add("李四");
		compare.add("霸天虎");
		Assert.assertEquals(list, compare);
	}

	@Test
	public void testTranslate2Set() {
		Set<String> set = CollStreamUtil.toSet(null, Student::getName);
		Assert.assertEquals(set, Collections.EMPTY_SET);
		List<Student> students = new ArrayList<>();
		set = CollStreamUtil.toSet(students, Student::getName);
		Assert.assertEquals(set, Collections.EMPTY_SET);
		students.add(new Student(1, 1, 1, "张三"));
		students.add(new Student(1, 2, 2, "李四"));
		students.add(new Student(2, 1, 1, "李四"));
		students.add(new Student(2, 2, 2, "李四"));
		students.add(new Student(2, 3, 2, "霸天虎"));
		set = CollStreamUtil.toSet(students, Student::getName);
		Set<String> compare = new HashSet<>();
		compare.add("张三");
		compare.add("李四");
		compare.add("霸天虎");
		Assert.assertEquals(set, compare);
	}

	@Test
	public void testMerge() {
		Map<Long, Student> map1 = null;
		Map<Long, Student> map2 = Collections.emptyMap();
		Map<Long, String> map = CollStreamUtil.merge(map1, map2, (s1, s2) -> s1.getName() + s2.getName());
		Assert.assertEquals(map, Collections.EMPTY_MAP);
		map1 = new HashMap<>();
		map1.put(1L, new Student(1, 1, 1, "张三"));
		map = CollStreamUtil.merge(map1, map2, this::merge);
		Map<Long, String> temp = new HashMap<>();
		temp.put(1L, "张三");
		Assert.assertEquals(map, temp);
		map2 = new HashMap<>();
		map2.put(1L, new Student(2, 1, 1, "李四"));
		map = CollStreamUtil.merge(map1, map2, this::merge);
		Map<Long, String> compare = new HashMap<>();
		compare.put(1L, "张三李四");
		Assert.assertEquals(map, compare);
	}

	private String merge(Student student1, Student student2) {
		if (student1 == null && student2 == null) {
			return null;
		} else if (student1 == null) {
			return student2.getName();
		} else if (student2 == null) {
			return student1.getName();
		} else {
			return student1.getName() + student2.getName();
		}
	}

	/**
	 * 班级类
	 */
	@Data
	@AllArgsConstructor
	@ToString
	public static class Student {
		private long termId;//学期id
		private long classId;//班级id
		private long studentId;//班级id
		private String name;//学生名称
	}
}
