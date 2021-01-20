package cn.hutool.extra.tokenizer;

/**
 * 分词引擎接口定义，用户通过实现此接口完成特定分词引擎的适配
 * 
 * @author looly
 *
 */
public interface TokenizerEngine {
	
	/**
	 * 文本分词处理接口，通过实现此接口完成分词，产生分词结果
	 * 
	 * @param text 需要分词的文本
	 * @return {@link Result}分词结果实现
	 */
	Result parse(CharSequence text);
}
