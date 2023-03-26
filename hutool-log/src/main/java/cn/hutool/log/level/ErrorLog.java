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

package cn.hutool.log.level;

/**
 * ERROR级别日志接口
 * @author Looly
 *
 */
public interface ErrorLog {
	/**
	 * @return ERROR 等级是否开启
	 */
	boolean isErrorEnabled();

	/**
	 * 打印 ERROR 等级的日志
	 *
	 * @param t 错误对象
	 */
	void error(Throwable t);

	/**
	 * 打印 ERROR 等级的日志
	 *
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void error(String format, Object... arguments);

	/**
	 * 打印 ERROR 等级的日志
	 *
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void error(Throwable t, String format, Object... arguments);

	/**
	 * 打印 ERROR 等级的日志
	 *
	 * @param fqcn 完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param t 错误对象
	 * @param format 消息模板
	 * @param arguments 参数
	 */
	void error(String fqcn, Throwable t, String format, Object... arguments);
}
