package cn.hutool.json.test.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 * @author 质量过关
 *
 */
@Data
public class UserInfoDict implements Serializable {
	private static final long serialVersionUID = -936213991463284306L;
	// 用户Id
	private Integer id;
	// 要展示的名字
	private String realName;
	// 头像地址
	private String photoPath;
	private List<ExamInfoDict> examInfoDict;
	private UserInfoRedundCount userInfoRedundCount;
}
