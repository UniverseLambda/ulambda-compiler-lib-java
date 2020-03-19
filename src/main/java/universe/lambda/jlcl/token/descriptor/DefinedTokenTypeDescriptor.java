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

/**
 * Descriptor recognizing the given {@link String}. Its name is defined in the constructor. This is the one used by the
 * {@link universe.lambda.jlcl.LanguageDefinitionInflater} for lines starting with "TOKEN".
 *
 * @since 0.1
 *
 * @see universe.lambda.jlcl.token.Token
 * @see universe.lambda.jlcl.LanguageDefinition
 */
public class DefinedTokenTypeDescriptor extends AbstractTokenTypeDescriptor {

	/**
	 * Value used to match inputs.
	 *
	 * @since 0.1
	 */
	private final String value;

	/**
	 * Creates a new {@code DefinedTokenTypeDescriptor} with the specified name and value.
	 *
	 * @param name name of this descriptor.
	 * @param value value of this descriptor.
	 *
	 * @since 0.1
	 */
	public DefinedTokenTypeDescriptor(String name, String value) {
		super(name);
		this.value = value;
	}

	/**
	 * Gets this instance value.
	 *
	 * @return this instance value.
	 *
	 * @since 0.1
	 */
	public String getValue() {
		return value;
	}

	@Override
	public boolean mayCorrespond(String value) {
		return this.value.startsWith(value);
	}

	@Override
	public boolean correspond(String value) {
		return this.value.equals(value);
	}
}
