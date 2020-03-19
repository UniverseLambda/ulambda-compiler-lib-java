/*
	Copyright 2020 Clément Saad

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

import universe.lambda.jlcl.LanguageDefinition;
import universe.lambda.jlcl.LanguageDefinitionInflater;
import universe.lambda.jlcl.token.Token;

/**
 * This class provides a skeletal implementation of the {@link TokenTypeDescriptor} interface to minimize effort required to
 * implement this interface.
 *
 * @since 0.1
 */
public abstract class AbstractTokenTypeDescriptor implements TokenTypeDescriptor {

	/**
	 * Name of the descriptor. Used by {@link LanguageDefinition} and {@link LanguageDefinitionInflater} to index descriptors.
	 *
	 * @since 0.1
	 */
	private String name;

	/**
	 * Construct a descriptor with the given name.
	 *
	 * @param name name of the descriptor.
	 *
	 * @since 0.1
	 */
	public AbstractTokenTypeDescriptor(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Token makeToken(String value, String source, int line, int column) {
		if(!correspond(value)) {
			return null;
		}
		return new Token(this, value, source, line, column);
	}
}
