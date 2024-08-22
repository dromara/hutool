/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.extra.tokenizer;

import org.dromara.hutool.core.collection.iter.IterableIter;

/**
 * 分词结果接口定义<br>
 * 实现此接口包装分词器的分词结果，通过实现Iterator相应方法获取分词中的单词
 *
 * @author looly
 *
 */
public interface Result extends IterableIter<Word> {

}
