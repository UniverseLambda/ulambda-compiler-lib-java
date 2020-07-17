/*
	Copyright 2019, 2020 Clément Saad

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

/**
 * {@code Logger} is a helper class for logging. All the library classes which needs logging use this class.
 * The user may choose not to use this class but is advised to, for log uniformity.
 *
 * @since 0.1
 */
public final class Logger {
	/**
	 * The log level from which this class starts logging.
	 *
	 * @since 0.1
	 */
	public static LogLevel minimumLogLevel = LogLevel.INFO;

	/**
	 * Name used in logs content.
	 *
	 * @since 0.1
	 */
	public static String name = "";

	/**
	 * Flag indicating whether {@code Logger.logErr} should redirect logs to standard output instead of
	 * standard error.
	 *
	 * @since 0.1
	 */
	public static boolean redirectErr = false;

	/**
	 * Convenience method equivalent to {@code Logger.log(LogLevel.DEBUG, message)}.
	 *
	 * @param message message to log.
	 *
	 * @since 0.1
	 */
	public static void debug(Object message) {
		log(LogLevel.DEBUG, message);
	}

	/**
	 * Convenience method equivalent to {@code Logger.log(LogLevel.DEBUG, message)}.
	 *
	 * @param message message to log.
	 *
	 * @since 0.1
	 */
	public static void info(Object message) {
		log(LogLevel.INFO, message);
	}

	/**
	 * Convenience method equivalent to {@code Logger.log(LogLevel.WARN, message)}.
	 *
	 * @param message message to log.
	 *
	 * @since 0.1
	 */
	public static void warn(Object message) {
		log(LogLevel.WARN, message);
	}

	/**
	 * Convenience method equivalent to {@code Logger.logErr(LogLevel.ERROR, message)}.
	 *
	 * @param message message to log.
	 *
	 * @since 0.1
	 */
	public static void error(Object message) {
		logErr(LogLevel.ERROR, message);
	}

	/**
	 * Convenience method equivalent to {@code Logger.logErr(LogLevel.FATAL, message)}.
	 *
	 * @param message message to log.
	 *
	 * @since 0.1
	 */
	public static void fatal(Object message) {
		logErr(LogLevel.FATAL, message);
	}

	/**
	 * Logs a message with the specified log level to the standard output.<br>
	 *
	 * Logs have the following syntax:<br><br>
	 *
	 * {@code Logger.name}: {@code level.value}: {@code message}<br><br>
	 *
	 * If the log level is such that {@code level.position < minimumLogLevel.position}, the log is ignored.
	 *
	 * @param level level of the message.
	 * @param message message to log.
	 *
	 * @since 0.1
	 */
	public static void log(LogLevel level, Object message) {
		if (level.position < minimumLogLevel.position) {
			return;
		}
		System.out.println(name + ": " + level.value + ": " + message);
	}

	/**
	 * Logs a message with the specified log level to the standard error.<br>
	 *
	 * Logs have the following syntax:<br><br>
	 *
	 * {@code Logger.name}: {@code level.value}: {@code message}<br><br>
	 *
	 * If the log level is such that {@code level.position < minimumLogLevel.position}, the log is ignored.<br>
	 * Furthermore, if {@code redirectErr} is {@code true}, then the logger uses the standard output instead of the
	 * standard error.
	 *
	 * @param level level of the message.
	 * @param message message to log.
	 *
	 * @since 0.1
	 */
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

	/**
	 * Represents a log level.
	 *
	 * @since 0.1
	 */
	public enum LogLevel {
		DEBUG(0, "debug"),
		INFO(1, "info"),
		WARN(2, "warning"),
		ERROR(3, "error"),
		FATAL(4, "fatal error"),
		;

		/**
		 * Integer value of the log level. It is used to position log levels relative to each other.
		 *
		 * @since 0.1
		 */
		public int position;

		/**
		 * String value of the log level. It is used in the log content.
		 *
		 * @since 0.1
		 */
		public String value;

		LogLevel(int position, String value) {
			this.position = position;
			this.value = value;
		}
	}
}
