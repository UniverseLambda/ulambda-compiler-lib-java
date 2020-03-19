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

import universe.lambda.jlcl.LanguageDefinition;

/**
 * Descriptor recognizing identifiers.
 *
 * Identifiers can be composed of letters, digits and underscores but must not start with a digit (to avoid
 * ambiguity with integers).
 *
 * @since 0.1
 *
 * @see universe.lambda.jlcl.feature.IdentifierFeature
 * @see TokenTypeDescriptor
 * @see universe.lambda.jlcl.token.Token
 * @see LanguageDefinition
 */
public class IdentifierTokenTypeDescriptor extends AbstractTokenTypeDescriptor {

	/**
	 * Creates a new {@code IdentifierTokenTypeDescriptor}.
	 */
	public IdentifierTokenTypeDescriptor() {
		super(LanguageDefinition.IDENTIFIER);
	}

	@Override
	public boolean mayCorrespond(String value) {
		return isIdentifier(value);
	}

	@Override
	public boolean correspond(String value) {
		return isIdentifier(value);
	}

	/**
	 * Checks if the input value is or may be an identifier.
	 *
	 * @param value value to test.
	 * @return {@code true} if the value is an identifier, {@code false} otherwise.
	 *
	 * @since 0.1
	 */
	private boolean isIdentifier(String value) {
		for(int i = 0; i < value.length(); i++) {
			int cp = value.codePointAt(i);

			var result = !Character.isWhitespace(cp);
			result = result && !(Character.isDigit(cp) && i == 0);
			result = result && (Character.isLetterOrDigit(cp) || cp == '_');

			if(!result) return false;
		}
		return true;
	}
}
