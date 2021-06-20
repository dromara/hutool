package cn.hutool.extra.tokenizer.engine.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import cn.hutool.extra.tokenizer.AbstractResult;
import cn.hutool.extra.tokenizer.TokenizerException;
import cn.hutool.extra.tokenizer.Word;

/**
 * Lucene-analysis分词抽象结果封装<br>
 * 项目地址：https://github.com/apache/lucene-solr/tree/master/lucene/analysis
 * 
 * @author looly
 *
 */
public class AnalysisResult extends AbstractResult {

	private final TokenStream stream;

	/**
	 * 构造
	 * 
	 * @param stream 分词结果
	 */
	public AnalysisResult(TokenStream stream) {
		this.stream = stream;
	}

	@Override
	protected Word nextWord() {
		try {
			if(this.stream.incrementToken()) {
				return new AnalysisWord(this.stream.getAttribute(CharTermAttribute.class));
			}
		} catch (IOException e) {
			throw new TokenizerException(e);
		}
		return null;
	}
}
