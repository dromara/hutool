package cn.hutool.core.lang.test.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 用户信息
 * @author 质量过关
 *
 */
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

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public List<ExamInfoDict> getExamInfoDict() {
		return examInfoDict;
	}
	public void setExamInfoDict(List<ExamInfoDict> examInfoDict) {
		this.examInfoDict = examInfoDict;
	}

	public UserInfoRedundCount getUserInfoRedundCount() {
		return userInfoRedundCount;
	}
	public void setUserInfoRedundCount(UserInfoRedundCount userInfoRedundCount) {
		this.userInfoRedundCount = userInfoRedundCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserInfoDict that = (UserInfoDict) o;
		return Objects.equals(id, that.id) && Objects.equals(realName, that.realName) && Objects.equals(photoPath, that.photoPath) && Objects.equals(examInfoDict, that.examInfoDict);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, realName, photoPath, examInfoDict);
	}

	@Override
	public String toString() {
		return "UserInfoDict [id=" + id + ", realName=" + realName + ", photoPath=" + photoPath + ", examInfoDict=" + examInfoDict + ", userInfoRedundCount=" + userInfoRedundCount + "]";
	}
}
