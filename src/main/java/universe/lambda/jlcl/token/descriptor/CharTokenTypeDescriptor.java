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
 * Descriptor recognizing characters.
 *
 * @since 0.1
 *
 * @see universe.lambda.jlcl.feature.CharacterFeature
 * @see TokenTypeDescriptor
 * @see universe.lambda.jlcl.token.Token
 * @see LanguageDefinition
 */
public class CharTokenTypeDescriptor extends AbstractTokenTypeDescriptor {
	/**
	 * Creates a new {@code CharTokenTypeDescriptor}.
	 */
	public CharTokenTypeDescriptor() {
		super(LanguageDefinition.CHAR);
	}

	@Override
	public boolean correspond(String value) {
		return value.startsWith("'") && value.length() == 3 && value.endsWith("'");
	}

	@Override
	public boolean mayCorrespond(String value) {
		return value.startsWith("'") && value.length() < 4;
	}
}
