/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.test.bean;

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
	public void setId(final Integer id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}
	public void setRealName(final String realName) {
		this.realName = realName;
	}

	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(final String photoPath) {
		this.photoPath = photoPath;
	}

	public List<ExamInfoDict> getExamInfoDict() {
		return examInfoDict;
	}
	public void setExamInfoDict(final List<ExamInfoDict> examInfoDict) {
		this.examInfoDict = examInfoDict;
	}

	public UserInfoRedundCount getUserInfoRedundCount() {
		return userInfoRedundCount;
	}
	public void setUserInfoRedundCount(final UserInfoRedundCount userInfoRedundCount) {
		this.userInfoRedundCount = userInfoRedundCount;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final UserInfoDict that = (UserInfoDict) o;
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
