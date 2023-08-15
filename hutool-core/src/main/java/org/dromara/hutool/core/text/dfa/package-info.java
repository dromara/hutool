/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
