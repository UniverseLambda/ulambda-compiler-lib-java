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

import universe.lambda.jlcl.token.descriptor.TokenTypeDescriptor;

/**
 * Immutable object representing a token.
 */
public class Token {
	/**
	 * Descriptor which created this {@code Token}.
	 */
	private final TokenTypeDescriptor descriptor;

	/**
	 * Value of this {@code Token}.
	 */
	private final String value;

	/**
	 * Source of this {@code Token} (often the filename).
	 */
	private final String source;

	/**
	 * Line of the first character of this {@code Token}.
	 */
	private final int line;

	/**
	 * Column of the first character of this {@code Token}.
	 */
	private final int col;

	/**
	 * Create an immutable Token.
	 * @param descriptor corresponding {@code TokenTypeDescriptor}.
	 * @param value of the {@code Token}.
	 * @param source from which it has been read.
	 * @param line of the first code point of this {@code Token}.
	 * @param col of the first code point of this {@code Token}.
	 */
	public Token(TokenTypeDescriptor descriptor, String value, String source, int line, int col) {
		this.descriptor = descriptor;
		this.value = value;
		this.source = source;
		this.line = line;
		this.col = col;
	}

	/**
	 * Get corresponding {@code TokenTypeDescriptor}.
	 * @return the corresponding {@code TokenTypeDescriptor}.
	 */
	public TokenTypeDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * Get value of this {@code Token}.
	 * @return the value motherfucker.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Get the name of the source from which this {@code Token} has been read.
	 * @return the name of the source.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Get the line of the beginning of this {@code Token}.
	 * @return the line of this {@code Token}.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Get the column of the beginning of this {@code Token}.
	 * @return the column of this {@code Token}.
	 */
	public int getColumn() {
		return col;
	}
}
