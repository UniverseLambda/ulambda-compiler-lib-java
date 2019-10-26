/*
	Copyright 2019 Clément Saad

	This file is part of the uLambda Compiler Library.

	The uLambda Compiler Library is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	The uLambda Compiler Library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with the uLambda Compiler Library.  If not, see <https://www.gnu.org/licenses/>.
 */

package universe.lambda.jlcl;

public class Logger {
	public static LogLevel minimumLogLevel = LogLevel.DEBUG;
	public static String name = "";

	public static boolean redirectErr = false;

	public static void debug(Object message) {
		log(LogLevel.DEBUG, message);
	}

	public static void info(Object message) {
		log(LogLevel.INFO, message);
	}

	public static void warn(Object message) {
		log(LogLevel.WARN, message);
	}

	public static void error(Object message) {
		logErr(LogLevel.ERROR, message);
	}

	public static void fatal(Object message) {
		logErr(LogLevel.FATAL, message);
	}

	public static void log(LogLevel level, Object message) {
		if (level.position < minimumLogLevel.position) {
			return;
		}
		System.out.println(name + ": " + level.value + ": " + message);
	}

	public static void logErr(LogLevel level, Object message) {
		if (redirectErr) {
			log(level, message);
			return;
		}

		if (level.position < minimumLogLevel.position) {
			return;
		}
		System.err.println(name + ": " + level.value + ": " + message);
	}

	public enum LogLevel {
		DEBUG(0, "debug"),
		INFO(1, "info"),
		WARN(2, "warning"),
		ERROR(3, "error"),
		FATAL(4, "fatal error"),
		;

		public int position;
		public String value;

		LogLevel(int position, String value) {
			this.value = value;
		}
	}
}
