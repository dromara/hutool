package cn.hutool.core.lang.tree.lambda;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * LambdaTreeTest
 *
 * @author adoph
 * @version 1.0
 * @since 2022/11/25
 */
public class LambdaTreeTest {

	static List<Menu> menuList = new ArrayList<>();

	/*
	 *	首页
	 *	用户管理
	 *		/用户列表
	 *	任务管理
	 *		/未处理任务
	 *		/已完成任务
	 *		/历史人物
	 *	工单管理
	 *		/我的工单
	 */
	static {
		menuList.add(new Menu(1L, 0L, "首页", 1));
		menuList.add(new Menu(2L, 0L, "用户管理", 2));
		menuList.add(new Menu(3L, 0L, "任务管理", 3));
		menuList.add(new Menu(4L, 0L, "工单管理", 1));
		menuList.add(new Menu(5L, 2L, "用户列表", 1));
		menuList.add(new Menu(7L, 3L, "未处理任务", 2));
		menuList.add(new Menu(8L, 3L, "已完成任务", 3));
		menuList.add(new Menu(9L, 3L, "历史任务", 1));
		menuList.add(new Menu(10L, 4L, "我的工单", 1));
	}

	@Test
	public void lambdaTreeTest() {
		List<Menu> treeData = LambdaTreeUtil.builder(menuList, 0L)
				.id(Menu::getId) // 指定节点标识function
				.pid(Menu::getPid) // 指定父节点标识function
				.children(Menu::getChildren) // 指定子节点集function
				.sort(Menu::getSortNum) //指定排序function
				.build();
		Assert.assertEquals(4, treeData.size());
	}

}

class Menu {
	private Long id;
	private Long pid;
	private String name;
	private Integer sortNum;
	private List<Menu> children;

	public Menu(Long id, Long pid, String name, Integer sortNum) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.sortNum = sortNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}
}
