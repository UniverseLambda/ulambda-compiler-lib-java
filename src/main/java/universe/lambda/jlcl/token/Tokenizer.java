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

package universe.lambda.jlcl.token;

import universe.lambda.jlcl.LanguageDefinition;
import universe.lambda.jlcl.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;


/**
 * Class used to read {@link Token}s from a {@link Reader} and a {@link LanguageDefinition}.
 *
 * @since 0.1
 */
public class Tokenizer {
	/**
	 * {@code Tokenizer} mode in which the Tokenizer keeps accumulating code points since it cannot issue a
	 * {@link Token}, but accumulating more code points may produce a {@code Token}.
	 *
	 * @since 0.1
	 */
	private static final int MODE_STANDBY = 0;

	/**
	 * {@code Tokenizer} mode in which the {@code Tokenizer} stop accumulating code points and return {@code result}. It
	 * indicates that a {@link Token} has been successfully created and that accumulating code points is no longer
	 * needed.
	 *
	 * @since 0.1
	 */
	private static final int MODE_DONE = 1;

	/**
	 * {@code Tokenizer} mode in which the {@code Tokenizer} stop accumulating code points and return {@code null}. It
	 * indicates that it cannot issue a {@link Token} because the accumulated code points do not correspond to any
	 * {@code Token}, and accumulating more would not produce a valid {@code Token}.
	 *
	 * @since 0.1
	 */
	private static final int MODE_ERROR = 2;

	/**
	 * {@code Tokenizer} mode in which the {@code Tokenizer} stop accumulating code points and return {@code null}.
	 * It Indicates that it cannot issue another {@link Token} because it has reached the end of the stream.
	 *
	 * @since 0.2
	*/
	private static final int MODE_EOS = 3;

	/**
	 * The {@link LanguageDefinition} used to generate {@link Token}s.
	 *
	 * @since 0.1
	 */
	private final LanguageDefinition def;

	/**
	 * The {@link Reader} from which the {@code Tokenizer} reads {@link Token}s.
	 *
	 * @since 0.1
	 */
	private final Reader src;

	/**
	 * Name of the source. Usually the relative path to the source file. It is primarily used in logs.
	 *
	 * @since 0.1
	 */
	private final String srcName;

	/**
	 * The lookup code-point. It is used to know if the {@code Tokenizer} should continue to accumulate or stop to
	 * produce a Token.
	 *
	 * @since 0.1
	 */
	private int next = -1;

	/**
	 * Buffer used to accumulate code-points.
	 *
	 * @since 0.1
	 */
	private StringBuilder buff;

	/**
	 * The current mode of this {@code Tokenizer}.
	 *
	 * @see #MODE_STANDBY
	 * @see #MODE_DONE
	 * @see #MODE_ERROR
	 * @see #MODE_EOS
	 *
	 * @since 0.1
	 */
	private int mode;

	/**
	 * The {@link Token} produced by {@link #finish}, if successful.
	 *
	 * @since 0.1
	 */
	private Token result;

	/**
	 * Line of the current character.
	 *
	 * @since 0.1
	 */
	private int line = 1;

	/**
	 * Column of the current character. Starting at zero for convenience.
	 *
	 * @since 0.1
	 */
	private int col = 0;

	/**
	 * Line of the first character of the {@link Token} currently being produced.
	 *
	 * @since 0.1
	 */
	private int startLine = line;

	/**
	 * Column of the first character of the {@link Token} currently being produced.
	 *
	 * @since 0.1
	 */
	private int startCol = col;

	/**
	 * Creates a new {@code Tokenizer}.
	 *
	 * @param definition definition of the language to create {@code Tokens} for.
	 * @param source source from which the Tokenizer need to read data.
	 * @param sourceName name of the source (mostly relative path to the file).
	 *
	 * @since 0.1
	 */
	public Tokenizer(LanguageDefinition definition, Reader source, String sourceName) {
		this.def = definition;
		this.src = source;
		this.srcName = sourceName;
		buff = new StringBuilder();
		read();
	}

	/**
	 * Reads all {@link Token}s from the source.
	 *
	 * @return all read tokens.
	 *
	 * @since 0.1
	 */
	public Token[] readAllTokens() {
		var tokens = new ArrayList<Token>();

		Token curr;

		while((curr = readToken()) != null) {
			tokens.add(curr);
		}

		return tokens.toArray(new Token[0]);
	}

	/**
	 * Reads one {@link Token} from the source.
	 *
	 * @return read {@code Token}, {@code null} if an error happened.
	 *
	 * @since 0.1
	 */
	public Token readToken() {
		if(mode == MODE_EOS) return null;

		mode = MODE_STANDBY;
		result = null;

		var loop = 0;

		do {
			while (Character.isWhitespace(next)) {
				read();
			}

			if (next == '#') {
				while (next != '\n' && next != '\r') {
					if (loop > 1000) break;
					read();
					loop++;
				}
				read();
			}
		} while (Character.isWhitespace(next) || next == '#');

		startCol = col;
		startLine = line;
		while(mode == MODE_STANDBY) {
			if(next != -1) {
				buff.appendCodePoint(next);
			} else {
				finish();
			}

			if(mayToken()) {
				read();
				continue;
			}

			buff.setLength(buff.length() - 1);

			finish();
		}

		if(result != null) {
			debug("TOKEN '" + result.getValue() + "' (" + result.getDescriptor().getName() + ")");
		} else {
			debug("TOKEN null (none)");
		}

		return result;
	}

	/**
	 * Convenience method which checks whether the buffer content correspond to a {@link universe.lambda.jlcl.token.descriptor.TokenTypeDescriptor}.
	 *
	 * @return {@code true} if a {@code TokenTypeDescriptor} correspond to the buffer content, {@code false} otherwise.
	 *
	 * @since 0.1
	 *
	 * @see LanguageDefinition#correspondToken(String)
	 */
	private boolean isToken() {
		return def.correspondToken(buff.toString());
	}

	/**
	 * Convenience method which checks whether the buffer content may correspond to a {@link universe.lambda.jlcl.token.descriptor.TokenTypeDescriptor}.
	 *
	 * @return {@code true} if a {@code TokenTypeDescriptor} may correspond to the buffer content, {@code false} otherwise.
	 *
	 * @since 0.1
	 *
	 * @see LanguageDefinition#mayCorrespondToken(String)
	 */
	private boolean mayToken() {
		return def.mayCorrespondToken(buff.toString());
	}

	/**
	 * Tries to finalize a {@link Token} from the buffer content.<br><br>
	 *
	 * This method updates the state of this {@code Tokenizer} according to the buffer content and the next code-point
	 * ({@link #buff} and {@link #next} respectively).
	 *
	 * @since 0.1
	 */
	private void finish() {
		final var content = buff.toString();
		if(content.length() == 0) {
			if(next == -1) {
				mode = MODE_EOS;
				return;
			}
			mode = MODE_ERROR;
			logerr(
					new StringBuilder("unexpected character: ").appendCodePoint(next).toString(),
					false
			);
			return;
		}

		var ttd = def.getTokenTypeDescriptorByValue(content);

		if(ttd == null) {
			result = null;
			if(next == -1) {
				logerr("unexpected end of file", false);
				mode = MODE_EOS;
				return;
			}
			logerr("unrecognized token: " + content);
			mode = MODE_ERROR;
		} else {
			result = ttd.makeToken(content, srcName, startLine, startCol);
			mode = MODE_DONE;
		}

		buff.setLength(0);
	}

	/**
	 * Reads a code-point from the source ({@link #src}), and assign it to {@link #next}.<br>
	 * This method updates the line, and the column counters according to what has been read.
	 *
	 * @since 0.1
	 */
	private void read() {
		int curr = -1;
		try {
			curr = src.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if((curr == '\n' && next != '\r') || curr == '\r') {
			col = 0;
			line++;
		} else {
			if(curr != '\n')
				col++;
		}

		next = curr;
	}

	/**
	 * Convenience method to log a debug message using {@link #startLine} and {@link #startCol} as the line and column
	 * of the message.
	 *
	 * @param message message to log.
	 *
	 * @since 0.2
	 */
	private void debug(String message) {
		debug(message, true);
	}

	/**
	 * Convenience method to log a debug message.
	 *
	 * @param message message to log.
	 * @param startOfToken indicates whether to use {@link #startLine} and {@link #startCol} or {@link #line} and
	 * {@link #col} as the line and column of the message ({@code true} and {@code false} respectively).
	 *
	 * @since 0.2
	 */
	private void debug(String message, boolean startOfToken) {
		Logger.debug(getLogMsg(message, startOfToken));
	}

	/**
	 * Convenience method to log an error message using {@link #startLine} and {@link #startCol} as the line and column
	 * of the message.
	 *
	 * @param message message to log.
	 *
	 * @since 0.2
	 */
	private void logerr(String message) {
		logerr(message, true);
	}

	/**
	 * Convenience method to log an error message.
	 *
	 * @param message message to log.
	 * @param startOfToken indicates whether to use {@link #startLine} and {@link #startCol} or {@link #line} and
	 * {@link #col} as the line and column of the message ({@code true} and {@code false} respectively).
	 *
	 * @since 0.2
	 */
	private void logerr(String message, boolean startOfToken) {
		Logger.error(getLogMsg(message, startOfToken));
	}

	/**
	 * Convenience method to build log message following a specific format.
	 *
	 * @param message message of the log.
	 * @param startOfToken indicates whether to use {@link #startLine} and {@link #startCol} or {@link #line} and
	 * {@link #col} as the line and column of the message ({@code true} and {@code false} respectively).
	 * @return the built log message.
	 *
	 * @since 0.2
	 */
	private String getLogMsg(String message, boolean startOfToken) {
		return srcName + ":" + (startOfToken ? startLine : line) + ":" + (startOfToken ? startCol : col) + ": " + message;
	}
}
