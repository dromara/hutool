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
