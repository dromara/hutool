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

/**
 * DFA全称为：Deterministic Finite Automaton,即确定有穷自动机。<br>
 * 解释起来原理其实也不难，就是用所有关键字构造一棵树，然后用正文遍历这棵树，遍历到叶子节点即表示文章中存在这个关键字。<br>
 * 我们暂且忽略构建关键词树的时间，每次查找正文只需要O(n)复杂度就可以搞定。<br>
 *
 * @author looly
 *
 */
package org.dromara.hutool.core.text.dfa;
