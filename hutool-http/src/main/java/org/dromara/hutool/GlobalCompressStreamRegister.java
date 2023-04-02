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

package org.dromara.hutool;

import org.dromara.hutool.compress.InflaterInputStream;
import org.dromara.hutool.map.CaseInsensitiveMap;

import java.io.InputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 全局响应内容压缩解压器注册中心<br>
 * 通过注册指定Accept-Encoding的流，来包装响应内容流，从而支持特殊压缩算法
 *
 * @author looly
 * @since 6.0.0
 */
public enum GlobalCompressStreamRegister {
	/**
	 * 单例对象
	 */
	INSTANCE;

	/**
	 * 存储内容压缩流信息
	 */
	private final Map<String, Class<? extends InputStream>> compressMap = new CaseInsensitiveMap<>();

	/**
	 * 构造，初始化默认的压缩算法
	 */
	GlobalCompressStreamRegister() {
		compressMap.put("gzip", GZIPInputStream.class);
		compressMap.put("deflate", InflaterInputStream.class);
	}

	/**
	 * 获取解压器
	 *
	 * @param contentEncoding Accept-Encoding名称，如gzip、defalte、br等，不区分大小写
	 * @return 解压器
	 */
	public Class<? extends InputStream> get(final String contentEncoding) {
		return compressMap.get(contentEncoding);
	}

	/**
	 * 注册解压器
	 *
	 * @param contentEncoding Accept-Encoding名称，如gzip、defalte、br等，不区分大小写
	 * @param streamClass     解压类
	 */
	synchronized public void register(final String contentEncoding, final Class<? extends InputStream> streamClass) {
		this.compressMap.put(contentEncoding, streamClass);
	}

	/**
	 * 注销压缩器
	 *
	 * @param contentEncoding Accept-Encoding名称，如gzip、defalte、br等，不区分大小写
	 */
	synchronized public void unRegister(final String contentEncoding) {
		this.compressMap.remove(contentEncoding);
	}
}
