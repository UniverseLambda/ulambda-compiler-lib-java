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

package universe.lambda.jlcl.token.descriptor;

import universe.lambda.jlcl.token.Token;
import universe.lambda.jlcl.token.Tokenizer;

/**
 * The descriptor of a type of {@link Token}.
 *
 * A {@code TokenTypeDescriptor} tells the {@link Tokenizer} whenever its buffer is a {@link Token} or at least if it
 * can become one. It is also used to create corresponding {@code Tokens}.
 *
 * @since 0.1
 */
public interface TokenTypeDescriptor {

	/**
	 * Gets the name of this descriptor.
	 *
	 * @return the name of this descriptor.
	 *
	 * @since  0.1
	 */
	String getName();

	/**
	 * Gets whether the input may correspond to this descriptor.
	 *
	 * @param value value to check.
	 * @return {@code true} if the input may correspond to this descriptor, {@code false} otherwise.
	 *
	 * @since 0.1
	 */
	boolean mayCorrespond(String value);

	/**
	 * Gets whether the input corresponds to this descriptor.
	 *
	 * @param value value to check.
	 * @return {@code true} if the input corresponds to this descriptor, {@code false} otherwise.
	 *
	 * @since 0.1
	 */
	boolean correspond(String value);

	/**
	 * Makes a {@link Token} from the specified arguments. Implementations of this method SHOULD check if the {@code value} corresponds to this descriptor and
	 * return {@code null} if not.
	 *
	 * @param value {@link String} value of the {@code Token}.
	 * @param source name of the source from which {@code value} has been read.
	 * @param line line where {@code value} has been read.
	 * @param column column where {@code value} has been read.
	 * @return the created {@code Token}.
	 *
	 * @since 0.1
	 */
	Token makeToken(String value, String source, int line, int column);
}
