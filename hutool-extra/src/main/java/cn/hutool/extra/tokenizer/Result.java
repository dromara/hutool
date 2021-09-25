package cn.hutool.extra.tokenizer;

import cn.hutool.core.collection.IterableIter;

/**
 * 分词结果接口定义<br>
 * 实现此接口包装分词器的分词结果，通过实现Iterator相应方法获取分词中的单词
 *
 * @author looly
 *
 */
public interface Result extends IterableIter<Word> {

}
