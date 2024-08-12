/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.log.engine.slf4j;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.dromara.hutool.log.AbsLogEngine;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

import org.dromara.hutool.log.Log;

/**
 * <a href="http://www.slf4j.org/">SLF4J</a> log.<br>
 * 同样无缝支持 <a href="http://logback.qos.ch/">LogBack</a>
 *
 * @author Looly
 */
public class Slf4jLogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public Slf4jLogEngine() {
		this(true);
	}

	/**
	 * 构造
	 *
	 * @param failIfNOP 如果未找到桥接包是否报错
	 */
	public Slf4jLogEngine(final boolean failIfNOP) {
		super("Slf4j");
		checkLogExist(LoggerFactory.class);
		if (!failIfNOP) {
			return;
		}

		// SFL4J writes it error messages to System.err. Capture them so that the user does not see such a message on
		// the console during automatic detection.
		final StringBuilder buf = new StringBuilder();
		final PrintStream err = System.err;
		try {
			System.setErr(new PrintStream(new OutputStream() {
				@Override
				public void write(final int b) {
					buf.append((char) b);
				}
			}, true, "US-ASCII"));
		} catch (final UnsupportedEncodingException e) {
			throw new Error(e);
		}

		try {
			if (LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
				throw new NoClassDefFoundError(buf.toString());
			} else {
				err.print(buf);
				err.flush();
			}
		} finally {
			System.setErr(err);
		}
	}

	@Override
	public Log createLog(final String name) {
		return new Slf4jLog(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new Slf4jLog(clazz);
	}

}
