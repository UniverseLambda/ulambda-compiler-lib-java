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

package universe.lambda.jlcl.token;

import universe.lambda.jlcl.LanguageDefinition;
import universe.lambda.jlcl.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Tokenizer {
	/**
	 * {@code Tokenizer} mode in which the Tokenizer keeps accumulating code points since it cannot issue a
	 * {@code Token}, but accumulating more code points may produce a {@code Token}.
	 */
	private static final int MODE_STANDBY = 0;

	/**
	 * {@code Tokenizer} mode in which the {@code Tokenizer} stop accumulating code points and return {@code result}. It
	 * indicates that a {@code Token} has been successfully created and that accumulating code points is no longer
	 * needed.
	 */
	private static final int MODE_DONE = 1;

	/**
	 * {@code Tokenizer} mode in which the {@code Tokenizer} stop accumulating code points and return {@code null}. It
	 * indicates that it cannot issue a {@code Token} because the accumulated code points do not correspond to any
	 * {@code Token}, and accumulating more would not produce a valid {@code Token}.
	 */
	private static final int MODE_ERROR = 2;

	private final LanguageDefinition def;
	private final Reader src;
	private final String srcName;

	private int next = -1;
	private StringBuilder buff;

	private int mode;

	private Token result;

	private int line = 1;
	private int col = 0;
	private int startLine = line;
	private int startCol = col;

	/**
	 * Create a Tokenizer instance.
	 * @param definition of the language to create {@code Tokens} for.
	 * @param source from which the Tokenizer need to read data.
	 * @param sourceName name of the source (mostly relative path to the file)
	 */
	public Tokenizer(LanguageDefinition definition, Reader source, String sourceName) {
		this.def = definition;
		this.src = source;
		this.srcName = sourceName;
		buff = new StringBuilder();
		read();
	}

	/**
	 * Read all {@code Tokens} from the source.
	 * @return all read tokens.
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
	 * Read one {@code Token} from the source.
	 * @return read {@code Token}, {@code null} if an error happened.
	 */
	public Token readToken() {
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
		} while (Character.isWhitespace(next));

		startCol = col;
		startLine = line;
		while(mode == MODE_STANDBY) {
			if(next != -1) {
				buff.appendCodePoint(next);
			} else {
				if(buff.length() == 0) finish();
				continue;
			}

			if(mayToken()) {
				read();
				continue;
			}


			buff.setLength(buff.length() - 1);

			finish();
		}

		return result;
	}

	private boolean isToken() {
		return def.correspondToken(buff.toString());
	}

	private boolean mayToken() {
		return def.mayCorrespondToken(buff.toString());
	}

	private void finish() {
		final var content = buff.toString();
		// System.out.println("CONTENT: " + content);
		if(content.length() == 0) {
			mode = MODE_ERROR;
			Logger.error("tried to finish with an empty buffer");
			return;
		}

		var ttd = def.getTokenTypeDescriptorByValue(content);

		if(ttd == null) {
			mode = MODE_ERROR;
			result = null;
		} else {
			result = ttd.makeToken(content, srcName, startLine, startCol);
			mode = MODE_DONE;
		}

		buff.setLength(0);
	}

	private void read() {
		var curr = -1;
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
}
