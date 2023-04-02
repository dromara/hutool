package org.dromara.hutool.db.pojo;

import lombok.Data;

/**
 * 测试用POJO，与测试数据库中的user表对应
 *
 * @author looly
 *
 */
@Data
public class User {
	private Integer id;
	private String name;
	private int age;
	private String birthday;
	private boolean gender;
}
