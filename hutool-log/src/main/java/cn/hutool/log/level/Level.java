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
 * 日志等级
 * @author Looly
 *
 */
public enum Level{
	/**
	 * 'ALL' log level.
	 */
	ALL,
	/**
	 * 'TRACE' log level.
	 */
	TRACE,
	/**
	 * 'DEBUG' log level.
	 */
	DEBUG,
	/**
	 * 'INFO' log level.
	 */
	INFO,
	/**
	 * 'WARN' log level.
	 */
	WARN,
	/**
	 * 'ERROR' log level.
	 */
	ERROR,
	/**
	 * 'FATAL' log level.
	 */
	FATAL,
	/**
	 * 'OFF' log.
	 */
	OFF
}
