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

package cn.hutool.extra.tokenizer.engine.analysis;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;

/**
 * Lucene-smartcn分词引擎实现<br>
 * 项目地址：https://github.com/apache/lucene-solr/tree/master/lucene/analysis/smartcn
 *
 * @author looly
 *
 */
public class SmartcnEngine extends AnalysisEngine {

	/**
	 * 构造
	 */
	public SmartcnEngine() {
		super(new SmartChineseAnalyzer());
	}

}
