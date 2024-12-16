/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.cron.pattern.builder;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.builder.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Cron表达式的分片构建器
 *
 * @author Looly
 * @since 6.0.0
 */
public interface PartBuilder extends Builder<String> {

	/**
	 * 始终匹配
	 *
	 * @return Always
	 */
	static Always always() {
		return Always.INSTANCE;
	}

	/**
	 * 始终匹配
	 */
	class Always implements PartBuilder {
		private static final long serialVersionUID = 1L;

		/**
		 * 始终匹配
		 */
		public static final Always INSTANCE = new Always();

		@Override
		public String build() {
			return "*";
		}
	}

	/**
	 * 固定值
	 */
	class On implements PartBuilder {
		private static final long serialVersionUID = 1L;

		private final String value;

		/**
		 * 构造
		 *
		 * @param value 值
		 */
		public On(final int value) {
			this(String.valueOf(value));
		}

		/**
		 * 构造
		 *
		 * @param value 值
		 */
		public On(final String value) {
			this.value = value;
		}

		@Override
		public String build() {
			return value;
		}
	}

	/**
	 * 区间表达式
	 */
	class Range implements PartBuilder {
		private static final long serialVersionUID = 1L;

		private final String start;
		private final String end;

		/**
		 * 构造
		 *
		 * @param start 起始值（包含）
		 * @param end   结束值（包含）
		 */
		public Range(final int start, final int end) {
			this(String.valueOf(start), String.valueOf(end));
		}

		/**
		 * 构造
		 *
		 * @param start 起始值（包含）
		 * @param end   结束值（包含）
		 */
		public Range(final String start, final String end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public String build() {
			return start + "-" + end;
		}
	}

	/**
	 * 逻辑与表达式
	 */
	class And implements PartBuilder {
		private static final long serialVersionUID = 1L;

		private final List<PartBuilder> builders;

		/**
		 * 构造
		 *
		 * @param values 值列表
		 */
		public And(final int... values) {
			this.builders = new ArrayList<>(values.length);
			for (final int value : values) {
				this.builders.add(new On(value));
			}
		}

		/**
		 * 构造
		 *
		 * @param builders 表达式列表
		 */
		public And(final PartBuilder... builders) {
			this.builders = ListUtil.of(builders);
		}

		/**
		 * 追加表达式
		 *
		 * @param builder 表达式
		 * @return this
		 */
		public And and(final PartBuilder builder) {
			this.builders.add(builder);
			return this;
		}

		@Override
		public String build() {
			return CollUtil.join(builders, ",", PartBuilder::build);
		}
	}

	/**
	 * 每隔指定步长<br>
	 * 如 5/3，表示每3步取一次，即5,8,11,14,17...
	 */
	class Every implements PartBuilder {
		private static final long serialVersionUID = 1L;

		private final PartBuilder partBuilder;
		private final int step;

		/**
		 * 构造
		 *
		 * @param step        步长
		 */
		public Every(final int step) {
			this(PartBuilder.always(), step);
		}

		/**
		 * 构造
		 *
		 * @param partBuilder 表达式
		 * @param step        步长
		 */
		public Every(final PartBuilder partBuilder, final int step) {
			this.partBuilder = partBuilder;
			this.step = step;
		}

		@Override
		public String build() {
			final String build = partBuilder.build();
			if ("*".equals(build) && 1 == step) {
				return build;
			}
			return build + "/" + step;
		}
	}
}
