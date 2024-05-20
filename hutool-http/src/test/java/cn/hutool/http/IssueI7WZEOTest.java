/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.http;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI7WZEOTest {
	@Test
	@Ignore
	public void postTest() {
		final String post = HttpUtil.post("https://tenapi.cn/v2/video", "url=https://v.douyin.com/ie1EX3LH/");
		Console.log(post);
	}
}
