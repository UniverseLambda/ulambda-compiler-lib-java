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

import universe.lambda.jlcl.utils.NumberUtil;

/**
 * Descriptor recognizing floats.
 *
 * @since 0.1
 *
 * @see universe.lambda.jlcl.token.Token
 * @see universe.lambda.jlcl.LanguageDefinition
 * @see universe.lambda.jlcl.feature.FloatFeature
 * @see universe.lambda.jlcl.utils.NumberUtil
 */
public class FloatTokenTypeDescriptor extends AbstractTokenTypeDescriptor {
	/**
	 * Array containing all recognized suffixes.
	 *
	 * @since 0.1
	 */
	private String[] floatSuffix;

	/**
	 * Default constructor defaulting suffixes to "F", "f", "D" and "d".
	 *
	 * @since 0.1
	 */
	public FloatTokenTypeDescriptor() {
		this(new String[]{"F", "f", "D", "d"});
	}

	/**
	 * Construct a {@link TokenTypeDescriptor} recognizing floats with and without the specified suffixes.
	 *
	 * @param suffixes recognized suffixes.
	 *
	 * @since 0.1
	 */
	public FloatTokenTypeDescriptor(String[] suffixes) {
		super("FLOAT");
		this.floatSuffix = (suffixes == null ? new String[0] : suffixes);
	}

	@Override
	public boolean mayCorrespond(String value) {
		return NumberUtil.isFloat(value, floatSuffix);
	}

	@Override
	public boolean correspond(String value) {
		if(!NumberUtil.isFloat(value, floatSuffix)) {
			return false;
		}

		return !NumberUtil.isInteger(value);
	}
}
