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

/**
 * Descriptor recognizing strings. Strings start and end with ' " '. A character can be escaped by preceding it with '\'.
 * It will be written as is with the escaping character.
 *
 * @since 0.1
 *
 * @see universe.lambda.jlcl.token.Token
 * @see universe.lambda.jlcl.LanguageDefinition
 * @see universe.lambda.jlcl.feature.StringFeature
 */
public class StringTokenTypeDescriptor extends AbstractTokenTypeDescriptor {
	/**
	 * Default constructor.
	 *
	 * @since 0.1
	 */
	public StringTokenTypeDescriptor() {
		super("STRING");
	}

	@Override
	public boolean correspond(String value) {
		if(!value.endsWith("\"")) return false;
		return analyse(value, false);
	}

	@Override
	public boolean mayCorrespond(String value) {
		return analyse(value, true);
	}

	/**
	 * Analyse the input value to determine if is a valid String. The method goes through each code-points of
	 * {@code value}, stopping at the first unescaped ' " ' character returning {@code true} if it stopped at the end
	 * of {@code value}, {@code false} otherwise. If the method reaches the end of {@code value} without encountering an
	 * unescaped ' " ', then the method returns {@code defaultReturn}.
	 *
	 * @param value value to analyse.
	 * @param defaultReturn value to return if the string is incomplete.
	 * @return {@code true} if {@code value} is a valid string, {@code false} if it is not, {@code defaultReturn} if
	 * {@code value} seems to be an incomplete string.
	 *
	 * @since 0.2
	 */
	private static boolean analyse(String value, boolean defaultReturn) {
		if(!value.startsWith("\"")) return false;

		boolean skipNext = false;

		for(int i = 1; i < value.length(); i++) {
			if(skipNext) {
				skipNext = false;
				continue;
			}

			int cp = value.codePointAt(i);

			if(cp == '\\') {
				skipNext = true;
				continue;
			}

			if(cp == '\"') {
				return i + 1 == value.length();
			}
		}

		return defaultReturn;
	}
}
